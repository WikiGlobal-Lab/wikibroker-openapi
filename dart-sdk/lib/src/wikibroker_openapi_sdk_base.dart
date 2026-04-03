import 'package:http/http.dart' as http;
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/adapters/dio.dart';
import 'package:wikibroker_openapi_sdk/src/adapters/http.dart';
import 'package:wikibroker_openapi_sdk/src/common/enums.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';
import 'package:wikibroker_openapi_sdk/src/common/utils.dart';
import 'package:wikibroker_openapi_sdk/src/core.dart';

void addXHeaders(
  HeadersLike headers,
  String apiKey,
  DateTime timestamp,
  String nonce,
) {
  headers[CustomHeaders.apiKey.value] = UuidValue.withValidation(apiKey).uuid;
  headers[CustomHeaders.timestamp.value] = timestamp.millisecondsSinceEpoch
      .toString();
  headers[CustomHeaders.nonce.value] = nonce;
}

void sign(RequestLike req, String key) {
  final canonicalString = generateCanonicalString(req);
  final signature = generateSignature(key, canonicalString);
  req.headers[CustomHeaders.signature.value] = signature;
}

HttpClient createHttpClient(
  http.Client raw,
  UuidValue apiKey,
  String apiSecret,
) {
  return HttpClient(
    raw,
    apiKey,
    apiSecret,
    addXHeaders,
    sign,
    DateTime.now,
    newUuid,
  );
}

DioRequestInterceptor createDioRequestInterceptor(
  UuidValue apiKey,
  String apiSecret,
  String Function(dynamic)? serializer,
) {
  return DioRequestInterceptor(
    serializer ?? (x) => x as String,
    apiKey,
    apiSecret,
    addXHeaders,
    sign,
    DateTime.now,
    newUuid,
  );
}
