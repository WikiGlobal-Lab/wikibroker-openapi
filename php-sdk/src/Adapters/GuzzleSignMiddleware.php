<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Adapters;

use Closure;
use Psr\Http\Message\RequestInterface;

final class GuzzleSignMiddleware extends Adapter {
    public function __invoke(callable $handler): Closure {
        return fn(RequestInterface $request, array $options) =>
            $handler($this->signRequest($request), $options);
    }
}