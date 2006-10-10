package de.unisiegen.tpml.core.languages.l2;

import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.BigStepProofRule;
import de.unisiegen.tpml.core.expressions.And;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Or;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l1.L1Language;

/**
 * Big step proof rules for the <b>L2</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.l1.L1BigStepProofRuleSet
 */
public class L2BigStepProofRuleSet extends L1BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L2BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L1</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L1BigStepProofRuleSet#L1BigStepProofRuleSet(L1Language)
   */
  public L2BigStepProofRuleSet(L2Language language) {
    super(language);
    
    // register the big step rules (order is important for guessing!)
    registerByMethodName(L2Language.L2, "AND-FALSE", "applyAnd", "updateAndFalse");
    registerByMethodName(L2Language.L2, "AND-TRUE", "applyAnd", "updateAndTrue");
    registerByMethodName(L2Language.L2, "UNFOLD", "applyUnfold", "updateUnfold");
    registerByMethodName(L2Language.L2, "OR-FALSE", "applyOr", "updateOrFalse");
    registerByMethodName(L2Language.L2, "OR-TRUE", "applyOr", "updateOrTrue");
  }
  
  
  
  //
  // The (AND-FALSE) and (AND-TRUE) rules
  //
  
  /**
   * Applies the <b>(AND-FALSE)</b> or <b>(AND-TRUE)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(AND-FALSE)</b> or <b>(AND-TRUE)</b> rule to.
   */
  public void applyAnd(BigStepProofContext context, BigStepProofNode node) {
    // add the first proof node
    context.addProofNode(node, ((And)node.getExpression()).getE1());
  }
  
  /**
   * Updates the <code>node</code> to which <b>(AND-FALSE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(AND-FALSE)</b>.
   */
  public void updateAndFalse(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // let (AND-TRUE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("AND-TRUE"));
        updateAndTrue(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // we're done with this node
        context.setProofNodeResult(node, result0);
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(AND-TRUE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(AND-TRUE)</b>.
   */
  public void updateAndTrue(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // add a child node for the second expression
        context.addProofNode(node, ((And)node.getExpression()).getE2());
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // let (AND-FALSE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("AND-FALSE"));
        updateAndFalse(context, node);
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  
  
  //
  // The (OR-FALSE) and (OR-TRUE) rules
  //
  
  /**
   * Applies the <b>(OR-FALSE)</b> or <b>(OR-TRUE)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(OR-FALSE)</b> or <b>(OR-TRUE)</b> rule to.
   */
  public void applyOr(BigStepProofContext context, BigStepProofNode node) {
    // add the first proof node
    context.addProofNode(node, ((Or)node.getExpression()).getE1());
  }
  
  /**
   * Updates the <code>node</code> to which <b>(OR-FALSE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(OR-FALSE)</b>.
   */
  public void updateOrFalse(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // let (OR-TRUE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("OR-TRUE"));
        updateOrTrue(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // add a child node for the second expression
        context.addProofNode(node, ((Or)node.getExpression()).getE2());
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(OR-TRUE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(OR-TRUE)</b>.
   */
  public void updateOrTrue(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // we're done with this node
        context.setProofNodeResult(node, result0);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // let (OR-FALSE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("OR-FALSE"));
        updateOrFalse(context, node);
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  
  
  //
  // The (UNFOLD) rule
  //
  
  /**
   * Applies the <b>(UNFOLD)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(UNFOLD)</b> rule to.
   */
  public void applyUnfold(BigStepProofContext context, BigStepProofNode node) {
    // can only be applied to Recursions
    Recursion recursion = (Recursion)node.getExpression();
    context.addProofNode(node, recursion.getE().substitute(recursion.getId(), recursion));
  }
  
  /**
   * Updates the <code>node</code> to which <b>(UNFOLD)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(UNFOLD)</b>.
   */
  public void updateUnfold(BigStepProofContext context, BigStepProofNode node) {
    // forward the result from the child node (may be null)
    context.setProofNodeResult(node, node.getChildAt(0).getResult());
  }
}
