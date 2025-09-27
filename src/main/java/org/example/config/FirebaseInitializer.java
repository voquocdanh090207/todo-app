package org.example.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;

@WebListener
public class FirebaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Lấy project id từ biến môi trường chuẩn của Google App Engine / Cloud Run
                String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
                if (projectId == null) projectId = System.getenv("GCLOUD_PROJECT");
                if (projectId == null) projectId = "todo-473210"; // fallback khi chạy local

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setProjectId(projectId)
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized using Application Default Credentials (projectId=" + projectId + ")");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase with ADC: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Unexpected error initializing Firebase: " + ex.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Firebase context destroyed");
    }
}
