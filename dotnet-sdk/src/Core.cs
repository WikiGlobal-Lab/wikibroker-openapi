using WikiBroker.OpenApi.Sdk.Common;
using WikiBroker.OpenApi.Sdk.Common.Enums;

namespace WikiBroker.OpenApi.Sdk;

public static class Core
{
    public static string GenerateSignature(string key, string message)
    {
        return Convert.ToHexString(Hash.HmacSha256(key, message));
    }

    public static async Task<string> GenerateCanonicalString(HttpRequestMessage req)
    {
        var method = req.Method.Method;
        var path = req.RequestUri?.AbsolutePath ?? "";
        var canonicalQuery = Core.BuildCanonicalQuery(req);
        var apiKey = req.Headers.GetValues(CustomHeaders.ApiKey.Value).ElementAt(0);
        var timestamp = req.Headers.GetValues(CustomHeaders.TimeStamp.Value).ElementAt(0);
        var nonce = req.Headers.GetValues(CustomHeaders.Nonce.Value).ElementAt(0);
        var bodyHash = await Core.CalculateBodyHash(req);
        return string.Join("\n", method, path, canonicalQuery, apiKey, timestamp, nonce, bodyHash);
    }

    private static async Task<string> CalculateBodyHash(HttpRequestMessage req)
    {
        var body = "";
        if (req.Method.Equals(HttpMethod.Post))
        {
            body = await (req.Content?.ReadAsStringAsync() ?? Task.FromResult(""));
        }

        var hash = Hash.Sha256Hash(body);
        return Convert.ToHexString(hash);
    }

    private static string BuildCanonicalQuery(HttpRequestMessage req)
    {
        var queryString = req.RequestUri?.Query ?? "";
        var query = queryString.Split("&")
            .Select(pair => pair.Split("="))
            .GroupBy(arr => arr[0])
            .ToDictionary(
                pair => pair.Key,
                pair => pair.ToList()
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