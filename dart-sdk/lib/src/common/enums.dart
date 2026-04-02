enum CustomHeaders {
  apiKey(value: 'X-Api-Key'),
  timestamp(value: 'X-Timestamp'),
  nonce(value: 'X-Nonce'),
  signature(value: 'X-Signature');

  const CustomHeaders({required this.value});

  final String value;
}
