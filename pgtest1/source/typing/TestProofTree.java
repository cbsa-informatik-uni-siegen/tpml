package typing;

import java.io.PushbackReader;
import java.io.StringReader;

import smallstep.Expression;

import l1.Translator;
import l1.lexer.Lexer;
import l1.node.Start;
import l1.parser.Parser;

/**
 * Simple test case for the {@link ProofTree} class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class TestProofTree {
  private static final String SIMPLE = "(+) 1 20";

  private static void printTree(ProofTree tree) {
    ProofTreePrinter printer = new ProofTreePrinter(tree);
    printer.print(System.out);
    System.out.println();
  }
  
  private static void typecheck(Expression expression) throws Exception {
    // create the initial proof tree
    ProofTree tree = ProofTreeFactory.getFactory().createProofTree(expression);
    printTree(tree);

    // apply (APP)
    System.out.println("by (APP):");
    tree = tree.apply(Rule.APP, tree.getRoot());
    printTree(tree);
    
    // apply (CONST)
    System.out.println("by (CONST):");
    tree = tree.apply(Rule.CONST, tree.getRoot().getChildAt(1));
    printTree(tree);
    
    // apply (APP)
    System.out.println("by (APP):");
    tree = tree.apply(Rule.APP, tree.getRoot().getChildAt(0));
    printTree(tree);
    
    // apply (CONST)
    System.out.println("by (CONST):");
    tree = tree.apply(Rule.CONST, tree.getRoot().getChildAt(0).getChildAt(1));
    printTree(tree);
    
    // apply (CONST)
    System.out.println("by (CONST):");
    tree = tree.apply(Rule.CONST, tree.getRoot().getChildAt(0).getChildAt(0));
    printTree(tree);
  }
  
  /**
   * Main function.
   * 
   * @param args command line args.
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
      
      // type checker test
      typecheck(translator.getExpression());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
