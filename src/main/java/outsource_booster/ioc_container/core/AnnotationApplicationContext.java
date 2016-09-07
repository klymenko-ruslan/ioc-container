package outsource_booster.ioc_container.core;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Scope;

public class AnnotationApplicationContext extends ApplicationContext{
	
	private final static Logger LOGGER = Logger.getLogger(AnnotationApplicationContext.class.getName()); 
	
	private static final String EXTENSION_CLASS = ".class";
	
	private Set<Class<?>> classes = new HashSet<>();
	
	private Map<Class<?>, Object> contextMap = new HashMap<>();
	
	public AnnotationApplicationContext(String basePackage) {
		this(new String[]{basePackage});
	}
	
	public AnnotationApplicationContext(Collection<String> basePackages) {
		this((String [])basePackages.toArray());
	}
	public AnnotationApplicationContext(String [] basePackages) {
		LOGGER.info("Context starts to scan packages.");
		for(String basePackage : basePackages) {
			LOGGER.info("Load classes for " + basePackage + ".");
			loadClassesForPackage(basePackage);
		}
		for(Class<?> clazz : classes) {
			if(clazz.getAnnotation(Named.class) != null) {
				LOGGER.info("Adding " + clazz.getName() + " to the context.");
				contextMap.put(clazz, inject(clazz));
			}
		}		
	}
	
	private Object inject(Class<?> clazz) {
		LOGGER.info("Creating " + clazz.getName() + " instance.");
		Object instantiatedObject = ReflectionUtil.createInstance(clazz);
		LOGGER.info("Injecting " + clazz.getName() + " fields.");
		injectFields(instantiatedObject);
		LOGGER.info("Injecting " + clazz.getName() + " methods.");
		injectMethods(instantiatedObject);
		LOGGER.info("Injecting " + clazz.getName() + " constructors.");
		injectConstructors(instantiatedObject);
		return instantiatedObject;
	}
	
	private void injectFields(Object object) {
		Arrays.stream(object.getClass().getDeclaredFields())
	      .filter(field -> field.getAnnotation(Inject.class) != null)
	      .forEach(field -> ReflectionUtil.setField(field, object, inject(field.getType())));
	}
	
	private void injectMethods(Object object) {
		Arrays.stream(object.getClass().getDeclaredMethods())
	      .filter(method -> method.getAnnotation(Inject.class) != null 
	      					&& ReflectionUtil.isSetterMethod(method, object))
	      .forEach(method -> ReflectionUtil
	    		  				.setField(ReflectionUtil.getPropertyForSetter(method, object), 
	    		  						  object, 
	    		  						  inject(method.getParameters()[0].getType())));
	}
	
	private void injectConstructors(Object object) {
		Arrays.stream(object.getClass().getConstructors())
	      .filter(constructor -> constructor.getAnnotation(Inject.class) != null)
	      .forEach(constructor -> {
	    	  Field [] fields = ReflectionUtil.getPropertiesForConstructor(constructor, object);
	    	  for(Field field : fields) {
	    		  ReflectionUtil.setField(field, object, inject(field.getType()));
	    	  }
	    	  
	});
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<?> clazz) {
		if(contextMap.get(clazz) == null) {
			throw new NoSuchBeanException("There's no bean of type " + clazz.getName() + " in context.");
		}
		return (T) contextMap.get(clazz);
	}
	
	private void loadClassesForPackage(String packageName) {
		URL url = Thread.currentThread()
		      .getContextClassLoader()
		      .getResource(convertPackageNameToUrlFormat(packageName));
		if(url != null && url.getFile() != null) {
			File location = new File(url.getFile());
			if(location.exists()) {
				for(File file : location.listFiles()) {
					if(file.isDirectory()) {
						loadClassesForPackage(packageName + "." + file.getName());
					} else if(file.getName().endsWith(EXTENSION_CLASS)) {
						try {
							classes.add(Class.forName(packageName + "." + file.getName().replace(EXTENSION_CLASS, "")));
						} catch (ClassNotFoundException e) {
							LOGGER.log(Level.WARNING, "There's a file with "
									+ ".class extension in package that is not valid java class.s");
						}
					}
				}
			}
		}
	}
	
	private static String convertPackageNameToUrlFormat(String packageName) {
		return packageName.replace(".", "/");
	}	
}
