using System.Security.Cryptography;
using System.Text;

namespace WikiBroker.OpenApi.Sdk.Common;

public static class Hash
{
    public static byte[] HmacSha256(string key, string message)
    {
        using var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(key));
        var data = Encoding.UTF8.GetBytes(message);
        return hmac.ComputeHash(data);
    }

    public static byte[] Sha256Hash(string message)
    {
        return SHA256.HashData(Encoding.UTF8.GetBytes(message));
    }
}