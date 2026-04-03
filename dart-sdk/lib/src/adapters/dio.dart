import 'package:dio/dio.dart';
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';

class DioRequest implements RequestLike {
  final RequestOptions _raw;
  String Function(dynamic) _serializer;

  DioRequest(this._raw, this._serializer);

  @override
  HeadersLike get headers => _raw.headers;

  @override
  String get method => _raw.method;

  @override
  Uri get url => _raw.uri;

  @override
  String get data => _serializer(_raw.data);
}

class DioRequestInterceptor extends Interceptor {
  final String Function(dynamic) _serializer;
  final UuidValue _apiKey;
  final String _apiSecret;
  final LoadHeaders _loadHeaders;
  final Sign _sign;
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
      _apiKey,
      _timestampGenerator(),
      _idGenerator().uuid,
    );
    _sign(proxy, _apiSecret);
    super.onRequest(options, handler);
  }
}
