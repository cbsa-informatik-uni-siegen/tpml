package expressions;

import java.util.Set;

/**
 * The deref operator, written as <code>!</code>.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Deref extends Value {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Deref</code> instance.
   */
  public Deref() {
    // nothing to do
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the set of free identifiers, which
   * is empty for the deref operator.
   * 
   * @return the empty set.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    return EMPTY_SET;
  }

  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 6);
    builder.appendKeyword("!");
    return builder;
  }
}
