package wikibroker_openapi_sdk

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"net/http"
	"slices"
	"strings"
	"wikibroker_openapi_sdk/common"
)

func generateSignature(key, message string) string {
	h := hmac.New(sha256.New, []byte(key))
	h.Write([]byte(message))
	return hex.EncodeToString(h.Sum(nil))
}

func generateCanonicalString(req *http.Request) (string, error) {
	method := strings.ToUpper(req.Method)
	path := req.URL.Path
	canonicalQuery := buildCanonicalQuery(req)
	apiKey := req.Header.Get(common.CustomHeaderApiKey.String())
	timestamp := req.Header.Get(common.CustomHeaderTimestamp.String())
	nonce := req.Header.Get(common.CustomHeaderNonce.String())
	bodyHash, err := calculateBodyHash(req)
	if err != nil {
		return "", err
	}
	return strings.Join([]string{
		method,
		path,
		canonicalQuery,
		apiKey,
		timestamp,
		nonce,
		bodyHash,
	}, "\n"), nil
}

func calculateBodyHash(req *http.Request) (hash string, err error) {
	body := make([]byte, 0)
	if req.Method == http.MethodPost {
		body, err = common.ReadRequestBody(req)
		if err != nil {
			return "", err
		}
	}
	h := sha256.New()
	h.Write(body)
	return hex.EncodeToString(h.Sum(nil)), nil
}

func buildCanonicalQuery(req *http.Request) string {
	query := req.URL.Query()
	groups := make(map[string][]string, len(query))
	for k, v := range query {
		validValues := common.Filter(func(x string) bool {
			return x != ""
		}, v)
		if len(validValues) == 0 {
			continue
		}
		slices.Sort(validValues)
		groups[k] = validValues
	}
	parts := common.MapItems(groups)
	slices.SortFunc(parts, common.SortByKey)
	pairs := make([]string, 0)
	for _, part := range parts {
		for _, v := range part.Value {
			pairs = append(pairs, fmt.Sprintf("%s=%s", part.Key, v))
		}
	}
	return strings.Join(pairs, "&")
}
