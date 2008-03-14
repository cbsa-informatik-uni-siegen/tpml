package de.unisiegen.tpml.core.latex;


/**
 * This interface is used for latex commands.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public interface LatexCommand
{

  /**
   * The description.
   */
  public static final String DESCRIPTION = "Needed latex commands"; //$NON-NLS-1$


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  public boolean equals ( Object pObject );


  /**
   * Returns the name.
   * 
   * @return The name.
   */
  public String getName ();


  /**
   * Returns the string value of this <code>LatexCommand</code>.
   * 
   * @return The string value of this <code>LatexCommand</code>.
   */
  public String toString ();
}
