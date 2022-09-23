package vttp.csf.day34.server.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vttp.csf.day34.server.models.Book;

@Repository
public interface RedisRepo {
    public void save(final Book book);
    public List<Book> findAll();
}
