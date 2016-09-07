package outsource_booster.ioc_container.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectionUtil {
	
	private static String SET_PREFIX = "set";
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> clazz) {
		try {
			if(clazz.getConstructors().length == 0) 
				return clazz.newInstance();
			int numOfParams = clazz.getConstructors()[0].getParameterCount();
			Object [] emptyArgs = new Object[numOfParams];
			return	(T)clazz.getConstructors()[0].newInstance(emptyArgs);
		} catch (InstantiationException 
				| IllegalAccessException 
				| InvocationTargetException 
				| SecurityException e) {
			throw new BeanCreationException(e + "");
		}
	}
	
	public static void setField(Field field, Object instance, Object value) {
		try {
			field.setAccessible(true);
			field.set(instance, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BeanCreationException();
		}
	}
	
	public static boolean isSetterMethod(Method method, Object object) {
		try {
			String propertyName = getPropertyNameForSetter(method);
			Field property = object.getClass().getDeclaredField(propertyName);
			return method.getName().startsWith(SET_PREFIX)
					&& property != null
					&& method.getParameters().length == 1
					&& method.getParameters()[0].getType().equals(property.getType());
		} catch (NoSuchFieldException | SecurityException e) {
			throw new BeanCreationException();
		}
	}
	
	private static String getPropertyNameForSetter(Method method) {
		String propertyName = method.getName().replaceFirst(SET_PREFIX, "");
		return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
	}
	
	public static Field getPropertyForSetter(Method method, Object object) {
		try {
			String propertyName = getPropertyNameForSetter(method);
			return object.getClass().getDeclaredField(propertyName);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new BeanCreationException();
		} 
	}
	
	public static Field[] getPropertiesForConstructor(Constructor<?> constructor, Object object) {
		try {
			Field [] fields = new Field[constructor.getParameters().length];
			int i = 0;
			for(Parameter parameter : constructor.getParameters()) {
				for(Field field : object.getClass().getDeclaredFields()) {
					if(field.getType().equals(parameter.getType())) {
						fields[i++] = field;
					}
				}
			}
			if(fields.length != i) throw new BeanCreationException();
			return fields;
		} catch (SecurityException e) {
			throw new BeanCreationException(e+"");
		} 
	}
}
