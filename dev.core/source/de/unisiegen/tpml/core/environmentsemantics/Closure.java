package de.unisiegen.tpml.core.environmentsemantics;
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
  
  private Expression exp;
  private ClosureEnvironment env;
}