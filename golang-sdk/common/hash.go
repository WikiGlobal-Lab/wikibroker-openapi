package common

import (
	"crypto/hmac"
	"crypto/sha256"
	"hash"
)

func Sha256Hash(message []byte) ([]byte, error) {
	return calculateHash(sha256.New(), message)
}

func HmacSha256(key, message []byte) ([]byte, error) {
	h := hmac.New(sha256.New, key)
	return calculateHash(h, message)
}

func calculateHash(h hash.Hash, data []byte) ([]byte, error) {
	_, err := h.Write(data)
	if err != nil {
		return nil, err
	}
	return h.Sum(nil), nil
}
