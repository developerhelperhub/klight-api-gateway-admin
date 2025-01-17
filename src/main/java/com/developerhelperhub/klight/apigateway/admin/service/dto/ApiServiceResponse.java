package com.developerhelperhub.klight.apigateway.admin.service.dto;

import java.time.LocalDateTime;

public record ApiServiceResponse(String id, String name, String host, String path, String protocol,
                                 LocalDateTime created, LocalDateTime updated) {
}
