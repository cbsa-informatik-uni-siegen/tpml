package de.unisiegen.tpml.core.expressions;

import java.util.Collections;
import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * Base class for all classes in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Id:Expression.java 66 2006-01-19 17:07:56Z benny $
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable
 */
public abstract class Expression implements PrettyPrintable, PrettyPrintPriorities {
  //
  // Constants
  //
  
  /**
   * Static empty set of strings, which is used to reduce the amount of newly allocated empty sets for the
   * <code>free</code> method implementations.
   */
  protected static final Set<String> EMPTY_SET = Collections.unmodifiableSet(Collections.<String>emptySet());
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the free (unbound) identifiers within the expression,
   * e.g. the name of the identifier for an identifier expression
   * or the free identifiers for its sub expressions in applications,
   * abstractions and recursions.
   * 
   * All expression classes in the expression hierarchy must override
   * this method in one or the other way.
   * 
   * The implementations of this method may either return the constant
   * <code>EMPTY_SET</code> or a dynamically allocated <code>Set</code>.
   * All implementors must ensure to not modify the set returned from
   * the {@link #free()} method.
   * 
   * @return the set of free (unbound) identifiers within the
   *         expression.
   *         
   * @see #EMPTY_SET
   */
  public abstract Set<String> free();
  
  /**
   * Substitutes the expression <code>e</code> for the identifier
   * <code>id</code> in this expression, and returns the resulting
   * expression.
   * 
   * The resulting expression may be a new <code>Expression</code>
   * object or if no substitution took place, the same object.
   * The method operates recursively.
   * 
   * @param id the name of the identifier.
   * @param e the <code>Expression</code> to substitute.
   * 
   * @return the resulting expression.
   */
  public abstract Expression substitute(String id, Expression e);
  
  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString() {
    return toPrettyStringBuilder(PrettyStringBuilderFactory.newInstance()).toPrettyString();
  }
  
  /**
   * Returns the pretty string builder used to pretty print this expression. The pretty
   * string builder must be allocated from the specified <code>factory</code>, which is
   * currently always the default factory, but may also be another factory in the future.
   * 
   * @param factory the {@link PrettyStringBuilderFactory} used to allocate the required
   *                pretty string builders to pretty print this expression.
   * 
   * @return the pretty string builder used to pretty print this expression.
   * 
   * @see #toPrettyString()
   * @see PrettyStringBuilder
   * @see PrettyStringBuilderFactory
   */
  protected abstract PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory);
  
  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public abstract boolean equals(Object obj);
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public abstract int hashCode();
  
  /**
   * Returns the string representation for this expression. This method is mainly used for debugging.
   *
   * @return the pretty printed string representation for this expression.
   * 
   * @see #toPrettyString()
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return toPrettyString().toString();
  }
}
