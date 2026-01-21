from datetime import datetime
from uuid import UUID
from wikibroker_openapi_sdk import add_x_headers, sign
from wikibroker_openapi_sdk.adapters.requests import Auth as RequestsAuth
from wikibroker_openapi_sdk.adapters.httpx import Auth as HttpxAuth
from wikibroker_openapi_sdk.adapters.aiohttp import build_auth
from wikibroker_openapi_sdk.common.enums import CustomHeaders
from requests import Request
from httpx import Request as HttpxRequest
from aiohttp import ClientRequest
from yarl import URL
from asyncer import asyncify
import asyncio


class TestApi:
    base_url = "https://api.example.com"
    path = "test?q1=a&q2=b&q1=c"
    body = '{"key":"value"}'
    method = "POST"
    api_key = UUID("ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b")
    api_secret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4"
    timestamp = datetime.fromtimestamp(1798115622000 / 1000)
    nonce = "4428a206-1afd-4b15-a98d-43e91f49a08d"
    expected_signature = (
        "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4"
    )

    @property
    def url(self):
        return f"{self.base_url}/{self.path}"

    def test_requests(self):
        raw = Request(method=self.method, url=self.url, data=self.body).prepare()
        auth = RequestsAuth(
            api_key=self.api_key,
            api_secret=self.api_secret,
            load_headers=add_x_headers,
            sign=sign,
            timestamp_generator=lambda: self.timestamp,
            id_generator=lambda: self.nonce,
        )
        auth(raw)
        assert raw.headers[str(CustomHeaders.SIGNATURE)] == self.expected_signature

    def test_httpx(self):
        raw = HttpxRequest(method=self.method, url=self.url, content=self.body)
        auth = HttpxAuth(
            api_key=self.api_key,
            api_secret=self.api_secret,
            load_headers=add_x_headers,
            sign=sign,
            timestamp_generator=lambda: self.timestamp,
            id_generator=lambda: self.nonce,
        )
        next(auth.auth_flow(raw))
        assert raw.headers[str(CustomHeaders.SIGNATURE)] == self.expected_signature

    def test_aiohttp(self):
        raw = ClientRequest(method=self.method, url=URL(self.url), data=self.body)
        m = build_auth(
            api_key=self.api_key,
            api_secret=self.api_secret,
            load_headers=add_x_headers,
            sign=sign,
            timestamp_generator=lambda: self.timestamp,
            id_generator=lambda: self.nonce,
        )

        asyncio.run(m(raw, asyncify(lambda x: x)))
        assert raw.headers[str(CustomHeaders.SIGNATURE)] == self.expected_signature
