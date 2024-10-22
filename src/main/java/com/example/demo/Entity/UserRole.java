package com.example.demo.Entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles") // Table name

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_role_id; // Primary key for the join table

    @ManyToOne
    @JoinColumn(name = "id", nullable = false) // Foreign key to User
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false) // Foreign key to Role
    private Role role;

    private String status; // Additional field (e.g., ACTIVE, INACTIVE)

    // private String roleName;  // Additional field to store the role name
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate; // Date when the role was assigned

    @PrePersist
    protected void onAssign() {
        this.assignedDate = new Date();
    }
    // Constructors, Getters, and Setters
    // Getters and Setters
    // ...
}
