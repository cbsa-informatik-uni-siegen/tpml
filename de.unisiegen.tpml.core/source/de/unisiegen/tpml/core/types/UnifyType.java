package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
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
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand ( "boldUnify" , 0 , //$NON-NLS-1$
        "\\textbf{unify}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIFY_TYPE , 0 ,
        "\\boldUnify" ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_PRIMITIVE , LATEX_UNIFY_TYPE ) ;
    }
    return this.latexStringBuilder ;
  }
}
