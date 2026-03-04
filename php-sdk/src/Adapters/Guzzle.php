<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Adapters;

use Closure;
use DateTimeInterface;
use Psr\Http\Message\RequestInterface;

class GuzzleMiddleware
{
    private function __construct() {}

    /**
     * @param string $apiKey
     * @param string $apiSecret
     * @param callable(RequestInterface $req, string $apiKey, DateTimeInterface $timestamp, string $nonce):RequestInterface $withHeaders
     * @param callable(RequestInterface $req, string $key):RequestInterface $withSign
     * @param callable():DateTimeInterface $timestampGenerator
     * @param callable():string $idGenerator
     * @return Closure
     */
    public static function sign(
        string $apiKey,
        string $apiSecret,
        callable $withHeaders,
        callable $withSign,
        callable $timestampGenerator,
        callable $idGenerator
    ) {
        return function (callable $handler) use (
            $apiKey,
            $apiSecret,
            $withHeaders,
            $withSign,
            $timestampGenerator,
            $idGenerator
        ) {
            return function (
                RequestInterface $request,
                array $options
            ) use (
                $handler,
                $apiKey,
                $apiSecret,
                $withHeaders,
                $withSign,
                $timestampGenerator,
                $idGenerator
            ) {
                $request = $withHeaders($request, $apiKey, $timestampGenerator(), $idGenerator());
                $request = $withSign($request, $apiSecret);
                return $handler($request, $options);
            };
        };
    }
}
