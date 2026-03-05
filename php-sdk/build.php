<?php

declare(strict_types=1);

$composerPath = __DIR__ . '/composer.json';
$composerConfig = json_decode(file_get_contents($composerPath), true);
$version = $composerConfig['version'];
$pkgName = "wikibroker-openapi-php-sdk-{$version}.zip";
$zipPath = __DIR__ . '/' . $pkgName;
if (file_exists($zipPath)) {
    unlink($zipPath);
}
$cmd = sprintf(
    'cd %s && zip -r %s src composer.*',
    escapeshellarg(__DIR__),
    escapeshellarg($pkgName)
);
passthru($cmd, $result);
