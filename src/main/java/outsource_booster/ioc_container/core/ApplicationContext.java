package outsource_booster.ioc_container.core;

public abstract class ApplicationContext {
	public abstract <T> T getInstance(Class<?> clazz);
}
