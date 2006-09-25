package de.unisiegen.tpml.core.languages.l3;

import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Fst;
import de.unisiegen.tpml.core.expressions.Projection;
import de.unisiegen.tpml.core.expressions.Snd;
import de.unisiegen.tpml.core.expressions.Tuple;
import de.unisiegen.tpml.core.expressions.UnaryOperatorException;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l2.L2Language;

/**
 * Big step proof rules for the <b>L3</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet
 */
public class L3BigStepProofRuleSet extends L2BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L3BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L3</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L2Language)
   */
  public L3BigStepProofRuleSet(L3Language language) {
    super(language);
    
    // register the rules (order is important for guessing!)
    registerByMethodName("PROJ", "applyProj");
    registerByMethodName("FST", "applyFst");
    registerByMethodName("SND", "applySnd");
    registerByMethodName("TUPLE", "applyTuple", "updateTuple");
  }
  
  
  
  //
  // The (FST) rule
  //
  
  /**
   * Applies the <b>(FST)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyFst(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of fst to a tuple
    Application application = (Application)node.getExpression();
    Fst e1 = (Fst)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  
  
  //
  // The (PROJ) rule
  //

  /**
   * Applies the <b>(PROJ)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyProj(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of a projection to a tuple
    Application application = (Application)node.getExpression();
    Projection e1 = (Projection)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  
  
  //
  // The (SND) rule
  //
  
  /**
   * Applies the <b>(SND)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applySnd(BigStepProofContext context, BigStepProofNode node) throws UnaryOperatorException {
    // the expression must be an application of snd to a tuple
    Application application = (Application)node.getExpression();
    Snd e1 = (Snd)application.getE1();
    Tuple e2 = (Tuple)application.getE2();
    context.setProofNodeResult(node, e1.applyTo(e2));
  }
  
  
  
  //
  // The (TUPLE) rule
  //
  
  /**
   * Applies the <b>(TUPLE)</b> to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void applyTuple(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to Tuples
    Tuple tuple = (Tuple)node.getExpression();
    
    // check if memory is enabled
    if (context.isMemoryEnabled()) {
      // add a child node for the first sub expression
      context.addProofNode(node, tuple.getExpressions(0));
    }
    else {
      // add all child nodes at once
      for (Expression e : tuple.getExpressions()) {
        context.addProofNode(node, e);
      }
    }
  }
  
  /**
   * Updates the <code>node</code>, to which <b>(TUPLE)</b> was applied previously, using
   * the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the big step proof node.
   */
  public void updateTuple(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression at this node
    Tuple tuple = (Tuple)node.getExpression();
    
    // check if all child nodes were created
    if (node.getChildCount() < tuple.getExpressions().length) {
      // verify that the last child node is proven
      if (node.getLastChild().isProven()) {
        // add the next child node
        context.addProofNode(node, tuple.getExpressions(node.getChildCount()));
      }
    }
    else {
      // check if all child nodes are proven
      Expression[] values = new Expression[node.getChildCount()];
      for (int n = 0; n < values.length; ++n) {
        BigStepProofResult result = node.getChildAt(n).getResult();
        if (result == null) {
          // atleast one is not yet proven
          return;
        }
        values[n] = result.getValue();
      }
      
      // all child nodes are proven, we're done
      context.setProofNodeResult(node, new Tuple(values));
    }
  }
}
