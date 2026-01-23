package wikibroker_openapi_sdk

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"

	"net/http"
	"net/url"

	"testing"
	"time"

	"github.com/levigross/grequests/v2"
	"github.com/parnurzeal/gorequest"
	"github.com/smartystreets/goconvey/convey"
	"github.com/stretchr/testify/assert"
	"resty.dev/v3"
)

func TestSign(t *testing.T) {
	baseURL := "https://api.example.com"
	path := "test"
	query := url.Values{
		"q1": []string{"c", "a"},
		"q2": []string{"b"},
	}
	apiUrl := fmt.Sprintf("%s/%s", baseURL, path)
	fullUrl := fmt.Sprintf("%s?%s", apiUrl, query.Encode())
	data := map[string]any{"key": "value"}
	body, err := json.Marshal(data)
	assert.Nil(t, err)
	method := "POST"
	apiKey := "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b"
	apiSecret := "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
	timestamp := time.UnixMilli(1798115622000)
	nonce := "4428a206-1afd-4b15-a98d-43e91f49a08d"
	expectedSignature := "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4"

	convey.Convey("native", t, func() {
		req, err := http.NewRequest(method, fullUrl, bytes.NewReader(body))
		assert.Nil(t, err)
		assert.Nil(t, AddXHeaders(req.Header, apiKey, timestamp, nonce))
		assert.Nil(t, Sign(req, apiSecret))
		assert.Equal(t, expectedSignature, req.Header.Get(CustomHeaderSignature.String()))
	})

	convey.Convey("resty", t, func() {
		client := resty.New()
		defer assert.Nil(t, client.Close())
		r := client.R().SetBody(body).SetQueryParamsFromValues(query)
		r.SetTimeout(1).Execute(method, apiUrl)
		req := r.RawRequest
		assert.NotNil(t, req)
		assert.Nil(t, AddXHeaders(req.Header, apiKey, timestamp, nonce))
		assert.Nil(t, Sign(req, apiSecret))
		assert.Equal(t, expectedSignature, req.Header.Get(CustomHeaderSignature.String()))
	})

	convey.Convey("grequests", t, func() {
		grequests.Request(
			context.TODO(), method, fullUrl,
			grequests.RequestBody(bytes.NewReader(body)),
			grequests.RequestTimeout(1),
			grequests.BeforeRequest(func(req *http.Request) error {
				assert.Nil(t, AddXHeaders(req.Header, apiKey, timestamp, nonce))
				assert.Nil(t, Sign(req, apiSecret))
				assert.Equal(t, expectedSignature, req.Header.Get(CustomHeaderSignature.String()))
				return nil
			}),
		)
	})

	convey.Convey("gorequest", t, func() {
		agent := gorequest.New().CustomMethod(method, apiUrl).Query(query.Encode()).Send(string(body))
		req, err := agent.MakeRequest()
		assert.Nil(t, err)
		assert.NotNil(t, req)
		assert.Nil(t, AddXHeaders(req.Header, apiKey, timestamp, nonce))
		assert.Nil(t, Sign(req, apiSecret))
		assert.Equal(t, expectedSignature, req.Header.Get(CustomHeaderSignature.String()))
	})
}
