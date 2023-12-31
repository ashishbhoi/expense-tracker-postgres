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
@Table(name = "categories")
public class Category {
    @Column(name = "category_id", updatable = false)
    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 10,
            initialValue = 5
    )
    @GeneratedValue(
            generator = "category_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    private String title;
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;
}
