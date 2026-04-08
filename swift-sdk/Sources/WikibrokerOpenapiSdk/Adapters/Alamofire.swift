import Alamofire
import Foundation

final class AlamofireAuthInterceptor: RequestInterceptor {
    private let apiKey: UUID
    private let apiSecret: String
    private let loadHeaders:
        @Sendable (inout URLRequest, UUID, Date, UUID) -> Void
    private let sign: @Sendable (inout URLRequest, String) -> Void
    private let timestampGenerator: @Sendable () -> Date
    private let idGenerator: @Sendable () -> UUID

    init(
        apiKey: UUID,
        apiSecret: String,
        loadHeaders:
            @escaping @Sendable (inout URLRequest, UUID, Date, UUID) -> Void,
        sign: @escaping @Sendable (inout URLRequest, String) -> Void,
        timestampGenerator: @escaping @Sendable () -> Date,
        idGenerator: @escaping @Sendable () -> UUID,
    ) {
        self.apiKey = apiKey
        self.apiSecret = apiSecret
        self.loadHeaders = loadHeaders
        self.sign = sign
        self.timestampGenerator = timestampGenerator
        self.idGenerator = idGenerator
    }

    func adapt(
        _ urlRequest: URLRequest,
        for session: Session,
        completion: @escaping @Sendable (Result<URLRequest, any Error>) -> Void
    ) {
        var req = urlRequest
        loadHeaders(&req, apiKey, timestampGenerator(), idGenerator())
        sign(&req, apiSecret)
        completion(.success(req))
    }

    func adapt(
        _ urlRequest: URLRequest,
        using state: RequestAdapterState,
        completion: @escaping @Sendable (Result<URLRequest, any Error>) -> Void
    ) {
        var req = urlRequest
        loadHeaders(&req, apiKey, timestampGenerator(), idGenerator())
        sign(&req, apiSecret)
        completion(.success(req))
    }
}
