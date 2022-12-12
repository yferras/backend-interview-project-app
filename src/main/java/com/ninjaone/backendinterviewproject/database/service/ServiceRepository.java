package com.ninjaone.backendinterviewproject.database.service;

import com.ninjaone.backendinterviewproject.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}