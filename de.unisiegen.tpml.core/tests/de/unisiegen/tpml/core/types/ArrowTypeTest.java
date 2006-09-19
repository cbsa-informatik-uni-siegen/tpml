package de.unisiegen.tpml.core.types;

import junit.framework.TestCase;

/**
 * Test class for the {@link de.unisiegen.tpml.core.types.ArrowType}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.types.ArrowType
 */
public class ArrowTypeTest extends TestCase {
  /**
   * Test method for the pretty printer.
   */
  public final void testToString() {
    ArrowType t1 = new ArrowType(UnitType.UNIT, BooleanType.BOOL);
    assertEquals("unit \u2192 bool", t1.toString());
    
    ArrowType t2 = new ArrowType(BooleanType.BOOL, IntegerType.INT);
    assertEquals("bool \u2192 int", t2.toString());
    
    ArrowType t3 = new ArrowType(t1, t2);
    assertEquals("(unit \u2192 bool) \u2192 bool \u2192 int", t3.toString());
    
    ArrowType t4 = new ArrowType(IntegerType.INT, t2);
    assertEquals("int \u2192 bool \u2192 int", t4.toString());
  }
}
