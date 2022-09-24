package vttp.csf.day34.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
            @RequestParam(name = "sort" , defaultValue = "author") String sort,
            @RequestParam(name = "result", defaultValue = "5") String result,
            @RequestParam(name = "alphabetical", defaultValue = "true") String alphabetical,
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
            if(alphabetical.equals("true")){
                this.searchResults = booksRepo.sortByAuthorAToZ(searchResult);
                Integer l = Math.min(this.searchResults.size(),limit);
                searchResult = this.searchResults.subList(0, l);
                System.out.println(">>>>>sort by author, from a-z");
            }else{
                this.searchResults = booksRepo.sortByAuthorZToA(searchResult);
                Integer l = Math.min(this.searchResults.size(),limit);
                searchResult = this.searchResults.subList(0, l);
                System.out.println(">>>>>sort by author, from z-a");
            }
            
        }else if(sort.equals("title")){
            if(alphabetical.equals("true")){
                this.searchResults = booksRepo.sortByTitleAToZ(searchResult);
                Integer l = Math.min(this.searchResults.size(),limit);
                searchResult = this.searchResults.subList(0, l);
                System.out.println(">>>>>sort by title, from a-z");
            } else{
                this.searchResults = booksRepo.sortByTitleZToA(searchResult);
                Integer l = Math.min(this.searchResults.size(),limit);
                searchResult = this.searchResults.subList(0, l);
                System.out.println(">>>>>sort by title, from z-a");
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
        model.addAttribute("title", title);
        model.addAttribute("author", author);
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
