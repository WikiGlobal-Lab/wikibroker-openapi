import type { InternalAxiosRequestConfig } from "axios";
import type { HeadersLike, RequestLike } from "../common/interfaces.js";
import { isRequestUsePostMethod, newUrlWithFakeBase } from "../common/utils.js";
import { QueryParseMode } from "../common/enums.js";

class AxiosRequest implements RequestLike {
  private raw: InternalAxiosRequestConfig<string>;
  public headers: HeadersLike;
  public url: string;

  constructor(raw: InternalAxiosRequestConfig<string>, mode: QueryParseMode) {
    this.raw = raw;
    this.headers = {
      set: (field: string, value: string) => {
        this.raw.headers.set(field, value, true);
      },
      get: (field: string) => (this.raw.headers.get(field) ?? "").toString(),
    };
    this.url = this.loadUrl(mode);
  }

  public get data() {
    return new Promise<string>((resolve) => {
      if (isRequestUsePostMethod(this.method)) {
        resolve(this.raw.data ?? "");
      } else {
        resolve("");
      }
    });
  }

  public get method() {
    return this.raw.method ?? "";
  }

  private loadUrl(mode: QueryParseMode) {
    if (!this.raw.params) {
      return this.raw.url ?? "";
    }
    const origin = newUrlWithFakeBase(this.raw.url ?? "");
    if (this.raw.params instanceof URLSearchParams) {
      this.copyQuery(origin.searchParams);
    } else {
      this.loadQuery(origin.searchParams, mode);
    }
    return origin.href;
  }

  private copyQuery(receiver: URLSearchParams) {
    (this.raw.params as URLSearchParams).forEach((value, key) => {
      if (value !== undefined) {
        receiver.set(key, value);
      }
    });
  }

  private loadQuery(receiver: URLSearchParams, mode: QueryParseMode) {
    for (const [name, value] of Object.entries(this.raw.params)) {
      if (value === undefined) {
        continue;
      }
      if (!(value instanceof Array)) {
        receiver.set(name, String(value));
        continue;
      }
      switch (mode) {
        case QueryParseMode.Bracket:
          value.forEach((item) => receiver.append(`${name}[]`, String(item)));
          break;
        case QueryParseMode.Comma:
          receiver.set(name, value.join(","));
          break;
        case QueryParseMode.Repeat:
          value.forEach((item) => receiver.append(name, String(item)));
          break;
      }
    }
  }
}

export function load<T>(
  raw: InternalAxiosRequestConfig<T>,
  f: (data: T) => string = String,
  mode: QueryParseMode = QueryParseMode.Repeat,
): RequestLike {
  return new AxiosRequest(
    {
      ...raw,
      data: f(raw.data!),
    },
    mode,
  );
}
