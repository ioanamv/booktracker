package com.example.booktracker.service;

import com.example.booktracker.entity.Book;
import com.example.booktracker.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository)
    {
        this.bookRepository=bookRepository;
    }

    public Book createBook(Book book){
        if (book==null){
            throw new IllegalArgumentException("Book must not be null");
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long userId){
        return bookRepository.findById(userId);
    }

    public Optional<Book> getBookByUserId(Long bookid, Long userId)
    {
        return bookRepository.findByBookIdAndUser_UserId(bookid,userId);
    }

    public List<Book> getBooksByUserId(Long userId){
        return bookRepository.findByUser_UserId(userId);
    }

    public Book updateBook(Long bookId, Book updatedBook){
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found"));

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setStatus(updatedBook.getStatus());
        book.setRating(updatedBook.getRating());
        book.setFinishedDate(updatedBook.getFinishedDate());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
        if(!bookRepository.existsById(id)){
            throw new RuntimeException("Book not found");
        }

        bookRepository.deleteById(id);
    }
}
