package outsource_booster.ioc_container.entities;

import javax.inject.Named;

@Named
public class D {
	private String message = "Hello from D";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
