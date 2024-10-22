package com.example.demo.Entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(nullable = false, length = 100)
    private String roleName;

    @Column(nullable = false, unique = true, length = 100)
    private String role_type;
    @Column(nullable = false, unique = true, length = 1)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;  // To track when the role was created

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate; // To track when the role was last modified

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // One role can be assigned to multiple users
    private Set<UserRole> userRoles = new HashSet<>();

}
