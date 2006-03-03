package typing;

import java.io.PrintStream;

/**
 * Test printer for the {@link ProofTree} class.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
final class ProofTreePrinter {
  /**
   * Default constructor.
   * 
   * @param proofTree the {@link ProofTree}.
   */
  ProofTreePrinter(ProofTree proofTree) {
    this.proofTree = proofTree;
  }
  
  /**
   * Prints the proof tree to <code>out</code>.
   * 
   * @param out a print stream.
   */
  void print(PrintStream out) {
    printNode((ProofNode) this.proofTree.getRoot(), out, 1, 0);
  }
  
  private int printNode(ProofNode node, PrintStream out, int number, int indent) {
    // indent the given number of spaces
    for (int n = 0; n < indent; ++n)
      out.append(' ');
    
    // print the number and the judgement
    out.print("(" + number + ") " + node.getJudgement());
    
    // append rule (if any)
    if (node.getRule() != null)
      out.print(" " + node.getRule());
    
    // append newline
    out.println();
    
    // increment the number
    number += 1;
    
    // print the child nodes
    for (int i = 0; i < node.getChildCount(); ++i)
      number = printNode((ProofNode) node.getChildAt(i), out, number, indent + 1);
    
    return number;
  }
  
  // member attributes
  private ProofTree proofTree;
}
