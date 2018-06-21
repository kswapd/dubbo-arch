package commons;

import annotations.MyColumn;
import annotations.MyMeasurement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */
public class AnnotationMapper {

	public void getClassFields(final Class<?>... classVarAgrs)
	{

		for (Class<?> clazz : classVarAgrs) {
			System.out.println(clazz.getName());
			for (Field field : clazz.getDeclaredFields()) {
			/*Column colAnnotation = field.getAnnotation(Column.class);
			if (colAnnotation != null) {
				influxColumnAndFieldMap.put(colAnnotation.name(), field);
			}*/
				System.out.println(field.getName());
				MyColumn col = field.getAnnotation(MyColumn.class);
				if(col != null){
					System.out.println("column:"+col.name());
				}

				Resource res = field.getAnnotation(Resource.class);
				if(res != null){
					System.out.println("resource:"+res.name());
				}


			}

			for(Method method:clazz.getDeclaredMethods()){
				System.out.println(method.getName());


			}
		}


	}



	//public <T> void setClassFields(T obj, Map<String, Object>mapValue, final Class<?>... classVarAgrs) throws IllegalArgumentException, IllegalAccessException
	public <T> void setClassFields(T obj, Map<String, Object>mapValue) throws IllegalArgumentException, IllegalAccessException
	{

		//List<?> li = new ArrayList<String>();
		//li.add("aaa");


		//for (Class<?> clazz : classVarAgrs)
		Class<?> clazz = obj.getClass();
		{
			System.out.println(clazz.getName());
			for (Field field : clazz.getDeclaredFields()) {
			/*Column colAnnotation = field.getAnnotation(Column.class);
			if (colAnnotation != null) {
				influxColumnAndFieldMap.put(colAnnotation.name(), field);
			}*/

				String fieldName = field.getName();
				System.out.println(fieldName);
				MyColumn col = field.getAnnotation(MyColumn.class);
				if(col != null){
					System.out.println("column:"+col.name());
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					Class<?> fieldType = field.getType();


					Object value = mapValue.get(fieldName);
					if(value != null) {
						if (String.class.isAssignableFrom(fieldType)) {
							if (value instanceof String) {
								field.set(obj, String.valueOf(value));
							}
						}


						if (double.class.isAssignableFrom(fieldType)) {
							if (value instanceof Double) {
								field.setDouble(obj, ((Double) value).doubleValue());
							}
						}


						if (long.class.isAssignableFrom(fieldType)) {
							if (value instanceof Long) {
								field.setLong(obj, ((Long) value).longValue());
							}
						}
						if (int.class.isAssignableFrom(fieldType)) {
							if (value instanceof Integer) {
								field.setInt(obj, ((Integer) value).intValue());
							}
						}
						if (boolean.class.isAssignableFrom(fieldType)) {
							if (value instanceof Boolean) {
								field.setBoolean(obj, Boolean.valueOf(String.valueOf(value)).booleanValue());
							}
						}


						if (Double.class.isAssignableFrom(fieldType)) {
							if (value instanceof Double) {
								field.set(obj, value);
							}

						}
						if (Long.class.isAssignableFrom(fieldType)) {
							if (value instanceof Long) {
								field.set(obj, Long.valueOf(((Double) value).longValue()));
							}
						}
						if (Integer.class.isAssignableFrom(fieldType)) {
							if (value instanceof Integer) {
								field.set(obj, Integer.valueOf(((Double) value).intValue()));
							}
						}
						if (Boolean.class.isAssignableFrom(fieldType)) {
							if (value instanceof Boolean) {
								field.set(obj, Boolean.valueOf(String.valueOf(value)));
							}
						}
					}






				}


			}

			for(Method method:clazz.getDeclaredMethods()){
				System.out.println(method.getName());


			}
		}


	}

	public <T> T toPOJO(final Class<T> clazz, Map<String, Object>mapValue) {
		List<T> result = new LinkedList<T>();
		System.out.println(" ok");
		getClassFields(clazz);

		System.out.println( ((MyMeasurement)clazz.getAnnotation(MyMeasurement.class))  .name());

		T myClassReflect = null;

		try {
			Constructor constructor = clazz.getConstructor(); //获取构造方法
			myClassReflect = (T)constructor.newInstance(); //创建对象
			//Method method = MyClass.class.getMethod("increase", int.class);  //获取方法
			//method.invoke(myClassReflect, 5); //调用方法
			//Field field = MyClass.class.getField("count"); //获取域
			//System.out.println("Reflect -> " + field.getInt(myClassReflect)); //获取域的值

			//setClassFields(myClassReflect, mapValue, clazz);

			setClassFields(myClassReflect, mapValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassReflect;
	}
}
