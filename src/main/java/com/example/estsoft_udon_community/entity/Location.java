package com.example.estsoft_udon_community.entity;

import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;


@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "upper_location")
    private UpperLocationEnum upperLocation;

    @OneToMany
    private List<Users> users;

}