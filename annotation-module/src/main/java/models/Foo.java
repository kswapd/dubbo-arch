package models;

import org.springframework.stereotype.Component;

/**
 * Created by kongxiangwen on 6/20/18 w:25.
 */

@Component("foo")
public class Foo {
	private String name="hihi,foo";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void sayName(){
		System.out.println(name);
	}
}
