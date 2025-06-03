package com.examensarbete.application.dto;

import lombok.*;

@Getter
@Setter
public class ReviewDto {

    private Long id;
    private String reviewText;
    private int rating;
    private String username;

    public ReviewDto(Long id, String reviewText, int rating, String username) {
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.username = username;
    }
}
