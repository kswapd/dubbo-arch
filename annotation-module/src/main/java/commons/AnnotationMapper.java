package commons;

import annotations.MyMeasurement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */
public class AnnotationMapper {

	public <T> T toPOJO(final Class<T> clazz) {
		List<T> result = new LinkedList<T>();
		System.out.println(" ok");


		System.out.println( ((MyMeasurement)clazz.getAnnotation(MyMeasurement.class))  .name());

		T myClassReflect = null;

		try {
			Constructor constructor = clazz.getConstructor(); //获取构造方法
			myClassReflect = (T)constructor.newInstance(); //创建对象
			//Method method = MyClass.class.getMethod("increase", int.class);  //获取方法
			//method.invoke(myClassReflect, 5); //调用方法
			//Field field = MyClass.class.getField("count"); //获取域
			//System.out.println("Reflect -> " + field.getInt(myClassReflect)); //获取域的值
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassReflect;
	}
}
