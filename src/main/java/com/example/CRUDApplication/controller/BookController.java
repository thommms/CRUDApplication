package com.example.CRUDApplication.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.CRUDApplication.model.Book;
import com.example.CRUDApplication.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @Autowired
    BookRepo bookRepo;

    @GetMapping("/getAllBooks")
    private ResponseEntity<List<Book>> getAllBooks(){
        try{
            List<Book> bookList = new ArrayList<>();
            bookRepo.findAll().forEach(bookList::add);

            if (bookList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList,HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    private ResponseEntity<Book> getBookById(@PathVariable Long id){
        Optional<Book> bookData =bookRepo.findById(id);

        if (bookData.isPresent()){
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @PostMapping("/addBook")
//    private ResponseEntity<Book> addBook(@RequestBody Book book){
//        Book bookObj = bookRepo.save(book);
//
//        return new ResponseEntity<>(bookObj, HttpStatus.OK);
//    }
    @PostMapping("/addBooks")
    private ResponseEntity<List<Book>> addBook(@RequestBody List<Book> book){
        List<Book> bookObj = book.stream()
                .map(bookRepo::save) //save each book and collect the results
                .collect(Collectors.toList());
        if (bookObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookObj, HttpStatus.OK);
    }

    @PutMapping("/updateBookById/{id}") //Alternatively, you can use PostMapping as well
    private ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {

        Optional<Book> oldBookData = bookRepo.findById(id);

        if (oldBookData.isPresent()) {
            Book updatedBookData = oldBookData.get();
            updatedBookData.setTitle(newBookData.getTitle());
            updatedBookData.setAuthor(newBookData.getAuthor());

            Book bookObj = bookRepo.save(updatedBookData);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBookById/{id}")
    private ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){
        bookRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
