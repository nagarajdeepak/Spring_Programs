package com.example.student.model;

public class Student {
    private int id;
    private String name;
    private String course;
    private String extraDetail;

    // Default Constructor
    public Student() {
    }

    // Parameterized Constructor matching your service initialization
    public Student(int id, String name, String course, String extraDetail) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.extraDetail = extraDetail;
    }

    // Constructor matching 3 arguments
    public Student(int id, String name, String course) {
        this(id, name, course, null);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getExtraDetail() {
        return extraDetail;
    }

    public void setExtraDetail(String extraDetail) {
        this.extraDetail = extraDetail;
    }
}