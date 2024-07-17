package com.example.employeemanagement.employee;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String department;
    private String photoPath;
}
