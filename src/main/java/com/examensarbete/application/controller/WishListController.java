package com.examensarbete.application.controller;

import com.examensarbete.application.model.*;
import com.examensarbete.application.repository.*;
import com.examensarbete.application.service.*;
import jakarta.servlet.http.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getWishlist(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Book> userBooks = wishListService.getBooksByUsername(username);
        return ResponseEntity.ok(userBooks);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        book.setUser(user);
        return ResponseEntity.ok(wishListService.addBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        wishListService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(wishListService.updateBook(id, book));
    }

    @GetMapping("/by-title")
    public ResponseEntity<Book> getBookByTitle(@RequestParam String title, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .filter(book -> book.getUser() != null && book.getUser().getUsername().equals(username))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

