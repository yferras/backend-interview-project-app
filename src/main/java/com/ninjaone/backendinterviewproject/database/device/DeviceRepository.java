package com.ninjaone.backendinterviewproject.database.device;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import com.ninjaone.backendinterviewproject.model.reports.ITotalCostByUser;
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
     * @return a list of instances of {@link ITotalCostByUser}.
     */
    @Query(
            "select " +
                    "       d.userId as userId, " +
                    "       count(d) as totalDevices, " +
                    "       s.name as serviceName, " +
                    "       s.price as servicePrice " +
                    " from Service s inner join s.devices d " +
                    " where " +
                    "       s.applyToAll = false " +
                    "   and d.userId = :userId " +
                    " group by s.id "
    )
    List<ITotalCostByUser> getTotalDevicesPerServiceByUserId(@Param("userId") long userId);


    @Query(
            " select " +
                    "       d.id as deviceId, " +
                    "       d.name as deviceName, " +
                    "       (sum(s.price) + (select sum(s.price) from Service s where s.applyToAll = true)) as currentCost " +
                    " from Device d inner join d.services s " +
                    " where " +
                    "       s.applyToAll = false " +
                    " group by d.id "
    )
    List<IDeviceReport> getDevicesCurrentCost();

    @Query(
            " select " +
                    "       d.id as deviceId, " +
                    "       d.name as deviceName, " +
                    "       (sum(s.price) + (select sum(s.price) from Service s where s.applyToAll = true)) as currentCost " +
                    " from Device d inner join d.services s " +
                    " where " +
                    "       s.applyToAll = false " +
                    "   and d.id = :deviceId " +
                    " group by d.id "
    )
    IDeviceReport getDeviceCurrentCost(@Param("deviceId") long deviceId);

    @Query(
            " select " +
                    "       d.id as deviceId, " +
                    "       d.name as deviceName, " +
                    "       (sum(s.price) + (select sum(s.price) from Service s where s.applyToAll = true)) as currentCost " +
                    " from Device d inner join d.services s " +
                    " where " +
                    "       s.applyToAll = false " +
                    "   and d.name = :deviceName " +
                    " group by d.id "
    )
    IDeviceReport getDeviceCurrentCost(@Param("deviceName") String deviceName);

    long countByUserId(long userId);

    boolean existsByUserId(long userId);

}