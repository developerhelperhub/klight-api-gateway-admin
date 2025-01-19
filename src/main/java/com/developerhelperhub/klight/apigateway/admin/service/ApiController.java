package com.developerhelperhub.klight.apigateway.admin.service;

import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceRequest;
import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/services")
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping
    public void create(@Valid @RequestBody ApiServiceRequest request) {
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotEmpty @PathVariable("id") String id, @Valid @RequestBody ApiServiceRequest request) {
        service.update(id, request);
    }

    @GetMapping("/{id}")
    public ApiServiceResponse get(@Valid @NotEmpty @PathVariable("id") String id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotEmpty @PathVariable("id") String id) {
        service.delete(id);
    }

}
