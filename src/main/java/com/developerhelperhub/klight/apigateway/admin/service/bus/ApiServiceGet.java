package com.developerhelperhub.klight.apigateway.admin.service.bus;

import com.developerhelperhub.klight.apigateway.admin.common.AdminServiceException;
import com.developerhelperhub.klight.apigateway.admin.common.ErrorCode;
import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceEntity;
import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceRepository;
import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class ApiServiceGet {

    private ApiServiceResponse response;

    private ApiServiceEntity entity;

    @Autowired
    private ApiServiceRepository repository;

    public ApiServiceResponse get(String id) {

        validate(id);

        map();

        return this.response;
    }

    private void validate(String id) {
        log.info("Validating: {}", id);

        this.entity = this.repository.findById(id).orElseThrow(() -> {
            return new AdminServiceException(ErrorCode.APA_I_00001);
        });
    }

    private void map() {
        log.info("Mapping");

        this.response = new ApiServiceResponse(this.entity.getId(),
                this.entity.getName(),
                this.entity.getHost(),
                this.entity.getPath(),
                this.entity.getProtocol(),
                this.entity.getCreatedDatetime(),
                this.entity.getUpdatedDatetime());
    }

}
