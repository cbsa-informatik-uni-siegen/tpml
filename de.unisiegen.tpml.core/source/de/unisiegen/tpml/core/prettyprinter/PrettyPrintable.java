package de.unisiegen.tpml.core.prettyprinter;


/**
 * Base interface for classes whose instances can be pretty printed.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 */
public interface PrettyPrintable extends PrettyCommandNames
{

  /**
   * Returns a {@link PrettyString} that can be used to represent this printable
   * object, and extract information about possible other printables contained
   * within this object.
   * 
   * @return the pretty string for this printable.
   * @see java.lang.Object#toString()
   */
  public PrettyString toPrettyString ();


  /**
   * Returns the pretty string builder used to pretty print this printable
   * object. The pretty string builder must be allocated from the specified
   * <code>pPrettyStringBuilderFactory</code>, which is currently always the
   * default factory, but may also be another factory in the future.
   * 
   * @param pPrettyStringBuilderFactory the {@link PrettyStringBuilderFactory}
   *          used to allocate the required pretty string builders to pretty
   *          print this printable object.
   * @return The pretty string builder used to pretty print this printable
   *         object.
   * @see #toPrettyString()
   * @see PrettyStringBuilder
   * @see PrettyStringBuilderFactory
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory );
}
