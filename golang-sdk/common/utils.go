package common

import (
	"bytes"
	"cmp"
	"errors"
	"io"
	"net/http"
)

func ReadRequestBody(r *http.Request) (data []byte, err error) {
	data, err = io.ReadAll(r.Body)
	if errors.Is(err, io.EOF) {
		err = nil
	}
	if err != nil {
		return nil, err
	}
	r.Body = io.NopCloser(bytes.NewBuffer(data))
	return data, nil
}

func Filter[T any](f func(T) bool, s []T) []T {
	output := make([]T, 0)
	for _, item := range s {
		if f(item) {
			output = append(output, item)
		}
	}
	return output
}

func MapItems[K comparable, V any](m map[K]V) []*MapItem[K, V] {
	items := make([]*MapItem[K, V], len(m))
	i := 0
	for k, v := range m {
		items[i] = &MapItem[K, V]{
			Key:   k,
			Value: v,
		}
		i++
	}
	return items
}

func SortByKey[K cmp.Ordered, V any](a, b *MapItem[K, V]) int {
	return cmp.Compare(a.Key, b.Key)
}
