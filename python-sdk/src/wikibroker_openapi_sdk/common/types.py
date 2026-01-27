from typing import Protocol
from collections.abc import MutableMapping

Headers = MutableMapping[str, str]


class Request(Protocol):
    @property
    def headers(self) -> Headers: ...

    @property
    def method(self) -> str: ...

    @property
    def url(self) -> str: ...

    @property
    def data(self) -> bytes: ...
