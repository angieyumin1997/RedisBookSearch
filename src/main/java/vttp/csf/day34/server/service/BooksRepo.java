package vttp.csf.day34.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vttp.csf.day34.server.models.Book;
import static java.util.Comparator.comparing;

@Service
public class BooksRepo implements RedisRepo {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String BOOK_ENTITY = "booklist";

    @Override
    public void save(final Book book) {
        redisTemplate.opsForList().leftPush(BOOK_ENTITY, book.getId());
        redisTemplate.opsForHash().put(BOOK_ENTITY + "_Map", book.getId(), book);
    }

    @Override
    public List<Book> findAll() {
        List<Object> fromBookList = redisTemplate.opsForList()
                .range(BOOK_ENTITY, 0, 10);
        List<Book> bks = (List<Book>) redisTemplate.opsForHash()
                .multiGet(BOOK_ENTITY + "_Map", fromBookList)
                .stream()
                .filter(Book.class::isInstance)
                .map(Book.class::cast)
                .toList();

        return bks;
    };

    public Book findById(String bookId) {
        Book b = ((Book) redisTemplate.opsForHash()
        .get(BOOK_ENTITY + "_Map", bookId));

        return b;
    };

    public List<Book> searchByTitle(String title) {
        List<Book> bks = findAll();
        List<Book> bookResults = bks.stream().filter(
                x -> x.getTitle()
                        .toLowerCase()
                        .contains(title))
                .collect(Collectors.toList());
        System.out.println(">>>>>>>>title"+title);
        System.out.println(">>>>>>>>booksbytitle"+bookResults);
        return bookResults;
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> bks = findAll();
        List<Book> bookResults = bks.stream().filter(
                x -> x.getAuthor()
                        .toLowerCase()
                        .contains(author))
                .collect(Collectors.toList());
        System.out.println(">>>>>>>>author"+author);
        System.out.println(">>>>>>>>booksByAuthor"+bookResults);
        return bookResults;
    }

    public List<Book> searchByTitleAndAuthor(String author, String title) {
        Set<String> ids = new HashSet<>();
        List<Book> searchResult = new ArrayList<>();
        List<Book> booksByTitle = searchByTitle(title);
        System.out.println(">>>>>>>>booksbytitle"+booksByTitle);
        List<Book> booksByAuthor = searchByAuthor(author);
        System.out.println(">>>>>>>>booksByAuthor"+booksByAuthor);

        for (Book b : booksByTitle) {
            ids.add(b.getId());
        }

        for (Book b : booksByAuthor) {
            if (!ids.add(b.getId())) {
                searchResult.add(b);
            }
        }
        return searchResult;
    }

    public List<Book> sortByAuthor(List<Book> listOfBooks){
        Collections.sort(listOfBooks, comparing(Book::getAuthor));
        return listOfBooks;
    }

    public List<Book> sortByTitle(List<Book> listOfBooks){
        Collections.sort(listOfBooks, comparing(Book::getTitle));
        return listOfBooks;
    }

    public List<Book> getBooks(Integer offset,Integer limit,List<Book> lisOfBooks){
        List<Book> b = new ArrayList<>();
        try{
            b = lisOfBooks.subList(offset, limit + offset);
        }catch(Exception e){
            e.printStackTrace();
        }
        return b;
    }
}
