# WikiBroker OpenAPI SDK

```bash
pip install ./wikibroker_openapi_sdk-0.1.0a0-py3-none-any.whl
```

```python
import requests
from wikibroker_openapi_sdk import build_requests_auth

auth = build_requests_auth('API_KEY','API_SECRET')
requests.get('https://api.example.com', auth=auth)
```
