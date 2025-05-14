package com.examensarbete.application.controller;

import com.examensarbete.application.model.*;
import com.examensarbete.application.service.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @GetMapping
    public ResponseEntity<List<Book>> getWishlist() {
        return ResponseEntity.ok(wishListService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(wishListService.addBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(wishListService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        wishListService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
