import 'dart:convert';
import 'package:crypto/crypto.dart';

/// 代表一个类似的请求对象接口
class RequestLike {
  final String method;
  final String url;
  final Map<String, dynamic> headers;
  final dynamic body; // 可以是 String 或 Map/List 等

  RequestLike({
    required this.method,
    required this.url,
    required this.headers,
    this.body,
  });
}

/// 自定义头部键名
class CustomHeaders {
  static const String apiKey = 'X-Api-Key';
  static const String timestamp = 'X-Timestamp';
  static const String nonce = 'X-Nonce';
  static const String signature = 'X-Signature';
}

/// 生成 HMAC-SHA256 签名
Future<String> generateSignature(String key, String message) async {
  final keyBytes = utf8.encode(key);
  final messageBytes = utf8.encode(message);
  final hmac = Hmac(sha256, keyBytes);
  final digest = hmac.convert(messageBytes);
  return digest.toString(); // hex 编码
}

/// 生成规范字符串
Future<String> generateCanonicalString(RequestLike req) async {
  final method = req.method.toUpperCase();
  
  // 解析 URL 获取 pathname (模拟 newUrlWithFakeBase)
  final uri = Uri.parse(req.url);
  final path = uri.path;
  
  final canonicalQuery = buildCanonicalQuery(req.url);
  
  final apiKey = req.headers[CustomHeaders.apiKey] ?? '';
  final timestamp = req.headers[CustomHeaders.timestamp] ?? '';
  final nonce = req.headers[CustomHeaders.nonce] ?? '';
  
  final bodyHash = await calculateBodyHash(req.method, req.body);

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

/// 计算请求体哈希
Future<String> calculateBodyHash(String method, dynamic body) async {
  String bodyStr = '';
  if (_isPostMethod(method)) {
    if (body != null) {
      bodyStr = body is String ? body : jsonEncode(body);
    }
  }
  
  final messageBytes = utf8.encode(bodyStr);
  final hash = sha256.convert(messageBytes);
  return hash.toString();
}

bool _isPostMethod(String method) {
  return method.toUpperCase() == 'POST';
}

/// 构建规范查询字符串
String buildCanonicalQuery(String url) {
  final uri = Uri.parse(url);
  final queryParameters = uri.queryParametersAll;
  
  final groups = <String, List<String>>{};
  
  for (final entry in queryParameters.entries) {
    final key = entry.key;
    // 过滤空值
    final validValues = entry.value.where((x) => x.isNotEmpty).toList();
    
    if (validValues.isEmpty) {
      continue;
    }
    
    validValues.sort();
    groups[key] = validValues;
  }
  
  final keys = groups.keys.toList();
  keys.sort();
  
  final parts = <String>[];
  for (final key in keys) {
    final values = groups[key]!;
    for (final value in values) {
      parts.add('$key=$value');
    }
  }
  
  return parts.join('&');
}

/// 为请求添加签名头
Future<void> signRequest(RequestLike req, String apiSecret) async {
  final canonicalString = await generateCanonicalString(req);
  final signature = await generateSignature(apiSecret, canonicalString);
  req.headers[CustomHeaders.signature] = signature;
}

/// 辅助方法：添加必要的 X-Headers
void addXHeaders(
  Map<String, dynamic> headers,
  String apiKey,
  DateTime timestamp,
  String nonce,
) {
  headers[CustomHeaders.apiKey] = apiKey;
  headers[CustomHeaders.timestamp] = timestamp.millisecondsSinceEpoch.toString();
  headers[CustomHeaders.nonce] = nonce;
}