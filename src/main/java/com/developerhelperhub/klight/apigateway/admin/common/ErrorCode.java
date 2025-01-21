package com.developerhelperhub.klight.apigateway.admin.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum ErrorCode {

    COM_F_00001("COM_F_00001", "Missing field in the request"),
    COM_E_00001("COM_E_00001", "Internal error"),
    AUTH_I_00001("AUTH_I_00001", "Authentication Required"),
    AUTH_I_00002("AUTH_I_00002", "Access Denied"),

    APA_F_00001("APA_F_00001", "Name cannot empty"),
    APA_F_00002("APA_F_00002", "Host cannot empty"),
    APA_F_00003("APA_F_00003", "Path cannot empty"),
    APA_F_00004("APA_F_00004", "Protocol cannot empty"),

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

    public static Optional<ErrorCode> find(String code) {
        return Arrays.stream(values()).filter(e -> StringUtils.equals(e.getCode(), code)).findFirst();
    }
}
