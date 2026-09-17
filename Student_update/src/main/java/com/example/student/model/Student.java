package com.example.student.model;
 
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	@NotNull(message="Id cannot be empty")
	private int id;
	@NotNull(message="name cannot be empty")
	private String name;
	@NotNull(message="course cannot be empty")
	private String course;
	@Min(value=18,message="Age must be greater than 18")
	private int age;
	
}