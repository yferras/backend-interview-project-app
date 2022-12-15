package com.ninjaone.backendinterviewproject.database.service;

import com.ninjaone.backendinterviewproject.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Set<Service> findByApplyToAll(boolean applyToAll);
    Optional<Service> findByName(String name);
}