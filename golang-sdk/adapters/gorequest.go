package adapters

import (
	"net/http"
	"time"
)

type goRequestInterceptor struct {
	http.RoundTripper
	apiKey             string
	apiSecret          string
	loadHeaders        func(http.Header, string, time.Time, string) error
	sign               func(*http.Request, string) error
	timestampGenerator func() time.Time
	idGenerator        func() string
}

func NewGorequestInterceptor(
	raw http.RoundTripper,
	apiKey, apiSecret string,
	loadHeaders func(http.Header, string, time.Time, string) error,
	sign func(*http.Request, string) error,
	timestampGenerator func() time.Time,
	idGenerator func() string,
) http.RoundTripper {
	return &goRequestInterceptor{
		RoundTripper:       raw,
		apiKey:             apiKey,
		apiSecret:          apiSecret,
		loadHeaders:        loadHeaders,
		sign:               sign,
		timestampGenerator: timestampGenerator,
		idGenerator:        idGenerator,
	}
}

func (r *goRequestInterceptor) RoundTrip(req *http.Request) (resp *http.Response, err error) {
	err = r.loadHeaders(req.Header, r.apiKey, r.timestampGenerator(), r.idGenerator())
	if err != nil {
		return
	}
	err = r.sign(req, r.apiSecret)
	if err != nil {
		return
	}
	return r.RoundTripper.RoundTrip(req)
}
