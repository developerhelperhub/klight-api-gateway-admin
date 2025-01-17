package com.developerhelperhub.klight.apigateway.admin.service.bus;

import com.developerhelperhub.klight.apigateway.admin.common.AdminServiceException;
import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceEntity;
import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class ApiServiceDelete {

    private ApiServiceEntity entity;

    @Autowired
    private ApiServiceRepository repository;

    public void delete(String id) {

        validate(id);

        this.repository.delete(this.entity);

        log.info("Deleted: {}", id);
    }

    private void validate(String id) {
        log.info("Validating: {}", id);

        this.entity = this.repository.findById(id).orElseThrow(() -> {
            return new AdminServiceException(ErrorCode.APA_I_00003);
        });
    }

}
