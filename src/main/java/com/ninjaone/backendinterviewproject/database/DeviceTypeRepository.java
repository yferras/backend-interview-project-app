package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {
    Optional<DeviceType> findByNameIgnoreCase(String name);
}