from enum import Enum


class CustomHeaders(Enum):
    API_KEY = "X-Api-Key"
    TIMESTAMP = "X-Timestamp"
    NONCE = "X-Nonce"
    SIGNATURE = "X-Signature"

    def __str__(self):
        return self.value
