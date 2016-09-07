package outsource_booster.ioc_container.core;

public class BeanCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BeanCreationException(){}
	
	public BeanCreationException(String message) {
		super(message);
	}
}
