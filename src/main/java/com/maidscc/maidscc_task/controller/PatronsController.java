package com.maidscc.maidscc_task.controller;

import com.maidscc.maidscc_task.entities.Patron;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.service.PatronsBusiness;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patrons")
@Validated
public class PatronsController {

    @Autowired
    private PatronsBusiness patronsBusiness;

    @GetMapping
    public ResponseEntity<ResponseModel<List<Patron>>> retrieveAllPatrons() {
        ResponseModel<List<Patron>> response = patronsBusiness.retrieveAllPatrons();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{patronId}")
    public ResponseEntity<ResponseModel<Patron>> retrievePatronById(@PathVariable("patronId") Long id) {
        ResponseModel<Patron> response = patronsBusiness.getPatronById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<Patron>> saveNewPatron(@Valid @RequestBody Patron patron) {
        ResponseModel<Patron> response = patronsBusiness.addPatron(patron);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{patronId}")
    public ResponseEntity<ResponseModel<Patron>> updatePatronById(@PathVariable("patronId") Long id,@Valid @RequestBody Patron patron) {
        ResponseModel<Patron> response = patronsBusiness.updatePatron(id, patron);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{patronId}")
    public ResponseEntity<ResponseModel<Boolean>> deletePatronById(@PathVariable("patronId") Long id) {
        ResponseModel<Boolean> response = patronsBusiness.deletePatron(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
