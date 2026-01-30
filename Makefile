.PHONY: build build-js build-go build-py build-java

GO_SDK_VERSION=1.0.0

build:
	@echo "清理已构建SDK包..."
	@echo ""
	@rm -rf wikibroker*.tgz wikibroker*.whl
	@echo ""
	@echo "开始构建所有SDK..."
	@echo ""
	@$(MAKE) build-js && $(MAKE) build-go && $(MAKE) build-py && $(MAKE) build-java
	@echo ""
	@echo "所有SDK构建成功！"

build-js:
	@echo "[1/4] 开始构建 JavaScript SDK..."
	@cd javascript-sdk && rm -rf dist && pnpm pack && rename 's/openapi-sdk/openapi-js-sdk/' wikibroker-*.tgz && mv wikibroker-*.tgz ..
	@echo "[1/4] JavaScript SDK 构建完成 ✓"
	@echo ""

build-go:
	@echo "[2/4] 开始构建 Go SDK..."
	@cp -r golang-sdk wikibroker_openapi_sdk && tar zcf wikibroker-openapi-go-sdk-$(GO_SDK_VERSION).tgz wikibroker_openapi_sdk && rm -rf wikibroker_openapi_sdk
	@echo "[2/4] Go SDK 构建完成 ✓"
	@echo ""

build-py:
	@echo "[3/4] 开始构建 Python SDK..."
	@cd python-sdk && rm -rf dist && poetry build && mv dist/wikibroker*.whl ..
	@echo "[3/4] Python SDK 构建完成 ✓"
	@echo ""

build-java:
	@echo "[4/4] 开始构建 Java SDK..."
	@cd java-sdk && rm -rf target && mvn package && mv target/wikibroker*.jar ..
	@echo "[4/4] Java SDK 构建完成 ✓"
	@echo ""