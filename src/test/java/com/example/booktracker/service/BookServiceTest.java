package com.example.booktracker.service;

import com.example.booktracker.entity.Book;
import com.example.booktracker.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //create book
    @Test
    void createBook_HappyCase(){
        Book book=new Book();
        book.setTitle("Test title");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book created=bookService.createBook(book);
        assertEquals("Test title", created.getTitle());
    }

    @Test
    void createBook_NullBook(){
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(null));
    }

    //get all books
    @Test
    void getAllBooks_HappyCase(){
        List<Book> books=List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result=bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    //get by id
    @Test
    void getBookById_HappyCase(){
        Book book=new Book();
        book.setBookId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> found=bookService.getBookById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getBookId());
    }

    @Test
    void findBookById_NotFound(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result=bookService.getBookById(1L);
        assertTrue(result.isEmpty());
    }

    //update book
    @Test
    void updateBook_HappyCase(){
        Book existing=new Book();
        existing.setBookId(1L);
        existing.setTitle("Old title");

        Book updateData=new Book();
        updateData.setTitle("New title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);

        Book updated =bookService.updateBook(1L, updateData);
        assertEquals("New title", updated.getTitle());
    }

    @Test
    void updateBook_Exception(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, ()-> bookService.updateBook(1L, new Book()));
    }

    //delete book
    @Test
    void deleteBook_HappyCase(){
        Book book=new Book();
        book.setBookId(1L);

        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_Exception(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, ()-> bookService.deleteBook(1L));
    }
}
