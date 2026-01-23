# WikiBroker OpenAPI JavaScript SDK

Support both JavaScript and TypeScript, both NodeJS and Browser.

```bash
npm install ./wikibroker-openapi-js-sdk-0.1.0-alpha.tgz
```

```javascript
import axios from 'axios'
import { QueryParseMode, sign, axiosHook } from "wikibroker-openapi-sdk"

// Global Setting
const hook = axiosHook(process.env.API_KEY, process.env.API_SECRET, JSON.stringify, QueryParseMode.Bracket)
axios.interceptors.request.use(hook) 
// or
axios.interceptors.request.use(async (config) => {
  // ... (Your handle logics)
  hook(config)
  // ... (Your handle logics)
})

// Send Post Request
axios.post('https://trade-max.client.wikibroker.com/v1/operation/popup/block',
  {
    ids: [0, 2]
  }
)

// Send Get Request
axios.get('https://trade-max.client.wikibroker.com/v1/client/account/balance/list',
  {
    params: {
      type: ["deposit_only", "normal"]
    }
  }
)
```
