package com.maidscc.maidscc_task.controller;


import com.maidscc.maidscc_task.entities.BorrowingRecord;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.service.BorrowingBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowingController {

    @Autowired
    private BorrowingBusiness borrowingBusiness;

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<ResponseModel<BorrowingRecord>> borrowBook(
            @PathVariable("bookId") Long bookId, @PathVariable("patronId") Long patronId) {
        ResponseModel<BorrowingRecord> response = borrowingBusiness.borrowBook(bookId, patronId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<ResponseModel<BorrowingRecord>> returnBook(
            @PathVariable("bookId") Long bookId, @PathVariable("patronId") Long patronId) {
        ResponseModel<BorrowingRecord> response = borrowingBusiness.returnBook(bookId, patronId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
