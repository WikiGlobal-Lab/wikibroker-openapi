import 'package:dio/dio.dart';
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';

class DioRequest implements RequestLike {
  final RequestOptions _raw;
  String Function(dynamic) _serializer;

  DioRequest(this._raw, this._serializer);

  HeadersLike get headers => _raw.headers;

  String get method => _raw.method;

  Uri get url => _raw.uri;

  String get data => _serializer(_raw.data);
}

class DioRequestInterceptor extends Interceptor {
  final String Function(dynamic) _serializer;
  final UuidValue _apiKey;
  final String _apiSecret;
  final void Function(HeadersLike, String, DateTime, String) _loadHeaders;
  final void Function(RequestLike, String) _sign;
  final DateTime Function() _timestampGenerator;
  final UuidValue Function() _idGenerator;

  DioRequestInterceptor(
    this._serializer,
    this._apiKey,
    this._apiSecret,
    this._loadHeaders,
    this._sign,
    this._timestampGenerator,
    this._idGenerator,
  );

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    final proxy = DioRequest(options, _serializer);
    _loadHeaders(
      proxy.headers,
      _apiKey.uuid,
      _timestampGenerator(),
      _idGenerator().uuid,
    );
    _sign(proxy, _apiSecret);
    super.onRequest(options, handler);
  }
}
