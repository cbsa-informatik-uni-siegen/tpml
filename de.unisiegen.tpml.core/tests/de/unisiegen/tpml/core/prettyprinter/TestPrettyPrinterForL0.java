package de.unisiegen.tpml.core.prettyprinter;

import de.unisiegen.tpml.core.expressions.Abstraction;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;

/**
 * Test run for the pretty printer on L0 expressions.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class TestPrettyPrinterForL0 {  
  public static void main(String[] args) {
    try {
      PrettyStringFactory factory = PrettyStringFactory.getFactoryForResource("/de/unisiegen/tpml/core/prettyprinter/TestPrettyPrinterForL0.xml");
      PrettyString string = factory.getPrettyStringForObject(simpleApplication());
      
      System.out.println(string);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Expression simpleApplication() {
    return new Application(new Abstraction("y", new Identifier("y")), new Identifier("x"));
  }
}
