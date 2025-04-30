package com.example.booktracker.controller;

import com.example.booktracker.entity.Book;
import com.example.booktracker.entity.User;
import com.example.booktracker.service.BookService;
import com.example.booktracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private UserService userService;

    //post create
    @Test
    void createBook_HappyCase() throws Exception {
        User user=new User();
        user.setUserId(1L);

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Title");
        book.setUser(user);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(bookService.createBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.title").value("Title"));

    }

    @Test
    void createBook_Error() throws Exception{
        User user=new User();
        user.setUserId(1L);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(bookService.createBook(any(Book.class))).thenThrow(new RuntimeException("Creation failed"));

        mockMvc.perform(post("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Title\"}"))
                .andExpect(status().isInternalServerError());
    }

    //get
    @Test
    void getAllBooks_HappyCase() throws Exception {
        Book book=new Book();
        book.setBookId(1L);
        book.setTitle("Title");

        when(bookService.getBooksByUserId(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/books/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void getAllBooks_Error() throws Exception {
        when(bookService.getBooksByUserId(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/books/user/1"))
                .andExpect(status().isInternalServerError());
    }

    //put update
    @Test
    void updateBook_HappyCase() throws Exception {
        User user=new User();
        user.setUserId(1L);

        Book existing=new Book();
        existing.setBookId(1L);
        existing.setUser(user);

        Book updated =new Book();
        updated.setBookId(1L);
        updated.setTitle("Updated book");
        updated.setUser(user);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(existing));
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updated);

        mockMvc.perform(put("/books/user/1/book/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Updated book\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.title").value("Updated book"));
    }

    @Test
    void updateBook_Error() throws Exception {
        User user=new User();
        user.setUserId(1L);

        Book existing=new Book();
        existing.setBookId(1L);
        existing.setUser(user);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(existing));
        when(bookService.updateBook(eq(1L), any(Book.class))).thenThrow(new RuntimeException("Update failed"));

        mockMvc.perform(put("/books/user/1/book/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Updated book\"}"))
                .andExpect(status().isInternalServerError());
    }

    //delete
    @Test
    void deleteBook_HappyCase() throws Exception {
        User user=new User();
        user.setUserId(1L);

        Book book=new Book();
        book.setBookId(1L);
        book.setUser(user);

        when(bookService.getBookByUserId(1L,1L)).thenReturn(Optional.of(book));
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/user/1/book/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_Error() throws Exception {
        User user=new User();
        user.setUserId(1L);

        Book book=new Book();
        book.setBookId(1L);
        book.setUser(user);

        when(bookService.getBookByUserId(1L,1L)).thenReturn(Optional.of(book));
        doThrow(new RuntimeException("Delete failed")).when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/user/1/book/1"))
                .andExpect(status().isInternalServerError());
    }
}
