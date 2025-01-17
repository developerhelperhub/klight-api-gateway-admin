package com.developerhelperhub.klight.apigateway.admin.service.bus;

import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceEntity;
import com.developerhelperhub.klight.apigateway.admin.service.data.ApiServiceRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractApiService {

    @Setter
    protected String name;

    @Setter
    protected String host;

    @Setter
    protected String path;

    @Setter
    protected String protocol;

    protected ApiServiceEntity entity;

    @Autowired
    protected ApiServiceRepository repository;

    protected void map() {
        log.info("Mapping data");

        log.trace("Name : {}", this.name);
        log.trace("Host : {}", this.host);
        log.trace("Path : {}", this.path);
        log.trace("Protocol : {}", this.protocol);

        this.entity.setName(this.name);
        this.entity.setHost(this.host);
        this.entity.setPath(this.path);
        this.entity.setProtocol(this.protocol);
    }


    protected void persist() {

        this.repository.save(this.entity);

        log.info("Persist data");
    }


    protected void validate() {
        log.info("Validating data");
    }
}
