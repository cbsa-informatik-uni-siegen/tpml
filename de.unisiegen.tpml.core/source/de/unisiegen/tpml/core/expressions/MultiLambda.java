package de.unisiegen.tpml.core.expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.util.StringUtilities;

/**
 * Represents a multi lambda abstract, which takes a single tuple argument as parameter and splits
 * the tuple items to various identifiers.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Lambda
 * @see de.unisiegen.tpml.core.expressions.MultiLet
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public final class MultiLambda extends Value {
  //
  // Attributes
  //
  
  /**
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
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
   * Allocates a new <code>MultiLambda</code> expression with the specified <code>identifiers</code> and
   * the function body <code>e</code>.
   * 
   * @param identifiers non-empty set of identifiers.
   * @param tau the type of the identifiers or <code>null</code>.
   * @param e the function body.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> list is empty.
   * @throws NullPointerException if <code>identifiers</code> or <code>e</code> is <code>null</code>.
   */
  public MultiLambda(String[] identifiers, MonoType tau, Expression e) {
    if (identifiers == null) {
      throw new NullPointerException("identifiers is null");
    }
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
  // Accessors
  //
  
  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   * 
   * @see #getIdentifiers()
   */
  public String[] getIdentifiers() {
    return this.identifiers;
  }
  
  /**
   * Returns the <code>n</code>th identifier.
   * 
   * @param n the index of the identifier to return.
   * 
   * @return the <code>n</code>th identifier.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of bounds.
   * 
   * @see #getIdentifiers()
   */
  public String getIdentifiers(int n) {
    return this.identifiers[n];
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

  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @Override
  public TreeSet<String> free() {
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(this.e.free());
    free.removeAll(Arrays.asList(this.identifiers));
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public Expression substitute(TypeSubstitution substitution) {
    MonoType tau = (this.tau != null) ? this.tau.substitute(substitution) : null;
    return new MultiLambda(this.identifiers, tau, this.e.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
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
        while (newE.free().contains(newIdentifiers[n]) || freeE.contains(newIdentifiers[n]) || newIdentifiers[n].equals(id)) {
          newIdentifiers[n] = newIdentifiers[n] + "'";
        }
        
        // perform the bound renaming
        newE = newE.substitute(this.identifiers[n], new Identifier(newIdentifiers[n]));
      }
      
      // perform the substitution
      newE = newE.substitute(id, e);
      
      // allocate the new multi lambda
      return new MultiLambda(newIdentifiers, this.tau, newE);
    }
  }

  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LAMBDA);
    builder.addKeyword("\u03bb");
    builder.addText("(" + StringUtilities.join(", ", this.identifiers) + ")");
    if (this.tau != null) {
      builder.addText(":");
      builder.addBuilder(this.tau.toPrettyStringBuilder(factory), PRIO_LAMBDA_TAU);
    }
    builder.addText(".");
    builder.addBuilder(this.e.toPrettyStringBuilder(factory), PRIO_LAMBDA_E);
    return builder;
  }

  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MultiLambda) {
      MultiLambda other = (MultiLambda)obj;
      return (Arrays.equals(this.identifiers, other.identifiers) && this.e.equals(other.e)
          && ((this.tau == null) ? (other.tau == null) : (this.tau.equals(other.tau))));
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @Override
  public int hashCode() {
    return this.identifiers.hashCode() + ((this.tau != null) ? this.tau.hashCode() : 0) + this.e.hashCode(); 
  }
}
