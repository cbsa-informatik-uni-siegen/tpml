package de.unisiegen.tpml.core.prettyprinter;


/**
 * Factory class for
 * {@link de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder}s. Use this
 * class to allocate new pretty string builders, which in turn are used to
 * generate new {@link de.unisiegen.tpml.core.prettyprinter.PrettyString}s.
 * 
 * @author Benedikt Meurer
 * @version $Id$
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyString
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 */
public final class PrettyStringBuilderFactory
{

  //
  // Constructor (private)
  //
  /**
   * Allocates a new <code>PrettyStringBuilderFactory</code>, which can be
   * used to allocate new {@link PrettyStringBuilder}s.
   * 
   * @see #newInstance()
   */
  private PrettyStringBuilderFactory ()
  {
    // nothing to do here...
  }


  //
  // Factory instantiation
  //
  /**
   * Allocates a new <code>PrettyStringBuilderFactory</code>, which can be
   * used to allocate new {@link PrettyStringBuilder}s.
   * 
   * @return a newly allocated <code>PrettyStringBuilderFactory</code>
   *         instance.
   * @see #newBuilder(PrettyPrintable,int)
   */
  public static PrettyStringBuilderFactory newInstance ()
  {
    return new PrettyStringBuilderFactory ();
  }


  //
  // Builder management
  //
  /**
   * Allocates a new <code>PrettyStringBuilder</code>, which will generate an
   * annotation for the <code>printable</code> for the whole string
   * represented by the builder.
   * 
   * @param printable the printable object.
   * @param returnPriority the return priority according to the priority grammar
   *          used for the printables in this builder.
   * @return a newly allocated <code>PrettyStringBuilder</code> for the
   *         <code>printable</code> with the specified
   *         <code>returnPriority</code>.
   * @throws NullPointerException if <code>printable</code> is
   *           <code>null</code>.
   * @see PrettyString
   * @see PrettyStringBuilder
   */
  public PrettyStringBuilder newBuilder ( PrettyPrintable printable,
      int returnPriority )
  {
    return new DefaultPrettyStringBuilder ( printable, returnPriority );
  }
}
