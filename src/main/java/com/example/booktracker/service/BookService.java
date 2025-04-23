package com.example.booktracker.service;

import com.example.booktracker.entity.Book;
import com.example.booktracker.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Book not found"));
    }

    public Book updateBook(Long id, Book updatedBook){
        Book book=bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));

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
