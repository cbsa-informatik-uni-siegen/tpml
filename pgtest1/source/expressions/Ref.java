package expressions;

import java.util.Set;

/**
 * The <code>ref</code> operator.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Ref extends Value {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Ref</code> instance.
   */
  public Ref() {
    // nothing to do
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return true;
  }

  /**
   * Returns the set of free identifiers, which is
   * empty for the <code>ref</code> operator.
   * 
   * @return the set of free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  /**
   * Returns the pretty string builder for the
   * <code>ref</code> operator.
   * 
   * @return the pretty string builder.
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendKeyword("ref");
    return builder;
  }
}
