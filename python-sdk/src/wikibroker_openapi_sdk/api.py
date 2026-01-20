from .common.types import Headers, Request
from .common.enums import CustomHeaders
from .core import generate_canonical_string, generate_signature
from uuid import UUID
from datetime import datetime


def add_x_headers(
    headers: Headers, api_key: UUID, timestamp: datetime, nonce: UUID
) -> None:
    headers[CustomHeaders.API_KEY.value] = str(api_key)
    headers[CustomHeaders.TIMESTAMP.value] = str(int(timestamp.timestamp() * 1000))
    headers[CustomHeaders.NONCE.value] = str(nonce)


def sign(req: Request, key: str) -> None:
    canonical_string = generate_canonical_string(req)
    signature = generate_signature(key, canonical_string)
    req.headers[CustomHeaders.SIGNATURE.value] = signature
