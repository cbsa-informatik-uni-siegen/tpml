package de.unisiegen.tpml.core.expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Instances of this class represents curried let expressions, which are
 * syntactic sugar for {@link de.unisiegen.tpml.core.expressions.Let} and
 * {@link de.unisiegen.tpml.core.expressions.Lambda}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
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
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   * @see #getTypes(int)
   */
  protected MonoType[] types;
  
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
   * @param types the types for the <code>identifiers</code>, see {@link #getTypes()} for an extensive
   *              description of the meaning of <code>types</code>.
   * @param e1 the function body.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> array contains less than two
   *                                  identifiers, or the arity of <code>identifiers</code> and
   *                                  <code>types</code> doesn't match.
   * @throws NullPointerException if <code>identifiers</code>, <code>types</code>, <code>e1</code> or
   *                              <code>e2</code> is <code>null</code>.
   */
  public CurriedLet(String[] identifiers, MonoType[] types, Expression e1, Expression e2) {
    if (e1 == null) {
      throw new NullPointerException("e1 is null");
    }
    if (e2 == null) {
      throw new NullPointerException("e2 is null");
    }
    if (types == null) {
      throw new NullPointerException("types is null");
    }
    if (identifiers == null) {
      throw new NullPointerException("identifiers is null");
    }
    if (identifiers.length < 2) {
      throw new IllegalArgumentException("identifiers must contain atleast two items");
    }
    if (identifiers.length != types.length) {
      throw new IllegalArgumentException("the arity of identifiers and types must match");
    }
    this.identifiers = identifiers;
    this.types = types;
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
   * Returns the types for the <code>identifiers</code>.
   * 
   * <code>let id (id1:tau1)...(idn:taun): tau = e1 in e2</code>
   * 
   * is translated to
   * 
   * <code>let id = lambda id1:tau1...lambda idn:taun.e1 in e2</code>
   *
   * which means <code>types[0]</code> is used for <code>identifiers[0]</code>
   * and so on (where <code>types[0]</code> corresponds to <code>tau</code> in
   * the example above, and <code>identifiers[0]</code> to <code>id</code>).
   * 
   * Any of the types may be null, in which case the type will be inferred
   * in the type checker, while the <code>types</code> itself may not be
   * <code>null</code>.
   * 
   * For recursion (see {@link CurriedLetRec}) the <code>tau</code> is used
   * to build the type of the recursive identifier.
   * 
   * @return the types.
   * 
   * @see #getTypes(int)
   */
  public MonoType[] getTypes() {
    return this.types;
  }
  
  /**
   * Returns the <code>n</code>th type.
   * 
   * @param n the index of the type.
   * 
   * @return the <code>n</code>th type.
   * 
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of bounds.
   * 
   * @see #getTypes()
   */
  public MonoType getTypes(int n) {
    return this.types[n];
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
    MonoType[] types = new MonoType[this.types.length];
    for (int n = 0; n < types.length; ++n) {
      types[n] = (this.types[n] != null) ? this.types[n].substitute(substitution) : null;
    }
    return new CurriedLet(this.identifiers, types, this.e1.substitute(substitution), this.e2.substitute(substitution));
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
        while (freeE.contains(identifiers[n])) {
          identifiers[n] = identifiers[n] + "'";
        }
        
        // perform the bound renaming
        e1 = e1.substitute(this.identifiers[n], new Identifier(identifiers[n]));
      }
      
      // substitute in e1 if
      e1 = e1.substitute(id, e);
    }
    
    // substitute e2 if id is not bound in e2
    if (!this.identifiers[0].equals(id)) {
      e2 = e2.substitute(id, e);
    }
    
    // generate the new expression
    return new CurriedLet(identifiers, this.types, e1, e2);
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
    builder.addText(" " + this.identifiers[0]);
    for (int n = 1; n < this.identifiers.length; ++n) {
      builder.addText(" ");
      if (this.types[n] != null) {
        builder.addText("(");
      }
      builder.addText(this.identifiers[n]);
      if (this.types[n] != null) {
        builder.addText(": ");
        builder.addBuilder(this.types[n].toPrettyStringBuilder(factory), PRIO_LET_TAU);
        builder.addText(")");
      }
    }
    if (this.types[0] != null) {
      builder.addText(": ");
      builder.addBuilder(this.types[0].toPrettyStringBuilder(factory), PRIO_LET_TAU);
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
    if (obj instanceof CurriedLet && getClass().equals(obj.getClass())) {
      CurriedLet other = (CurriedLet)obj;
      return (this.identifiers.equals(other.identifiers)
           && ((this.types == null) ? (other.types == null) : Arrays.equals(this.types, other.types))
           && this.e1.equals(other.e1) && this.e2.equals(other.e2));
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
         + ((this.types != null) ? this.types.hashCode() : 0);
  }
}
