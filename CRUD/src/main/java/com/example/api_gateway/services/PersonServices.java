package com.example.api_gateway.services;

import com.example.api_gateway.exceptions.ResourceNotFoundException;
import com.example.api_gateway.model.Person;
import com.example.api_gateway.repositories.PersonRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonServices.class);
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public Person findById(Long id) throws ResourceNotFoundException {
        logger.info("Find one people");
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
    }

    public List<Person> findAll(){
        logger.info("Find all persons");
        return repository.findAll();
    }

    public Person create(Person person){
        logger.info("creating one person");
        return repository.save(person);
    }

    public Person update(Person person) throws ResourceNotFoundException {
        logger.info("updating one person");

        var entity = repository.findById(person.getId())
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(entity);
    }
    public void delete(Long id) throws ResourceNotFoundException {
        logger.info("Deleting one person");
        var entity = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }
}
