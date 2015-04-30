import static org.junit.Assert.*;

import org.junit.Test;


public class ClassBTest {

	@Test
	public void testIncrement() {
		int a = 5;
		assertEquals(ClassB.increment(a),6);
	}

	@Test
	public void testDecrement() {
		int a = 5;
		assertEquals(ClassB.decrement(a),4);
	}
}
