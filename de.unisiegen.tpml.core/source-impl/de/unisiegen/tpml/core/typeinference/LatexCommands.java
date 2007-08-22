package de.unisiegen.tpml.core.typeinference ;


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
   * The latex print command for a comma.
   */
  public static final String LATEX_COMMA = "," ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationTypeInference}.
   */
  public static final String LATEX_TYPE_EQUATION_TYPE_INFERENCE = "TypeEquationTypeInference" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeSubType}.
   */
  public static final String LATEX_TYPE_SUB_TYPE = "TypeSubType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeEquationListTypeInference}.
   */
  public static final String LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE = "TypeEquationListTypeInference" ; //$NON-NLS-1$
}
