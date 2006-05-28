package expressions;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import expressions.annotation.SyntacticSugar;

import util.StringUtilities;

/**
 * Represents a curried <code>let</code> expression.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
@SyntacticSugar
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
   * @param identifiers an array with atleast two identifiers,
   *                    where the first identifier is the name
   *                    to use for the function and the remaining
   *                    identifiers specify the parameters for
   *                    the function.
   * @param e1 the function body.
   * @param e2 the second expression.
   * 
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *                                  contains less than two identifiers.
   */
  public CurriedLet(String[] identifiers, Expression e1, Expression e2) {
    // validate the identifiers
    if (identifiers == null || identifiers.length < 2) {
      throw new IllegalArgumentException("identifiers must contain atleast two items");
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
   * Performs the substitution for <b>CurriedLet</b> expressions.
   * 
   * @param id the identifier for the substitution.
   * @param e the expression to substitute.
   * 
   * @return the new expression.
   */
  @Override
  public Expression substitute(String id, Expression e) {
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

  /**
   * Returns the free identifiers of
   * the subexpressions.
   * 
   * @return the free identifiers.
   * 
   * @see expressions.Expression#free()
   */
  @Override
  public Set<String> free() {
    Set<String> freeE2 = new TreeSet<String>();
    freeE2.addAll(this.e2.free());
    freeE2.remove(this.identifiers[0]);
    
    Set<String> freeE1 = new TreeSet<String>();
    freeE1.addAll(this.e1.free());
    for (int n = 1; n < this.identifiers.length; ++n)
      freeE1.remove(this.identifiers[n]);

    Set<String> free = new TreeSet<String>();
    free.addAll(freeE1);
    free.addAll(freeE2);
    return free;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see expressions.Expression#translateSyntacticSugar()
   */
  @Override
  public Expression translateSyntacticSugar() {
    // translate to: let id1 = lambda id2...lambda idn.e1 in e2
    Expression e1 = this.e1;
    for (int n = this.identifiers.length - 1; n > 0; --n) {
      e1 = new Lambda(this.identifiers[n], e1);
    }
    return new Let(this.identifiers[0], e1, this.e2);
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
    builder.appendText(" " + StringUtilities.join(" ", this.identifiers) + " = ");
    builder.appendBuilder(this.e1.toPrettyStringBuilder(), 0);
    builder.appendBreak();
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
   * Returns the identifiers, where the first identifier is
   * the name of the function and the remaining identifiers
   * name the parameters of the functions, in a curried
   * fashion.
   * 
   * @return the identifiers.
   */
  public String[] getIdentifiers() {
    return this.identifiers;
  }
  
  /**
   * Returns the function body.
   * 
   * @return the function body.
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
