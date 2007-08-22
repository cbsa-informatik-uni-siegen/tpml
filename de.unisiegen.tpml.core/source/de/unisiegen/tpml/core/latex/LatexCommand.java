package de.unisiegen.tpml.core.latex ;


/**
 * This interface is used for latex commands.
 * 
 * @author Christian Fehler
 */
public interface LatexCommand extends Comparable < LatexCommand >
{
  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex commands" ; //$NON-NLS-1$


  /**
   * Returns the string value of this <code>LatexCommand</code>.
   * 
   * @return The string value of this <code>LatexCommand</code>.
   */
  public String toString ( ) ;
}
