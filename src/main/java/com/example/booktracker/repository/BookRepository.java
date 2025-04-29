package com.example.booktracker.repository;

import com.example.booktracker.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookIdAndUser_UserId(Long bookId, Long userId);
    List<Book> findByUser_UserId(Long userId);
}
