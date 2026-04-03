typedef HeadersLike = Map<String, dynamic>;

interface class RequestLike {
  final HeadersLike headers = {};
  final String method = '';
  final String url = '';
  final String data = '';
}
