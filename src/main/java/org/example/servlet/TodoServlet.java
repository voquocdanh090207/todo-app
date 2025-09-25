package org.example.servlet;

import com.google.gson.Gson;
import org.example.model.Todo;
import org.example.service.TodoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/todos/*")
public class TodoServlet extends HttpServlet {
    private TodoService todoService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new Gson();
        todoService = new TodoService();
        System.out.println("Using Firebase TodoService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setResponseHeaders(response);

        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all todos
                List<Todo> todos = todoService.getAllTodos();
                out.print(gson.toJson(todos));
            } else {
                // Get specific todo by ID
                String id = pathInfo.substring(1);
                Todo todo = todoService.getTodoById(id);
                if (todo != null) {
                    out.print(gson.toJson(todo));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Todo not found\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setResponseHeaders(response);

        PrintWriter out = response.getWriter();

        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Todo todo = gson.fromJson(requestBody, Todo.class);

            String id = todoService.createTodo(todo);
            todo.setId(id);

            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(gson.toJson(todo));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setResponseHeaders(response);

        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"ID is required for update\"}");
            return;
        }

        try {
            String id = pathInfo.substring(1);
            String requestBody = request.getReader().lines().collect(Collectors.joining());

            if (requestBody.contains("\"toggle\":true")) {
                // Toggle completion status
                boolean success = todoService.toggleTodoStatus(id);

                if (success) {
                    Todo updatedTodo = todoService.getTodoById(id);
                    out.print(gson.toJson(updatedTodo));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Todo not found\"}");
                }
            } else {
                // Update todo
                Todo todo = gson.fromJson(requestBody, Todo.class);
                boolean success = todoService.updateTodo(id, todo);

                if (success) {
                    Todo updatedTodo = todoService.getTodoById(id);
                    out.print(gson.toJson(updatedTodo));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Todo not found\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setResponseHeaders(response);

        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"ID is required for deletion\"}");
            return;
        }

        try {
            String id = pathInfo.substring(1);
            boolean success = todoService.deleteTodo(id);

            if (success) {
                out.print("{\"message\":\"Todo deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Todo not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setResponseHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
