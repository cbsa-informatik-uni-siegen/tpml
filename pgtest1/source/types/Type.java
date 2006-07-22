package types;

import java.util.Collections;
import java.util.Set;

import common.prettyprinter.PrettyPrintable;
import common.prettyprinter.PrettyString;
import common.prettyprinter.PrettyStringBuilder;

/**
 * Abstract base class for all types used within the
 * type checker and the various parsers.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see common.prettyprinter.PrettyPrintable
 */
public abstract class Type implements PrettyPrintable {
  //
  // Constants
  //
  
  /**
   * Shared empty set, returned by {@link #free()} if the type does
   * not contain any free type variables.
   */
  protected static final Set<String> EMPTY_SET = Collections.unmodifiableSet(Collections.<String>emptySet());
  
  
  
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
  public Set<String> free() {
    return EMPTY_SET;
  }
  
  
  
  //
  // Pretty printing
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString() {
    return toPrettyStringBuilder().toPrettyString();
  }
  
  /**
   * Returns the pretty string builder for this type. The returned
   * builder is used to construct the {@link PrettyString} for this
   * type.
   * 
   * @return the {@link PrettyStringBuilder} for this type.
   * 
   * @see #toPrettyString()
   */
  protected abstract PrettyStringBuilder toPrettyStringBuilder();
  
  
  
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
