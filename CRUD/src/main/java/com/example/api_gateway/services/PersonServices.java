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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonServices.class);
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PagedResourcesAssembler<PersonVO> assembler;

    public PersonVO findById(Long id) throws ResourceNotFoundException {
        logger.info("Find one people");
        var entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        var vo =  MyMapper.parseObject(entity,PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) throws ResourceNotFoundException {
        logger.info("Find all people");
        var personPage = repository.findAll(pageable);
        var personVosPage = personPage.map(p -> MyMapper.parseObject(p,PersonVO.class));
        personVosPage.map(p-> {
            try {
                return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
        return assembler.toModel(personVosPage,link);
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
