package com.developerhelperhub.klight.apigateway.admin.service.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "service")
public class ApiServiceEntity {

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    private String host;

    @NotNull
    @Size(max = 50)
    private String path;

    @NotNull
    @Size(max = 4)
    private String protocol;

    @CreatedDate
    private LocalDateTime createdDatetime;

    @LastModifiedDate
    private LocalDateTime updatedDatetime;

}
