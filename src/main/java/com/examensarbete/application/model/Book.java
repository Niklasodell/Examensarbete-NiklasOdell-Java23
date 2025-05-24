package com.examensarbete.application.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private String status;
}
