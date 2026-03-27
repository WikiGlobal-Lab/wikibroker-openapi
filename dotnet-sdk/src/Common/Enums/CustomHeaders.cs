using Ardalis.SmartEnum;

namespace WikiBroker.OpenApi.Sdk.Common.Enums;

public sealed class CustomHeaders : SmartEnum<CustomHeaders, string>
{
    private CustomHeaders(string name, string value) : base(name, value)
    {
    }

    public static readonly CustomHeaders ApiKey = new(nameof(ApiKey), "X-Api-Key");
    public static readonly CustomHeaders TimeStamp = new(nameof(TimeStamp), "X-Timestamp");
    public static readonly CustomHeaders Nonce = new(nameof(Nonce), "X-Nonce");
    public static readonly CustomHeaders Signature = new(nameof(Signature), "X-Signature");
}