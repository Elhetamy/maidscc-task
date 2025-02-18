package com.maidscc.maidscc_task.controller;

import com.maidscc.maidscc_task.entities.Book;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.service.BooksBusiness;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
public class BooksController {

    @Autowired
    private BooksBusiness booksBusiness;

    @GetMapping
    public ResponseEntity<ResponseModel<List<Book>>> retrieveAllBooks() {
        ResponseModel<List<Book>> response = booksBusiness.retrieveAllBooks();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ResponseModel<Book>> retrieveBookById(@PathVariable("bookId") Long id) {
        ResponseModel<Book> response = booksBusiness.getBookById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<Book>> saveNewBook(@Valid @RequestBody Book book) {
        ResponseModel<Book> response = booksBusiness.addBook(book);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ResponseModel<Book>> updateBookById(@PathVariable("bookId") Long id,@Valid @RequestBody Book book) {
        ResponseModel<Book> response = booksBusiness.updateBook(id, book);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ResponseModel<Boolean>> deleteBookById(@PathVariable("bookId") Long id) {
        ResponseModel<Boolean> response = booksBusiness.deleteBook(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
