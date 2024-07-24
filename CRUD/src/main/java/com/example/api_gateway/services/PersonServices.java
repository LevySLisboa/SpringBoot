package com.example.api_gateway.services;

import com.example.api_gateway.controller.PersonController;
import com.example.api_gateway.data.vo.v1.PersonVO;
import com.example.api_gateway.exceptions.RequiredObjectIsNullException;
import com.example.api_gateway.exceptions.ResourceNotFoundException;
import com.example.api_gateway.mapper.MyMapper;
import com.example.api_gateway.model.Person;
import com.example.api_gateway.repositories.PersonRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonServices.class);
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public PersonVO findById(Long id) throws ResourceNotFoundException {
        logger.info("Find one people");
        var entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        var vo =  MyMapper.parseObject(entity,PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<PersonVO> findAll(){
        logger.info("Find all people");
        var persons =  MyMapper.parseListObjects(repository.findAll(),PersonVO.class);
        persons.forEach(x-> {
            try {
                x.add(linkTo(methodOn(PersonController.class).findById(x.getKey())).withSelfRel());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return persons;
    }

    public PersonVO create(PersonVO person) throws ResourceNotFoundException, RequiredObjectIsNullException {
        if(person==null) throw new RequiredObjectIsNullException();
        logger.info("creating one person");
        var entity = MyMapper.parseObject(person, Person.class);
        var vo =  MyMapper.parseObject(repository.save(entity),PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) throws ResourceNotFoundException, RequiredObjectIsNullException {
        if(person==null) throw new RequiredObjectIsNullException();
        logger.info("updating one person");
        var entity = repository.findById(person.getKey())
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo =  MyMapper.parseObject(repository.save(entity),PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
    public void delete(Long id) throws ResourceNotFoundException {
        logger.info("Deleting one person");
        var entity = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }
}
