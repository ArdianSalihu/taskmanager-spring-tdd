package com.ardian.taskmanager_spring;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NutzerRepository extends JpaRepository<Nutzer, Long> {
    Nutzer findByUsername(String username);
}
