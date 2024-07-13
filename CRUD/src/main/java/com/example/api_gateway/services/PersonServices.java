package com.example.api_gateway.services;

import com.example.api_gateway.data.vo.v1.PersonVO;
import com.example.api_gateway.exceptions.ResourceNotFoundException;
import com.example.api_gateway.mapper.MyMapper;
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

    public PersonVO findById(Long id) throws ResourceNotFoundException {
        logger.info("Find one people");
        var entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        return MyMapper.parseObject(entity,PersonVO.class);
    }

    public List<PersonVO> findAll(){
        logger.info("Find all people");
        return MyMapper.parseListObjects(repository.findAll(),PersonVO.class);
    }

    public PersonVO create(PersonVO person){
        logger.info("creating one person");
        var entity = MyMapper.parseObject(person, Person.class);
        return MyMapper.parseObject(repository.save(entity),PersonVO.class);
    }

    public PersonVO update(PersonVO person) throws ResourceNotFoundException {
        logger.info("updating one person");

        var entity = repository.findById(person.getId())
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return MyMapper.parseObject(repository.save(entity),PersonVO.class);
    }
    public void delete(Long id) throws ResourceNotFoundException {
        logger.info("Deleting one person");
        var entity = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }
}
