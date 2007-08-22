package de.unisiegen.tpml.core.typechecker ;


import de.unisiegen.tpml.core.latex.LatexStringBuilder ;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
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
   * The latex print command for {@link TypeEnvironment}.
   */
  public static final String LATEX_TYPE_ENVIRONMENT = "TypeEnvironment" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link SeenTypes}.
   */
  public static final String LATEX_SEEN_TYPES = "SeenTypes" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationTypeChecker}.
   */
  public static final String LATEX_TYPE_EQUATION_TYPE_CHECKER = "TypeEquationTypeChecker" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeSubstitution}.
   */
  public static final String LATEX_TYPE_SUBSTITUTION = "TypeSubstitution" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationListTypeChecker}.
   */
  public static final String LATEX_TYPE_EQUATION_LIST_TYPE_CHECKER = "TypeEquationListTypeChecker" ; //$NON-NLS-1$
}
