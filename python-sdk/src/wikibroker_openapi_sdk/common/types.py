from dataclasses import dataclass

Headers = dict[str, str]


@dataclass
class Request:
    headers: Headers
    method: str
    url: str
    data: bytes
