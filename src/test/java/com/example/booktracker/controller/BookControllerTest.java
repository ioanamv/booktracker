package com.example.booktracker.controller;

import com.example.booktracker.entity.Book;
import com.example.booktracker.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

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

    //post create
    @Test
    void createBook_HappyCase() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");

        when(bookService.createBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"));

    }

    @Test
    void createBook_Error() throws Exception{
        when(bookService.createBook(any(Book.class))).thenThrow(new RuntimeException("Creation failed"));

        mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Title\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Creation failed"));
    }

    //get
    @Test
    void getAllBooks_HappyCase() throws Exception {
        Book book=new Book();
        book.setId(1L);
        book.setTitle("Title");

        when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void getAllBooks_Error() throws Exception {
        when(bookService.getAllBooks()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isInternalServerError());
    }

    //put update
    @Test
    void updateBook_HappyCase() throws Exception {
        Book book=new Book();
        book.setId(1L);
        book.setTitle("Updated book");

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Updated book\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated book"));
    }

    @Test
    void updateBook_Error() throws Exception {
        when(bookService.updateBook(eq(1L), any(Book.class))).thenThrow(new RuntimeException("Update failed"));

        mockMvc.perform(put("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Updated book\"}"))
                .andExpect(status().isInternalServerError());
    }

    //delete
    @Test
    void deleteBook_HappyCase() throws Exception {
        doNothing().when(bookService).deleteBook(1L);
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_Error() throws Exception {
        doThrow(new RuntimeException("Delete failed")).when(bookService).deleteBook(1L);
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isInternalServerError());
    }
}
