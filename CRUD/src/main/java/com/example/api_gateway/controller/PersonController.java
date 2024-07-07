package com.example.api_gateway.controller;

import com.example.api_gateway.model.Person;
import com.example.api_gateway.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController{
    @Autowired
    private PersonServices services;

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findById(@PathVariable(value = "id") int id){
        return services.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll(){
        return services.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestBody Person person){
        return services.create(person);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Person update(@RequestBody Person person){
        return services.update(person);
    }
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id")int id){
        services.delete(id);
    }
}
