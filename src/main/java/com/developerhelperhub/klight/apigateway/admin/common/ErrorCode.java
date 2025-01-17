package com.developerhelperhub.klight.apigateway.admin.common;

import lombok.Getter;

public enum ErrorCode {

    APA_F_00001("APA_F_00001", "Name cannot empty"),
    APA_I_00001("APA_I_00001", "Invalid id"),
    APA_I_00002("APA_I_00002", "Invalid id"),
    APA_I_00003("APA_I_00003", "Invalid id");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
