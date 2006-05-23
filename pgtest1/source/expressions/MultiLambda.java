package expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import util.StringUtilities;

/**
 * Represents a multi lambda abstraction, which takes
 * a single tuple argument as parameter.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class MultiLambda extends Value {
  //
  // Attributes
  //
  
  /**
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   */
  private String[] identifiers;
  
  /**
   * The function body expression.
   * 
   * @see #getE()
   */
  private Expression e;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>MultiLambda</code> expression with
   * the specified <code>identifiers</code> and the function
   * body <code>e</code>.
   * 
   * @param identifiers non-empty set of identifiers.
   * @param e the function body.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code>
   *                                  list is empty.
   */
  public MultiLambda(String[] identifiers, Expression e) {
    // validate the identifiers
    if (identifiers.length == 0) {
      throw new IllegalArgumentException("identifiers is empty");
    }
    
    // initialize the attributes
    this.identifiers = identifiers;
    this.e = e;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#substitute(java.lang.String, expressions.Expression)
   */
  @Override
  public Expression substitute(String id, Expression e) {
    // check if we can substitute below the lambda abstraction
    if (Arrays.asList(this.identifiers).contains(id)) {
      return this;
    }
    else {
      // bound rename for substituting e in this.e
      Expression newE = this.e;
      Set<String> freeE = e.free();
      String[] newIdentifiers = this.identifiers.clone();
      for (int n = 0; n < newIdentifiers.length; ++n) {
        // generate a new unique identifier
        while (freeE.contains(newIdentifiers[n]))
          newIdentifiers[n] = newIdentifiers[n] + "'";
        
        // perform the bound renaming
        newE = newE.substitute(this.identifiers[n], new Identifier(newIdentifiers[n]));
      }
      
      // perform the substitution
      newE = newE.substitute(id, e);
      
      // allocate the new multi lambda
      return new MultiLambda(newIdentifiers, newE);
    }
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(this.e.free());
    free.removeAll(Arrays.asList(this.identifiers));
    return free;
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
   * {@inheritDoc}
   *
   * @see expressions.Expression#containsSyntacticSugar()
   */
  @Override
  public boolean containsSyntacticSugar() {
    return true;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    // determine a new unique identifier
    // to be used for the tuple parameter
    String id = "id";
    while (this.e.free().contains(id) || Arrays.asList(this.identifiers).contains(id)) {
      id = id + "'";
    }
    
    // generate the required let's
    Expression e = this.e;
    for (int n = this.identifiers.length - 1; n >= 0; --n) {
      e = new Let(this.identifiers[n],
                  new Application(new Projection(this.identifiers.length, n + 1),
                                  new Identifier(id)),
                  e);
    }
    
    // and return the new lambda expression
    return new Lambda(id, e);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("\u03bb");
    builder.appendText("(" + StringUtilities.join(", ", this.identifiers) + ").");
    builder.appendBuilder(this.e.toPrettyStringBuilder(), 0);
    return builder;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the arity of the tuple parameter.
   * 
   * @return the arity of the tuple parameter.
   */
  public int getArity() {
    return this.identifiers.length;
  }
  
  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   */
  public String[] getIdentifiers() {
    return this.identifiers;
  }
  
  /**
   * Returns the function body expression.
   * 
   * @return the function body expression.
   */
  public Expression getE() {
    return this.e;
  }
}
