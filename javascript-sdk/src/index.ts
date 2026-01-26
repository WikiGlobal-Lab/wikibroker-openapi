import type { UUID } from "crypto";
import type { HeadersLike, RequestLike } from "./common/interfaces.js";
import { generateCanonicalString, generateSignature } from "./core.js";
import { CustomHeaders, QueryParseMode } from "./common/enums.js";
import { load as loadAxios } from "./adapters/axios.js";
import type { InternalAxiosRequestConfig } from "axios";
import { load as loadNative } from "./adapters/native.js";

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

export { loadAxios, loadNative };
export { QueryParseMode, CustomHeaders } from "./common/enums.js";
export type { HeadersLike, RequestLike } from "./common/interfaces.js";

export function axiosHook<T>(
  apiKey: UUID,
  apiSecret: string,
  f?: (data: T) => string,
  mode?: QueryParseMode,
): (config: InternalAxiosRequestConfig) => Promise<InternalAxiosRequestConfig> {
  return async (config: InternalAxiosRequestConfig) => {
    const req = loadAxios(config, f, mode);
    addXHeaders(req.headers, apiKey, new Date(), crypto.randomUUID());
    await sign(req, apiSecret);
    return config;
  };
}

export function wrappedFetch(
  apiKey: UUID,
  apiSecret: string,
): (req: Request) => Promise<Response> {
  return async (raw: Request) => {
    const req = loadNative(raw);
    addXHeaders(req.headers, apiKey, new Date(), crypto.randomUUID());
    await sign(req, apiSecret);
    return await fetch(raw);
  };
}
