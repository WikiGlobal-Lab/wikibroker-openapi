from typing import Callable
from requests import PreparedRequest
from requests.auth import AuthBase
from ..common.types import Request, Headers
from uuid import UUID
from datetime import datetime


class RequestsRequest:
    def __init__(self, raw: PreparedRequest):
        self._raw = raw

    @property
    def headers(self) -> Headers:
        return self._raw.headers

    @property
    def method(self) -> str:
        return self._raw.method or ""

    @property
    def url(self) -> str:
        return self._raw.url or ""

    @property
    def data(self) -> bytes:
        if isinstance(self._raw.body, str):
            return self._raw.body.encode("utf-8")
        if isinstance(self._raw.body, bytes):
            return self._raw.body
        return "".encode("utf-8")


def load(raw: PreparedRequest) -> Request:
    return RequestsRequest(raw)


class Auth(AuthBase):
    def __init__(
        self,
        api_key: UUID,
        api_secret: str,
        load_headers: Callable[[Headers, UUID, datetime, UUID], None],
        sign: Callable[[Request, str], None],
        timestamp_generator: Callable[[], datetime],
        id_generator: Callable[[], UUID],
    ):
        self.api_key = api_key
        self.api_secret = api_secret
        self.sign = sign
        self.load_headers = load_headers
        self.timestamp_generator = timestamp_generator
        self.id_generator = id_generator

    def __call__(self, r: PreparedRequest):
        req = load(r)
        self.load_headers(
            req.headers, self.api_key, self.timestamp_generator(), self.id_generator()
        )
        self.sign(req, self.api_secret)
        return r
