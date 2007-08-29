package de.unisiegen.tpml.core.latex ;


/**
 * Base interface for classes whose instances can be printed in latex.
 * 
 * @author Christian Fehler
 */
public interface LatexPrintableNode extends LatexPrintable
{
  /**
   * Returns a {@link LatexString} that can be used to export this latex
   * printable object.
   * 
   * @param pDepth the depth of this proof node
   * @param pId the ID of this proof node
   * @return A {@link LatexString} that can be used to export this latex
   *         printable object.
   */
  public LatexString toLatexString ( int pDepth , int pId ) ;


  /**
   * Returns the latex string builder used to latex print thislatex printable
   * object. The latex string builder must be allocated from the specified
   * <code>pLatexStringBuilderFactory</code>, which is currently always the
   * default factory, but may also be another factory in the future.
   * 
   * @param pLatexStringBuilderFactory the {@link LatexStringBuilderFactory}
   *          used to allocate the required latex string builders to latex print
   *          this latex printable object.
   * @param pIndent The indent of this object.
   * @param pDepth the depth of this proof node
   * @param pId the ID of this proof node
   * @return The latex string builder used to latex print this latex printable
   *         object.
   * @see #toLatexString(int,int)
   * @see LatexStringBuilder
   * @see LatexStringBuilderFactory
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent ,
      int pDepth , int pId ) ;
}
