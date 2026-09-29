#!/usr/bin/env bash
set -euo pipefail

echo "==> Creating unified project structure..."
mkdir -p src/main/java
mkdir -p src/main/resources
mkdir -p src/test/java
mkdir -p src/test/resources

echo "==> Merging Java source files from common and modern templates..."
if [ -d "common/src/main/java" ]; then
    cp -r common/src/main/java/* src/main/java/
fi

if [ -d "modern/templates/java" ]; then
    cp -r modern/templates/java/* src/main/java/
fi

echo "==> Merging test sources..."
if [ -d "common/src/test/java" ]; then
    cp -r common/src/test/java/* src/test/java/
fi

echo "==> Merging resources..."
if [ -d "common/src/main/resources" ]; then
    cp -r common/src/main/resources/* src/main/resources/
fi

if [ -d "modern/templates/resources" ]; then
    cp -r modern/templates/resources/* src/main/resources/
fi

if [ -d "common/src/test/resources" ]; then
    cp -r common/src/test/resources/* src/test/resources/
fi

echo "==> Consolidating Access Widener files into src/main/resources/moulconfig.accesswidener..."
find . -type f -name "*.accesswidener" ! -path "./src/main/resources/*" -exec cat {} + > src/main/resources/moulconfig.accesswidener 2>/dev/null || true

echo "==> Removing legacy directories (build-src, common, modern, docs)..."
rm -rf build-src
rm -rf common
rm -rf modern
rm -rf docs
rm -rf src/doc
rm -f .github/workflows/javadoc.yml

echo "==> Removing leftover Kotlin build scripts (.gradle.kts)..."
find . -type f -name "*.gradle.kts" -delete

echo "==> Merge complete!"
