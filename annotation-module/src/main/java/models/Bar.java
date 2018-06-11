package models;


import annotations.MyMeasurement;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */

@MyMeasurement(name="hi,annotation")
public class Bar {
	private  String name = "test";
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
