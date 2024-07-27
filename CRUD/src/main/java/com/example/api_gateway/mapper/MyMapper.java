package com.example.api_gateway.mapper;

import com.example.api_gateway.data.vo.v1.BookVO;
import com.example.api_gateway.data.vo.v1.PersonVO;
import com.example.api_gateway.model.Book;
import com.example.api_gateway.model.Person;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MyMapper {
    private static ModelMapper mapper = new ModelMapper();
    static {
        mapper.createTypeMap(Person.class, PersonVO.class).addMapping(Person::getId,PersonVO::setKey);
        mapper.createTypeMap(PersonVO.class, Person.class).addMapping(PersonVO::getKey,Person::setId);
        mapper.createTypeMap(Book.class, BookVO.class).addMapping(Book::getId,BookVO::setKey);
        mapper.createTypeMap(BookVO.class, Book.class).addMapping(BookVO::getKey,Book::setId);
    }
    public static <O,D> D parseObject(O origin,Class<D> destination){
        return mapper.map(origin,destination);
    }
    public static <O,D> List<D> parseListObjects(List<O> origin, Class<D> destination){
        List<D> list = new ArrayList<>();
        for(O o: origin){
            list.add(mapper.map(o,destination));
        }
        return list;
    }
}

