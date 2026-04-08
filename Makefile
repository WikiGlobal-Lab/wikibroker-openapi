.PHONY: init-js init-go init-py init-java init-php init-dart

init-js:
	@cd javascript-sdk && pnpm install

init-go:
	@cd golang-sdk && go mod tidy

init-py:
	@cd python-sdk && uv sync --all-groups

init-java:
	@cd java-sdk && mvn install

init-php:
	@cd php-sdk && composer install

init-dart:
	@cd dart-sdk && dart pub get

.PHONY: build build-js build-go build-py build-java build-php build-cs build-dart

build:
	@echo "清理已构建 SDK 包..."
	@echo ""
	@rm -rf wikibroker*.tgz wikibroker*.whl wikibroker*.jar wikibroker*.zip WikiBroker*.nupkg
	@echo ""
	@echo "开始构建所有 SDK ..."
	@echo ""
	@echo "[1/8] 开始构建 JavaScript SDK..." && $(MAKE) build-js && echo "[1/8] JavaScript SDK 构建完成 ✓"
	@echo "[2/8] 开始构建 Go SDK..." && $(MAKE) build-go && echo "[2/8] Go SDK 构建完成 ✓"
	@echo "[3/8] 开始构建 Python SDK..." && $(MAKE) build-py && echo "[3/8] Python SDK 构建完成 ✓"
	@echo "[4/8] 开始构建 Java SDK..." && $(MAKE) build-java && echo "[4/8] Java SDK 构建完成 ✓"
	@echo "[5/8] 开始构建 PHP SDK..." && $(MAKE) build-php && echo "[5/8] PHP SDK 构建完成 ✓"
	@echo "[6/8] 开始构建 .NET SDK..." && $(MAKE) build-cs && echo "[6/8] .NET SDK 构建完成 ✓"
	@echo "[7/8] 开始构建 Dart SDK..." && $(MAKE) build-dart && echo "[7/8] Dart SDK 构建完成 ✓"
	@echo "[8/8] 开始构建 Swift SDK..." && $(MAKE) build-sw && echo "[8/8] Swift SDK 构建完成 ✓"
	@echo ""
	@echo "所有SDK构建成功！"

build-js: init-js
	@cd javascript-sdk && rm -rf dist && pnpm pack && rename 's/openapi-sdk/openapi-js-sdk/' wikibroker-*.tgz && mv wikibroker-*.tgz ..

GO_SDK_VERSION = 1.0.1

build-go:
	@cp -r golang-sdk wikibroker_openapi_sdk && tar zcf wikibroker-openapi-go-sdk-$(GO_SDK_VERSION).tgz wikibroker_openapi_sdk && rm -rf wikibroker_openapi_sdk

build-py: init-py
	@cd python-sdk && rm -rf dist && uv build && mv dist/wikibroker*.whl ..

build-java: init-java
	@cd java-sdk && rm -rf target && mvn package && mv target/wikibroker*.jar ..

build-php: init-php
	@cd php-sdk && composer build && rename 's/wikiglobal-wikibroker-openapi-sdk/wikibroker-openapi-php-sdk/' wikiglobal*.zip && mv wikibroker*.zip ..

build-cs:
	@cd dotnet-sdk/src && rm -rf bin/ obj/ && dotnet pack && mv bin/Release/WikiBroker*.nupkg ../..

SWIFT_SDK_VERSION = 0.1.0-alpha

build-sw:
	@cd swift-sdk && cp -r . WikibrokerOpenapiSdk && swift package archive-source --package-path=WikibrokerOpenapiSdk && zip -d WikibrokerOpenapiSdk/WikibrokerOpenapiSdk.zip "WikibrokerOpenapiSdk/.*/*" && mv WikibrokerOpenapiSdk/WikibrokerOpenapiSdk.zip ../wikibroker-openapi-swift-sdk-$(SWIFT_SDK_VERSION).zip && rm -rf WikibrokerOpenapiSdk/

DART_SDK_VERSION := $(shell yq '.version' dart-sdk/pubspec.yaml)

build-dart:
	@cp -r dart-sdk wikibroker_openapi_sdk && cd wikibroker_openapi_sdk && rm -rf .dart_tool .idea *.iml && cd .. && tar zcf wikibroker-openapi-dart-sdk-$(DART_SDK_VERSION).tgz wikibroker_openapi_sdk && rm -rf wikibroker_openapi_sdk

.PHONY: test test-js test-go test-py test-java test-php test-cs test-dart

