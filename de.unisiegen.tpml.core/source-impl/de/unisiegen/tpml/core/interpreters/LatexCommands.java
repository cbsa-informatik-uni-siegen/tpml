package de.unisiegen.tpml.core.interpreters ;


import de.unisiegen.tpml.core.latex.LatexStringBuilder ;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
 * @see DefaultStore
 * @see LatexStringBuilder
 */
public interface LatexCommands
{
  /**
   * The latex print command for a space.
   */
  public static final String LATEX_SPACE = "\\ " ; //$NON-NLS-1$


  /**
   * The latex print command for an equal.
   */
  public static final String LATEX_EQUAL = "=" ; //$NON-NLS-1$


  /**
   * The latex print command for a comma.
   */
  public static final String LATEX_COMMA = "," ; //$NON-NLS-1$


  /**
   * The latex print command for a colon.
   */
  public static final String LATEX_COLON = "\\colon" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link Store}.
   */
  public static final String LATEX_STORE = "Store" ; //$NON-NLS-1$
}
