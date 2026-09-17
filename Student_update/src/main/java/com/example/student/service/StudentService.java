package com.example.student.service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.student.model.Student;
import jakarta.validation.Valid;
 
@Service
public class StudentService {
	List<Student> stlist=new ArrayList<>(
			Arrays.asList(new Student(101,"janani","java",22),
						  new Student(102,"Mala","Python",23),
						  new Student(103,"Malar","java",25)
					));
	//fetch all the students details
	public List<Student> getallstudents()
	{
		return stlist;
	}
	public Student getbyid(int id)
	{
		for(Student s:stlist)
		{
			if(s.getId()==id)
			{
				return s;
			}
		}
		return null;
	}
	public String addStudent(Student student)
	{
		stlist.add(student);
		return "Records Inserted";
	}
	public Student addStudent1(Student student)
	{
		stlist.add(student);
		return student;
	}
	
	public String update(int id,Student student)
	{
		for(Student s:stlist)
		{
			if(s.getId() == id)
			{
				s.setId(student.getId());
				s.setName(student.getName());
				s.setCourse(student.getCourse());
				s.setAge(student.getAge());
				return "Students details updated successfully";  
			}
		}
		return "student Id not found";
	}
	public String delete(int id)
	{
		for(Student s:stlist)
		{
			if(s.getId()==id)
			{
				stlist.remove(s);
				return "Students details deleted successfully";
			}
		}
		return "Student Id not found";
		}
		
	}