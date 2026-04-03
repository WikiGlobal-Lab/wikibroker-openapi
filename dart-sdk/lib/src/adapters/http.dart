import 'package:http/http.dart' as http;
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';

class HttpRequest implements RequestLike {
  final http.BaseRequest _raw;

  HttpRequest(this._raw);

  HeadersLike get headers => _raw.headers;

  String get method => _raw.method;

  String get url => '${_raw.url.path}?${_raw.url.query}';

  String get data =>
      _raw is http.Request ? _raw.encoding.decode(_raw.bodyBytes) : '';
}

class HttpClient extends http.BaseClient {
  final http.Client _inner;
  final UuidValue _apiKey;
  final String _apiSecret;
  final void Function(HeadersLike, String, DateTime, String) _loadHeaders;
  final void Function(RequestLike, String) _sign;
  final DateTime Function() _timestampGenerator;
  final UuidValue Function() _idGenerator;

  HttpClient(
    this._inner,
    this._apiKey,
    this._apiSecret,
    this._loadHeaders,
    this._sign,
    this._timestampGenerator,
    this._idGenerator,
  );

  @override
  Future<http.StreamedResponse> send(http.BaseRequest request) async {
    _loadHeaders(
      request.headers,
      _apiKey.uuid,
      _timestampGenerator(),
      _idGenerator().uuid,
    );
    _sign(HttpRequest(request), _apiSecret);
    return _inner.send(request);
  }
}
