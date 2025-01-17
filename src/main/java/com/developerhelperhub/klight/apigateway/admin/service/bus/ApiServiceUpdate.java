package com.developerhelperhub.klight.apigateway.admin.service.bus;

import com.developerhelperhub.klight.apigateway.admin.common.AdminServiceException;
import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import com.developerhelperhub.klight.apigateway.admin.service.bus.AbstractApiService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Scope("prototype")
public class ApiServiceUpdate extends AbstractApiService {

    @Setter
    private String id;

    public void update() {
        log.info("Updating ....");

        validate();

        map();

        persist();

        log.info("Created!");
    }


    protected void validate() {
        log.info("Validating data");

        log.info("Validating: {}", this.id);

        this.entity = this.repository.findById(this.id).orElseThrow(() -> {
            return new AdminServiceException(ErrorCode.APA_I_00002);
        });

        super.validate();

    }
}
