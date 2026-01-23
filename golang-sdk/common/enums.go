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
