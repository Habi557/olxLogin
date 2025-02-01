package com.olx.user.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity()
@Table(name="TestData")
public class TestEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int testId;
	@Column(name="name")
	private String name;
	@Column(name="city")
	private String city;
	public TestEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestEntity(int testId, String name, String city) {
		super();
		this.testId = testId;
		this.name = name;
		this.city = city;
	}
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="userTestDetails_id",referencedColumnName = "id")
	@Override
	public String toString() {
		return "TestEntity [testId=" + testId + ", name=" + name + ", city=" + city + "]";
	}
	
	

}
