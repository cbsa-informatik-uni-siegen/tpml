/**
 * 
 */

package l1;

import java.io.*;
import java.util.ListIterator;

import expressions.Exn;
import expressions.Expression;
import expressions.Rule;
import expressions.RuleChain;
import expressions.Value;

import l1.lexer.*;
import l1.node.*;
import l1.parser.*;

/**
 * Simple test class for the parser translator logic.
 *
 * @author bmeurer
 * @version $Id$
 */
public class Test1 {
  private static final String SIMPLE = "let f = lambda x.(if x = 0 then (+) else (-)) x 1 in f (f 9)";
  
  private static void evaluate(Expression e) {
    System.out.println(e);
    while (!(e instanceof Value) && !(e instanceof Exn)) {
      RuleChain ruleChain = new RuleChain();
      e = e.evaluate(ruleChain);
      
      if (ruleChain.isEmpty())
        break;
      
      System.out.print(" -> " + e + " [");
      
      ListIterator<Rule> it = ruleChain.listIterator();
      while (it.hasNext()) {
        if (it.hasPrevious())
          System.out.print(" ");
        System.out.print("(" + it.next() + ")");
      }
      System.out.println("]");
    }
    System.out.println();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      // Allocate the parser
      Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(SIMPLE), 1024)));
      
      // Parse the input
      Start tree = parser.parse();
      
      // translate the AST to a small step expression
      Translator translator = new Translator();
      tree.apply(translator);
      
      // evaluate the resulting small step expression
      evaluate(translator.getExpression());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
