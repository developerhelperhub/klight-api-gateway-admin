package com.developerhelperhub.klight.apigateway.admin.config.excep;

import java.util.List;

public record ErrorInputValidationResponse(String code, String message, List<ErrorFieldResponse> fields) {
}
