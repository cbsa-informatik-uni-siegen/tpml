package expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import common.prettyprinter.PrettyStringBuilder;

import expressions.annotation.SyntacticSugar;

import util.StringUtilities;

/**
 * Represents a multiple let expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
public final class MultiLet extends Expression {
  //
  // Attributes
  //
  
  /**
   * The bound identifiers.
   * 
   * @see #getIdentifiers()
   */
  private String[] identifiers;
  
  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  private Expression e1;
  
  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  private Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>MultiLet</code> expression with
   * the specified <code>identifiers</code> and the given
   * expressions <code>e1</code> and <code>e2</code>.
   * 
   * @param identifiers non-empty set of identifiers.
   * @param e1 the first expression.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code>
   *                                  list is empty.
   */
  public MultiLet(String[] identifiers, Expression e1, Expression e2) {
    // validate the identifiers
    if (identifiers.length == 0) {
      throw new IllegalArgumentException("identifiers is empty");
    }
    
    // initialize the attributes
    this.identifiers = identifiers;
    this.e1 = e1;
    this.e2 = e2;
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
    // determine the expressions and the identifiers
    String[] identifiers = this.identifiers;
    Expression e1 = this.e1;
    Expression e2 = this.e2;
    
    // substitute in e1
    e1 = e1.substitute(id, e);
    
    // check if we can substitute below e2
    if (!Arrays.asList(identifiers).contains(id)) {
      // bound rename for substituting e in e2
      identifiers = identifiers.clone();
      Set<String> freeE = e.free();
      for (int n = 0; n < identifiers.length; ++n) {
        // generate a new unique identifier
        while (freeE.contains(identifiers[n]))
          identifiers[n] = identifiers[n] + "'";
        
        // perform the bound renaming
        e2 = e2.substitute(this.identifiers[n], new Identifier(identifiers[n]));
      }
      
      // substitute in e2
      e2 = e2.substitute(id, e);
    }
    
    // generate the new expression
    return new MultiLet(identifiers, e1, e2);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(this.e2.free());
    free.removeAll(Arrays.asList(this.identifiers));
    free.addAll(this.e1.free());
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
    while (this.e1.free().contains(id) || this.e2.free().contains(id) || Arrays.asList(this.identifiers).contains(id)) {
      id = id + "'";
    }
    
    // generate the required let's
    Expression e2 = this.e2;
    for (int n = this.identifiers.length - 1; n >= 0; --n) {
      e2 = new Let(this.identifiers[n],
                   new Application(new Projection(this.identifiers.length, n + 1),
                                   new Identifier(id)),
                   e2);
    }
    
    // and return the new let expression
    return new Let(id, this.e1, e2);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#toPrettyStringBuilder()
   */
  @Override
  protected PrettyStringBuilder toPrettyStringBuilder() {
    PrettyStringBuilder builder = new PrettyStringBuilder(this, 0);
    builder.appendKeyword("let");
    builder.appendText(" (" + StringUtilities.join(", ", this.identifiers) + ") = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendText(" ");
    builder.appendKeyword("in");
    builder.appendText(" ");
    builder.appendBuilder(this.e2.toPrettyStringBuilder(), 0);
    return builder;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifiers for the tuple items.
   * 
   * @return the identifiers for the tuple items.
   */
  public String[] getIdentifiers() {
    return this.identifiers;
  }
  
  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1() {
    return this.e1;
  }
  
  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2() {
    return this.e2;
  }
}
