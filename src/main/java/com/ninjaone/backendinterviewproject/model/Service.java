package com.ninjaone.backendinterviewproject.model;

import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
            length = 32,
            nullable = false
    )
    private String name;

    private double price;

}