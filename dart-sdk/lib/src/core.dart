import 'dart:convert';

import 'package:wikibroker_openapi_sdk/src/common/enums.dart';
import 'package:wikibroker_openapi_sdk/src/common/hash.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';
import 'package:wikibroker_openapi_sdk/src/common/utils.dart';

String generateSignature(String key, String message) {
  return hmacSha256(utf8.encode(key), utf8.encode(message)).toString();
}

String generateCanonicalString(RequestLike req) {
  final method = req.method;
  final path = Uri.parse(req.url).path;
  final canonicalQuery = _buildCanonicalQuery(req);
  final apiKey = req.headers[CustomHeaders.apiKey.value] ?? '';
  final timestamp = req.headers[CustomHeaders.timestamp.value] ?? '';
  final nonce = req.headers[CustomHeaders.nonce.value] ?? '';
  final bodyHash = _calculateBodyHash(req);
  return [
    method,
    path,
    canonicalQuery,
    apiKey,
    timestamp,
    nonce,
    bodyHash,
  ].join('\n');
}

String _calculateBodyHash(RequestLike req) {
  final body = isRequestUsePostMethod(req.method) ? req.data : '';
  return sha256Hash(utf8.encode(body)).toString();
}

String _buildCanonicalQuery(RequestLike req) {
  final query = Uri.parse(req.url).queryParametersAll;
  var pairs = <String, List<String>>{};
  query.entries.forEach(
    (pair) => pairs[pair.key] = List<String>.from(pair.value)..sort(),
  );
  var parts = pairs.entries.toList()..sort((a, b) => a.key.compareTo(b.key));
  return parts
      .map((pair) => pair.value.map((v) => '${pair.key}=$v').join('&'))
      .join('&');
}
