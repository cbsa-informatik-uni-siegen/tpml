package de.unisiegen.tpml.core.expressions;
import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.expressions.Expression;

/**
 * TODO
 *
 */
public class Closure
{
  public Closure(Expression exp, ClosureEnvironment env)
  {
    this.exp = exp;
    this.env = env;
  }
  
  public Expression getExpression()
  {
    return exp;
  }
  
  public ClosureEnvironment getEnvironment()
  {
    return env;
  }
  
  private Expression exp;
  private ClosureEnvironment env;
}