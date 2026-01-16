import type { RequestLike } from "./interfaces.js";
import {
  hexEncodeToString,
  hmacSha256,
  newUrlWithFakeBase,
  sha256Hash,
} from "./utils.js";

export async function generateSignature(key: string, message: string) {
  const hash = await hmacSha256(key, message);
  return hexEncodeToString(hash);
}

export async function generateCanonicalString(req: RequestLike) {
  const method = req.method.toUpperCase();
  const path = newUrlWithFakeBase(req.url).pathname;
  const canonicalQuery = buildCanonicalQuery(req);
  const apiKey = req.headers.get("X-Api-Key");
  const timestamp = req.headers.get("X-Timestamp");
  const nonce = req.headers.get("X-Nonce");
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
  const body = await req.data;
  const hash = await sha256Hash(
    typeof body === "string" ? body : JSON.stringify(body)
  );
  return hexEncodeToString(hash);
}

const ignoredQueryKeys = ["sign", "signature", "SIGN", "SIGNATURE"];

function buildCanonicalQuery(req: RequestLike) {
  const query = newUrlWithFakeBase(req.url).searchParams;
  const groups = {} as Record<string, string[]>;
  for (const k of query.keys()) {
    if (ignoredQueryKeys.includes(k)) {
      continue;
    }
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
    .map((item) => {
      const [key, values] = item;
      return values.map((value) => `${key}=${value}`).join("&");
    })
    .join("&");
}
