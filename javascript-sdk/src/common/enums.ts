export const enum CustomHeaders {
  ApiKey = "X-Api-Key",
  Timestamp = "X-Timestamp",
  Nonce = "X-Nonce",
  Signature = "X-Signature",
}

export const enum QueryParseMode {
  Repeat, // https://api.example.com?q1=a&q1=b&q2=c
  Bracket, // https://api.example.com?q1[]=a&q1[]=b&q2=c
  Comma, // https://api.example.com?q1=a,b&q2=c
}
