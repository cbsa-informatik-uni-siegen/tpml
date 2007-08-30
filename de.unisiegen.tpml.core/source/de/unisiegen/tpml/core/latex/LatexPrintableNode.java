package de.unisiegen.tpml.core.latex ;


import de.unisiegen.tpml.core.ProofNode ;


/**
 * Base interface for classes whose instances can be printed in latex and are an
 * instance of {@link ProofNode}.
 * 
 * @author Christian Fehler
 */
public interface LatexPrintableNode extends LatexPrintable
{
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
   * @see #toLatexString()
   * @see LatexStringBuilder
   * @see LatexStringBuilderFactory
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent ,
      int pDepth , int pId ) ;
}
