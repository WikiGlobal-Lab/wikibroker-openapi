package adapters

import (
	"net/http"
	"time"
)

func NewGRequestsHook(
	apiKey, apiSecret string,
	loadHeaders func(http.Header, string, time.Time, string) error,
	sign func(*http.Request, string) error,
	timestampGenerator func() time.Time,
	idGenerator func() string,
) func(*http.Request) error {
	return func(r *http.Request) (err error) {
		err = loadHeaders(r.Header, apiKey, timestampGenerator(), idGenerator())
		if err != nil {
			return
		}
		return sign(r, apiSecret)
	}
}
