package com.maidscc.maidscc_task;

import com.maidscc.maidscc_task.entities.Book;
import com.maidscc.maidscc_task.entities.BorrowingRecord;
import com.maidscc.maidscc_task.entities.Patron;
import com.maidscc.maidscc_task.exceptions.BorrowingRecordNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.BooksRepository;
import com.maidscc.maidscc_task.repository.BorrowingRecordRepository;
import com.maidscc.maidscc_task.repository.PatronsRepository;
import com.maidscc.maidscc_task.service.BorrowingBusiness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingBusinessTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private PatronsRepository patronsRepository;

    @InjectMocks
    private BorrowingBusiness borrowingBusiness;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        // Initialize a book with availability status "Available"
        book = new Book(1L, "Book One", "Author One", 2021, "ISBN-1", "Fiction", "Available");

        // Initialize a patron using the custom constructor
        patron = new Patron(1L,"john", "john.doe@example.com", "1234567890","cc","member");

        // Initialize a borrowing record
        borrowingRecord = new BorrowingRecord(1L, book, patron, LocalDate.now(), null);
    }

    @Test
    void borrowBook_ShouldBorrowBookWhenAvailable() {
        // Arrange
        when(booksRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronsRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        // Act
        ResponseModel<BorrowingRecord> response = borrowingBusiness.borrowBook(1L, 1L);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Book borrowed successfully", response.getMessage());
        assertEquals(borrowingRecord, response.getData());
        verify(booksRepository, times(1)).save(book);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldThrowExceptionWhenBookOrPatronNotFound() {
        // Arrange
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());
        when(patronsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingBusiness.borrowBook(1L, 1L));
        verify(booksRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenBookIsNotAvailable() {
        // Arrange
        book.setAvailabilityStatus("Borrowed"); // Set book as not available
        when(booksRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronsRepository.findById(1L)).thenReturn(Optional.of(patron));

        // Act
        ResponseModel<BorrowingRecord> response = borrowingBusiness.borrowBook(1L, 1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Book is not available for borrowing", response.getMessage());
        assertNull(response.getData());
        verify(booksRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void returnBook_ShouldReturnBookWhenBorrowingRecordExists() {
        // Arrange
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        // Act
        ResponseModel<BorrowingRecord> response = borrowingBusiness.returnBook(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Book returned successfully", response.getMessage());
        assertEquals(borrowingRecord, response.getData());
        assertEquals("Available", book.getAvailabilityStatus());
        verify(booksRepository, times(1)).save(book);
        verify(borrowingRecordRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void returnBook_ShouldReturnNotFoundWhenNoActiveBorrowingRecordExists() {
        // Arrange
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.empty());

        // Act
        ResponseModel<BorrowingRecord> response = borrowingBusiness.returnBook(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No active borrowing record found for this book and patron", response.getMessage());
        assertNull(response.getData());
        verify(booksRepository, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }
}