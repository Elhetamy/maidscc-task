package com.maidscc.maidscc_task.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patrons")
@Data
@AllArgsConstructor
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String address;

    @Column(name = "membership_Status", nullable = false)
    private String membershipStatus;
}
