<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Adapters;

use Closure;
use Psr\Http\Client\ClientInterface;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

class HttpClientWithSign implements ClientInterface
{
    /**
     * @param ClientInterface $rawClient
     * @param string $apiKey
     * @param string $apiSecret
     * @param callable(RequestInterface $req, string $apiKey, DateTimeInterface $timestamp, string $nonce):RequestInterface $withHeaders
     * @param callable(RequestInterface $req, string $key):RequestInterface $withSign
     * @param callable():DateTimeInterface $timestampGenerator
     * @param callable():string $idGenerator
     * @return ClientInterface
     */
    public function __construct(
        private ClientInterface $rawClient,
        private string $apiKey,
        private string $apiSecret,
        private Closure $withHeaders,
        private Closure $withSign,
        private Closure $timestampGenerator,
        private Closure $idGenerator
    ) {}

    public function sendRequest(RequestInterface $request): ResponseInterface
    {

        $request = ($this->withHeaders)(
            $request,
            $this->apiKey,
            ($this->timestampGenerator)(),
            ($this->idGenerator)()
        );
        $request = ($this->withSign)($request, $this->apiSecret);
        return $this->rawClient->sendRequest($request);
    }
}
