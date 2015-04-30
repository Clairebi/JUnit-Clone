import static org.junit.Assert.*;

import org.junit.Test;


public class ClassATest {

	@Test
	public void testTriple() 
	{
		int a = 5;
		assertEquals(ClassA.triple(a), 15);
	}
	
	@Test
	public void testQuadruple()
	{
		int a = 5;
		assertEquals(ClassA.quadruple(a), 20);		
	}

}
