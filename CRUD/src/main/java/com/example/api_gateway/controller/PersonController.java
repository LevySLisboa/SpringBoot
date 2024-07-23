package com.example.api_gateway.controller;

import com.example.api_gateway.data.vo.v1.PersonVO;
import com.example.api_gateway.exceptions.ResourceNotFoundException;
import com.example.api_gateway.services.PersonServices;
import com.example.api_gateway.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
    @Autowired
    private PersonServices services;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO findById(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
        return services.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public List<PersonVO> findAll() {
        return services.findAll();
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO create(@RequestBody PersonVO PersonVO) {
        return services.create(PersonVO);
    }

    @PutMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO update(@RequestBody PersonVO PersonVO) throws ResourceNotFoundException {
        return services.update(PersonVO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
