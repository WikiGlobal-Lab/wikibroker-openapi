import type { UUID } from "crypto";
import type { HeadersLike, RequestLike } from "./common/interfaces.js";
import { generateCanonicalString, generateSignature } from "./core.js";
import { CustomHeaders } from "./common/enums.js";

export function addXHeaders(
  headers: HeadersLike,
  apiKey: UUID,
  timestamp: Date,
  nonce: UUID,
) {
  headers.set(CustomHeaders.ApiKey, apiKey);
  headers.set(CustomHeaders.Timestamp, timestamp.getTime().toString());
  headers.set(CustomHeaders.Nonce, nonce);
}

export async function sign(req: RequestLike, key: string) {
  const canonicalString = await generateCanonicalString(req);
  const signature = await generateSignature(key, canonicalString);
  req.headers.set(CustomHeaders.Signature, signature);
}

export { load as loadNative } from "./adapters/native.js";
export { load as loadAxios } from "./adapters/axios.js";
export { QueryParseMode, CustomHeaders } from "./common/enums.js";
export type { HeadersLike, RequestLike } from "./common/interfaces.js";
