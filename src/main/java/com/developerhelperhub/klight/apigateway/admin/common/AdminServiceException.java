package com.developerhelperhub.klight.apigateway.admin.common;


public class AdminServiceException extends  RuntimeException {

    private ErrorCode code;

    public AdminServiceException(ErrorCode code) {
        super(String.format("%s:%s", code.getCode(), code.getMessage()));
    }
}
