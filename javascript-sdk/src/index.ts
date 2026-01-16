import type { UUID } from "crypto";
import type { HeadersLike, RequestLike } from "./interfaces.js";
import { generateCanonicalString, generateSignature } from "./core.js";

export function addXHeaders(
  headers: HeadersLike,
  apiKey: UUID,
  timestamp: Date,
  nonce: UUID
) {
  headers.set("X-Api-Key", apiKey);
  headers.set("X-Timestamp", timestamp.getTime().toString());
  headers.set("X-Nonce", nonce);
}

export async function sign(req: RequestLike, key: string) {
  const canonicalString = await generateCanonicalString(req);
  console.log("canonical string = ", canonicalString);
  const signature = await generateSignature(key, canonicalString);
  req.headers.set("X-Signature", signature);
}

export { load as loadNative } from "./adapters/native.js";
export { load as loadAxios, QueryParseMode } from "./adapters/axios.js";
