package com.ninjaone.backendinterviewproject.database.device;

import com.ninjaone.backendinterviewproject.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}