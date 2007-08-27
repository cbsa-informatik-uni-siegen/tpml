package de.unisiegen.tpml.core.latex ;


import java.util.TreeSet ;


/**
 * Base interface for classes whose instances can be printed in latex.
 * 
 * @author Christian Fehler
 */
public interface LatexPrintableNode extends LatexCommandNames
{
  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( ) ;


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( ) ;


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( ) ;


  /**
   * Returns a {@link LatexString} that can be used to export this latex
   * printable object.
   * @param pDepth the depth of this proof node
 	* @param pId the ID of this proof node
   * 
   * @return A {@link LatexString} that can be used to export this latex
   *         printable object.
   */
  public LatexString toLatexString ( int pDepth, int pId ) ;


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
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent, int pDepth, int pId ) ;
}

