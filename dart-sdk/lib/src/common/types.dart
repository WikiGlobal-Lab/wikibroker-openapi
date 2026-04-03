import 'package:uuid/uuid.dart';

typedef HeadersLike = Map<String, dynamic>;

interface class RequestLike {
  final HeadersLike headers = {};
  final String method = '';
  final Uri url = Uri();
  final String data = '';
}

typedef LoadHeaders = void Function(HeadersLike, UuidValue, DateTime, String);

typedef Sign = void Function(RequestLike, String);
