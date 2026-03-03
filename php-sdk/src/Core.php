<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk;

use Psr\Http\Message\RequestInterface;
use WikibrokerOpenapiSdk\Enum\CustomHeaders;

final class Core
{
    private function __construct() {}

    private static $algo = 'sha256';
    public static function generateSignature(string $key, string $message): string
    {
        return hash_hmac(self::$algo, $message, $key);
    }
    public static function generateCanonicalString(RequestInterface $req): string
    {
        $method = strtoupper($req->getMethod());
        $path = $req->getUri()->getPath();
        $canonicalQuery = self::buildCanonicalQuery($req);
        $apiKey = $req->getHeader(CustomHeaders::ApiKey->value);
        $timestamp = $req->getHeader(CustomHeaders::Timestamp->value);
        $nonce = $req->getHeader(CustomHeaders::Nonce->value);
        $bodyHash = self::calculateBodyHash($req);
        return join("\n", [
            $method,
            $path,
            $canonicalQuery,
            $apiKey,
            $timestamp,
            $nonce,
            $bodyHash
        ]);
    }

    private static function calculateBodyHash(RequestInterface $req): string
    {
        $body = (string) $req->getBody();
        return hash(self::$algo, $body);
    }

    private static function buildCanonicalQuery(RequestInterface $req): string
    {
        $queryString = $req->getUri()->getQuery();
        $query = [];
        foreach (explode('&', $queryString) as $item) {
            [$k, $v] = explode('=', $item);
            if (!array_key_exists($k, $query)) {
                $query[$k] = [];
            }
            array_push($query[$k], $v);
        }
        ksort($query);
        $pairs = [];
        foreach ($query as $key => $values) {
            sort($values);
            foreach ($values as $value) {
                array_push($pairs, $key . '=' . $value);
            }
        }
        return join('&', $pairs);
    }
}
