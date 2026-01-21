from typing import Protocol
from collections.abc import MutableMapping

Headers = MutableMapping[str, str]


class Request(Protocol):
    headers: Headers
    method: str
    url: str

    @property
    def data(self) -> bytes: ...
