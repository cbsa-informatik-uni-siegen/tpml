package expressions;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a lambda abstraction.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Abstraction extends Value {
  /**
   * Generates a new abstraction.
   * 
   * @param id the name of the parameter.
   * @param e the expression.
   */
  public Abstraction(String id, Expression e) {
    this.id = id;
    this.e = e;
  }

  /**
   * Performs the substitution for <b>(LAMBDA)</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
   */
  @Override
  public Expression substitute(String id, Expression e) {
    if (this.id.equals(id)) {
      return this;
    }
    else {
      // determine the free identifiers for e
      Set<String> free = e.free();
      
      // generate a new unique identifier
      String newId = this.id;
      while (free.contains(newId))
        newId = newId + "'";

      // perform the bound renaming
      Expression newE = this.e.substitute(this.id, new Identifier(newId));
      
      // perform the substitution
      return new Abstraction(newId, newE.substitute(id, e));
    }
  }

  /**
   * Returns the free identifiers minus the bound identifier.
   * @return the free identifiers minus the bound identifier.
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> set = new TreeSet<String>();
    set.addAll(this.e.free());
    set.remove(this.id);
    return set;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsReferences()
   */
  @Override
  public boolean containsReferences() {
    return this.e.containsReferences();
  }
  
  /**
   * @return Returns the id.
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * @return Returns the e.
   */
  public Expression getE() {
    return this.e;
  }
  
  /**
   * Returns the pretty string builder for abstractions.
   * @return the pretty string builder for abstractions.
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("\u03bb");
    builder.appendText(this.id + ".");
    builder.appendBuilder(this.e.toPrettyStringBuilder(), 0);
    return builder;
  }

  // the internal structure
  private String id;
  private Expression e;
}
