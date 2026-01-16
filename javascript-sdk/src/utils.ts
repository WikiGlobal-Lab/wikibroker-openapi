export function hexEncodeToString(buf: ArrayBuffer) {
  return Array.from(new Uint8Array(buf))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
}

const encoder = new TextEncoder();

export async function sha256Hash(message: string) {
  const data = encoder.encode(message);
  return await crypto.subtle.digest("SHA-256", data);
}

export async function hmacSha256(key: string, message: string) {
  const hashKey = await crypto.subtle.importKey(
    "raw",
    encoder.encode(key),
    {
      name: "HMAC",
      hash: "SHA-256",
    },
    false,
    ["sign"]
  );
  const hash = await crypto.subtle.sign(
    "HMAC",
    hashKey,
    encoder.encode(message)
  );
  return hash;
}

const fakeBaseURL = "https://api.example.com";

export function newUrlWithFakeBase(url: string) {
  return new URL(url, fakeBaseURL);
}
