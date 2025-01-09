package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.repository.TodoRepository;
import com.example.todo.model.Todo;

import java.util.List;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    public List<Todo> getAllTodos() {
        return todoRepository.findByOrderByCreatedAtDesc();
    }
    
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }
    
    public void toggleTodo(Long id) {
        todoRepository.findById(id).ifPresent(todo -> {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        });
    }
    
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}