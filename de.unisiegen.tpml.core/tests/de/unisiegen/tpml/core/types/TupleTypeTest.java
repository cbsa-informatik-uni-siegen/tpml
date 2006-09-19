package de.unisiegen.tpml.core.types;

import junit.framework.TestCase;

/**
 * Test class for the {@link de.unisiegen.tpml.core.types.ArrowType}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.types.TupleType
 */
public class TupleTypeTest extends TestCase {
  /**
   * Test method for the pretty printer.
   */
  public void testToString() {
    TupleType t1 = new TupleType(new MonoType[] { IntegerType.INT, BooleanType.BOOL });
    assertEquals("int * bool", t1.toString());
    
    TupleType t2 = new TupleType(new MonoType[] { t1, t1 });
    assertEquals("(int * bool) * (int * bool)", t2.toString());
    
    TupleType t3 = new TupleType(new MonoType[] { t2, new ArrowType (t1, t1) });
    assertEquals("((int * bool) * (int * bool)) * (int * bool \u2192 int * bool)", t3.toString());
    
    ArrowType t4 = new ArrowType(new ArrowType (t1, t1), t1);
    assertEquals("(int * bool \u2192 int * bool) \u2192 int * bool", t4.toString());
  }
}
