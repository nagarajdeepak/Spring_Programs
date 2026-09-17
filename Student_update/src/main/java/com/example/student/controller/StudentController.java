package com.example.student.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
 
import com.example.student.model.Student;
import com.example.student.service.StudentService;
 
import jakarta.validation.Valid;
 
@RestController
@Validated  
public class StudentController
{
	@Autowired
	StudentService studentservice;
	@GetMapping("/students")
	public List<Student> getallstudents()
	{
		return studentservice.getallstudents();
	}
	@GetMapping("/students/{id}")
	public Student getbyid(@PathVariable int id)
	{
		return studentservice.getbyid(id);
	}
	@PostMapping("/students")
	public String savestudent(@Valid @RequestBody Student student)
	{
		return studentservice.addStudent(student);
		
	}
	@PostMapping("/students1")
	public Student savestudent1(@Valid @RequestBody Student student)
	{
		return studentservice.addStudent1(student);
	}
	
	@PutMapping("/students/{id}")
	public String update(@PathVariable int id, @Valid @RequestBody Student student)
	{
		return studentservice.update(id,student);
	}
	
	@DeleteMapping("/students/{id}")
	public String delete(@PathVariable int id)
	{
		return studentservice.delete(id);
		
	}
	
}
 
 