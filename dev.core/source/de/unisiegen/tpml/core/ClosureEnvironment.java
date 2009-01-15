package de.unisiegen.tpml.core;


import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.util.Environment;


/**
 * An environment that holds pairs of Identifiers and Closures.
 */
public interface ClosureEnvironment extends
    Environment < Identifier, Closure >, Cloneable
{

  /**
   * Prepend id : cl to this environment
   * 
   * @param identifier
   * @param closure
   */
  public void put ( Identifier identifier, Closure closure );


  /**
   * Clone this environment.
   * 
   * @param newIndex The index for the new environment.
   * @return The cloned environment
   */
  public Object clone ( int newIndex );


  /**
   * @return The environment's name (e.g. eta + index)
   */
  public PrettyString getName ();


  /**
   * @return Do we need to print this environment in the current node?
   */
  public boolean isNotPrinted ();


  /**
   * @return The whole string for this environment, containing all its elements.
   */
  public PrettyString toPrettyFullString ();


  /**
   * @return The string builder for the environment's name.
   */
  public PrettyStringBuilder getNameBuilder ();


  /**
   * @return The string builder for the environment's content.
   */
  public PrettyStringBuilder toPrettyFullStringBuilder ();
}
