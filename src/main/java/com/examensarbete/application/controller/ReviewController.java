package com.examensarbete.application.controller;

import com.examensarbete.application.dto.ReviewDto;
import com.examensarbete.application.model.Review;
import com.examensarbete.application.model.User;
import com.examensarbete.application.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Review> addReview(@PathVariable Long bookId, @RequestBody Review request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Review review = reviewService.addReview(bookId, user.getId(), request.getReviewText(), request.getRating());
        return ResponseEntity.ok(review);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long bookId) {
        List<ReviewDto> reviews = reviewService.getReviewDtosByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{bookId}/average")
    public ResponseEntity<Double> getAverage(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getAverageRating(bookId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        reviewService.deleteReview(reviewId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
