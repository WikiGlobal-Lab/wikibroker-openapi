import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:test/test.dart';
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/adapters/http.dart';
import 'package:wikibroker_openapi_sdk/wikibroker_openapi_sdk.dart';

void main() {
  group('Test sign', () {
    final baseURL = 'https://api.example.com';
    final path = 'test?q1=c&q2=b&q1=a';
    final url = '$baseURL/$path';
    final body = {'key': 'value'};
    final method = 'POST';
    final apiKey = UuidValue.withValidation(
      "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b",
    );
    final apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
    final nonce = UuidValue.withValidation(
      "4428a206-1afd-4b15-a98d-43e91f49a08d",
    );
    final timestamp = DateTime.fromMillisecondsSinceEpoch(1798115622000);
    final expectedSignature =
        "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

    setUp(() {
      // Additional setup goes here.
    });

    // test('Core Test', () {
    //   final req = (
    //     headers: <String, String?>{},
    //     method: method,
    //     url: url,
    //     data: jsonEncode(body),
    //   );
    //   addXHeaders(req.headers, apiKey.uuid, timestamp, nonce.uuid);
    //   sign(req, apiSecret);
    //   final signature = req.headers[CustomHeaders.signature.value];
    //   expect(signature, expectedSignature);
    // });

    test('http test', () async {
      final client = HttpClient(
        http.Client(),
        apiKey,
        apiSecret,
        addXHeaders,
        sign,
        () => timestamp,
        () => nonce,
      );
      final req = http.Request(method, Uri.parse(url))..body = jsonEncode(body);
      try {
        await client.send(req).timeout(Duration(milliseconds: 1));
      } catch (_) {}
      final signature = req.headers[CustomHeaders.signature.value];
      expect(signature, expectedSignature);
    });

    test('dio test', () {});
  });
}
