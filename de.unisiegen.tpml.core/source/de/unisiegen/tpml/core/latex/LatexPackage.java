package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex packages.
 * 
 * @author Christian Fehler
 */
public interface LatexPackage extends Comparable < LatexPackage >
{
  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex packages" ; //$NON-NLS-1$


  /**
   * Returns the string value of this <code>LatexInstruction</code>.
   * 
   * @return The string value of this <code>LatexInstruction</code>.
   */
  public String toString ( ) ;
}
