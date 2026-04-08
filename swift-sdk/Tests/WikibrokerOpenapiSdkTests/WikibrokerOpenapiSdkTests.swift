import Alamofire
import Foundation
import Testing

@testable import WikibrokerOpenapiSdk

@Suite("OpenAPI Tests")
struct OpenAPITests {
    let baseURL = "https://api.example.com"
    let path = "test?q1=a&q2=b&q1=c"
    var url: URL { URL(string: "\(baseURL)/\(path)")! }
    let body = ["key": "value"]
    let method = "POST"
    let apiKey = UUID(uuidString: "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b")!
    let apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
    let timestamp = Date(
        timeIntervalSince1970: TimeInterval(1_798_115_622_000 / 1000)
    )
    let nonce = UUID(uuidString: "4428a206-1afd-4b15-a98d-43e91f49a08d")!
    let expectedSignature =
        "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4"

    @Test func testNative() async throws {
        let c = URLSessionConfiguration.default
        c.timeoutIntervalForRequest = 0.001
        let s = URLSession(configuration: c)
        s.setAuth(
            apiKey: apiKey,
            apiSecret: apiSecret,
            loadHeaders: addXHeaders,
            sign: sign,
            timestampGenerator: { timestamp },
            idGenerator: { nonce }
        )

        var req = URLRequest(url: url)
        req.httpMethod = method
        req.httpBody = try JSONSerialization.data(withJSONObject: body)
        do {
            try await s.dataWithAuth(for: &req)
        } catch {}
        let actualSignature =
            req.value(forHTTPHeaderField: CustomHeaders.signature.rawValue)
            ?? ""
        #expect(actualSignature == expectedSignature)
    }

    @Test func testAlamofire() async throws {
        let c = URLSessionConfiguration.default
        c.timeoutIntervalForRequest = 0.001
        let interceptor = AlamofireAuthInterceptor(
            apiKey: apiKey,
            apiSecret: apiSecret,
            loadHeaders: addXHeaders,
            sign: sign,
            timestampGenerator: { timestamp },
            idGenerator: { nonce }
        )
        let s = Session(configuration: c, interceptor: interceptor)

        let req = s.request(
            url,
            method: HTTPMethod(rawValue: method),
            parameters: body,
            encoding: JSONEncoding.default
        ).resume()
        try await Task.sleep(for: .seconds(c.timeoutIntervalForRequest))
        let actualSignature =
            req.request!.value(
                forHTTPHeaderField: CustomHeaders.signature.rawValue
            ) ?? ""
        #expect(actualSignature == expectedSignature)
    }
}
