package com.maidscc.maidscc_task.service;

import com.maidscc.maidscc_task.entities.Patron;
import com.maidscc.maidscc_task.exceptions.PatronNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.PatronsRepository;
import com.maidscc.maidscc_task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatronsBusiness {

    @Autowired
    private PatronsRepository patronsRepository;

    @Transactional(readOnly = true)
    public ResponseModel<List<Patron>> retrieveAllPatrons() {
        List<Patron> patrons = patronsRepository.findAll();
        if (patrons.isEmpty()) {
            return new ResponseModel<>("No patrons found", HttpStatus.NO_CONTENT.value());
        }
        return new ResponseModel<>(patrons, "Patrons retrieved successfully", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = Constants.PATRON_CACHE_KEY , key = "#id")
    public ResponseModel<Patron> getPatronById(Long id) {
        return patronsRepository.findById(id)
                .map(patron -> new ResponseModel<>(patron, "Patron retrieved successfully", HttpStatus.OK.value()))
                .orElseThrow(() -> new PatronNotFoundException("Patron with ID " + id + " not found"));
    }

    @Transactional
    public ResponseModel<Patron> addPatron(Patron patron) {
        Patron savedPatron = patronsRepository.save(patron);
        return new ResponseModel<>(savedPatron, "Patron added successfully", HttpStatus.CREATED.value());
    }

    @Transactional
    @CacheEvict(value = Constants.PATRON_CACHE_KEY , key = "#id")
    public ResponseModel<Patron> updatePatron(Long id, Patron updatedPatron) {
        return patronsRepository.findById(id).map(existingPatron -> {
            existingPatron.setName(updatedPatron.getName());
            existingPatron.setEmail(updatedPatron.getEmail());
            existingPatron.setMembershipStatus(updatedPatron.getMembershipStatus());
            Patron savedPatron = patronsRepository.save(existingPatron);
            return new ResponseModel<>(savedPatron, "Patron updated successfully", HttpStatus.OK.value());
        }).orElse(new ResponseModel<>("No patron found with this ID, update failed", HttpStatus.NOT_FOUND.value()));
    }

    @Transactional
    @CacheEvict(value = Constants.PATRON_CACHE_KEY , key = "#id")
    public ResponseModel<Boolean> deletePatron(Long id) {
        if (patronsRepository.existsById(id)) {
            patronsRepository.deleteById(id);
            return new ResponseModel<>(true, "Patron deleted successfully", HttpStatus.OK.value());
        }
        return new ResponseModel<>(false, "No patron found with this ID, deletion failed", HttpStatus.NOT_FOUND.value());
    }
}
