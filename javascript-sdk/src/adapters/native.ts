import type { HeadersLike, RequestLike } from "../interfaces.js";

class NativeRequest implements RequestLike {
  private raw: Request;
  public headers: HeadersLike;
  constructor(raw: Request) {
    this.raw = raw;
    this.headers = {
      set: (field: string, value: string) => this.raw.headers.set(field, value),
      get: (f) => this.raw.headers.get(f) ?? "",
    };
  }
  public get data() {
    if (this.method.toUpperCase() !== "POST") {
      return new Promise<string>((resolve) => {
        resolve("");
      });
    }
    return this.raw.clone().text();
  }
  public get method() {
    return this.raw.method;
  }
  public get url() {
    return this.raw.url;
  }
}

export function load(raw: Request): RequestLike {
  return new NativeRequest(raw);
}
