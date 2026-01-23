package wikibroker_openapi_sdk

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"testing"
	"time"

	"github.com/smartystreets/goconvey/convey"
	"github.com/stretchr/testify/assert"
)

func TestSign(t *testing.T) {
	baseURL := "https://api.example.com"
	path := "test?q1=a&q2=b&q1=c"
	url := fmt.Sprintf("%s/%s", baseURL, path)
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
		req, err := http.NewRequest(method, url, bytes.NewReader(body))
		assert.Nil(t, err)
		assert.Nil(t, AddXHeaders(req.Header, apiKey, timestamp, nonce))
		assert.Nil(t, Sign(req, apiSecret))
		assert.Equal(t, expectedSignature, req.Header.Get(CustomHeaderSignature.String()))
	})
}
