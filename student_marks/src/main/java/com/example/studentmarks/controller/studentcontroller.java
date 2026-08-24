package com.example.studentmarks.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmarks.model.student;
import com.example.studentmarks.service.studentservice;

@RestController
@RequestMapping("/student")
public class studentcontroller {
	@Autowired
	private studentservice studentservice;
	
	@PostMapping("/marks")
	public Map<String, Object> calculateMarks(@RequestBody student student){
		return studentservice.calculateMarks(student);
	}
	
	@GetMapping("/test")
	public String test() {
		return "Student Marks Application is Running";
	}
	
}
