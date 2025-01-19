package com.developerhelperhub.klight.apigateway.admin.config.excep;

import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
@Slf4j
public class ExceptionAccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    public AccessDeniedHandler handler() {
        return (request, response, accessDeniedException) -> {

           log.error(accessDeniedException.getMessage(), accessDeniedException);

            response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            ErrorInternalResponse errorResponse = new ErrorInternalResponse(
                    ErrorCode.AUTH_I_00002.getCode(),
                    ErrorCode.AUTH_I_00002.getMessage(),
                    accessDeniedException.getMessage());

            PrintWriter writer = response.getWriter();
            this.objectMapper.writeValue(writer, errorResponse);
            writer.flush();
        };
    }
}
