package org.example.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.InputStream;
import java.io.IOException;

@WebListener
public class FirebaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Đọc file từ resources
                InputStream serviceAccount = getClass()
                        .getClassLoader()
                        .getResourceAsStream("todo-473210-929d7aa0a54a.json");

                if (serviceAccount == null) {
                    throw new RuntimeException("Firebase credentials file not found in resources!");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId("todo-473210") // Project ID thật
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully with service account file from resources");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            System.err.println("Application will continue with MockTodoService");
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Firebase context destroyed");
    }

}
