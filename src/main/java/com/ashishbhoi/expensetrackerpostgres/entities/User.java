package com.ashishbhoi.expensetrackerpostgres.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = "email"),
        @UniqueConstraint(name = "unique_username", columnNames = "username")
})
public class User {
    @Column(name = "user_id", updatable = false)
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 10,
            initialValue = 11
    )
    @GeneratedValue(
            generator = "user_sequence"
    )
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Category> categories;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;
}
