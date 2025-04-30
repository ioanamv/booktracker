package com.example.booktracker.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {
    public String verifyToken(String token) throws FirebaseAuthException {
        FirebaseToken decodedToken= FirebaseAuth.getInstance().verifyIdToken(token);
        return decodedToken.getUid();
    }
}
