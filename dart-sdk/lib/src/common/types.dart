typedef HeadersLike = Map<String, String?>;

typedef RequestLike = ({
  HeadersLike headers,
  String method,
  String url,
  String data,
});
