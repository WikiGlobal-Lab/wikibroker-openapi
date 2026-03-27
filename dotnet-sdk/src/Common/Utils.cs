namespace WikiBroker.OpenApi.Sdk.Common;

public static class Utils
{
    public static async Task<byte[]> ReadRequestBody(HttpRequestMessage r)
    {
        if (!r.Method.Equals(HttpMethod.Post))
        {
            return [];
        }

        return await (r.Content?.ReadAsByteArrayAsync() ?? Task.FromResult(Array.Empty<byte>()));
    }
}