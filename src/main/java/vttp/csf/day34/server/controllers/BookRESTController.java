package vttp.csf.day34.server.controllers;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.csf.day34.server.models.Book;
import vttp.csf.day34.server.models.Response;
import vttp.csf.day34.server.service.BooksRepo;
import vttp.csf.day34.server.service.RedisRepo;

@RestController
@RequestMapping(path="/api/book", produces=MediaType.APPLICATION_JSON_VALUE)
public class BookRESTController {

    @Autowired
    private RedisRepo redisRepo;

    @Autowired
    private BooksRepo bkrepo;

    private Logger logger = Logger.getLogger(BookRESTController.class.getName());
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postRegistration(@RequestBody String payload) {

        logger.info("Payload:%s".formatted(payload));

        //Read the payload and save the data to database
        String id = UUID.randomUUID().toString().substring(0,8);
        Book book;
        Response resp;
        
        try{
            book = Book.create(payload);
            book.setId(id);
        }catch(Exception ex){
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }

        //Save to database
        redisRepo.save(book);

        resp = new Response();
        resp.setCode(201);
        resp.setMessage(id);
        resp.setData(book.toJson().toString());
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(resp.toJson().toString());
    }

    @GetMapping
    public ResponseEntity<String> getBooks(){
        List<Book> books = redisRepo.findAll();

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for ( Book registration:books)
            arrBuilder.add(registration.toJson());
        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<String> getDetails(
        @PathVariable(name="bookId") String bookId
    ){
        Book book = bkrepo.findById(bookId);
        return ResponseEntity.ok(book.toJson().toString());
    }
}


