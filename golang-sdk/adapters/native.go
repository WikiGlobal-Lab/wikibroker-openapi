package adapters

import (
	"io"
	"net/http"
	"time"
)

type RawHttpClient interface {
	Do(req *http.Request) (*http.Response, error)
	Get(url string) (resp *http.Response, err error)
	Post(url string, contentType string, body io.Reader) (resp *http.Response, err error)
}

type HttpClient struct {
	RawHttpClient
	apiKey             string
	apiSecret          string
	loadHeaders        func(http.Header, string, time.Time, string) error
	sign               func(*http.Request, string) error
	timestampGenerator func() time.Time
	idGenerator        func() string
}

func NewHttpClient(
	raw RawHttpClient,
	apiKey, apiSecret string,
	loadHeaders func(http.Header, string, time.Time, string) error,
	sign func(*http.Request, string) error,
	timestampGenerator func() time.Time,
	idGenerator func() string,
) *HttpClient {
	return &HttpClient{
		RawHttpClient:      raw,
		apiKey:             apiKey,
		apiSecret:          apiSecret,
		loadHeaders:        loadHeaders,
		sign:               sign,
		timestampGenerator: timestampGenerator,
		idGenerator:        idGenerator,
	}
}

func (c *HttpClient) Do(req *http.Request) (resp *http.Response, err error) {
	err = c.loadHeaders(req.Header, c.apiKey, c.timestampGenerator(), c.idGenerator())
	if err != nil {
		return
	}
	err = c.sign(req, c.apiSecret)
	if err != nil {
		return
	}
	return c.RawHttpClient.Do(req)
}

func (c *HttpClient) Post(url, contentType string, body io.Reader) (resp *http.Response, err error) {
	req, err := http.NewRequest("POST", url, body)
	if err != nil {
		return nil, err
	}
	req.Header.Set("Content-Type", contentType)
	return c.Do(req)
}

func (c *HttpClient) Get(url string) (resp *http.Response, err error) {
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}
	return c.Do(req)
}
