import Foundation

extension URLSession {
    @available(macOS 12.0, *)
    public func dataWithAuth(
        for with: inout URLRequest
    ) async throws -> (Data, URLResponse) {
        URLSession.addSign(&with)
        return try await self.data(for: with)
    }

    @available(macOS 12.0, *)
    public func dataWithAuth(
        for with: inout URLRequest,
        delegate: (any URLSessionTaskDelegate)?
    ) async throws -> (Data, URLResponse) {
        URLSession.addSign(&with)
        return try await self.data(for: with, delegate: delegate)
    }

    public func dataTaskWithAuth(
        with: inout URLRequest
    ) -> URLSessionDataTask {
        URLSession.addSign(&with)
        return self.dataTask(with: with)
    }

    public func dataTaskWithAuth(
        with: inout URLRequest,
        completionHandler:
            @escaping (@Sendable (Data?, URLResponse?, Error?) -> Void)
    ) -> URLSessionDataTask {
        URLSession.addSign(&with)
        return self.dataTask(with: with, completionHandler: completionHandler)

    }

    nonisolated(unsafe) private static var addSign:
        @Sendable (inout URLRequest) -> Void = { _ in }

    public func setAuth(
        apiKey: UUID,
        apiSecret: String,
        loadHeaders:
            @escaping @Sendable (inout URLRequest, UUID, Date, UUID) -> Void,
        sign: @escaping @Sendable (inout URLRequest, String) -> Void,
        timestampGenerator: @escaping @Sendable () -> Date,
        idGenerator: @escaping @Sendable () -> UUID
    ) {
        URLSession.addSign = {
            loadHeaders(&$0, apiKey, timestampGenerator(), idGenerator())
            sign(&$0, apiSecret)
        }
    }
}
