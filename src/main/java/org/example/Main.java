package org.example;

import org.example.model.Todo;
import org.example.service.TodoService;

/**
 * Main utility class for Todo Application
 * This class provides helper methods and can be used for testing the application
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Todo List Application Utility");
        System.out.println("==============================");

        // This is a utility class for the Todo web application
        // The main application runs as a web servlet

        System.out.println("To run the application:");
        System.out.println("1. Build with: mvn clean package");
        System.out.println("2. Deploy the WAR file to a servlet container (Tomcat, Jetty, etc.)");
        System.out.println("3. Access via: http://localhost:8080/java-1.0-SNAPSHOT/");

        // Test todo creation (for debugging purposes)
        try {
            Todo testTodo = new Todo("Sample Todo", "This is a test todo");
            System.out.println("\nSample Todo created: " + testTodo);
        } catch (Exception e) {
            System.err.println("Error creating sample todo: " + e.getMessage());
        }
    }

    /**
     * Helper method to validate todo data
     */
    public static boolean isValidTodo(Todo todo) {
        return todo != null &&
               todo.getTitle() != null &&
               !todo.getTitle().trim().isEmpty();
    }

    /**
     * Helper method to format todo for display
     */
    public static String formatTodo(Todo todo) {
        if (todo == null) return "null";

        return String.format("[%s] %s - %s (%s)",
            todo.isCompleted() ? "✓" : " ",
            todo.getTitle(),
            todo.getDescription() != null ? todo.getDescription() : "No description",
            todo.getCreatedAt()
        );
    }
}