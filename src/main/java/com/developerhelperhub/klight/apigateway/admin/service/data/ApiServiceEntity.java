package com.developerhelperhub.klight.apigateway.admin.service.data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Document(collection = "service")
public class ApiServiceEntity {

    @Id
    private String id;

    @NotEmpty
    @Size(min=3, max = 50)
    private String name;

    @NotEmpty
    @Size(min=3, max = 50)
    private String host;

    @NotEmpty
    @Size(min=3, max = 50)
    private String path;

    @NotEmpty
    @Size(min = 4, max = 5)
    private String protocol;

    @CreatedDate
    private LocalDateTime createdDatetime;

    @LastModifiedDate
    private LocalDateTime updatedDatetime;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

}
