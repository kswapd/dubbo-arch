package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyMeasurement {
	String name();
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
