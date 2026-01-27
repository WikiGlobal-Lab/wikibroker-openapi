from typing import Callable
from aiohttp import ClientRequest, ClientHandlerType, ClientResponse
from asyncer import syncify, asyncify
from ..common.types import Request, Headers
from uuid import UUID
from datetime import datetime


class AiohttpRequest:
    def __init__(self, raw: ClientRequest):
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
        return syncify(self._raw.body.as_bytes)()


def load(raw: ClientRequest) -> Request:
    return AiohttpRequest(raw)


def build_auth(
    api_key: UUID,
    api_secret: str,
    load_headers: Callable[[Headers, UUID, datetime, UUID], None],
    sign: Callable[[Request, str], None],
    timestamp_generator: Callable[[], datetime],
    id_generator: Callable[[], UUID],
):
    async def auth_middleware(
        req: ClientRequest, handler: ClientHandlerType
    ) -> ClientResponse:
        r = load(req)
        load_headers(r.headers, api_key, timestamp_generator(), id_generator())
        await asyncify(sign)(r, api_secret)
        return await handler(req)

    return auth_middleware
