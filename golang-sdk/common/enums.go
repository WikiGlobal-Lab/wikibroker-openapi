package common

const (
	CustomHeaderApiKey    = "X-Api-Key"
	CustomHeaderTimestamp = "X-Timestamp"
	CustomHeaderNonce     = "X-Nonce"
	CustomHeaderSignature = "X-Signature"
)

type QueryParseMode int

const (
	QueryParseModeRepeat  QueryParseMode = iota // https://api.example.com?q1=a&q1=b&q2=c
	QueryParseModeBracket                       // https://api.example.com?q1[]=a&q1[]=b&q2=c
	QueryParseModeComma                         // https://api.example.com?q1=a,b&q2=c
)
