import CryptoKit
import Foundation

public func sha256Hash(message: Data) -> Data {
    return Data(SHA256.hash(data: message))
}

public func hmacSha256(key: Data, message: Data) -> Data {
    let symmetricKey = SymmetricKey(data: key)
    let authCode = HMAC<SHA256>.authenticationCode(
        for: message,
        using: symmetricKey
    )
    return Data(authCode)
}
