package com.maidscc.maidscc_task;

import com.maidscc.maidscc_task.entities.Book;
import com.maidscc.maidscc_task.exceptions.BookNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.BooksRepository;
import com.maidscc.maidscc_task.service.BooksBusiness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BooksBusinessTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BooksBusiness booksBusiness;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(1L, "Book One", "Author One", 2021, "ISBN-1", "Fiction", "Available");
        book2 = new Book(2L, "Book Two", "Author Two", 2022, "ISBN-2", "Non-Fiction", "Available");
    }

    @Test
    void retrieveAllBooks_ShouldReturnListOfBooks() {
        // Arrange
        when(booksRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        ResponseModel<List<Book>> response = booksBusiness.retrieveAllBooks();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Books retrieved successfully", response.getMessage());
        assertEquals(2, response.getData().size());
        verify(booksRepository, times(1)).findAll();
    }

    @Test
    void retrieveAllBooks_ShouldReturnNoContentWhenNoBooksExist() {
        // Arrange
        when(booksRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseModel<List<Book>> response = booksBusiness.retrieveAllBooks();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No Available Books", response.getMessage());
        assertNull(response.getData());
        verify(booksRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ShouldReturnBookWhenFound() {
        // Arrange
        when(booksRepository.findById(1L)).thenReturn(Optional.of(book1));

        // Act
        ResponseModel<Book> response = booksBusiness.getBookById(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Book retrieved successfully", response.getMessage());
        assertEquals(book1, response.getData());
        verify(booksRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowExceptionWhenBookNotFound() {
        // Arrange
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> booksBusiness.getBookById(1L));
        verify(booksRepository, times(1)).findById(1L);
    }

    @Test
    void addBook_ShouldSaveAndReturnBook() {
        // Arrange
        when(booksRepository.save(book1)).thenReturn(book1);

        // Act
        ResponseModel<Book> response = booksBusiness.addBook(book1);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Book added successfully", response.getMessage());
        assertEquals(book1, response.getData());
        verify(booksRepository, times(1)).save(book1);
    }

    @Test
    void updateBook_ShouldUpdateAndReturnBookWhenFound() {
        // Arrange
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author", 2023, "ISBN-1", "Updated Genre", "Not Available");
        when(booksRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(booksRepository.save(book1)).thenReturn(updatedBook);

        // Act
        ResponseModel<Book> response = booksBusiness.updateBook(1L, updatedBook);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Book updated successfully", response.getMessage());
        assertEquals(updatedBook, response.getData());
        verify(booksRepository, times(1)).findById(1L);
        verify(booksRepository, times(1)).save(book1);
    }

    @Test
    void updateBook_ShouldReturnNotFoundWhenBookDoesNotExist() {
        // Arrange
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseModel<Book> response = booksBusiness.updateBook(1L, book1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No book found with this ID, update failed", response.getMessage());
        assertNull(response.getData());
        verify(booksRepository, times(1)).findById(1L);
        verify(booksRepository, never()).save(any());
    }

    @Test
    void deleteBook_ShouldReturnTrueWhenBookExists() {
        // Arrange
        when(booksRepository.existsById(1L)).thenReturn(true);

        // Act
        ResponseModel<Boolean> response = booksBusiness.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Book deleted successfully", response.getMessage());
        assertTrue(response.getData());
        verify(booksRepository, times(1)).existsById(1L);
        verify(booksRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldReturnFalseWhenBookDoesNotExist() {
        // Arrange
        when(booksRepository.existsById(1L)).thenReturn(false);

        // Act
        ResponseModel<Boolean> response = booksBusiness.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No book found with this ID, deletion failed", response.getMessage());
        assertFalse(response.getData());
        verify(booksRepository, times(1)).existsById(1L);
        verify(booksRepository, never()).deleteById(any());
    }
}