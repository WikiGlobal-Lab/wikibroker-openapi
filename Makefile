.PHONY: build build-js build-go build-py build-java build-php

GO_SDK_VERSION = 1.0.0

build:
	@echo "清理已构建SDK包..."
	@echo ""
	@rm -rf wikibroker*.tgz wikibroker*.whl wikibroker*.jar wikibroker*.zip
	@echo ""
	@echo "开始构建所有SDK..."
	@echo ""
	@$(MAKE) build-js && $(MAKE) build-go && $(MAKE) build-py && $(MAKE) build-java && $(MAKE) build-php
	@echo ""
	@echo "所有SDK构建成功！"

build-js:
	@echo "[1/5] 开始构建 JavaScript SDK..."
	@cd javascript-sdk && rm -rf dist && pnpm pack && rename 's/openapi-sdk/openapi-js-sdk/' wikibroker-*.tgz && mv wikibroker-*.tgz ..
	@echo "[1/5] JavaScript SDK 构建完成 ✓"
	@echo ""

build-go:
	@echo "[2/5] 开始构建 Go SDK..."
	@cp -r golang-sdk wikibroker_openapi_sdk && tar zcf wikibroker-openapi-go-sdk-$(GO_SDK_VERSION).tgz wikibroker_openapi_sdk && rm -rf wikibroker_openapi_sdk
	@echo "[2/5] Go SDK 构建完成 ✓"
	@echo ""

build-py:
	@echo "[3/5] 开始构建 Python SDK..."
	@cd python-sdk && rm -rf dist && uv build && mv dist/wikibroker*.whl ..
	@echo "[3/5] Python SDK 构建完成 ✓"
	@echo ""

build-java:
	@echo "[4/5] 开始构建 Java SDK..."
	@cd java-sdk && rm -rf target && mvn package && mv target/wikibroker*.jar ..
	@echo "[4/5] Java SDK 构建完成 ✓"
	@echo ""

build-php:
	@echo "[5/5] 开始构建 PHP SDK..."
	@cd php-sdk && composer build && rename 's/wikiglobal-wikibroker-openapi-sdk/wikibroker-openapi-php-sdk/' wikiglobal*.zip && mv wikibroker*.zip ..
	@echo "[5/5] PHP SDK 构建完成 ✓"
	@echo ""

.PHONY: test test-js test-go test-py test-java test-php

test:
	@echo "运行所有 SDK 测试..."
	@$(MAKE) test-js && $(MAKE) test-go && $(MAKE) test-py && $(MAKE) test-java && $(MAKE) test-php
	@echo "所有 SDK 测试完成！"

test-js:
	@echo "[1/5] 运行 JavaScript SDK 测试..."
	@cd javascript-sdk && pnpm test

test-go:
	@echo "[2/5] 运行 Go SDK 测试..."
	@cd golang-sdk && go test -v

test-py:
	@echo "[3/5] 运行 Python SDK 测试..."
	@cd python-sdk && uv run pytest

test-java:
	@echo "[4/5] 运行 Java SDK 测试..."
	@cd java-sdk && mvn test

test-php:
	@echo "[5/5] 运行 PHP SDK 测试..."
	@cd php-sdk && composer test