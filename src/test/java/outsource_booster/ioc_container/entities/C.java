package outsource_booster.ioc_container.entities;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class C {
	private D d;
	
	public D getD() {
		return d;
	}

	public void setD(D d) {
		this.d = d;
	}

	@Inject
	public C(D d) {
		
	}
}
