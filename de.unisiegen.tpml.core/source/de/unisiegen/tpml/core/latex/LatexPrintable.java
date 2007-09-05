package de.unisiegen.tpml.core.latex ;


/**
 * Base interface for classes whose instances can be printed in latex.
 * 
 * @author Christian Fehler
 */
public interface LatexPrintable extends LatexCommandNames
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ( ) ;


  /**
   * Returns a list of needed latex instructions for this latex printable
   * object.
   * 
   * @return A lsit of needed latex instructions for this latex printable
   *         object.
   */
  public LatexInstructionList getLatexInstructions ( ) ;


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ( ) ;


  /**
   * Returns a {@link LatexString} that can be used to export this latex
   * printable object.
   * 
   * @return A {@link LatexString} that can be used to export this latex
   *         printable object.
   */
  public LatexString toLatexString ( ) ;


  /**
   * Returns the latex string builder used to latex print this latex printable
   * object. The latex string builder must be allocated from the specified
   * <code>pLatexStringBuilderFactory</code>, which is currently always the
   * default factory, but may also be another factory in the future.
   * 
   * @param pLatexStringBuilderFactory the {@link LatexStringBuilderFactory}
   *          used to allocate the required latex string builders to latex print
   *          this latex printable object.
   * @param pIndent The indent of this object.
   * @return The latex string builder used to latex print this latex printable
   *         object.
   * @see #toLatexString()
   * @see LatexStringBuilder
   * @see LatexStringBuilderFactory
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent ) ;
}
