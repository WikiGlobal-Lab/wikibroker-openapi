.PHONY: init-js init-go init-py init-java init-php

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

.PHONY: build build-js build-go build-py build-java build-php build-cs

GO_SDK_VERSION = 1.0.1

build:
	@echo "清理已构建SDK包..."
	@echo ""
	@rm -rf wikibroker*.tgz wikibroker*.whl wikibroker*.jar wikibroker*.zip WikiBroker*.nupkg
	@echo ""
	@echo "开始构建所有SDK..."
	@echo ""
	@echo "[1/6] 开始构建 JavaScript SDK..." && $(MAKE) build-js && echo "[1/6] JavaScript SDK 构建完成 ✓"
	@echo "[2/6] 开始构建 Go SDK..." && $(MAKE) build-go && echo "[2/6] Go SDK 构建完成 ✓"
	@echo "[3/6] 开始构建 Python SDK..." && $(MAKE) build-py && echo "[3/6] Python SDK 构建完成 ✓"
	@echo "[4/6] 开始构建 Java SDK..." && $(MAKE) build-java && echo "[4/6] Java SDK 构建完成 ✓"
	@echo "[5/6] 开始构建 PHP SDK..." && $(MAKE) build-php && echo "[5/6] PHP SDK 构建完成 ✓"
	@echo "[6/6] 开始构建 .NET SDK..." && $(MAKE) build-cs && echo "[6/6] .NET SDK 构建完成 ✓"
	@echo ""
	@echo "所有SDK构建成功！"

build-js: init-js
	@cd javascript-sdk && rm -rf dist && pnpm pack && rename 's/openapi-sdk/openapi-js-sdk/' wikibroker-*.tgz && mv wikibroker-*.tgz ..

build-go: init-go
	@cp -r golang-sdk wikibroker_openapi_sdk && tar zcf wikibroker-openapi-go-sdk-$(GO_SDK_VERSION).tgz wikibroker_openapi_sdk && rm -rf wikibroker_openapi_sdk

build-py: init-py
	@cd python-sdk && rm -rf dist && uv build && mv dist/wikibroker*.whl ..

build-java: init-java
	@cd java-sdk && rm -rf target && mvn package && mv target/wikibroker*.jar ..

build-php: init-php
	@cd php-sdk && composer build && rename 's/wikiglobal-wikibroker-openapi-sdk/wikibroker-openapi-php-sdk/' wikiglobal*.zip && mv wikibroker*.zip ..

build-cs:
	@cd dotnet-sdk/src && rm -rf bin/ obj/ && dotnet pack && mv bin/Release/WikiBroker*.nupkg ../..

.PHONY: test test-js test-go test-py test-java test-php test-cs

test:
	@echo "运行所有 SDK 测试..."
	@echo "[1/6] 运行 JavaScript SDK 测试..." && $(MAKE) test-js && echo "[1/6] JavaScript SDK 测试通过 ✓"
	@echo "[2/6] 运行 Go SDK 测试..." && $(MAKE) test-go && echo "[2/6] Go SDK 测试通过 ✓"
	@echo "[3/6] 运行 Python SDK 测试..." && $(MAKE) test-py && echo "[3/6] Python SDK 测试通过 ✓"
	@echo "[4/6] 运行 Java SDK 测试..." && $(MAKE) test-java && echo "[4/6] Java SDK 测试通过 ✓"
	@echo "[5/6] 运行 PHP SDK 测试..." && $(MAKE) test-php && echo "[5/6] PHP SDK 测试通过 ✓"
	@echo "[6/6] 运行 .NET SDK 测试..." && $(MAKE) test-cs && echo "[6/6] .NET SDK 测试通过 ✓"
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

.PHONY: doc

doc:
	@echo "生成可视化文档..."
	@cd docs && npx @redocly/cli build-docs openapi.json -o index.html
	@echo "可视化文档生成完成！"

.PHONY: cloc cloc-js cloc-go cloc-py cloc-java cloc-php cloc-cs

cloc:
	@echo "统计所有SDK代码行数..."; \
	TS=$$($(MAKE) cloc-js | grep TypeScript | awk '{print $$1 "\t" $$5}'); \
	GO=$$($(MAKE) cloc-go | grep "Go  " | awk '{print $$1 "\t" $$5}'); \
	PY=$$($(MAKE) cloc-py | grep "Python  " | awk '{print $$1 "\t" $$5}'); \
	JAVA=$$($(MAKE) cloc-java | grep "Java  " | awk '{print $$1 "\t" $$5}'); \
	PHP=$$($(MAKE) cloc-php | grep "PHP  " | awk '{print $$1 "\t" $$5}'); \
	CS=$$($(MAKE) cloc-cs | grep "C#  " | awk '{print $$1 "\t" $$5}'); \
	printf "%-15s %15s\n" $$TS $$GO $$PY $$JAVA $$PHP $$CS
	@echo "所有 SDK 代码行数统计完毕！"

cloc-js:
	@echo "统计 JavaScript SDK 代码行数..."
	@cd javascript-sdk && cloc src

cloc-go:
	@echo "统计 Go SDK 代码行数..."
	@cd golang-sdk && cloc . --exclude_list_file=wikibroker_openapi_sdk_test.go

cloc-py:
	@echo "统计 Python SDK 代码行数..."
	@cd python-sdk && cloc src

cloc-java:
	@echo "统计 Java SDK 代码行数..."
	@cd java-sdk && cloc src/main

cloc-php:
	@echo "统计 PHP SDK 代码行数..."
	@cd php-sdk && cloc src

cloc-cs:
	@echo "统计 .NET SDK 代码行数..."
	@cd dotnet-sdk && cloc src