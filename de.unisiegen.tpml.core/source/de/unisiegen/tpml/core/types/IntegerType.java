package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * This class represents the <tt>int</tt> type in our type system. Only a
 * single instance of this class exists at all times.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:277 $
 * @see PrimitiveType
 */
public final class IntegerType extends PrimitiveType
{
  /**
   * The keyword <code>int</code>.
   */
  private static final String INT = "int" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Type}.
   */
  private static final String CAPTION = Type.getCaption ( IntegerType.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_INT , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_TYPE + "}{int}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_INTEGER_TYPE , 0 ,
        "\\" + LATEX_KEY_INT ) ) ; //$NON-NLS-1$
    return commands ;
  }


  /**
   * Allocates a new <code>IntegerType</code> instance.
   */
  public IntegerType ( )
  {
    super ( INT ) ;
  }


  /**
   * Allocates a new <code>IntegerType</code> instance.
   * 
   * @param pParserStartOffset The start offset of this {@link Type} in the
   *          source code.
   * @param pParserEndOffset The end offset of this {@link Type} in the source
   *          code.
   */
  public IntegerType ( int pParserStartOffset , int pParserEndOffset )
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
  public IntegerType clone ( )
  {
    return new IntegerType ( ) ;
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
        PRIO_PRIMITIVE , LATEX_INTEGER_TYPE , pIndent , this.toPrettyString ( )
            .toString ( ) ) ;
    return builder ;
  }
}
