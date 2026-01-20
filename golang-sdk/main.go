package wikibroker_openapi_sdk

import (
	"net/http"
	"strconv"
	"time"
	"wikibroker_openapi_sdk/common"

	"github.com/google/uuid"
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
	signature := generateSignature(key, canonicalString)
	req.Header.Add(common.CustomHeaderSignature.String(), signature)
	return nil
}

var (
	CustomHeaderApiKey    = common.CustomHeaderApiKey
	CustomHeaderTimestamp = common.CustomHeaderTimestamp
	CustomHeaderNonce     = common.CustomHeaderNonce
	CustomHeaderSignature = common.CustomHeaderSignature
)

var (
	QueryParseModeRepeat  = common.QueryParseModeRepeat
	QueryParseModeBracket = common.QueryParseModeBracket
	QueryParseModeComma   = common.QueryParseModeComma
)
