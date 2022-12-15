package com.ninjaone.backendinterviewproject.database.device;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.reports.TotalDevicesPerServiceByUserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByName(String name);

    /**
     * <p>
     * Gets a report containing the amount of devices per service, given the user id.
     * </p>
     * <p>
     * The services that by default are applied to all devices by default (when {@code Service.applyToAll} is true) are
     * excluded.
     * </p>
     *
     * @param userId user's identifier.
     * @return a list of instances of {@link TotalDevicesPerServiceByUserReport}.
     */
    @Query("select " +
            " new com.ninjaone.backendinterviewproject.model.reports.TotalDevicesPerServiceByUserReport(d.userId, count(d), s.name, s.price) " +
            " from Service s inner join s.devices d " +
            " where " +
            " s.applyToAll = false and d.userId = :userId " +
            " group by s.id "
    )
    List<TotalDevicesPerServiceByUserReport> getTotalDevicesPerServiceByUserId(@Param("userId") long userId);

    long countByUserId(long userId);

    boolean existsByUserId(long userId);

}