package com.developerhelperhub.klight.apigateway.admin.config.excep;

import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Slf4j
@Component
public class ExcetionAuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    public AuthenticationEntryPoint handler() {
        return (request, response, authException) -> {

            log.error(authException.getMessage(), authException);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            ErrorInternalResponse errorResponse = new ErrorInternalResponse(
                    ErrorCode.AUTH_I_00001.getCode(),
                    ErrorCode.AUTH_I_00001.getMessage(),
                    authException.getMessage());

            PrintWriter writer = response.getWriter();
            this.objectMapper.writeValue(writer, errorResponse);
            writer.flush();
        };
    }
}
