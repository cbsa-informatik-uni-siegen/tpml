package smallstep;

import java.util.Set;

/**
 * Abstract class to represent a constant expression
 * (only values can be constants).
 *
 * @author bmeurer
 * @version $Id$
 */
public abstract class Constant extends Value {
  /**
   * Returns the empty set, as constants
   * don't have any free identifiers.
   * @return the empty set.
   * @see smallstep.Expression#free()
   */
  @Override
  public Set<String> free() {
    return Expression.EMPTY_SET;
  }
  
  /**
   * Returns <code>false</code> as constants cannot contain
   * syntactic sugar.
   * 
   * @return <code>false</code>
   * 
   * @see smallstep.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return false;
  }
  
  /**
   * Returns the expression itself as constants cannot
   * contain syntactic sugar.
   * 
   * @return the expression itself.
   * 
   * @see smallstep.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    return this;
  }
}
