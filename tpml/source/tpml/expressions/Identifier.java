package tpml.expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an identifier in the expression hierarchy.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Identifier extends Expression {
  /**
   * Returns a set that contains exacty one element, which is
   * the name of the identifier.
   * 
   * @return a set which contains the name of the identifier.
   * 
   * @see #getName()
   * @see tpml.expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> free = new TreeSet<String>();
    free.add(getName());
    return free;
  }

  /**
   * Returns <code>e</code> if <code>id</code> equals the
   * name of the identifier. Else the identifier itself
   * is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the
   *         name of the identifier, else the identifier
   *         itself.
   *
   * @see #getName()
   * @see tpml.expressions.Expression#substitute(java.lang.String, tpml.expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (id.equals(getName()))
      return e;
    else
      return this;
  }

  /**
   * Returns the name of the identifier.
   * @return the name of the identifier.
   */
  public String getName() {
    return this.name;
  }
  
  private String name;
}
