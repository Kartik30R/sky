package com.askver.sky.model;

 import jakarta.persistence.*;
 import lombok.Data;

 import java.util.UUID;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String password;

    @ManyToOne
    private Company company;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_EMPLOYEE;

}