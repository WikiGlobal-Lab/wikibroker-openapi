<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Adapters;

use Psr\Http\Client\ClientInterface;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;

final class PsrHttpClientWithSign extends Adapter implements ClientInterface
{
    private ClientInterface $rawClient;
    public function setClient(ClientInterface $client): PsrHttpClientWithSign
    {
        $this->rawClient = $client;
        return $this;
    }

    public function sendRequest(RequestInterface $request): ResponseInterface
    {
        return $this->rawClient->sendRequest($this->signRequest($request));
    }
}