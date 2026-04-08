import Foundation

func addXHeaders(
    req: inout URLRequest,
    apiKey: UUID,
    timestamp: Date,
    nonce: UUID
) {
    req.setValue(
        apiKey.uuidString.lowercased(),
        forHTTPHeaderField: CustomHeaders.apiKey.rawValue
    )
    req.setValue(
        String(Int64(timestamp.timeIntervalSince1970 * 1000)),
        forHTTPHeaderField: CustomHeaders.timestamp.rawValue
    )
    req.setValue(
        nonce.uuidString.lowercased(),
        forHTTPHeaderField: CustomHeaders.nonce.rawValue
    )
}

func sign(req: inout URLRequest, key: String) {
    let canonicalString = generateCanonicalString(req: req)
    let signature = generateSignature(key: key, message: canonicalString)
    req.setValue(
        signature,
        forHTTPHeaderField: CustomHeaders.signature.rawValue
    )
}

extension URLSession {
    public func setAuth(apiKey: String, apiSecret: String) {
        self.setAuth(
            apiKey: UUID(uuidString: apiKey)!,
            apiSecret: apiSecret,
            loadHeaders: addXHeaders,
            sign: sign,
            timestampGenerator: { Date() },
            idGenerator: { UUID() }
        )
    }
}
