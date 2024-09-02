package com.example.api_gateway.services;

import com.example.api_gateway.controller.BookController;
import com.example.api_gateway.data.vo.v1.BookVO;
import com.example.api_gateway.exceptions.RequiredObjectIsNullException;
import com.example.api_gateway.exceptions.ResourceNotFoundException;
import com.example.api_gateway.mapper.MyMapper;
import com.example.api_gateway.model.Book;
import com.example.api_gateway.repositories.BookRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class BookServices {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BookServices.class);
    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    private BookRepository repository;

    @Autowired
    private PagedResourcesAssembler<BookVO> assembler;

    public BookVO findById(Long id) throws ResourceNotFoundException {
        logger.info("Find one Book");
        var entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        var vo = MyMapper.parseObject(entity,BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }
    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) throws ResourceNotFoundException{
        logger.info("Find all books");
        var bookPage = repository.findAll(pageable);
        var bookVosPage = bookPage.map(b -> MyMapper.parseObject(b,BookVO.class));
        bookVosPage.map(b -> {
            try {
                return b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
        return assembler.toModel(bookVosPage,link);
    }

    public BookVO create(BookVO book) throws RequiredObjectIsNullException, ResourceNotFoundException {
        if(book ==null) throw new RequiredObjectIsNullException();
        logger.info("creating one book");
        var entity = MyMapper.parseObject(book, Book.class);
        var vo = MyMapper.parseObject(repository.save(entity),BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO uptade(BookVO book) throws ResourceNotFoundException, RequiredObjectIsNullException {
        if(book==null) throw new RequiredObjectIsNullException();
        logger.info("updating one book");
        var entity = repository.findById(book.getKey()).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        entity.setLaunchDate(book.getLaunchDate());
        var vo = MyMapper.parseObject(repository.save(entity),BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) throws ResourceNotFoundException {
        logger.info("Deleting  one Book");
        var entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
