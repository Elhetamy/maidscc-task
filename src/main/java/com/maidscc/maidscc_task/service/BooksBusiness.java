package com.maidscc.maidscc_task.service;

import com.maidscc.maidscc_task.entities.Book;
import com.maidscc.maidscc_task.exceptions.BookNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.BooksRepository;
import com.maidscc.maidscc_task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BooksBusiness {

    @Autowired
    private BooksRepository booksRepository;

    @Transactional(readOnly = true)
    public ResponseModel<List<Book>> retrieveAllBooks() {
        List<Book> books = booksRepository.findAll();
        if (books.isEmpty()) {
            return new ResponseModel<>("No Available Books", HttpStatus.NO_CONTENT.value());
        }
        return new ResponseModel<>(books, "Books retrieved successfully", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = Constants.BOOK_CACHE_KEY , key = "#id")
    public ResponseModel<Book> getBookById(Long id) {
        return booksRepository.findById(id)
                .map(book -> new ResponseModel<>(book, "Book retrieved successfully", HttpStatus.OK.value()))
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
    }

    @Transactional
    public ResponseModel<Book> addBook(Book book) {
        Book savedBook = booksRepository.save(book);
        return new ResponseModel<>(savedBook, "Book added successfully", HttpStatus.CREATED.value());
    }

    @Transactional
    @CacheEvict(value = Constants.BOOK_CACHE_KEY , key = "#id")
    public ResponseModel<Book> updateBook(Long id, Book updatedBook) {
        return booksRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPublicationYear(updatedBook.getPublicationYear());
            existingBook.setIsbn(updatedBook.getIsbn());
            existingBook.setGenre(updatedBook.getGenre());
            existingBook.setAvailabilityStatus(updatedBook.getAvailabilityStatus());
            Book savedBook = booksRepository.save(existingBook);
            return new ResponseModel<>(savedBook, "Book updated successfully", HttpStatus.OK.value());
        }).orElse(new ResponseModel<>("No book found with this ID, update failed", HttpStatus.NOT_FOUND.value()));
    }

    @Transactional
    @CacheEvict(value = Constants.BOOK_CACHE_KEY , key = "#id")
    public ResponseModel<Boolean> deleteBook(Long id) {
        if (booksRepository.existsById(id)) {
            booksRepository.deleteById(id);
            return new ResponseModel<>(true, "Book deleted successfully", HttpStatus.OK.value());
        }
        return new ResponseModel<>(false, "No book found with this ID, deletion failed", HttpStatus.NOT_FOUND.value());
    }
}