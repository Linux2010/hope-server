package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.annotation.Trim;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class TrimAspect {

    @Before("execution(* cc.mrbird.febs..*.*(..)) && args(object)")
    public void trimStrings(Object object) throws IllegalAccessException {
        if (object != null) {
            Class<?> clazz = object.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Trim.class) && field.getType().equals(String.class)) {
                    field.setAccessible(true);
                    String value = (String) field.get(object);
                    if (value != null) {
                        field.set(object, value.trim());
                    }
                }
            }
        }
    }
}