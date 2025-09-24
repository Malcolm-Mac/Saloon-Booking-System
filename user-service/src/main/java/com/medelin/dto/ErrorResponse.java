package com.medelin.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
       String code,
       String message,
       List<Map<String,String>> details,
       int status,
       Instant timestamp
)
{ }
