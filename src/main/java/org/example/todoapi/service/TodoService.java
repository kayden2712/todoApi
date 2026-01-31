package org.example.todoapi.service;

import org.example.todoapi.entity.Todo;
import org.example.todoapi.entity.User;
import org.example.todoapi.repository.TodoRepository;
import org.example.todoapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepo;
    private final UserRepository userRepo;

    public TodoService(TodoRepository todoRepo, UserRepository userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    public List<Todo> getMyTodos(String username) {
        return todoRepo.findTodoByUser_Username(username);
    }

    public Todo createTodo(String title, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(false);
        todo.setUser(user);
        return todoRepo.save(todo);
    }

    public Todo updateTodo(Long id, String title, Boolean completed, String username) {
        Todo todo = todoRepo.findByIdAndUser_Username(id, username).orElseThrow(() -> new RuntimeException("Todo not found"));
        if (title != null) {
            todo.setTitle(title);
        }
        if (completed != null) {
            todo.setCompleted(completed);
        }
        return todoRepo.save(todo);
    }

    public void deleteTodo(Long id, String username) {
        Todo todo = todoRepo.findByIdAndUser_Username(id, username).orElseThrow(() -> new RuntimeException("Todo not found"));
        todoRepo.delete(todo);
    }

    public void adminDelete(Long id){
        todoRepo.deleteById(id);
    }

}
