package vttp.csf.day34.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.csf.day34.server.models.Book;
import vttp.csf.day34.server.service.BooksRepo;

@Controller
@RequestMapping(path = "/search")
public class BookController {

    @Autowired
    private BooksRepo booksRepo;

    private List<Book> searchResults;

    private Logger logger = Logger.getLogger(BookController.class.getName());

    @GetMapping
    public String search(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "author") String author,
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "result") String result,
            Model model) {

        List<Book> searchResult = new ArrayList<>();
        Integer limit = Integer.parseInt(result);
        if (title.isEmpty()) {
            System.out.println(">>>>>>>>>search by author");
            searchResult = booksRepo.searchByAuthor(author);
            System.out.println(">>>>>>>>>results"+searchResult);

        } else if (author.isEmpty()) {
            System.out.println(">>>>>>>>>search by title");
            searchResult = booksRepo.searchByTitle(title);
            System.out.println(">>>>>>>>>results"+searchResult);

        } else {
            System.out.println(">>>>>>>>>search by author and title");
            searchResult = booksRepo.searchByTitleAndAuthor(author,title);
            System.out.println(">>>>>>>>>results"+searchResult);

        }

        if (sort.equals("author")){
            try{
            this.searchResults = booksRepo.sortByAuthor(searchResult);
            searchResult = this.searchResults.subList(0, limit + 0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                this.searchResults = booksRepo.sortByTitle(searchResult);
                searchResult = this.searchResults.subList(0, limit + 0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        List <Book> prevResults = booksRepo.getBooks(0-limit, limit, searchResults);
        List <Book> nextResults = booksRepo.getBooks(0+limit, limit, searchResults);

        model.addAttribute("books", searchResult);
        model.addAttribute("prev", 0 - limit);
        model.addAttribute("next", 0 + limit);  
        model.addAttribute("limit", limit);
        model.addAttribute("prevResults", prevResults);
        model.addAttribute("nextResults", nextResults);
        return "results";
    }

    @PostMapping
    public String pagination(
        @RequestParam(name = "limit") Integer limit,
        @RequestParam(name = "offset") Integer offset,
        Model model) {
            List <Book> results = booksRepo.getBooks(offset, limit, searchResults);
            List <Book> prevResults = booksRepo.getBooks(offset-limit, limit, searchResults);
            List <Book> nextResults = booksRepo.getBooks(offset+limit, limit, searchResults);
    
            model.addAttribute("books", results);
            model.addAttribute("prev", offset - limit);
            model.addAttribute("next", offset + limit);
            model.addAttribute("limit", limit);
            model.addAttribute("prevResults", prevResults);
            model.addAttribute("nextResults", nextResults);
            return "results";
    }
}
