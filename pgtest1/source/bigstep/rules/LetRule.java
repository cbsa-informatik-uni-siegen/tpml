package bigstep.rules;

import common.ProofRuleException;
import expressions.Application;
import expressions.CurriedLet;
import expressions.CurriedLetRec;
import expressions.Expression;
import expressions.Lambda;
import expressions.Let;
import expressions.LetRec;
import expressions.MultiLet;
import expressions.Projection;
import expressions.Recursion;

import bigstep.BigStepProofContext;
import bigstep.BigStepProofNode;
import bigstep.BigStepProofRule;

/**
 * This class represents the big step rule <b>(LET)</b>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class LetRule extends BigStepProofRule {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>LetRule</code> instance.
   */
  public LetRule() {
    super(false, "LET");
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#apply(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void apply(BigStepProofContext context, BigStepProofNode node) throws ProofRuleException {
    try {
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
      else if (e instanceof MultiLet) {
        // proof the first sub expression
        MultiLet multiLet = (MultiLet)e;
        context.addProofNode(node, multiLet.getE1());
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
    catch (ClassCastException e) {
      throw new ProofRuleException(node, this, e);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see bigstep.BigStepProofRule#update(bigstep.BigStepProofContext, bigstep.BigStepProofNode)
   */
  @Override
  public void update(BigStepProofContext context, BigStepProofNode node) {
    // check if we have exactly one proven child node
    if (node.getChildCount() == 1 && node.getChildAt(0).isProven()) {
      // determine the value of the first child node
      BigStepProofNode node0 = (BigStepProofNode)node.getChildAt(0);
      Expression value0 = node0.getValue();
      
      // determine the expression for the node
      Expression e = (Expression)node.getExpression();
      
      // check the expression type
      if (e instanceof CurriedLet) {
        // add a proof node for e2 (CurriedLet/CurriedLetRec)
        CurriedLet curriedLet = (CurriedLet)e;
        context.addProofNode(node, curriedLet.getE2().substitute(curriedLet.getIdentifiers()[0], value0));
      }
      else if (e instanceof MultiLet) {
        // determine the second sub expression e2 (MultiLet)
        MultiLet multiLet = (MultiLet)e;
        Expression e2 = multiLet.getE2();
        
        // perform the required substitutions
        String[] identifiers = multiLet.getIdentifiers();
        for (int n = 0; n < identifiers.length; ++n) {
          // substitute: (#l_n value0) for id
          e2 = e2.substitute(identifiers[n], new Application(new Projection(identifiers.length, n + 1), value0));
        }
        
        // add a proof node for e2
        context.addProofNode(node, e2);
      }
      else {
        // add a proof node for e2 (Let/LetRec)
        Let let = (Let)e;
        context.addProofNode(node, let.getE2().substitute(let.getId(), value0));
      }
    }
    else if (node.getChildCount() == 2) {
      // forward the value of the second child node
      BigStepProofNode node1 = (BigStepProofNode)node.getChildAt(1);
      context.setProofNodeValue(node, node1.getValue());
    }
    else {
      super.update(context, node);
    }
  }
}
