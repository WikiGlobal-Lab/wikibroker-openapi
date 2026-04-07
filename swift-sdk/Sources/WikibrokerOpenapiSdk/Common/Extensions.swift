import Foundation

extension String {
    func toBytes() -> Data {
        return self.data(using: .utf8) ?? Data()
    }
}

extension Data {
    func toHexString() -> String {
        return map { String(format: "%02x", $0) }.joined()
    }
    func toString() -> String {
        return String(data: self, encoding: .utf8) ?? ""
    }
}
