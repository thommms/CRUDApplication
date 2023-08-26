package com.example.CRUDApplication.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="Books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String author;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
