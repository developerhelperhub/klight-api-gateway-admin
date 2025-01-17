package com.developerhelperhub.klight.apigateway.admin.service;

import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceRequest;
import com.developerhelperhub.klight.apigateway.admin.service.dto.ApiServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/services")
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping
    public void create(@RequestBody ApiServiceRequest request) {
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id, @RequestBody ApiServiceRequest request) {
        service.update(id, request);
    }

    @GetMapping("/{id}")
    public ApiServiceResponse get(@PathVariable("id") String id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        service.delete(id);
    }

}
