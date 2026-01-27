import type { HeadersLike, RequestLike } from "../common/interfaces.js";
import { isRequestUsePostMethod } from "../common/utils.js";

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
    if (isRequestUsePostMethod(this.method)) {
      return this.raw.clone().text();
    }
    return new Promise<string>((resolve) => {
      resolve("");
    });
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
