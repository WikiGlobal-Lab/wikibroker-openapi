package common

type CustomHeaders string

func (h CustomHeaders) String() string {
	return string(h)
}

const (
	CustomHeaderApiKey    CustomHeaders = "X-Api-Key"
	CustomHeaderTimestamp CustomHeaders = "X-Timestamp"
	CustomHeaderNonce     CustomHeaders = "X-Nonce"
	CustomHeaderSignature CustomHeaders = "X-Signature"
)

type QueryParseMode int

const (
	QueryParseModeRepeat  QueryParseMode = iota // https://api.example.com?q1=a&q1=b&q2=c
	QueryParseModeBracket                       // https://api.example.com?q1[]=a&q1[]=b&q2=c
	QueryParseModeComma                         // https://api.example.com?q1=a,b&q2=c
)
