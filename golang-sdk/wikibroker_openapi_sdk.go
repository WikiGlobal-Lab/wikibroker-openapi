package wikibroker_openapi_sdk

import (
	"io"
	"net/http"
	"strconv"
	"time"
	"wikibroker_openapi_sdk/adapters"
	"wikibroker_openapi_sdk/common"

	"github.com/google/uuid"
	"github.com/levigross/grequests/v2"
	"github.com/parnurzeal/gorequest"
	"resty.dev/v3"
)

func AddXHeaders(
	headers http.Header,
	apiKey string,
	timestamp time.Time,
	nonce string,
) error {
	if _, err := uuid.Parse(apiKey); err != nil {
		return err
	}
	if _, err := uuid.Parse(nonce); err != nil {
		return err
	}
	headers.Set(common.CustomHeaderApiKey.String(), apiKey)
	headers.Set(common.CustomHeaderTimestamp.String(), strconv.FormatInt(timestamp.UnixMilli(), 10))
	headers.Set(common.CustomHeaderNonce.String(), nonce)
	return nil
}

func Sign(req *http.Request, key string) error {
	canonicalString, err := generateCanonicalString(req)
	if err != nil {
		return err
	}
	signature, err := generateSignature(key, canonicalString)
	if err != nil {
		return err
	}
	req.Header.Add(common.CustomHeaderSignature.String(), signature)
	return nil
}

var (
	CustomHeaderApiKey    = common.CustomHeaderApiKey
	CustomHeaderTimestamp = common.CustomHeaderTimestamp
	CustomHeaderNonce     = common.CustomHeaderNonce
	CustomHeaderSignature = common.CustomHeaderSignature
)

func NewHttpClient(raw adapters.RawHttpClient, apiKey, apiSecret string) *adapters.HttpClient {
	return adapters.NewHttpClient(raw, apiKey, apiSecret, AddXHeaders, Sign, time.Now, uuid.NewString)
}

func NewRestyRequestMiddleware(apiKey, apiSecret string, bodyBuilder func(any) (io.ReadCloser, error)) resty.RequestMiddleware {
	return adapters.NewRestyRequestMiddleware(apiKey, apiSecret, AddXHeaders, Sign, time.Now, uuid.NewString, bodyBuilder)
}

func GRequestsAuthOption(apiKey, apiSecret string) grequests.Option {
	return grequests.BeforeRequest(adapters.NewGRequestsHook(apiKey, apiSecret, AddXHeaders, Sign, time.Now, uuid.NewString))
}

func LoadGorequestInterceptor(agent *gorequest.SuperAgent, apiKey, apiSecret string) {
	agent.Client.Transport = adapters.NewGorequestInterceptor(agent.Client.Transport, apiKey, apiSecret, AddXHeaders, Sign, time.Now, uuid.NewString)
}
