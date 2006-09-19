package de.unisiegen.tpml.core.expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.util.StringUtilities;

/**
 * Instances of this class represents curried let expressions, which are
 * syntactic sugar for {@link de.unisiegen.tpml.core.expressions.Let} and
 * {@link de.unisiegen.tpml.core.expressions.Lambda}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.expressions.Let
 * @see de.unisiegen.tpml.core.expressions.Lambda
 */
public class CurriedLet extends Expression {
  //
  // Attributes
  //
  
  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected String[] identifiers;

  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  protected Expression e1;
  
  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  protected Expression e2;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>CurriedLet</code> instance.
   * 
   * @param identifiers an array with atleast two identifiers, where the first identifier is the name
   *                    to use for the function and the remaining identifiers specify the parameters for
   *                    the function.
   * @param e1 the function body.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> array contains less than two identifiers.
   * @throws NullPointerException if <code>identifiers</code>, <code>e1</code> or <code>e2</code>
   *                              is <code>null</code>.
   */
  public CurriedLet(String[] identifiers, Expression e1, Expression e2) {
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    if (identifiers == null) {
      throw new NullPointerException("identifiers is null");
    }
    if (identifiers.length < 2) {
      throw new IllegalArgumentException("identifiers must contain atleast two items");
    }
    this.identifiers = identifiers;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * Returns the identifiers, where the first identifier is the name of the function and the remaining
   * identifiers name the parameters of the functions, in a curried fashion.
   * 
   * @return the identifiers.
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
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is invalid.
   */
  public String getIdentifiers(int n) {
    return this.identifiers[n];
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
    TreeSet<String> freeE2 = new TreeSet<String>();
    freeE2.addAll(this.e2.free());
    freeE2.remove(this.identifiers[0]);
    
    TreeSet<String> freeE1 = new TreeSet<String>();
    freeE1.addAll(this.e1.free());
    for (int n = 1; n < this.identifiers.length; ++n)
      freeE1.remove(this.identifiers[n]);

    TreeSet<String> free = new TreeSet<String>();
    free.addAll(freeE1);
    free.addAll(freeE2);
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @Override
  public CurriedLet substitute(TypeSubstitution substitution) {
    return new CurriedLet(this.identifiers, this.e1.substitute(substitution), this.e2.substitute(substitution));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String, de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public CurriedLet substitute(String id, Expression e) {
    // determine the expressions and the identifiers
    String[] identifiers = this.identifiers;
    Expression e1 = this.e1;
    Expression e2 = this.e2;
    
    // check if we can substitute below e1
    if (!Arrays.asList(identifiers).subList(1, identifiers.length).contains(id)) {
      // bound rename for substituting e in e1
      identifiers = identifiers.clone();
      Set<String> freeE = e.free();
      for (int n = 1; n < identifiers.length; ++n) {
        // generate a new unique identifier
        while (freeE.contains(identifiers[n]))
          identifiers[n] = identifiers[n] + "'";
        
        // perform the bound renaming
        e1 = e1.substitute(this.identifiers[n], new Identifier(identifiers[n]));
      }
      
      // substitute in e1 if
      e1 = e1.substitute(id, e);
    }
    
    // substitute e2 if id is not bound in e2
    if (!this.identifiers[0].equals(id))
      e2 = e2.substitute(id, e);
    
    // generate the new expression
    return new CurriedLet(identifiers, e1, e2);
  }
  
  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public @Override PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory) {
    PrettyStringBuilder builder = factory.newBuilder(this, PRIO_LET);
    builder.addKeyword("let");
    builder.addText(" " + StringUtilities.join(" ", this.identifiers) + " = ");
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
    if (obj instanceof CurriedLet) {
      CurriedLet other = (CurriedLet)obj;
      return (this.identifiers.equals(other.identifiers) && this.e1.equals(other.e1) && this.e2.equals(other.e2));
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
    return this.identifiers.hashCode() + this.e1.hashCode() + this.e2.hashCode();
  }
}
