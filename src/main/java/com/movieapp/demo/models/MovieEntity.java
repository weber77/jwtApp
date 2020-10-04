package com.movieapp.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String length;
    private String actor;

    public MovieEntity(Long id, String name, String length, String actor) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.actor = actor;
    }

}
