const encoder = new TextEncoder();
const hash = "SHA-256";
const name = "HMAC";

export async function sha256Hash(message: Uint8Array<ArrayBuffer>) {
  return await crypto.subtle.digest(hash, message);
}

export async function hmacSha256(
  key: Uint8Array<ArrayBuffer>,
  message: Uint8Array<ArrayBuffer>,
) {
  const hashKey = await crypto.subtle.importKey(
    "raw",
    key,
    { name, hash },
    false,
    ["sign"],
  );
  return await crypto.subtle.sign(name, hashKey, message);
}
