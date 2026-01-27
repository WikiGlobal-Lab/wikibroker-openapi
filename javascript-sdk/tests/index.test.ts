import { expect, test, describe } from "vitest";
import {
  addXHeaders,
  CustomHeaders,
  loadAxios,
  loadNative,
  sign,
} from "../src/index.js";
import { AxiosHeaders } from "axios";

describe("sign", () => {
  const baseURL = "https://api.example.com";
  const path = "test?q1=a&q2=b&q1=c";
  const url = `${baseURL}/${path}`;
  const body = /*json*/ { key: "value" };
  const method = "POST";
  const apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
  const apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
  const timestamp = new Date(1798115622000);
  const nonce = "4428a206-1afd-4b15-a98d-43e91f49a08d";
  const expectedSignature =
    "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

  test("native", () => {
    const raw = new Request(url, { body: JSON.stringify(body), method });
    const req = loadNative(raw);
    addXHeaders(req.headers, apiKey, timestamp, nonce);
    sign(req, apiSecret).then(() =>
      expect(req.headers.get(CustomHeaders.Signature)).toBe(expectedSignature),
    );
  });

  test("axios - full url", () => {
    const raw = {
      url,
      headers: new AxiosHeaders(),
      method,
      data: JSON.stringify(body),
    };
    const req = loadAxios(raw);
    addXHeaders(req.headers, apiKey, timestamp, nonce);
    sign(req, apiSecret).then(() =>
      expect(req.headers.get(CustomHeaders.Signature)).toBe(expectedSignature),
    );
  });

  test("axios - relative path", () => {
    const raw = {
      url: path,
      headers: new AxiosHeaders(),
      method,
      data: body,
    };
    const req = loadAxios(raw, JSON.stringify);
    addXHeaders(req.headers, apiKey, timestamp, nonce);
    sign(req, apiSecret).then(() =>
      expect(req.headers.get(CustomHeaders.Signature)).toBe(expectedSignature),
    );
  });
});
