package adapters

import (
	"fmt"
	"io"
	"net/http"
	"net/url"
	"time"

	"resty.dev/v3"
)

func NewRestyRequestMiddleware(
	apiKey, apiSecret string,
	loadHeaders func(http.Header, string, time.Time, string) error,
	sign func(*http.Request, string) error,
	timestampGenerator func() time.Time,
	idGenerator func() string,
	bodyBuilder func(body any) (io.ReadCloser, error),
) resty.RequestMiddleware {
	return func(c *resty.Client, r *resty.Request) (err error) {
		err = loadHeaders(r.Header, apiKey, timestampGenerator(), idGenerator())
		if err != nil {
			return
		}
		url, err := url.Parse(fmt.Sprintf("%s?%s", r.URL, r.QueryParams.Encode()))
		if err != nil {
			return
		}
		body, err := bodyBuilder(r.Body)
		if err != nil {
			return
		}
		req := &http.Request{
			URL:    url,
			Method: r.Method,
			Header: r.Header,
			Body:   body,
		}
		return sign(req, apiSecret)
	}
}