test:
	@echo "运行所有 SDK 测试..."
	@echo "[1/8] 运行 JavaScript SDK 测试..." && $(MAKE) test-js && echo "[1/8] JavaScript SDK 测试通过 ✓"
	@echo "[2/8] 运行 Go SDK 测试..." && $(MAKE) test-go && echo "[2/8] Go SDK 测试通过 ✓"
	@echo "[3/8] 运行 Python SDK 测试..." && $(MAKE) test-py && echo "[3/8] Python SDK 测试通过 ✓"
	@echo "[4/8] 运行 Java SDK 测试..." && $(MAKE) test-java && echo "[4/8] Java SDK 测试通过 ✓"
	@echo "[5/8] 运行 PHP SDK 测试..." && $(MAKE) test-php && echo "[5/8] PHP SDK 测试通过 ✓"
	@echo "[6/8] 运行 .NET SDK 测试..." && $(MAKE) test-cs && echo "[6/8] .NET SDK 测试通过 ✓"
	@echo "[7/8] 运行 Dart SDK 测试..." && $(MAKE) test-dart && echo "[7/8] Dart SDK 测试通过 ✓"
	@echo "[8/8] 运行 Swift SDK 测试..." && $(MAKE) test-sw && echo "[8/8] Swift SDK 测试通过 ✓"
	@echo "所有 SDK 测试完成！"

test-js: init-js
	@cd javascript-sdk && pnpm test

test-go: init-go
	@cd golang-sdk && go test -v

test-py: init-py
	@cd python-sdk && uv run pytest

test-java: init-java
	@cd java-sdk && mvn test

test-php: init-php
	@cd php-sdk && composer test

test-cs:
	@cd dotnet-sdk/tests && dotnet test

test-dart: init-dart
	@cd dart-sdk && dart test

test-sw:
	@cd swift-sdk && swift test

.PHONY: doc

doc:
	@echo "生成可视化文档..."
	@cd docs && npx @redocly/cli build-docs openapi.json -o index.html
	@echo "可视化文档生成完成！"

.PHONY: cloc cloc-js cloc-go cloc-py cloc-java cloc-php cloc-cs cloc-dart

cloc:
	@echo "统计所有 SDK 核心逻辑代码行数..."; \
	TS=$$($(MAKE) cloc-js | grep TypeScript | awk '{print $$1 "\t" $$5}'); \
	GO=$$($(MAKE) cloc-go | grep "Go  " | awk '{print $$1 "\t" $$5}'); \
	PY=$$($(MAKE) cloc-py | grep "Python  " | awk '{print $$1 "\t" $$5}'); \
	JAVA=$$($(MAKE) cloc-java | grep "Java  " | awk '{print $$1 "\t" $$5}'); \
	PHP=$$($(MAKE) cloc-php | grep "PHP  " | awk '{print $$1 "\t" $$5}'); \
	CS=$$($(MAKE) cloc-cs | grep "C#  " | awk '{print $$1 "\t" $$5}'); \
	DART=$$($(MAKE) cloc-dart | grep "Dart  " | awk '{print $$1 "\t" $$5}'); \
	SW=$$($(MAKE) cloc-sw | grep "Swift  " | awk '{print $$1 "\t" $$5}'); \
	printf "%-15s %15s\n" $$TS $$GO $$PY $$JAVA $$PHP $$CS $$DART $$SW
	@echo "所有 SDK 代码行数统计完毕！"

cloc-js:
	@echo "统计 JavaScript SDK 核心逻辑代码行数..."
	@cd javascript-sdk && cloc src/core.ts

cloc-go:
	@echo "统计 Go SDK 核心逻辑代码行数..."
	@cd golang-sdk && cloc core.go

cloc-py:
	@echo "统计 Python SDK 核心逻辑代码行数..."
	@cd python-sdk && cloc src/wikibroker_openapi_sdk/core.py

cloc-java:
	@echo "统计 Java SDK 核心逻辑代码行数..."
	@cd java-sdk && cloc src/main/java/com/wikiglobal/wikibroker/openapi/Core.java

cloc-php:
	@echo "统计 PHP SDK 核心逻辑代码行数..."
	@cd php-sdk && cloc src/Core.php

cloc-cs:
	@echo "统计 .NET SDK 核心逻辑代码行数..."
	@cd dotnet-sdk && cloc src/Core.cs

cloc-dart:
	@echo "统计 Dart SDK 核心逻辑代码行数..."
	@cd dart-sdk && cloc lib/src/core.dart

cloc-sw:
	@echo "统计 Swift SDK 核心逻辑代码行数..."
	@cd swift-sdk && cloc Sources/WikibrokerOpenapiSdk/Core.swift