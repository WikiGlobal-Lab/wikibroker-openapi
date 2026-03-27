using System.Security.Cryptography;
using System.Text;

namespace WikiBroker.OpenApi.Sdk.Common;

public static class Hash
{
    public static byte[] HmacSha256(byte[] key, byte[] message)
    {
        using var hmac = new HMACSHA256(key);
        return hmac.ComputeHash(message);
    }

    public static byte[] Sha256Hash(byte[] message)
    {
        return SHA256.HashData(message);
    }
}