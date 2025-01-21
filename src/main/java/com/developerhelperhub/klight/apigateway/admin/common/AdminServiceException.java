package com.developerhelperhub.klight.apigateway.admin.common;


import lombok.Getter;

public class AdminServiceException extends  RuntimeException {

    @Getter
    private ErrorCode code;

    public AdminServiceException(ErrorCode code) {
        super(String.format("%s:%s", code.getCode(), code.getMessage()));
        this.code = code;
    }
}
