import httpx
from typing import Callable
from ..common.types import Headers, Request
from uuid import UUID
from datetime import datetime


class HttpxRequest:
    def __init__(self, raw: httpx.Request):
        self._raw = raw

    @property
    def headers(self) -> Headers:
        return self._raw.headers

    @property
    def method(self) -> str:
        return self._raw.method

    @property
    def url(self) -> str:
        return str(self._raw.url)

    @property
    def data(self) -> bytes:
        return self._raw.content


def load(raw: httpx.Request) -> Request:
    return HttpxRequest(raw)


class Auth(httpx.Auth):
    requires_request_body = True

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

    def auth_flow(self, r: httpx.Request):
        req = load(r)
        self.load_headers(
            req.headers, self.api_key, self.timestamp_generator(), self.id_generator()
        )
        self.sign(req, self.api_secret)
        yield r
