package expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import common.prettyprinter.PrettyStringBuilder;

import expressions.annotation.SyntacticSugar;

import types.MonoType;
import util.StringUtilities;

/**
 * Represents a multi lambda abstraction, which takes
 * a single tuple argument as parameter.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
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
   * The type of the <code>identifiers</code> or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
  /**
   * The function body expression.
   * 
   * @see #getE()
   */
  private Expression e;
  
  
  
  //
  // Constructors
  //
  
  /**
   * Convenience wrapper for {@link #MultiLambda(String[], MonoType, Expression)}
   * passing <code>null</code> for <code>tau</code>.
   * 
   * @param identifiers non-empty set of identifiers.
   * @param e the function body.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code>
   *                                  list is empty.
   * @throws NullPointerException if <code>e</code> is <code>null</code>.
   */
  public MultiLambda(String[] identifiers, Expression e) {
    this(identifiers, null, e);
  }
  
  /**
   * Allocates a new <code>MultiLambda</code> expression with
   * the specified <code>identifiers</code> and the function
   * body <code>e</code>.
   * 
   * @param identifiers non-empty set of identifiers.
   * @param tau the type of the identifiers or <code>null</code>.
   * @param e the function body.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code>
   *                                  list is empty.
   * @throws NullPointerException if <code>e</code> is <code>null</code>.
   */
  public MultiLambda(String[] identifiers, MonoType tau, Expression e) {
    if (identifiers.length == 0) {
      throw new IllegalArgumentException("identifiers is empty");
    }
    if (e == null) {
      throw new NullPointerException("e is null");
    }
    
    this.identifiers = identifiers;
    this.tau = tau;
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
      return new MultiLambda(newIdentifiers, this.tau, newE);
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
    return new Lambda(id, this.tau, e);
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
    builder.appendText("(" + StringUtilities.join(", ", this.identifiers) + ")");
    if (this.tau != null) {
      builder.appendText(":");
      builder.appendText(this.tau.toString());
    }
    builder.appendText(".");
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
   * Returns the type of the <code>identifiers</code> or
   * <code>null</code> if no type was specified.
   * 
   * @return the type of the identifiers;
   */
  public MonoType getTau() {
    return this.tau;
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
