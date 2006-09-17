package de.unisiegen.tpml.core.languages.l1;

import de.unisiegen.tpml.core.bigstep.BigStepProofContext;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.bigstep.BigStepProofRule;
import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.BinaryOperatorException;
import de.unisiegen.tpml.core.expressions.BooleanConstant;
import de.unisiegen.tpml.core.expressions.Condition;
import de.unisiegen.tpml.core.expressions.CurriedLet;
import de.unisiegen.tpml.core.expressions.CurriedLetRec;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Let;
import de.unisiegen.tpml.core.expressions.LetRec;
import de.unisiegen.tpml.core.expressions.Recursion;
import de.unisiegen.tpml.core.languages.l0.L0BigStepProofRuleSet;
import de.unisiegen.tpml.core.languages.l0.L0Language;

/**
 * Big step proof rules for the <b>L1</b> and derived languages.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.languages.l0.L0BigStepProofRuleSet
 */
public class L1BigStepProofRuleSet extends L0BigStepProofRuleSet {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1BigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L0</b> or a derived language.
   * 
   * @param language the language for the proof rule set.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   * 
   * @see L0BigStepProofRuleSet#L0BigStepProofRuleSet(L0Language)
   */
  public L1BigStepProofRuleSet(L1Language language) {
    super(language);

    // register the big step rules (order is important for guessing!)
    registerByMethodName("COND-FALSE", "applyCond", "updateCondFalse");
    registerByMethodName("COND-TRUE", "applyCond", "updateCondTrue");
    registerByMethodName("LET", "applyLet", "updateLet");
    registerByMethodName("OP", "applyOp");
  }
  
  
  
  //
  // The (COND-FALSE) and (COND-TRUE) rule
  //
  
  /**
   * Applies the <b>(COND-FALSE)</b> or <b>(COND-TRUE)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(COND-FALSE)</b> or <b>(COND-TRUE)</b> rule to.
   */
  public void applyCond(BigStepProofContext context, BigStepProofNode node) {
    // add the first child node
    context.addProofNode(node, ((Condition)node.getExpression()).getE0());
  }
  
  /**
   * Updates the <code>node</code> to which <b>(COND-FALSE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(COND-FALSE)</b>.
   */
  public void updateCondFalse(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the value of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.TRUE) {
        // let (COND-TRUE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("COND-TRUE"));
        updateCondTrue(context, node);
      }
      else if (result0.getValue() == BooleanConstant.FALSE) {
        // add next proof node for e2
        Condition condition = (Condition)node.getExpression();
        context.addProofNode(node, condition.getE2());
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(COND-TRUE)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(COND-TRUE)</b>.
   */
  public void updateCondTrue(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the result of the first child node
      BigStepProofResult result0 = node.getChildAt(0).getResult();
      
      // the result of the child node must be a boolean value
      if (result0.getValue() == BooleanConstant.FALSE) {
        // let (COND-FALSE) handle the node
        context.setProofNodeRule(node, (BigStepProofRule)getRuleByName("COND-FALSE"));
        updateCondFalse(context, node);
      }
      else if (result0.getValue() == BooleanConstant.TRUE) {
        // add next proof node for e1
        Condition condition = (Condition)node.getExpression();
        context.addProofNode(node, condition.getE1());
      }
    }
    else if (node.getChildCount() == 2 && node.getChildAt(0).isProven() && node.getChildAt(1).isProven()) {
      // use the result of the second child node for this node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  
  
  //
  // The (LET) rule
  //
  
  /**
   * Applies the <b>(LET)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(LET)</b> rule to.
   */
  public void applyLet(BigStepProofContext context, BigStepProofNode node) {
    Expression e = node.getExpression();
    if (e instanceof CurriedLet || e instanceof CurriedLetRec) {
      // determine the first sub expression
      CurriedLet curriedLet = (CurriedLet)e;
      Expression e1 = curriedLet.getE1();
      
      // generate the appropriate lambda abstractions
      String[] identifiers = curriedLet.getIdentifiers();
      for (int n = identifiers.length - 1; n > 0; --n) {
        e1 = new Lambda(identifiers[n], e1);
      }
      
      // add the recursion for letrec
      if (e instanceof CurriedLetRec) {
        e1 = new Recursion(identifiers[0], e1);
      }
      
      // add the proof node
      context.addProofNode(node, e1);
    }
    else {
      // determine the first sub expression
      Let let = (Let)e;
      Expression e1 = let.getE1();
      
      // add the recursion for letrec
      if (e instanceof LetRec) {
        e1 = new Recursion(let.getId(), e1);
      }
      
      // add the proof node
      context.addProofNode(node, e1);
    }
  }
  
  /**
   * Updates the <code>node</code> to which <b>(LET)</b> was applied previously.
   * 
   * @param context the big step proof context.
   * @param node the node to update according to <b>(LET)</b>.
   */
  public void updateLet(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the value of the first child node
      Expression value0 = node.getChildAt(0).getResult().getValue();
      
      // determine the expression for the node
      Expression e = node.getExpression();
      
      // check the expression type
      if (e instanceof CurriedLet) {
        // add a proof node for e2 (CurriedLet/CurriedLetRec)
        CurriedLet curriedLet = (CurriedLet)e;
        context.addProofNode(node, curriedLet.getE2().substitute(curriedLet.getIdentifiers()[0], value0));
      }
      else {
        // add a proof node for e2 (Let/LetRec)
        Let let = (Let)e;
        context.addProofNode(node, let.getE2().substitute(let.getId(), value0));
      }
    }
    else if (node.getChildCount() == 2) {
      // forward the result of the second child node
      context.setProofNodeResult(node, node.getChildAt(1).getResult());
    }
  }
  
  
  
  //
  // The (OP) rule
  //
  
  /**
   * Applies the <b>(OP)</b> rule to the <code>node</code> using the <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(OP)</b> rule to.
   * 
   * @throws BinaryOperatorException if the <b>(OP)</b> 
   */
  public void applyOp(BigStepProofContext context, BigStepProofNode node) throws BinaryOperatorException {
    // depends on whether we have an Application or InfixOperation
    BinaryOperator op;
    Expression e1;
    Expression e2;
    
    // check if Application or InfixOperation
    Expression e = node.getExpression();
    if (e instanceof Application) {
      // Application: (op e1) e2
      Application a1 = (Application)e;
      Application a2 = (Application)a1.getE1();
      op = (BinaryOperator)a2.getE1();
      e1 = a2.getE2();
      e2 = a1.getE2();
    }
    else {
      // otherwise must be an InfixOperation
      InfixOperation infixOperation = (InfixOperation)e;
      op = infixOperation.getOp();
      e1 = infixOperation.getE1();
      e2 = infixOperation.getE2();
    }
    
    // perform the application
    context.setProofNodeResult(node, op.applyTo(e1, e2));
  }
}
