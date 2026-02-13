import hmac
import hashlib
from .common.types import Request
from .common.enums import CustomHeaders
from urllib.parse import urlparse, parse_qsl


def generate_signature(key: str, message: str) -> str:
    return (
        hmac.new(
            key.encode("utf-8"),
            message.encode("utf-8"),
            hashlib.sha256,
        )
        .digest()
        .hex()
    )


def generate_canonical_string(req: Request) -> str:
    method = req.method.upper()
    path = urlparse(req.url).path
    canonical_query = build_canonical_query(req)
    api_key = req.headers[str(CustomHeaders.API_KEY)]
    timestamp = req.headers[str(CustomHeaders.TIMESTAMP)]
    nonce = req.headers[str(CustomHeaders.NONCE)]
    body_hash = calculate_body_hash(req)
    return "\n".join(
        [
            method,
            path,
            canonical_query,
            api_key,
            timestamp,
            nonce,
            body_hash,
        ]
    )


def calculate_body_hash(req: Request) -> str:
    body = req.data if req.method == "POST" else "".encode("utf-8")
    print(f"{body =}")
    return hashlib.sha256(body).digest().hex()


def build_canonical_query(req: Request) -> str:
    query = parse_qsl(urlparse(req.url).query)
    query.sort()
    return "&".join(map(lambda x: f"{x[0]}={x[1]}", query))
