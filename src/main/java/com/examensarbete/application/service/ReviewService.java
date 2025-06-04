package com.examensarbete.application.service;

import com.examensarbete.application.dto.*;
import com.examensarbete.application.model.*;
import com.examensarbete.application.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Review addReview(Long bookId, Long userId, String text, Integer rating) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!reviewRepository.findByBookIdAndUserId(bookId, userId).isEmpty()) {
            throw new RuntimeException("User already reviewed this book");
        }

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setReviewText(text);
        review.setRating(rating);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public double getAverageRating(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public List<ReviewDto> getReviewDtosByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getReviewText(),
                        review.getRating(),
                        review.getUser().getUsername()
                ))
                .toList();
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own review");
        }
        reviewRepository.delete(review);
    }
}
