package com.developerhelperhub.klight.apigateway.admin.service.bus;

import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Scope("prototype")
public class ApiServiceCreate extends AbstractApiService {

    public void create() {
        log.info("Creating ....");

        validate();

        map();

        persist();

        log.info("Created!");
    }

    protected void validate() {
        log.info("Validating data");

        super.validate();

        this.entity = new ApiServiceEntity();
    }
}
