package applications;

import commons.AnnotationMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import models.Bar;
import models.Foo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */
public class main {
	public static void main(String args[]){
		//Bar bar = new Bar();
		//System.out.println(bar.getName());

		Map<String,Object> mapValue = new ConcurrentHashMap<String,Object>();

		mapValue.put("name", "kxw");
		mapValue.put("age", 33);

		System.out.println("test self annotation------");

		Bar bar = null;
		Bar bb = new Bar();
		//bb.getClass() = Bar.class
		AnnotationMapper mapper = new AnnotationMapper();
		bar = mapper.toPOJO(Bar.class, mapValue);
		System.out.println(bar.toString());




		System.out.println("test spring annotation------");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "application.xml" });
		context.start();




		Foo f = (Foo)context.getBean("foo");
		f.sayName();
		Bar b = (Bar)context.getBean("bar");
		b.sayNameByFoo();




	}
}
