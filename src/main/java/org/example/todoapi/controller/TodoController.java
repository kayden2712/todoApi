package org.example.todoapi.controller;

import org.springframework.security.core.Authentication;
import org.example.todoapi.entity.Todo;
import org.example.todoapi.service.TodoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Todo> getTodos(Authentication auth) {
        return service.getMyTodos(auth.getName());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Todo create(@RequestBody Map<String, String> body, Authentication auth) {
        return service.createTodo(body.get("title"), auth.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Todo update(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        return service.updateTodo(id, (String) body.get("title"), (Boolean) body.get("completed"), auth.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void delete(@PathVariable Long id, Authentication auth) {
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            service.adminDelete(id);
        } else {
            service.deleteTodo(id, auth.getName());
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public String test(Authentication auth) {
        System.out.println(auth.getAuthorities());
        return "OK";
    }

    @GetMapping("/whoami")
    public Object whoami(Authentication auth) {
        return auth;
    }


}
