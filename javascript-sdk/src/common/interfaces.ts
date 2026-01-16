export interface HeadersLike {
  set(field: string, value: string): void;
  get(field: string): string;
}

export interface RequestLike {
  headers: HeadersLike;
  method: string;
  url: string;
  data: Promise<string>;
}
