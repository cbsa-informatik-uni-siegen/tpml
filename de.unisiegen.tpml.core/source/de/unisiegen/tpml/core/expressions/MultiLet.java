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
 * Instances of this class represent multi let expressions in the expression hierarchy, which assign
 * identifiers values from tuple items.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.expressions.Let
 * @see de.unisiegen.tpml.core.expressions.MultiLambda
 */
public final class MultiLet extends Expression {
  //
  // Attributes
  //
  
  /**
   * The bound identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private String[] identifiers;
  
  /**
   * The type of the <code>identifiers</code> tuple.
   * 
   * @see #getTau()
   */
  private MonoType tau;
  
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
   * @param tau the type of the <code>identifiers</code> tuple (that is the type of <code>e1</code>)
   *            or <code>null</code>.
   * @param e1 the first expression.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> list is empty.
   * @throws NullPointerException if <code>identifiers</code>, <code>e1</code> or <code>e2</code>
   *                              is <code>null</code>.
   */
  public MultiLet(String[] identifiers, MonoType tau, Expression e1, Expression e2) {
    if (identifiers == null) {
      throw new NullPointerException("identifiers is null");
    }
    if (identifiers.length == 0) {
      throw new IllegalArgumentException("identifiers is empty");
    }
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    this.identifiers = identifiers;
    this.tau = tau;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifiers for the tuple items.
   * 
   * @return the identifiers for the tuple items.
   * 
   * @see #getIdentifiers(int)
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
   * Returns the tuple type for <code>e1</code> or <code>null</code>.
   * 
   * @return the type for <code>e1</code> or <code>null</code>.
   */
  public MonoType getTau() {
    return this.tau;
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
    free.addAll(this.e2.free());
    free.removeAll(Arrays.asList(this.identifiers));
    free.addAll(this.e1.free());
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
    return new MultiLet(this.identifiers, tau, this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
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
    return new MultiLet(identifiers, this.tau, e1, e2);
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
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LET);
    builder.addKeyword("let");
    builder.addText(" (" + StringUtilities.join(", ", this.identifiers) + ")");
    if (this.tau != null) {
      builder.addText(":");
      builder.addBuilder(this.tau.toPrettyStringBuilder(factory), PRIO_CONSTANT);
    }
    builder.addText(" = ");
    builder.addBuilder(this.e1.toPrettyStringBuilder(factory), PRIO_LET_E1);
    builder.addBreak();
    builder.addText(" ");
    builder.addKeyword("in");
    builder.addText(" ");
    builder.addBuilder(this.e2.toPrettyStringBuilder(factory), PRIO_LET_E2);
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
    if (obj instanceof MultiLet) {
      MultiLet other = (MultiLet)obj;
      return (this.identifiers.equals(other.identifiers)
           && this.e1.equals(other.e1) && this.e2.equals(other.e2)
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
    return this.identifiers.hashCode() + this.e1.hashCode() + this.e2.hashCode()
         + ((this.tau != null) ? this.tau.hashCode() : 0);
  }
}
