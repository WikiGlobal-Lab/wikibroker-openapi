import type { RequestLike } from "./common/interfaces.js";
import { hmacSha256, sha256Hash } from "./common/hash.js";
import {
  hexEncodeToString,
  isRequestUsePostMethod,
  newUrlWithFakeBase,
} from "./common/utils.js";
import { CustomHeaders } from "./common/enums.js";

export async function generateSignature(key: string, message: string) {
  const hash = await hmacSha256(key, message);
  return hexEncodeToString(hash);
}

export async function generateCanonicalString(req: RequestLike) {
  const method = req.method.toUpperCase();
  const path = newUrlWithFakeBase(req.url).pathname;
  const canonicalQuery = buildCanonicalQuery(req);
  const apiKey = req.headers.get(CustomHeaders.ApiKey);
  const timestamp = req.headers.get(CustomHeaders.Timestamp);
  const nonce = req.headers.get(CustomHeaders.Nonce);
  const bodyHash = await calculateBodyHash(req);
  return [
    method,
    path,
    canonicalQuery,
    apiKey,
    timestamp,
    nonce,
    bodyHash,
  ].join("\n");
}

async function calculateBodyHash(req: RequestLike) {
  const body = isRequestUsePostMethod(req.method) ? await req.data : "";
  const hash = await sha256Hash(
    typeof body === "string" ? body : JSON.stringify(body),
  );
  return hexEncodeToString(hash);
}

function buildCanonicalQuery(req: RequestLike) {
  const query = newUrlWithFakeBase(req.url).searchParams;
  const groups = {} as Record<string, string[]>;
  for (const k of query.keys()) {
    const validValues = query.getAll(k).filter((x) => x !== "");
    if (validValues.length === 0) {
      continue;
    }
    validValues.sort();
    groups[k] = validValues;
  }
  const parts = Object.entries(groups);
  parts.sort((a, b) => (a[0] > b[0] ? 1 : a[0] < b[0] ? -1 : 0));
  return parts
    .flatMap((item) => {
      const [key, values] = item;
      return values.map((value) => `${key}=${value}`);
    })
    .join("&");
}
