package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.IsEmpty;
import de.unisiegen.tpml.core.typechecker.DefaultTypeEnvironment;
import de.unisiegen.tpml.core.typechecker.TypeEquation;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;

public final class TypeEquationList implements TypeFormula{
	  //
	  // Constants
	  //
	  
	  /**
	   * The empty equation list.
	   * 
	   * @see #TypeEquationList()
	   */
	  public static final TypeEquationList EMPTY_LIST = new TypeEquationList();

	  
	  
	  //
	  // Attributes
	  //

	  /**
	   * The first equation in the list.
	   */
	  private TypeEquation first;
	  
	  /**
	   * The remaining equations or <code>null</code>.
	   */
	  private TypeEquationList remaining;
	  
	  
	  
	  //
	  // Constructors (private)
	  //
	  
	  /**
	   * Allocates a new empty equation list.
	   * 
	   * @see #EMPTY_LIST
	   */
	  private TypeEquationList() {
	    super();
	  }
	  
	  /**
	   * Allocates a new equation list, which basicly extends <code>remaining</code> with a new {@link TypeEquation}
	   * <code>first</code>.
	   * 
	   * @param first the new {@link TypeEquation}.
	   * @param remaining an existing {@link TypeEquationList}
	   * 
	   * @throws NullPointerException if <code>first</code> or <code>remaining</code> is <code>null</code>.
	   */
	  private TypeEquationList(TypeEquation first, TypeEquationList remaining) {
	    if (first == null) {
	      throw new NullPointerException("first is null");
	    }
	    if (remaining == null) {
	      throw new NullPointerException("remaining is null");
	    }
	    this.first = first;
	    this.remaining = remaining;
	  }

	  
	  
	  //
	  // Primitives
	  //
	  
	  /**
	   * Allocates a new {@link TypeEquationList}, which extends this equation list with a new {@link TypeEquation}
	   * for <code>left</code> and <code>right</code>.
	   * 
	   * @param left the left side of the new equation.
	   * @param right the right side of the new equation.
	   * 
	   * @return the extended {@link TypeEquationList}.
	   * 
	   * @throws NullPointerException if <code>left</code> or <code>right</code> is <code>null</code>.
	   */
	  public TypeEquationList extend(MonoType left, MonoType right) {
	    return new TypeEquationList(new TypeEquation(left, right), this);
	  }

	  /**
	   * Applies the {@link Substitution} <code>s</code> to all
	   * equations contained within this list.
	   * 
	   * @param s the {@link Substitution} to apply.
	   * 
	   * @return the resulting list of equations.
	   * 
	   * @see Equation#substitute(Substitution)
	   */
	  TypeEquationList substitute(TypeSubstitution s) {
	    // nothing to substitute on the empty list
	    if (this == EMPTY_LIST) {
	      return this;
	    }
	    
	    // apply the substitution to the first and the remaining equations
	    return new TypeEquationList(this.first.substitute(s), this.remaining.substitute(s));
	  }
	  
 
	  
	  //
	  // Base methods
	  //
	  
	  /**
	   * Returns the string representation of the equations contained in this list. This method is mainly useful
	   * for debugging purposes.
	   * 
	   * @return the string representation.
	   * 
	   * @see TypeEquation#toString()
	   * @see java.lang.Object#toString()
	   */
	  @Override
	  public String toString() {
	    StringBuilder builder = new StringBuilder(128);
	    builder.append('{');
	    for (TypeEquationList list = this; list != EMPTY_LIST; list = list.remaining) {
	      if (list != this)
	        builder.append(", ");
	      builder.append(list.first);
	    }
	    builder.append('}');
	    return builder.toString();
	  }

	public DefaultTypeEnvironment getEnvironment() {
		return new DefaultTypeEnvironment();
	}

	public Expression getExpression() {
		return new IsEmpty();
	}

	public MonoType getType() {
		return null;
	}


	}

