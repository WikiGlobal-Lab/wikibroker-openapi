import 'package:http/http.dart' as http;
import 'package:uuid/uuid.dart';
import 'package:wikibroker_openapi_sdk/src/common/types.dart';

extension HttpRequest on http.Request {
  String readBody() {
    return encoding.decode(bodyBytes);
  }
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
    this._loadHeaders(
      request.headers,
      this._apiKey.uuid,
      this._timestampGenerator(),
      this._idGenerator().uuid,
    );
    this._sign(await this._requestInfo(request), this._apiSecret);
    return _inner.send(request);
  }

  Future<RequestLike> _requestInfo(http.BaseRequest request) async {
    return (
      headers: request.headers,
      method: request.method,
      url: '${request.url.path}?${request.url.query}',
      data: request is http.Request ? request.readBody() : '',
    );
  }
}
