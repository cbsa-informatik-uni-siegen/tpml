/**
 * 
 */
package l1;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import l1.analysis.*;
import l1.node.*;
import l1.parser.ParserException;
import smallstep.*;

/**
 * Used to translate an abstract syntax tree as produced
 * by the SableCC generated parser into an expression
 * required for the evaluation in the small step interpreter.
 *
 * @author bmeurer
 * @version $Id$
 */
public class Translator extends DepthFirstAdapter {
  /**
   * Allocates a new translator.
   */
  public Translator() {
  }
  
  /**
   * Returns the parsed small step expression.
   * @return the parsed small step expression.
   */
  public final Expression getExpression() {
    assert (this.expressions.size() == 1);
    return this.expressions.get(0);
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAFalseExpression(l1.node.AFalseExpression)
   */
  @Override
  public void outAFalseExpression(AFalseExpression node) {
    this.expressions.push(BooleanConstant.FALSE);
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outATrueExpression(l1.node.ATrueExpression)
   */
  @Override
  public void outATrueExpression(ATrueExpression node) {
    this.expressions.push(BooleanConstant.TRUE);
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAAbstractionExpression(l1.node.AAbstractionExpression)
   */
  @Override
  public void outAAbstractionExpression(AAbstractionExpression node) {
    Expression e = this.expressions.pop();
    this.expressions.push(new Abstraction(node.getId().getText(), e));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAApplicationExpression(l1.node.AApplicationExpression)
   */
  @Override
  public void outAApplicationExpression(AApplicationExpression node) {
    Expression e2 = this.expressions.pop();
    Expression e1 = this.expressions.pop();
    this.expressions.push(new Application(e1, e2));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAConditionExpression(l1.node.AConditionExpression)
   */
  @Override
  public void outAConditionExpression(AConditionExpression node) {
    Expression e2 = this.expressions.pop();
    Expression e1 = this.expressions.pop();
    Expression e0 = this.expressions.pop();
    this.expressions.push(new Condition(e0, e1, e2));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outADivideExpression(l1.node.ADivideExpression)
   */
  @Override
  public void outADivideExpression(ADivideExpression node) {
    this.expressions.push(ArithmeticOperator.DIV);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAEqualExpression(l1.node.AEqualExpression)
   */
  @Override
  public void outAEqualExpression(AEqualExpression node) {
    this.expressions.push(RelationalOperator.EQUALS);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAGreaterEqualExpression(l1.node.AGreaterEqualExpression)
   */
  @Override
  public void outAGreaterEqualExpression(AGreaterEqualExpression node) {
    this.expressions.push(RelationalOperator.GREATER_EQUAL);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAGreaterThanExpression(l1.node.AGreaterThanExpression)
   */
  @Override
  public void outAGreaterThanExpression(AGreaterThanExpression node) {
    this.expressions.push(RelationalOperator.GREATER_THAN);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAIdentifierExpression(l1.node.AIdentifierExpression)
   */
  @Override
  public void outAIdentifierExpression(AIdentifierExpression node) {
    this.expressions.push(new Identifier(node.getIdentifier().getText()));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outALetExpression(l1.node.ALetExpression)
   */
  @Override
  public void outALetExpression(ALetExpression node) {
    Expression e1 = this.expressions.pop();
    Expression e0 = this.expressions.pop();
    this.expressions.push(new Let(node.getId().getText(), e0, e1));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outALetrecExpression(l1.node.ALetrecExpression)
   */
  @Override
  public void outALetrecExpression(ALetrecExpression node) {
    Expression e1 = this.expressions.pop();
    Expression e0 = this.expressions.pop();
    this.expressions.push(new LetRec(node.getId().getText(), e0, e1));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outARecursionExpression(l1.node.ARecursionExpression)
   */
  @Override
  public void outARecursionExpression(ARecursionExpression node) {
    Expression e = this.expressions.pop();
    this.expressions.push(new Recursion(node.getId().getText(), e));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outALowerEqualExpression(l1.node.ALowerEqualExpression)
   */
  @Override
  public void outALowerEqualExpression(ALowerEqualExpression node) {
    this.expressions.push(RelationalOperator.LOWER_EQUAL);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outALowerThanExpression(l1.node.ALowerThanExpression)
   */
  @Override
  public void outALowerThanExpression(ALowerThanExpression node) {
    this.expressions.push(RelationalOperator.LOWER_THAN);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAMinusExpression(l1.node.AMinusExpression)
   */
  @Override
  public void outAMinusExpression(AMinusExpression node) {
    this.expressions.push(ArithmeticOperator.MINUS);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAModuloExpression(l1.node.AModuloExpression)
   */
  @Override
  public void outAModuloExpression(AModuloExpression node) {
    this.expressions.push(ArithmeticOperator.MOD);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAMultiplyExpression(l1.node.AMultiplyExpression)
   */
  @Override
  public void outAMultiplyExpression(AMultiplyExpression node) {
    this.expressions.push(ArithmeticOperator.MULT);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outANumberExpression(l1.node.ANumberExpression)
   */
  @Override
  public void outANumberExpression(ANumberExpression node) {
    this.expressions.push(new IntegerConstant(Integer.valueOf(node.getNumber().getText())));
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAPlusExpression(l1.node.APlusExpression)
   */
  @Override
  public void outAPlusExpression(APlusExpression node) {
    this.expressions.push(ArithmeticOperator.PLUS);
  }

  /**
   * @see l1.analysis.DepthFirstAdapter#outAUnitExpression(l1.node.AUnitExpression)
   */
  @Override
  public void outAUnitExpression(AUnitExpression node) {
    this.expressions.push(UnitConstant.UNIT);
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAAndExpression(l1.node.AAndExpression)
   */
  @Override
  public void outAAndExpression(AAndExpression node) {
    Expression e1 = this.expressions.pop();
    Expression e0 = this.expressions.pop();
    this.expressions.push(new And(e0, e1));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAOrExpression(l1.node.AOrExpression)
   */
  @Override
  public void outAOrExpression(AOrExpression node) {
    Expression e1 = this.expressions.pop();
    Expression e0 = this.expressions.pop();
    this.expressions.push(new Or(e0, e1));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAInfixExpression(l1.node.AInfixExpression)
   */
  @Override
  public void outAInfixExpression(AInfixExpression node) {
    Expression e2 = this.expressions.pop();
    Operator op = (Operator)this.expressions.pop();
    Expression e1 = this.expressions.pop();
    this.expressions.push(new InfixOperation(op, e1, e2));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outATupleExpression(l1.node.ATupleExpression)
   */
  @Override
  public void outATupleExpression(ATupleExpression node) {
    Expression[] expressions = new Expression[node.getExpressions().size()];
    for (int n = expressions.length - 1; n >= 0; --n)
      expressions[n] = this.expressions.pop();
    this.expressions.push(new Tuple(expressions));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAProjectionExpression(l1.node.AProjectionExpression)
   */
  @Override
  public void outAProjectionExpression(AProjectionExpression node) {
    Pattern pattern = Pattern.compile("#(\\d+)_(\\d+)");
    Matcher matcher = pattern.matcher(node.getProjection().getText());
    if (!matcher.matches())
      throw new IllegalArgumentException("Invalid projection, shouldn't happen");
    
    // determine arity and index for the projection
    int arity = Integer.parseInt(matcher.group(1));
    int index = Integer.parseInt(matcher.group(2));

    // try to allocate a projection operator (throws an
    // exception if either arity or index is invalid)
    this.expressions.push(new Projection(arity, index));
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outAFstExpression(l1.node.AFstExpression)
   */
  @Override
  public void outAFstExpression(AFstExpression node) {
    this.expressions.push(new Fst());
  }
  
  /**
   * @see l1.analysis.DepthFirstAdapter#outASndExpression(l1.node.ASndExpression)
   */
  @Override
  public void outASndExpression(ASndExpression node) {
    this.expressions.push(new Snd());
  }
    
  private Stack<Expression> expressions = new Stack<Expression>();
}
