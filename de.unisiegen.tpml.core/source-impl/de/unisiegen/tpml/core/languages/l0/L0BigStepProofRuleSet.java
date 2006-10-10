package de.unisiegen.tpml.core.languages.l0;

import de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRuleSet;
import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.MultiLambda;
import de.unisiegen.tpml.core.expressions.Projection;

/**
 * Big step proof rules for the <b>L0</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.bigstep.AbstractBigStepProofRuleSet
 */
public class L0BigStepProofRuleSet extends AbstractBigStepProofRuleSet {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new <code>L0BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L0</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  public L0BigStepProofRuleSet(L0Language language) {
    super(language);
    
    // register the big step rules (order is important for guessing!)
    registerByMethodName(L0Language.L0, "APP", "applyApplication", "updateApplication");
    registerByMethodName(L0Language.L0, "BETA-V", "applyBetaValue", "updateBetaValue");
    registerByMethodName(L0Language.L0, "VAL", "applyValue");
  }
  
  
  
  //
  // The (APP) rule
  //
  
  /**
   * Applies the <b>(APP)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(APP)</b> rule to.
   */
  public void applyApplication(BigStepProofContext context, BigStepProofNode node) {
    // check which type of "application" we have here
    Expression e = node.getExpression();
    if (e instanceof Application) {
      // add the first node for the application
      Application application = (Application)e;
      context.addProofNode(node, application.getE1());
    
      // we can add the second node as well if memory is disabled
      if (!context.isMemoryEnabled()) {
        context.addProofNode(node, application.getE2());
      }
    }
    else {
      // otherwise it must be an infix operation
      InfixOperation infixOperation = (InfixOperation)e;
      context.addProofNode(node, new Application(infixOperation.getOp(), infixOperation.getE1()));
      
      // we can add the second as well if memory is disabled
      if (!context.isMemoryEnabled()) {
        context.addProofNode(node, infixOperation.getE2());
      }
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(APP)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(APP)</b>.
   */
  public void updateApplication(BigStepProofContext context, BigStepProofNode node) {
    // determine the expression for the node
    Expression e = node.getExpression();
    
    // further operation depends on the number of child nodes
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the first child node
      BigStepProofNode node0 = node.getChildAt(0);
      
      // add the second child node for the application/infixOperation
      if (e instanceof Application) {
        // the Application case
        Application application = (Application)e;
        context.addProofNode(node, application.getE2(), node0.getResult().getStore());
      }
      else {
        // the InfixOperation case
        InfixOperation infixOperation = (InfixOperation)e;
        context.addProofNode(node, infixOperation.getE2(), node0.getResult().getStore());
      }
    }
    else if (node.getChildCount() == 2) {
      // check if both child nodes are proven
      BigStepProofNode node0 = node.getChildAt(0);
      BigStepProofNode node1 = node.getChildAt(1);
      if (node0.isProven() && node1.isProven()) {
        // add the third child node
        BigStepProofResult result0 = node0.getResult();
        BigStepProofResult result1 = node1.getResult();
        Application application = new Application(result0.getValue(), result1.getValue());
        context.addProofNode(node, application, result1.getStore());
      }
    }
    else if (node.getChildCount() == 3 && node.getChildAt(2).isProven()) {
      // the result of the third child node is the result for this node
      context.setProofNodeResult(node, node.getChildAt(2).getResult());
    }
  }
  
  
  
  //
  // The (BETA-V) rule
  //
  
  /**
   * Applies the <b>(BETA-V)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(BETA-V)</b> rule to.
   */
  public void applyBetaValue(BigStepProofContext context, BigStepProofNode node) {
    // the expression must be an application to a value...
    Application application = (Application)node.getExpression();
    Expression e2 = application.getE2();
    if (!e2.isValue()) {
      throw new IllegalArgumentException("e2 must be a value");
    }
    
    // ...with a lambda or multi lambda expression
    Expression e1 = application.getE1();
    if (e1 instanceof MultiLambda) {
      // multi lambda is special
      MultiLambda multiLambda = (MultiLambda)e1;
      Expression e = multiLambda.getE();
      
      // perform the required substitutions
      String[] identifiers = multiLambda.getIdentifiers();
      for (int n = 0; n < identifiers.length; ++n) {
        // substitute: (#l_n e2) for id
        e = e.substitute(identifiers[n], new Application(new Projection(identifiers.length, n + 1), e2));
      }
      
      // add the proof node for e
      context.addProofNode(node, e);
    }
    else {
      Lambda lambda = (Lambda)application.getE1();
      context.addProofNode(node, lambda.getE().substitute(lambda.getId(), e2));
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(BETA-V)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(BETA-V)</b>.
   */
  public void updateBetaValue(BigStepProofContext context, BigStepProofNode node) {
    // forward the result of the first child node to this node (may be null)
    context.setProofNodeResult(node, node.getChildAt(0).getResult());
  }
  
  
  
  //
  // The (VAL) rule
  //
  
  /**
   * Applies the <b>(VAL)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(VAL)</b> rule to.
   */
  public void applyValue(BigStepProofContext context, BigStepProofNode node) {
    if (!node.getExpression().isValue()) {
      throw new IllegalArgumentException("(VAL) can only be applied to values");
    }
    context.setProofNodeResult(node, node.getExpression());
  }
}
