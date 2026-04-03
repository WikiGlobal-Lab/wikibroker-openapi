import 'package:uuid/uuid.dart';

bool isRequestUsePostMethod(String method) {
  return method.toUpperCase() == 'POST';
}

final _uuid = Uuid();

UuidValue newUuid() {
  return UuidValue.withValidation(_uuid.v4());
}
