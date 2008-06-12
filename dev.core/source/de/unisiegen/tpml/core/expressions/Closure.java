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
    System.err.println ("Constructor");
    System.err.println (env);
    this.exp = exp;
    this.env = env;
  }
  
  public Expression getExpression()
  {
    return exp;
  }
  
  public ClosureEnvironment getEnvironment()
  {
    System.err.println ("getEnvironment: " + env);
    return env;
  }
  
  private Expression exp;
  private ClosureEnvironment env;
}