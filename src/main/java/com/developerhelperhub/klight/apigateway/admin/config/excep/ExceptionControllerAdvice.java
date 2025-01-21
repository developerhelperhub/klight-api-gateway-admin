package com.developerhelperhub.klight.apigateway.admin.config.excep;

import com.developerhelperhub.klight.apigateway.admin.common.AdminServiceException;
import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInputValidationResponse> handleValidationException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage(), ex);

        List<ErrorFieldResponse> fields = ex.getBindingResult().getAllErrors().stream().map(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorCode = error.getDefaultMessage();

            Optional<ErrorCode> codeOpt = ErrorCode.find(errorCode);
            String message = errorCode;
            String code = "";

            if (codeOpt.isPresent()) {
                code = codeOpt.get().getCode();
                message = codeOpt.get().getMessage();
            }

            return new ErrorFieldResponse(fieldName, code, message);
        }).toList();

        ErrorInputValidationResponse errorResponse = new ErrorInputValidationResponse(
                ErrorCode.COM_F_00001.getCode(),
                ErrorCode.COM_F_00001.getMessage(), fields);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<Object> authorizationDeniedException(Exception ex) {

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorInternalResponse(
                ErrorCode.AUTH_I_00002.getCode(),
                ErrorCode.AUTH_I_00002.getMessage(), ex.getMessage()),
                HttpStatus.FORBIDDEN);


    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        ErrorInputValidationResponse errorResponse;

        log.error(ex.getMessage(), ex);

        if (ex instanceof AdminServiceException exception) {

            return new ResponseEntity<>(new ErrorBusinessResponse(
                    exception.getCode().getCode(),
                    exception.getCode().getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);

        } else {

            return new ResponseEntity<>(new ErrorInternalResponse(
                    ErrorCode.COM_E_00001.getCode(),
                    ErrorCode.COM_E_00001.getMessage(), ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
