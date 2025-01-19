package com.developerhelperhub.klight.apigateway.admin.service;

import com.developerhelperhub.klight.apigateway.admin.service.bus.*;
import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceRequest;
import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ApiService {

    @Autowired
    private ObjectFactory<ApiServiceCreate> createFactory;

    @Autowired
    private ObjectFactory<ApiServiceUpdate> updateFactory;

    @Autowired
    private ObjectFactory<ApiServiceGet> getFactory;

    @Autowired
    private ObjectFactory<ApiServiceDelete> deleteFactory;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void create(ApiServiceRequest request) {
        log.info("Service creating");

        ApiServiceCreate service = createFactory.getObject();

        map(service, request);

        service.create();

    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void update(String id, ApiServiceRequest request) {
        log.info("Service updating");

        ApiServiceUpdate service = updateFactory.getObject();

        service.setId(id);

        map(service, request);

        service.update();

    }

    private void map(AbstractApiService service, ApiServiceRequest request) {
        log.info("Service map");

        service.setName(request.name());
        service.setHost(request.host());
        service.setPath(request.path());
        service.setProtocol(request.protocol());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiServiceResponse get(String id) {

        log.info("Service get");

        ApiServiceGet service = getFactory.getObject();

        return service.get(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(String id) {

        log.info("Service delete");

        ApiServiceDelete service = deleteFactory.getObject();

        service.delete(id);
    }
}
