import 'package:uuid/uuid.dart';

typedef HeadersLike = Map<String, dynamic>;

abstract interface class RequestLike {
  HeadersLike get headers;

  String get method;

  Uri get url;

  String get data;
}

typedef LoadHeaders = void Function(HeadersLike, UuidValue, DateTime, String);

typedef Sign = void Function(RequestLike, String);
