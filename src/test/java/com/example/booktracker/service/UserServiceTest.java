package com.example.booktracker.service;

import com.example.booktracker.entity.User;
import com.example.booktracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //create user
    @Test
    void createUser_HappyCase(){
        User user=new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser=userService.createUser(user);
        assertEquals(1L, createdUser.getId());
    }

    @Test
    void createUser_Error(){
        User user=new User();
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Error"));

        assertThrows(RuntimeException.class, ()->userService.createUser(user));
        verify(userRepository).save(user);
    }

    //get user by id
    @Test
    void getUserById_HappyCase(){
        User user=new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser=userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_Error(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser=userService.getUserById(1L);
        assertFalse(foundUser.isPresent());
        verify(userRepository).findById(1L);
    }

    //get user by email
    @Test
    void getUserByEmail_HappyCase(){
        User user=new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser=userService.getUserByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_Error(){
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser=userService.getUserByEmail("test@example.com");
        assertFalse(foundUser.isPresent());
        verify(userRepository).findByEmail("test@example.com");
    }

    //delete user
    @Test
    void deleteUser_HappyCase(){
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_Error(){
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class,()->userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
