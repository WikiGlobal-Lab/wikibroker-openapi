<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Test;

use DateTime;
use GuzzleHttp\Psr7\Request;
use PHPUnit\Framework\TestCase;
use Symfony\Component\HttpClient\Psr18Client;
use WikibrokerOpenapiSdk\Api;
use WikibrokerOpenapiSdk\Enum\CustomHeaders;

final class ApiTest extends TestCase {
    private static $baseURL = "https://api.example.com";
    private static $path = "test?q1=c&q2=b&q1=a";

    private static function url(): string {
        return self::$baseURL . '/' . self::$path;
    }

    private static $body = ["key" => "value"];
    private static $method = "POST";
    private static $apiKey = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
    private static $apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
    private static $nonce = "4428a206-1afd-4b15-a98d-43e91f49a08d";
    private static $timestamp = 1798115622000;
    private static $expectedSignature = "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

    public function testSymfony(): void {
        $client = new Psr18Client();
        $body = $client->createStream(json_encode(self::$body));
        $request = $client->createRequest(self::$method, self::url())->withBody($body);
        $timestamp = DateTime::createFromFormat('U.v', sprintf('%d.%03d', intdiv(self::$timestamp, 1000), self::$timestamp % 1000));
        $request = Api::withXHeaders(
            $request,
            self::$apiKey,
            $timestamp,
            self::$nonce
        );
        $request = Api::withSign($request, self::$apiSecret);
        $actualSignature = $request->getHeader(CustomHeaders::Signature->value)[0];
        $this->assertSame(self::$expectedSignature, $actualSignature);
    }

    public function testGuzzle(): void {
        $body = json_encode(self::$body);
        $request = new Request(self::$method, self::url(), [], $body);
        $timestamp = DateTime::createFromFormat('U.v', sprintf('%d.%03d', intdiv(self::$timestamp, 1000), self::$timestamp % 1000));
        $request = Api::withXHeaders(
            $request,
            self::$apiKey,
            $timestamp,
            self::$nonce
        );
        $request = Api::withSign($request, self::$apiSecret);
        $actualSignature = $request->getHeader(CustomHeaders::Signature->value)[0];
        $this->assertSame(self::$expectedSignature, $actualSignature);
    }
}
