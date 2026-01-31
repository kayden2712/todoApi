package org.example.todoapi.repository;

import org.example.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findTodoByUser_Username(String username);
    Optional<Todo> findByIdAndUser_Username(Long id, String username);
}
