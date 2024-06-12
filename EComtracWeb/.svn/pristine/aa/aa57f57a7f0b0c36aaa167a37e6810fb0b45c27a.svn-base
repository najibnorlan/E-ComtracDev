package com.ssm.llp.ezbiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_zamzam")
public class TestZamZam implements java.io.Serializable {
	
	private long id;
	private String name;
	private int age;
	
	
	public TestZamZam() {
	}
	public TestZamZam(String name, int age) {
		this.name=name;
		this.age=age;
	}

	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Column(name = "age", nullable = false)
	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}
	
	
	
}
