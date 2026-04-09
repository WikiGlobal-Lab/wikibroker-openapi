import Foundation

extension String {
    public func toBytes() -> Data {
        return self.data(using: .utf8) ?? Data()
    }
}

extension Data {
    public func toHexString() -> String {
        return map { String(format: "%02x", $0) }.joined()
    }
    public func toString() -> String {
        return String(data: self, encoding: .utf8) ?? ""
    }
}
