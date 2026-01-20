from enum import Enum, auto


class CustomHeaders(Enum):
    API_KEY = "X-Api-Key"
    TIMESTAMP = "X-Timestamp"
    NONCE = "X-Nonce"
    SIGNATURE = "X-Signature"


class QueryParseMode(Enum):
    REPEAT = auto()  # https://api.example.com?q1=a&q1=b&q2=c
    BRACKET = auto()  # https://api.example.com?q1[]=a&q1[]=b&q2=c
    COMMA = auto()  # https://api.example.com?q1=a,b&q2=c
