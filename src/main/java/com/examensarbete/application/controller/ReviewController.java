package com.examensarbete.application.controller;

import com.examensarbete.application.dto.ReviewDto;
import com.examensarbete.application.model.*;
import com.examensarbete.application.repository.*;
import com.examensarbete.application.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, BookRepository bookRepository, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
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

    @GetMapping("/by-title")
    public ResponseEntity<List<ReviewDto>> getReviewsByTitle(@RequestParam String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ReviewDto> allDtos = new ArrayList<>();
        for (Book book : books) {
            List<Review> reviews = reviewRepository.findByBookId(book.getId());
            for (Review review : reviews) {
                allDtos.add(new ReviewDto(
                        review.getId(),
                        review.getReviewText(),
                        review.getRating(),
                        review.getUser().getUsername()
                ));
            }
        }
        return ResponseEntity.ok(allDtos);
    }

    @GetMapping("/by-title/average")
    public ResponseEntity<Double> getAverageByTitle(@RequestParam String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }
        List<Integer> allRatings = new ArrayList<>();
        for (Book book : books) {
            List<Review> reviews = reviewRepository.findByBookId(book.getId());
            for (Review review : reviews) {
                allRatings.add(review.getRating());
            }
        }
        double average = allRatings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        return ResponseEntity.ok(average);
    }

    @PostMapping("/by-title")
    public ResponseEntity<Review> submitReviewByTitle(@RequestParam String title,
                                                      @RequestBody Review reviewData,
                                                      HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<Book> books = bookRepository.findByTitle(title);
        Optional<Book> matchingBook = books.stream()
                .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
                .findFirst();
        if (matchingBook.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        Review newReview = reviewService.addReview(
                matchingBook.get().getId(),
                user.getId(),
                reviewData.getReviewText(),
                reviewData.getRating()
        );
        return ResponseEntity.ok(newReview);
    }

}
