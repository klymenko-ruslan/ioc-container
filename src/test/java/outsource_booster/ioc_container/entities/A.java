package outsource_booster.ioc_container.entities;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class A {
	
	// Property injection
	@Inject
	private B b;

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
}
