package com.examensarbete.application.repository;

import com.examensarbete.application.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByBookIdAndUserId(Long bookId, Long userId);
}
