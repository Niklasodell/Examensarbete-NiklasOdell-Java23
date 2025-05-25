package com.examensarbete.application.service;

import com.examensarbete.application.model.*;
import com.examensarbete.application.repository.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class WishListService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooksByUsername(String username) {
        return bookRepository.findByUser_Username(username);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setImageUrl(book.getImageUrl());
        existingBook.setStatus(book.getStatus());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

