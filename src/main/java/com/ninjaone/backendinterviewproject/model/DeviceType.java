package com.ninjaone.backendinterviewproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "device_type")
@Getter
@Setter
public class DeviceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy="type")
    private Set<Device> devices;

    @ManyToMany
    @JoinTable(
            name = "service_in_device_type",
            joinColumns = @JoinColumn(name = "device_type_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    Set<Service> services;
}
