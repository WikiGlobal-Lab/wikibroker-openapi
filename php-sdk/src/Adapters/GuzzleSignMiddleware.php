<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Adapters;

use Closure;
use DateTimeInterface;
use Psr\Http\Message\RequestInterface;

class GuzzleSignMiddleware {
    /**
     * @param string $apiKey
     * @param string $apiSecret
     * @param callable(RequestInterface $req, string $apiKey, DateTimeInterface $timestamp, string $nonce):RequestInterface $withHeaders
     * @param callable(RequestInterface $req, string $key):RequestInterface $withSign
     * @param callable():DateTimeInterface $timestampGenerator
     * @param callable():string $idGenerator
     * @return callable(callable $handler):Closure
     */
    public function __construct(
        private string $apiKey,
        private string $apiSecret,
        private Closure $withHeaders,
        private Closure $withSign,
        private Closure $timestampGenerator,
        private Closure $idGenerator
    ) {
    }

    public function __invoke(callable $handler) {
        return function (RequestInterface $request, array $options) use ($handler) {
            $request = ($this->withHeaders)(
                $request,
                $this->apiKey,
                ($this->timestampGenerator)(),
                ($this->idGenerator)()
            );
            $request = ($this->withSign)($request, $this->apiSecret);
            return $handler($request, $options);
        };
    }
}
