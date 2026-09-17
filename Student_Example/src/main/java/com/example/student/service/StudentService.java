package com.example.student.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.student.exception.StudentNotFoundException;
import com.example.student.model.Student;

@Service
public class StudentService {

    // Removed empty Student() constructor call to avoid NullPointerExceptions
    private final List<Student> students = Arrays.asList(
            new Student(101, "Janani", "Java"),
            new Student(102, "Ravi", "Python"),
            new Student(103, "Kumar", "Spring Boot"),
            new Student(104, "Pavi", "Django")
    );

    public Student getStudentById(int id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student ID not found: " + id));
    }

    public List<Student> getStudentByName(String name) {
        List<Student> result = students.stream()
                .filter(s -> s.getName() != null && s.getName().equalsIgnoreCase(name))
                .toList();

        if (result.isEmpty()) {
            throw new StudentNotFoundException("Student not Found: " + name);
        }
        return result;
    }

    public int getStudentCount() {
        return students.size();
    }

    public boolean studentExists(int id) {
        return students.stream().anyMatch(s -> s.getId() == id);
    }

    public List<Student> sortByName() {
        return students.stream()
                .filter(s -> s.getName() != null)
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
                .toList();
    }
}