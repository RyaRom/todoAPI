package com.TodoApp.controller;

import com.TodoApp.model.Step;
import com.TodoApp.model.Task;
import com.TodoApp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{taskId}/steps")
    public ResponseEntity<List<Step>> getAllSteps(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getSteps(taskId));
    }

    @GetMapping("/steps/{stepId}")
    public ResponseEntity<Step> getStep(@PathVariable Long stepId) {
        return taskService.getStepById(stepId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        return ResponseEntity.ok(taskService.getCompletedTasks());
    }

    @PostMapping("")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.createOrUpdateTask(task), HttpStatus.CREATED);
    }

    @PostMapping("/{taskId}/steps")
    public ResponseEntity<Step> addStep(@PathVariable Long taskId, @RequestBody Step step) {
        return new ResponseEntity<>(taskService.addStep(taskId, step), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Optional<Task> old = taskService.getTaskById(id);
        if (old.isEmpty()) return ResponseEntity.notFound().build();
        task.setId(id);
        return ResponseEntity.ok(taskService.createOrUpdateTask(task));
    }

    @PutMapping("/{taskId}/steps/{stepId}")
    public ResponseEntity<Step> updateStep(@PathVariable Long taskId, @PathVariable Long stepId, @RequestBody Step step) {
        Optional<Step> old = taskService.getStepById(stepId);
        if (old.isEmpty()) return ResponseEntity.notFound().build();
        step.setId(stepId);
        return ResponseEntity.ok(taskService.addStep(taskId, step));
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long stepId) {
        taskService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
