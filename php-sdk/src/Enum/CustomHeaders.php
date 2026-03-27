<?php

declare(strict_types=1);

namespace WikibrokerOpenapiSdk\Enum;

enum CustomHeaders: string
{
    case ApiKey = 'X-Api-Key';
    case Timestamp = 'X-Timestamp';
    case Nonce = 'X-Nonce';
    case Signature = 'X-Signature';
}
