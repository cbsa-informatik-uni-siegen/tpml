package de.unisiegen.tpml.core.bigstepclosure;

import de.unisiegen.tpml.core.expressions.Application;
import de.unisiegen.tpml.core.expressions.BinaryOperator;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.InfixOperation;


/**
 * TODO
 *
 */
public class BinaryOpArgs
{
  public BinaryOpArgs(final BigStepClosureProofNode node)
  {
    final BigStepClosureProofNode child0 = node.getChildAt ( 0 ), child1 = node
    .getChildAt ( 1 );
    
    final Expression e = node.getExpression ();
    if ( e instanceof Application )
    {
      Application app = ( Application ) child0.getResult ().getClosure ()
          .getExpression ();
      this.op2 = ( BinaryOperator ) app.getE1 ();
      this.operand1 = app.getE2 ();
      this.operand2 = child1.getResult ().getClosure ().getExpression ();
    }
    else
    {
      // otherweise we must have an infix operation, pull op2 out of node itself
      final InfixOperation inf = ( InfixOperation ) node.getExpression ();
      this.op2 = inf.getOp ();
      this.operand1 = child0.getResult ().getClosure ().getExpression ();
      this.operand2 = child1.getResult ().getClosure ().getExpression ();
    }
  }
  
  public Expression getOperand1()
  {
    return operand1;
  }
  
  public Expression getOperand2()
  {
    return operand2;
  }
  
  public BinaryOperator getOperator()
  {
    return op2;
  }
  
  private Expression operand1;
  private Expression operand2;
  private BinaryOperator op2;
}
