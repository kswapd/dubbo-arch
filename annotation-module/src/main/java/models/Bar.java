package models;


import annotations.BeanBuilder;
import annotations.MyColumn;
import annotations.MyMeasurement;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */

@Component("bar")
@MyMeasurement(name="hi,annotation")
@BeanBuilder
public class Bar {

	@Resource(name="foo")
	private Foo foo;
	@MyColumn(name="col_name")
	private  String name = "test";

	@MyColumn(name="col_name")
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

	public void sayNameByFoo()
	{
		foo.sayName();
	}
	@Override
	public String toString() {
		return "Bar{" +
				"foo=" + foo +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
