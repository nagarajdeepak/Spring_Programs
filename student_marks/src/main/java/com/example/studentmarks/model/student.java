package com.example.studentmarks.model;

public class student {
	private int id;
	private String name;
	private int java;
	private int spring;
	private int sql;
	
	public student(int id, String name, int java, int spring, int sql) {
		this.id = id;
		this.name = name;
		this.java = java;
		this.spring = spring;
		this.sql = sql;
	}

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

	public int getJava() {
		return java;
	}

	public void setJava(int java) {
		this.java = java;
	}

	public int getSpring() {
		return spring;
	}

	public void setSpring(int spring) {
		this.spring = spring;
	}

	public int getSql() {
		return sql;
	}

	public void setSql(int sql) {
		this.sql = sql;
	}
}
