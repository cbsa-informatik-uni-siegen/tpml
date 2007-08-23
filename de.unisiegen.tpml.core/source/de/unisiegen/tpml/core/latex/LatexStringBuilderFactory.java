package de.unisiegen.tpml.core.latex ;


/**
 * Factory class for {@link LatexStringBuilder}s. Use this class to allocate
 * new latex string builders, which in turn are used to generate new
 * {@linkLatexString}s.
 * 
 * @author Christian Fehler
 * @see LatexString
 * @seeLatexStringBuilder
 */
public final class LatexStringBuilderFactory
{
  /**
   * Allocates a new <code>LatexStringBuilderFactory</code>, which can be
   * used to allocate new {@link LatexStringBuilder}s.
   * 
   * @see #newInstance()
   */
  private LatexStringBuilderFactory ( )
  {
    // nothing to do here
  }


  /**
   * Allocates a new <code>LatexStringBuilderFactory</code>, which can be
   * used to allocate new {@link LatexStringBuilder}s.
   * 
   * @return A newly allocated <code>LatexStringBuilderFactory</code>
   *         instance.
   * @see #newBuilder(LatexPrintable,int,String,int)
   */
  public static LatexStringBuilderFactory newInstance ( )
  {
    return new LatexStringBuilderFactory ( ) ;
  }


  /**
   * Allocates a new <code>LatexStringBuilder</code>, which will generate an
   * annotation for the <code>LatexPrintable</code> for the whole string
   * represented by the builder.
   * 
   * @param pLatexPrintable the printable object.
   * @param pReturnPriority the return priority according to the priority
   *          grammar used for the printables in this builder.
   * @param pName The name of the latex command.
   * @param pIndent The indent of this object.
   * @return A newly allocated <code>LatexStringBuilder</code> for the
   *         <code>LatexPrintable</code> with the specified
   *         <code>pReturnPriority</code>.
   * @throws NullPointerException If <code>LatexPrintable</code> is
   *           <code>null</code>.
   * @see LatexString
   * @see LatexStringBuilder
   */
  public LatexStringBuilder newBuilder ( LatexPrintable pLatexPrintable ,
      int pReturnPriority , String pName , int pIndent )
  {
    return new DefaultLatexStringBuilder ( pLatexPrintable , pReturnPriority ,
        pName , pIndent ) ;
  }
}
