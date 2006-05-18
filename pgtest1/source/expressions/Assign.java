package expressions;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Assign extends Operator {
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Operator#canApplyTo(java.lang.Class, java.lang.Class)
   */
  @Override
  public boolean canApplyTo(Class c1, Class c2) {
    return (c1 == Location.class);
  }

  /**
   * {@inheritDoc}
   * 
   * @see expressions.Operator#applyTo(expressions.Value, expressions.Value)
   */
  @Override
  public Expression applyTo(Value v1, Value v2) {
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see expressions.Operator#getPrettyPriority()
   */
  @Override
  public int getPrettyPriority() {
    return 1;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#toString()
   */
  @Override
  public final String toString() {
    return ":=";
  }
}
