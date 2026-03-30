namespace WikiBroker.OpenApi.Sdk.Adapters;

public class HttpRequestDelegatingHandler : DelegatingHandler
{
    private readonly Func<HttpRequestMessage, Task> _onRequest;

    public HttpRequestDelegatingHandler(
        HttpMessageHandler innerHandler,
        Guid apiKey,
        string apiSecret,
        Action<HttpRequestMessage, Guid, DateTimeOffset, Guid> loadHeaders,
        Func<HttpRequestMessage, string, Task> sign,
        Func<DateTimeOffset> timestampGenerator,
        Func<Guid> idGenerator
    ) : base(innerHandler)
    {
        _onRequest = async (r) =>
        {
            loadHeaders(r, apiKey, timestampGenerator(), idGenerator());
            await sign(r, apiSecret);
        };
    }

    protected override HttpResponseMessage Send(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        _onRequest(request).GetAwaiter().GetResult();
        return base.Send(request, cancellationToken);
    }

    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
        CancellationToken cancellationToken)
    {
        await _onRequest(request);
        return await base.SendAsync(request, cancellationToken);
    }
}