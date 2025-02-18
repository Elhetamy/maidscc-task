package com.maidscc.maidscc_task.service;

import com.maidscc.maidscc_task.entities.Book;
import com.maidscc.maidscc_task.entities.BorrowingRecord;
import com.maidscc.maidscc_task.entities.Patron;
import com.maidscc.maidscc_task.exceptions.BorrowingRecordNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.BooksRepository;
import com.maidscc.maidscc_task.repository.BorrowingRecordRepository;
import com.maidscc.maidscc_task.repository.PatronsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingBusiness {

    @Autowired
    private BorrowingRecordRepository BorrowingRecordRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private PatronsRepository patronsRepository;

    @Transactional
    public ResponseModel<BorrowingRecord> borrowBook(Long bookId, Long patronId) {
        Optional<Book> bookOpt = booksRepository.findById(bookId);
        Optional<Patron> patronOpt = patronsRepository.findById(patronId);

        if (!bookOpt.isPresent() || !patronOpt.isPresent()) {
            throw new BorrowingRecordNotFoundException("Book ID " + bookId + " or Patron ID " + patronId + " not found");
        }

        Book book = bookOpt.get();
        if (!"Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
            return new ResponseModel<>("Book is not available for borrowing", HttpStatus.BAD_REQUEST.value());
        }

        BorrowingRecord borrowing = new BorrowingRecord();
        borrowing.setBook(book);
        borrowing.setPatron(patronOpt.get());
        borrowing.setBorrowDate(LocalDate.now());

        book.setAvailabilityStatus("Borrowed");
        booksRepository.save(book);

        BorrowingRecord savedBorrowing = BorrowingRecordRepository.save(borrowing);
        return new ResponseModel<>(savedBorrowing, "Book borrowed successfully", HttpStatus.CREATED.value());
    }

    @Transactional
    public ResponseModel<BorrowingRecord> returnBook(Long bookId, Long patronId) {
        Optional<BorrowingRecord> borrowingOpt = BorrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);

        if (!borrowingOpt.isPresent()) {
            return new ResponseModel<>("No active borrowing record found for this book and patron", HttpStatus.NOT_FOUND.value());
        }

        BorrowingRecord borrowing = borrowingOpt.get();
        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setAvailabilityStatus("Available");
        booksRepository.save(book);

        BorrowingRecord updatedBorrowing = BorrowingRecordRepository.save(borrowing);
        return new ResponseModel<>(updatedBorrowing, "Book returned successfully", HttpStatus.OK.value());
    }
}
