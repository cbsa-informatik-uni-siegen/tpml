package expressions;


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
