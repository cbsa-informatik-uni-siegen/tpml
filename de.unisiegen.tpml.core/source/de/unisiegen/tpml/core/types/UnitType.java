package de.unisiegen.tpml.core.types ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * This class represents the <tt>unit</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 */
public final class UnitType extends PrimitiveType
{
  /**
   * The keyword <code>unit</code>.
   */
  private static final String UNIT = "unit" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( UnitType.class ) ;


  /**
   * Allocates a new <code>UnitType</code> instance.
   */
  public UnitType ( )
  {
    super ( UNIT ) ;
  }


  /**
   * Allocates a new <code>UnitType</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public UnitType ( int pParserStartOffset , int pParserEndOffset )
  {
    this ( ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Type#clone()
   */
  @ Override
  public UnitType clone ( )
  {
    return new UnitType ( ) ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_KEYWORD_UNIT , 0 ,
        "\\textbf{unit}" ) ) ; //$NON-NLS-1$
    commands.add ( new DefaultLatexCommand ( LATEX_UNIT_TYPE , 0 ,
        "\\" + LATEX_KEYWORD_UNIT ) ) ; //$NON-NLS-1$
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
          PRIO_PRIMITIVE , LATEX_UNIT_TYPE ) ;
    }
    return this.latexStringBuilder ;
  }
}
