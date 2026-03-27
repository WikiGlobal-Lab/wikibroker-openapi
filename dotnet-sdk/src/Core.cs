using System.Security.Cryptography;
using System.Text;
using WikiBroker.OpenApi.Sdk.Common;
using WikiBroker.OpenApi.Sdk.Common.Enums;

namespace WikiBroker.OpenApi.Sdk;

public static class Core
{
    public static string GenerateSignature(string key, string message)
    {
        return Convert.ToHexString(HMACSHA256.HashData(
                Encoding.UTF8.GetBytes(key),
                Encoding.UTF8.GetBytes(message)
            )
        ).ToLower();
    }

    public static async Task<string> GenerateCanonicalString(HttpRequestMessage req)
    {
        var method = req.Method.Method;
        var path = req.RequestUri?.AbsolutePath ?? "";
        var canonicalQuery = BuildCanonicalQuery(req);
        var apiKey = req.Headers.GetValues(CustomHeaders.ApiKey.Value).ElementAt(0);
        var timestamp = req.Headers.GetValues(CustomHeaders.TimeStamp.Value).ElementAt(0);
        var nonce = req.Headers.GetValues(CustomHeaders.Nonce.Value).ElementAt(0);
        var bodyHash = await CalculateBodyHash(req);
        return string.Join("\n", method, path, canonicalQuery, apiKey, timestamp, nonce, bodyHash);
    }

    private static async Task<string> CalculateBodyHash(HttpRequestMessage req)
    {
        var body = await Utils.ReadRequestBody(req);
        var hash = SHA256.HashData(body);
        return Convert.ToHexString(hash).ToLower();
    }

    private static string BuildCanonicalQuery(HttpRequestMessage req)
    {
        var queryString = req.RequestUri?.Query.TrimStart('?') ?? "";
        var query = queryString.Split("&")
            .Select(pair => pair.Split("="))
            .GroupBy(arr => arr[0])
            .ToDictionary(
                pair => pair.Key,
                pair => pair.Select(arr => arr[1]).ToList()
            );
        foreach (var pair in query)
        {
            pair.Value.Sort();
        }

        var parts = query.OrderBy(pair => pair.Key);
        return string.Join("&",
            parts.SelectMany(item =>
                item.Value.Select(value => $"{item.Key}={value}")
            )
        );
    }
}