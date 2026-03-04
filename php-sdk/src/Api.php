<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk;

use DateTime;
use Psr\Http\Message\RequestInterface;
use Ramsey\Uuid\Uuid;
use WikibrokerOpenapiSdk\Enum\CustomHeaders;

class Api
{
    private function __construct() {}

    public static function withXHeaders(
        RequestInterface $req,
        string $apiKey,
        DateTime $timestamp,
        string $nonce,
    ): RequestInterface {
        return $req
            ->withHeader(CustomHeaders::ApiKey->value, Uuid::fromString($apiKey)->toString())
            ->withHeader(CustomHeaders::Timestamp->value, $timestamp->format('Uv'))
            ->withHeader(CustomHeaders::Nonce->value, Uuid::fromString($nonce)->toString());
    }

    public static function withSign(RequestInterface $req, string $key): RequestInterface
    {
        $canonicalString = Core::generateCanonicalString($req);
        $signature = Core::generateSignature($key, $canonicalString);
        return $req->withHeader(CustomHeaders::Signature->value, $signature);
    }
}
