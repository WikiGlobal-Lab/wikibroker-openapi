package adapters

import (
	"net/http"
	"time"

	"resty.dev/v3"
)

func NewRestyRequestMiddleware(
	apiKey, apiSecret string,
	loadHeaders func(http.Header, string, time.Time, string) error,
	sign func(*http.Request, string) error,
	timestampGenerator func() time.Time,
	idGenerator func() string,
) resty.RequestMiddleware {
	return func(c *resty.Client, r *resty.Request) (err error) {
		req := r.RawRequest
		err = loadHeaders(req.Header, apiKey, timestampGenerator(), idGenerator())
		if err != nil {
			return
		}
		return sign(req, apiSecret)
	}
}
