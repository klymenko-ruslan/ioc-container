package outsource_booster.ioc_container.core;

import org.junit.Test;

import junit.framework.Assert;
import outsource_booster.ioc_container.entities.A;
import outsource_booster.ioc_container.entities.B;
import outsource_booster.ioc_container.entities.C;
import outsource_booster.ioc_container.entities.D;

public class ContextTest {
	
	@Test
	public void testHierarchy() {
		AnnotationApplicationContext context = new AnnotationApplicationContext("outsource_booster.ioc_container.entities");
		A a = context.getInstance(A.class);
		Assert.assertEquals("Hello from D", a.getB().getC().getD().getMessage());
	}
	
	@Test(expected = NoSuchBeanException.class)
	public void testWrongContext() {
		AnnotationApplicationContext context = new AnnotationApplicationContext("fake");
		context.getInstance(A.class);	
	}
	
	@Test
	public void testSingleton() {
		AnnotationApplicationContext context = new AnnotationApplicationContext("outsource_booster.ioc_container.entities");
		Assert.assertTrue(context.getInstance(A.class).equals(context.getInstance(A.class)));
		Assert.assertTrue(context.getInstance(B.class).equals(context.getInstance(B.class)));
		Assert.assertTrue(context.getInstance(C.class).equals(context.getInstance(C.class)));
		Assert.assertTrue(context.getInstance(D.class).equals(context.getInstance(D.class)));
	}
	
	@Test
	public void testDifferentContextEquality() {
		AnnotationApplicationContext context = new AnnotationApplicationContext("outsource_booster.ioc_container.entities");
		AnnotationApplicationContext context2 = new AnnotationApplicationContext("outsource_booster.ioc_container.entities");
		Assert.assertTrue(!context.getInstance(A.class).equals(context2.getInstance(A.class)));
		Assert.assertTrue(!context.getInstance(B.class).equals(context2.getInstance(B.class)));
		Assert.assertTrue(!context.getInstance(C.class).equals(context2.getInstance(C.class)));
		Assert.assertTrue(!context.getInstance(D.class).equals(context2.getInstance(D.class)));
	}
}
