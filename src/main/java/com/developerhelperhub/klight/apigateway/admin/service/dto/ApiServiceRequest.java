package com.developerhelperhub.klight.apigateway.admin.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApiServiceRequest(
        @NotEmpty(message = "APA_F_00001")
        @Size(min = 3, max = 50)
        String name,
        @NotEmpty(message = "APA_F_00002")
        @Size(min = 3, max = 50)
        String host,
        @NotEmpty(message = "APA_F_00003")
        @Size(min = 3, max = 50)
        String path,
        @NotEmpty(message = "APA_F_00004")
        @Size(min = 4, max = 5)
        String protocol) {
}
