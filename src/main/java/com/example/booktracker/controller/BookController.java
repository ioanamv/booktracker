package com.example.booktracker.controller;

import com.example.booktracker.entity.Book;
import com.example.booktracker.entity.User;
import com.example.booktracker.service.BookService;
import com.example.booktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookController(BookService bookService, UserService userService){
        this.bookService=bookService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Book> createBookForUser(Principal principal, @RequestBody Book book){
        String userId = principal.getName();
        Optional<User> user=userService.getUserById(userId);

        if(user.isPresent()){
            book.setUser(user.get());
            Book savedBook=bookService.createBook(book);
            return ResponseEntity.ok(bookService.createBook(book));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(Principal principal){
        String userId = principal.getName();
        List<Book> books=bookService.getBooksByUserId(userId);
        if (books.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(Principal principal, @PathVariable Long bookId) {
        String userId = principal.getName();
        Optional<Book> book=bookService.getBookById(bookId);

        if (book.isPresent() && book.get().getUser().getUserId().equals(userId))
            return ResponseEntity.ok(book.get());
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(Principal principal, @PathVariable Long bookId, @RequestBody Book updatedBook){
        String userId = principal.getName();
        Optional<Book> existingBook=bookService.getBookById(bookId);

        if(existingBook.isPresent() && existingBook.get().getUser().getUserId().equals(userId))
        {
            updatedBook.setBookId(bookId);
            updatedBook.setUser(existingBook.get().getUser());
            return ResponseEntity.ok(bookService.updateBook(bookId, updatedBook));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(Principal principal, @PathVariable Long bookId){
        String userId = principal.getName();
        Optional<Book> book=bookService.getBookByUserId(bookId, userId);

        if (book.isPresent() && book.get().getUser().getUserId().equals(userId))
        {
            bookService.deleteBook(bookId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
