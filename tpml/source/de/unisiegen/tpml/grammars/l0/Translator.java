package de.unisiegen.tpml.grammars.l0;

import java.util.Stack;

import de.unisiegen.tpml.expressions.Abstraction;
import de.unisiegen.tpml.expressions.Application;
import de.unisiegen.tpml.expressions.Expression;
import de.unisiegen.tpml.expressions.Identifier;
import de.unisiegen.tpml.grammars.l0.analysis.DepthFirstAdapter;
import de.unisiegen.tpml.grammars.l0.node.AAbstractionExpression;
import de.unisiegen.tpml.grammars.l0.node.AApplicationExpression;
import de.unisiegen.tpml.grammars.l0.node.AIdentifierExpression;

/**
 * Translator class, which is used to transform the abstract
 * syntax tree returned by the parser for the l0 grammar to
 * an expression tree.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Translator extends DepthFirstAdapter {
  /**
   * Allocates a new translator instance, which can be used
   * to transform the abstract syntax tree returned by the
   * parser for the l0 grammar to an expression tree.
   */
  public Translator() {
  }
  
  /**
   * Returns the resulting expression that was constructed from the abstract syntax tree.
   * @return the resulting expression that was constructed from the abstract syntax tree.
   */
  public Expression getExpression() {
    assert (this.expressions.size() == 1);
    return this.expressions.get(0);
  }
    
  @Override
  public void outAAbstractionExpression(AAbstractionExpression node) {
    Expression e1 = this.expressions.pop();
    this.expressions.push(new Abstraction(node.getId().getText(), e1));
  }

  @Override
  public void outAApplicationExpression(AApplicationExpression node) {
    Expression e2 = this.expressions.pop();
    Expression e1 = this.expressions.pop();
    this.expressions.push(new Application(e1, e2));
  }

  @Override
  public void outAIdentifierExpression(AIdentifierExpression node) {
    this.expressions.push(new Identifier(node.getId().getText()));
  }

  private Stack<Expression> expressions = new Stack<Expression>();
}
