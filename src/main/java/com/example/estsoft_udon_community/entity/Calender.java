package com.example.estsoft_udon_community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "calender")
@Getter
@Setter
public class Calender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "gathering_id", nullable = false)
//    private Gathering gathering;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime date;

}
