package com.example.booktracker.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize(){
        try{
            FileInputStream serviceAccount=new FileInputStream("src/main/resources/firebase.json");
            FirebaseOptions options=FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            if (FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
