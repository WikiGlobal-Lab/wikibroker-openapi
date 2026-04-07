import Foundation

func generateSignature(key: String, message: String) -> String {
    let hash = hmacSha256(key: key.toBytes(), message: message.toBytes())
    return hash.toHexString()
}

func generateCanonicalString(req: URLRequest) -> String {
    let method = req.httpMethod ?? ""
    let path = req.url?.path ?? ""
    let canonicalQuery = buildCanonicalQuery(req: req)
    let apiKey = req.allHTTPHeaderFields?[CustomHeaders.apiKey.rawValue] ?? ""
    let timestamp =
        req.allHTTPHeaderFields?[CustomHeaders.timestamp.rawValue] ?? ""
    let nonce = req.allHTTPHeaderFields?[CustomHeaders.nonce.rawValue] ?? ""
    let bodyHash = calculateBodyHash(req: req)
    return [
        method,
        path,
        canonicalQuery,
        apiKey,
        timestamp,
        nonce,
        bodyHash,
    ].joined(separator: "\n")
}

func calculateBodyHash(req: URLRequest) -> String {
    let body =
        isRequestUsePostMethod(method: req.httpMethod ?? "")
        ? (req.httpBody?.toString() ?? "") : ""
    let message = body.toBytes()
    let hash = sha256Hash(message: message)
    return hash.toHexString()
}

func buildCanonicalQuery(req: URLRequest) -> String {
    var query =
        URLComponents(url: req.url!, resolvingAgainstBaseURL: false)?.queryItems
        ?? []
    query.sort {
        $0.name != $1.name
            ? $0.name < $1.name : ($0.value ?? "") < ($1.value ?? "")
    }
    return query.map { "\($0.name)=\($0.value ?? "")" }.joined(separator: "&")
}
