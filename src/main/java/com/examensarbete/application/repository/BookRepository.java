package com.examensarbete.application.repository;

import com.examensarbete.application.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser_Username(String username);
}
