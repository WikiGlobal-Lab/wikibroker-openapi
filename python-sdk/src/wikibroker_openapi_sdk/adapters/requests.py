from typing import Callable
from requests import PreparedRequest
from requests.auth import AuthBase
from ..common.types import Request
from ..common.utils import DictProxy


def load(raw: PreparedRequest) -> Request:
    data = "".encode("utf-8")
    if isinstance(raw.body, str):
        data = raw.body.encode("utf-8")
    if isinstance(raw.body, bytes):
        data = raw.body
    return Request(
        headers=DictProxy(raw.headers),
        method=raw.method or "",
        url=raw.url or "",
        data=data,
    )


class Auth(AuthBase):
    def __init__(self, key: str, sign: Callable[[Request, str], None]):
        self.key = key
        self.sign = sign

    def __call__(self, r: PreparedRequest):
        self.sign(load(r), self.key)
