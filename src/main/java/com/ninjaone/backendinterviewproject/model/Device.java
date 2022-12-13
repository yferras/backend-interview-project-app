package com.ninjaone.backendinterviewproject.model;

import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "device")
@Getter
@Setter
public class Device implements IBusinessEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            length = 64,
            nullable = false,
            unique = true
    )
    private String name;

    @Column(name = "app_user_id", nullable = false)
    private long userId = 0;

    @ManyToOne(optional=false)
    @JoinColumn(name="device_type_id", nullable=false, updatable=false)
    private DeviceType type;

    @ManyToMany
    @JoinTable(
            name = "service_in_device",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    Set<Service> services;
}
