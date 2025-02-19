package com.maidscc.maidscc_task;

import com.maidscc.maidscc_task.entities.Patron;
import com.maidscc.maidscc_task.exceptions.PatronNotFoundException;
import com.maidscc.maidscc_task.models.ResponseModel;
import com.maidscc.maidscc_task.repository.PatronsRepository;
import com.maidscc.maidscc_task.service.PatronsBusiness;
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
public class PatronsBusinessTest {

    @Mock
    private PatronsRepository patronsRepository;

    @InjectMocks
    private PatronsBusiness patronsBusiness;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    void setUp() {
        patron1 = new Patron(1L,"John Doe", "john.doe@example.com", "1234567890","cc","member");
        patron1.setId(1L);
        patron1.setAddress("123 Main St");
        patron1.setMembershipStatus("Active");

        patron2 = new Patron(2L,"Jane Doe", "jane.doe@example.com", "0987654321","cc","member");
        patron2.setId(2L);
        patron2.setAddress("456 Elm St");
        patron2.setMembershipStatus("Inactive");
    }

    @Test
    void retrieveAllPatrons_ShouldReturnListOfPatrons() {
        // Arrange
        when(patronsRepository.findAll()).thenReturn(Arrays.asList(patron1, patron2));

        // Act
        ResponseModel<List<Patron>> response = patronsBusiness.retrieveAllPatrons();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patrons retrieved successfully", response.getMessage());
        assertEquals(2, response.getData().size());
        verify(patronsRepository, times(1)).findAll();
    }

    @Test
    void retrieveAllPatrons_ShouldReturnNoContentWhenNoPatronsExist() {
        // Arrange
        when(patronsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseModel<List<Patron>> response = patronsBusiness.retrieveAllPatrons();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No patrons found", response.getMessage());
        assertNull(response.getData());
        verify(patronsRepository, times(1)).findAll();
    }

    @Test
    void getPatronById_ShouldReturnPatronWhenFound() {
        // Arrange
        when(patronsRepository.findById(1L)).thenReturn(Optional.of(patron1));

        // Act
        ResponseModel<Patron> response = patronsBusiness.getPatronById(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patron retrieved successfully", response.getMessage());
        assertEquals(patron1, response.getData());
        verify(patronsRepository, times(1)).findById(1L);
    }

    @Test
    void getPatronById_ShouldThrowExceptionWhenPatronNotFound() {
        // Arrange
        when(patronsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PatronNotFoundException.class, () -> patronsBusiness.getPatronById(1L));
        verify(patronsRepository, times(1)).findById(1L);
    }

    @Test
    void addPatron_ShouldSaveAndReturnPatron() {
        // Arrange
        when(patronsRepository.save(patron1)).thenReturn(patron1);

        // Act
        ResponseModel<Patron> response = patronsBusiness.addPatron(patron1);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Patron added successfully", response.getMessage());
        assertEquals(patron1, response.getData());
        verify(patronsRepository, times(1)).save(patron1);
    }

    @Test
    void updatePatron_ShouldUpdateAndReturnPatronWhenFound() {
        // Arrange
        Patron updatedPatron = new Patron(3L,"John Updated", "john.updated@example.com", "1111111111","cc","member");
        updatedPatron.setAddress("Updated Address");
        updatedPatron.setMembershipStatus("Updated Status");

        when(patronsRepository.findById(1L)).thenReturn(Optional.of(patron1));
        when(patronsRepository.save(patron1)).thenReturn(updatedPatron);

        // Act
        ResponseModel<Patron> response = patronsBusiness.updatePatron(1L, updatedPatron);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patron updated successfully", response.getMessage());
        assertEquals(updatedPatron, response.getData());
        verify(patronsRepository, times(1)).findById(1L);
        verify(patronsRepository, times(1)).save(patron1);
    }

    @Test
    void updatePatron_ShouldReturnNotFoundWhenPatronDoesNotExist() {
        // Arrange
        when(patronsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseModel<Patron> response = patronsBusiness.updatePatron(1L, patron1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No patron found with this ID, update failed", response.getMessage());
        assertNull(response.getData());
        verify(patronsRepository, times(1)).findById(1L);
        verify(patronsRepository, never()).save(any(Patron.class));
    }

    @Test
    void deletePatron_ShouldReturnTrueWhenPatronExists() {
        // Arrange
        when(patronsRepository.existsById(1L)).thenReturn(true);

        // Act
        ResponseModel<Boolean> response = patronsBusiness.deletePatron(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patron deleted successfully", response.getMessage());
        assertTrue(response.getData());
        verify(patronsRepository, times(1)).existsById(1L);
        verify(patronsRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatron_ShouldReturnFalseWhenPatronDoesNotExist() {
        // Arrange
        when(patronsRepository.existsById(1L)).thenReturn(false);

        // Act
        ResponseModel<Boolean> response = patronsBusiness.deletePatron(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No patron found with this ID, deletion failed", response.getMessage());
        assertFalse(response.getData());
        verify(patronsRepository, times(1)).existsById(1L);
        verify(patronsRepository, never()).deleteById(anyLong());
    }
}