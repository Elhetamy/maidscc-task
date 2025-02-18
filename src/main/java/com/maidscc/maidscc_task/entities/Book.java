package com.maidscc.maidscc_task.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOOK")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column
    private String genre;

    @Column(name = "availability_status", nullable = false)
    private String availabilityStatus = "Available";

}
