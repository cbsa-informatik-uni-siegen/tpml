package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex instructions.
 * 
 * @author Christian Fehler
 */
public interface LatexInstruction
{
  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex instructions" ; //$NON-NLS-1$


  /**
   * Returns the text.
   * 
   * @return The text.
   */
  public String getText ( ) ;


  /**
   * Returns the string value of this <code>LatexInstruction</code>.
   * 
   * @return The string value of this <code>LatexInstruction</code>.
   */
  public String toString ( ) ;
}
