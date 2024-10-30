package com.example.estsoft_udon_community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "gathering")
@NoArgsConstructor
@AllArgsConstructor
public class Gathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // @ManyToOne
    // @JoinColumn(name = "article_id", nullable = false)
    // private Article article;
}
