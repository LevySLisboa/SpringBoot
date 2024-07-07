package com.example.api_gateway.services;

import com.example.api_gateway.model.Person;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PersonServices.class);
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById(int id){
        logger.info("Find one people");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro");
        person.setLastName("Costa");
        person.setAddress("Uberlandia - Minas Gerais - Brasil");
        person.setGender("Male");

        return person;
    }

    public List<Person> findAll(){
        List<Person> list = new ArrayList<>();
        logger.info("Find all persons");
        for(int i=0;i<8;i++){
            Person mockPerson = mockPerson(i);
            list.add(mockPerson);
        }
        return list;
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("First name: "+ i);
        person.setLastName("Last name: "+ i);
        person.setAddress("Address: "+ i);
        person.setGender("Male");
        return person;
    }
    public Person create(Person person){
        logger.info("creating one person");
        return person;
    }

    public Person update(Person person){
        logger.info("updating one person");
        return person;
    }
    public void delete(int id){
        logger.info("Deleting one person");
    }
}
