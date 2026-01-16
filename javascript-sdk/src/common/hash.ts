const encoder = new TextEncoder();
const hash = "SHA-256";
const name = "HMAC";

export async function sha256Hash(message: string) {
  const data = encoder.encode(message);
  return await crypto.subtle.digest(hash, data);
}

export async function hmacSha256(key: string, message: string) {
  const hashKey = await crypto.subtle.importKey(
    "raw",
    encoder.encode(key),
    {
      name,
      hash,
    },
    false,
    ["sign"]
  );
  return await crypto.subtle.sign(name, hashKey, encoder.encode(message));
}
