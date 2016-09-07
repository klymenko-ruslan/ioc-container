package outsource_booster.ioc_container.core;

public class NoSuchBeanException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NoSuchBeanException(){}
	
	public NoSuchBeanException(String message) {
		super(message);
	}

}
