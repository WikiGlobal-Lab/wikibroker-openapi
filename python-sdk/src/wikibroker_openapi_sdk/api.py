from .common.types import Headers, Request
from .common.enums import CustomHeaders
from .core import generate_canonical_string, generate_signature
from .adapters.requests import Auth as RequestsAuth
from uuid import UUID, uuid4
from datetime import datetime


def add_x_headers(
    headers: Headers, api_key: UUID, timestamp: datetime, nonce: UUID
) -> None:
    headers[CustomHeaders.API_KEY] = str(api_key)
    headers[CustomHeaders.TIMESTAMP] = str(int(timestamp.timestamp() * 1000))
    headers[CustomHeaders.NONCE] = str(nonce)


def sign(req: Request, key: str) -> None:
    canonical_string = generate_canonical_string(req)
    signature = generate_signature(key, canonical_string)
    req.headers[CustomHeaders.SIGNATURE] = signature


def build_requests_auth(api_key: str, api_secret: str):
    return RequestsAuth(
        UUID(api_key), api_secret, add_x_headers, sign, datetime.now, uuid4
    )
