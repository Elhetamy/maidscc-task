package com.maidscc.maidscc_task.repository;

import com.maidscc.maidscc_task.entities.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronsRepository extends JpaRepository<Patron, Long> {
}
