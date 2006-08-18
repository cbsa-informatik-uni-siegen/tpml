package de.unisiegen.tpml.core.prettyprinter;

/**
 * Base interface for classes whose instances can be pretty printed.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public interface PrettyPrintable {
  //
  // Primitives
  //
  
  /**
   * Returns a {@link PrettyString} that can be used to represent
   * this printable object, and extract information about possible
   * other printables contained within this object.
   * 
   * @return the pretty string for this printable.
   * 
   * @see java.lang.Object#toString()
   */
  public PrettyString toPrettyString();
}
