typedef HeadersLike = Map<String, dynamic>;

interface class RequestLike {
  final HeadersLike headers = {};
  final String method = '';
  final Uri url = Uri();
  final String data = '';
}
