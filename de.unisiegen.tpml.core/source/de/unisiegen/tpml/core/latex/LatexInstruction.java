package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex instructions.
 * 
 * @author Christian Fehler
 */
public interface LatexInstruction extends Comparable < LatexInstruction >
{
  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex instructions" ; //$NON-NLS-1$


  /**
   * Returns the string value of this <code>LatexInstruction</code>.
   * 
   * @return The string value of this <code>LatexInstruction</code>.
   */
  public String toString ( ) ;
}
