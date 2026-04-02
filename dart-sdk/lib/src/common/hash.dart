import 'dart:convert';
import 'dart:typed_data';

import 'package:crypto/crypto.dart';

Digest sha256Hash(Uint8List message) {
  return _calculateHash(sha256, message);
}

Digest hmacSha256(Uint8List key, Uint8List message) {
  final h = Hmac(sha256, key);
  return _calculateHash(h, message);
}

Digest _calculateHash(Converter<List<int>, Digest> h, Uint8List data) {
  return h.convert(data);
}
