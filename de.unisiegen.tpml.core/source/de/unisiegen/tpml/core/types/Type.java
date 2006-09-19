package de.unisiegen.tpml.core.types;

import java.util.Collections;
import java.util.Set;

import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;

/**
 * Abstract base class for all types used within the
 * type checker and the various parsers.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable
 */
public abstract class Type implements PrettyPrintable, PrettyPrintPriorities {
  //
  // Constants
  //
  
  /**
   * Shared empty set, returned by {@link #free()} if the type does
   * not contain any free type variables.
   */
  protected static final Set<TypeVariable> EMPTY_SET = Collections.unmodifiableSet(Collections.<TypeVariable>emptySet());
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Constructor for all types.
   */
  protected Type() {
    // nothing to do here...
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * Returns the set of free type variables present within this type.
   * 
   * The default implementation simply returns the empty set, and
   * derived classes will need to override this method to return
   * the set of free type variables.
   * 
   * The returned set should be considered read-only by the caller
   * and must not be modified.
   * 
   * @return the set of free type variables within this type.
   */
  public Set<TypeVariable> free() {
    return EMPTY_SET;
  }
  
  /**
   * Applies the <code>substitution</code> to this type and returns
   * the resulting type, which may be this type itself if either this
   * type does not contain any type variables or no type variables that
   * are present in the <code>substitution</code>.
   * 
   * @param substitution the <code>TypeSubstitution</code> to apply to
   *                     this type.
   * 
   * @return the new type.
   * 
   * @throws NullPointerException if the <code>substitution</code> is
   *                              <code>null</code>.
   */
  public abstract Type substitute(TypeSubstitution substitution);
  
  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString() {
    return toPrettyStringBuilder(PrettyStringBuilderFactory.newInstance()).toPrettyString();
  }
  
  /**
   * Returns the pretty string builder used to pretty print this type. The pretty
   * string builder must be allocated from the specified <code>factory</code>, which
   * is currently always the default factory, but may also be another factory in the
   * future.
   * 
   * @param factory the {@link PrettyStringBuilderFactory} used to allocate the required
   *                pretty string builders to pretty print this type.
   * 
   * @return the pretty string builder used to pretty print this type.
   * 
   * @see #toPrettyString()
   * @see PrettyStringBuilder
   * @see PrettyStringBuilderFactory
   */
  public abstract PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory factory);
  
  
  
  //
  // Base methods
  //
  
  /**
   * Returns the string representation for this type. This method
   * is mainly used for debugging.
   *
   * @return the pretty printed string representation for this type.
   * 
   * @see #toPrettyString()
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    return toPrettyString().toString();
  }
}
