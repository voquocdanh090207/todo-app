package org.example.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.example.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodoService {
    private static final String COLLECTION_NAME = "todos";
    private Firestore firestore;

    public TodoService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public String createTodo(Todo todo) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(COLLECTION_NAME).add(todo);
        DocumentReference docRef = future.get();
        String id = docRef.getId();
        todo.setId(id);
        System.out.println("Created todo with ID: " + id);
        // Update the document with the ID
        ApiFuture<WriteResult> updateFuture = docRef.set(todo);
        updateFuture.get();

        return id;
    }

    public List<Todo> getAllTodos() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Todo> todos = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Todo todo = document.toObject(Todo.class);
            todo.setId(document.getId());
            todos.add(todo);
        }

        return todos;
    }

    public Todo getTodoById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = firestore.collection(COLLECTION_NAME).document(id).get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Todo todo = document.toObject(Todo.class);
            todo.setId(document.getId());
            return todo;
        }

        return null;
    }

    public boolean updateTodo(String id, Todo todo) throws ExecutionException, InterruptedException {
        todo.setId(id);
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(id).set(todo);
        WriteResult result = future.get();
        return result != null;
    }

    public boolean deleteTodo(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(id).delete();
        WriteResult result = future.get();
        return result != null;
    }

    public boolean toggleTodoStatus(String id) throws ExecutionException, InterruptedException {
        Todo todo = getTodoById(id);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            return updateTodo(id, todo);
        }
        return false;
    }
}
