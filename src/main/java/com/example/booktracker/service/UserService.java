package com.example.booktracker.service;

import com.example.booktracker.entity.User;
import com.example.booktracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(String id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}
