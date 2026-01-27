export function hexEncodeToString(buf: ArrayBuffer) {
  return Array.from(new Uint8Array(buf))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
}

export function newUrlWithFakeBase(url: string) {
  return new URL(url, "https://api.example.com");
}

export function isRequestUsePostMethod<T extends string>(method: T) {
  return method.toUpperCase() === "POST";
}
