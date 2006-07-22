package types;

import junit.framework.TestCase;

/**
 * Test case for pretty printing of types. 
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public class TestPrettyPrinting extends TestCase {
  /**
   * Tests the pretty print priorities for arrow types.
   */
  public void testArrowType() {
    ArrowType tau1 = new ArrowType(UnitType.UNIT, IntegerType.INT);
    assertEquals(tau1.toString(), "unit \u2192 int");
    
    ArrowType tau2 = new ArrowType(tau1, BooleanType.BOOL);
    assertEquals(tau2.toString(), "(unit \u2192 int) \u2192 bool");
    
    ArrowType tau3 = new ArrowType(tau1, tau2);
    assertEquals(tau3.toString(), "(unit \u2192 int) \u2192 (unit \u2192 int) \u2192 bool");
  }
}
