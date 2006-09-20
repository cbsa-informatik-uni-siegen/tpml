package de.unisiegen.tpml.core.types;

import junit.framework.TestCase;

/**
 * Test class for the {@link de.unisiegen.tpml.core.types.RefType}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.types.RefType
 */
public class RefTypeTest extends TestCase {
  /**
   * Test method for the pretty printer.
   */
  public void testToString() {
    RefType t1 = new RefType(IntegerType.INT);
    assertEquals("int ref", t1.toString());
    
    ArrowType t2 = new ArrowType(IntegerType.INT, t1);
    assertEquals("int \u2192 int ref", t2.toString());
    
    RefType t3 = new RefType(t1);
    assertEquals("int ref ref", t3.toString());
    
    TupleType t4 = new TupleType(new MonoType[] { t1, t2, t3 });
    assertEquals("int ref * (int \u2192 int ref) * int ref ref", t4.toString());
    
    RefType t5 = new RefType(t2);
    assertEquals("(int \u2192 int ref) ref", t5.toString());
  }
}
