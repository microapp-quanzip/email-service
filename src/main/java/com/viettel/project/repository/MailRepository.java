package com.viettel.project.repository;

import com.viettel.project.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MailRepository extends JpaRepository<Email, Long> {
}
