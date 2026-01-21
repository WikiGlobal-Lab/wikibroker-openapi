from typing import Callable
from requests import PreparedRequest
from requests.auth import AuthBase
from ..common.types import Request, Headers
from ..common.utils import DictProxy
from uuid import UUID
from datetime import datetime


class RequestsRequest:

    def __init__(self, raw: PreparedRequest):
        self.raw = raw
        self.headers: Headers = DictProxy(raw.headers)
        self.method = raw.method or ""
        self.url = raw.url or ""

    @property
    def data(self) -> bytes:
        if isinstance(self.raw.body, str):
            return self.raw.body.encode("utf-8")
        if isinstance(self.raw.body, bytes):
            return self.raw.body
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
