/**
 * 
 */
package smallstep.printer;

/**
 * Test case for the pretty printer.
 *
 * @author bmeurer
 * @version $Id$
 */
public class Test1 {
  private static Item simpleLet() {
    return new LetItem("x", new AtomicItem("1"), new AtomicItem("x"));
  }
  
  /**
   * Test main method.
   * @param args
   */
  public static void main(String[] args) {
    // simple let expression
    System.out.println(simpleLet().toString());
  }
}
