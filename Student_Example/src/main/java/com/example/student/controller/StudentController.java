package com.example.student.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.model.Student;
import com.example.student.service.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/student/{id}")
    public Student getStudent(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/student/name/{name}")
    public List<Student> getStudentByName(@PathVariable String name) {
        return studentService.getStudentByName(name);
    }

    @GetMapping("/student/count")
    public int getStudentCount() {
        return studentService.getStudentCount();
    }

    @GetMapping("/students/exists/{id}")
    public boolean studentExists(@PathVariable int id) {
        return studentService.studentExists(id);
    }

    @GetMapping("/students/sort")
    public List<Student> sortByName() {
        return studentService.sortByName();
    }
}