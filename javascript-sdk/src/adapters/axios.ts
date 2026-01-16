import type { InternalAxiosRequestConfig } from "axios";
import type { HeadersLike, RequestLike } from "../common/interfaces.js";
import { isRequestUsePostMethod, newUrlWithFakeBase } from "../common/utils.js";
import { QueryParseMode } from "../common/enums.js";

class AxiosRequest implements RequestLike {
  private raw: InternalAxiosRequestConfig<string>;
  public headers: HeadersLike;
  public url: string;
  constructor(raw: InternalAxiosRequestConfig<string>, mode: QueryParseMode) {
    console.log(
      "data = ",
      raw.data,
      "url = ",
      raw.url,
      "params = ",
      raw.params
    );
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
      this.raw.params.forEach((value, key) => {
        if (value !== undefined) {
          origin.searchParams.set(key, value);
        }
      });
    } else {
      for (const [name, value] of Object.entries(this.raw.params)) {
        if (value === undefined) {
          continue;
        }
        if (value instanceof Array) {
          switch (mode) {
            case QueryParseMode.Bracket:
              value.forEach((item) =>
                origin.searchParams.append(`${name}[]`, String(item))
              );
              break;
            case QueryParseMode.Comma:
              origin.searchParams.set(name, value.join(","));
              break;
            case QueryParseMode.Repeat:
              value.forEach((item) =>
                origin.searchParams.append(name, String(item))
              );
              break;
          }
        } else {
          origin.searchParams.set(name, String(value));
        }
      }
    }
    return origin.href;
  }
}

export function load<T>(
  raw: InternalAxiosRequestConfig<T>,
  f: (data: T) => string = String,
  mode: QueryParseMode = QueryParseMode.Repeat
): RequestLike {
  return new AxiosRequest(
    {
      ...raw,
      data: f(raw.data!),
    },
    mode
  );
}
