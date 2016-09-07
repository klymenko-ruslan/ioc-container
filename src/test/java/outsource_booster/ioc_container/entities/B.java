package outsource_booster.ioc_container.entities;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class B {
	private C c;

	public C getC() {
		return c;
	}

	// Setter injection
	@Inject
	public void setC(C c) {
		this.c = c;
	}
}
