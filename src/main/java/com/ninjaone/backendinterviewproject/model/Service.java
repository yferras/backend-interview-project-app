package com.ninjaone.backendinterviewproject.model;

import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Class that represents the service.
 */
@Entity
@Table(name = "service")
@Getter
@Setter
public class Service implements IBusinessEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            unique = true,
            length = 64,
            nullable = false
    )
    private String name;

    private double price;

    @Column(name = "apply_to_all", nullable = false)
    private boolean applyToAll = false;

    @ManyToMany(mappedBy = "services")
    Set<DeviceType> deviceTypes;

    @ManyToMany(mappedBy = "services")
    Set<Device> devices;

}