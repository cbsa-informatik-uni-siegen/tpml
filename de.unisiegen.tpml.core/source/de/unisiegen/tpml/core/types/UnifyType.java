package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * Type for the type equation in the unification algorithm
 * 
 * @author Benjamin Mies
 */
public class UnifyType extends PrimitiveType
{
  /**
   * The keyword <code>bool</code>.
   */
  private static final String UNIFY = "unify" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( UnifyType.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_UNIFY , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_TYPE + "}{unify}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_TYPE , 0 , "\\" //$NON-NLS-1$
        + LATEX_KEY_UNIFY ) ) ;
    return commands ;
  }


  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnifyType ( )
  {
    super ( UNIFY ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public UnifyType clone ( )
  {
    return new UnifyType ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return CAPTION ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = super.getLatexCommands ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_PRIMITIVE , LATEX_UNIFY_TYPE , pIndent , this.toPrettyString ( )
            .toString ( ) ) ;
    return builder ;
  }
}
