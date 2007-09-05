package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Instances of this class represent conditions without an else block in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Condition
 * @see Expression
 */
public final class Condition1 extends Expression implements DefaultExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 0 , 1 } ;


  /**
   * String for the case that e0 is null.
   */
  private static final String E0_NULL = "e0 is null" ; //$NON-NLS-1$


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Condition1.class ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_IF , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{if}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_KEY_THEN , 0 ,
        "\\textbf{\\color{" + LATEX_COLOR_KEYWORD + "}{then}}" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    commands.add ( new DefaultLatexCommand ( LATEX_CONDITION1 , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}\\" + LATEX_KEY_IF + "\\ #1\\ \\" //$NON-NLS-1$//$NON-NLS-2$
        + LATEX_KEY_THEN + "\\ #2" , "e0" , "e1" ) ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    return commands ;
  }


  /**
   * The expressions.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new <code>Condition1</code> with the specified
   * <code>e0</code> and <code>e1</code>.
   * 
   * @param pExpression0 the conditional block.
   * @param pExpression1 the <code>true</code> case.
   * @throws NullPointerException if <code>e0</code> or <code>e1</code> is
   *           <code>null</code>.
   */
  public Condition1 ( Expression pExpression0 , Expression pExpression1 )
  {
    if ( pExpression0 == null )
    {
      throw new NullPointerException ( E0_NULL ) ;
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL ) ;
    }
    this.expressions = new Expression [ ]
    { pExpression0 , pExpression1 } ;
    this.expressions [ 0 ].setParent ( this ) ;
    this.expressions [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new <code>Condition1</code> with the specified
   * <code>e0</code> and <code>e1</code>.
   * 
   * @param pExpression0 the conditional block.
   * @param pExpression1 the <code>true</code> case.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>e0</code> or <code>e1</code> is
   *           <code>null</code>.
   */
  public Condition1 ( Expression pExpression0 , Expression pExpression1 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression0 , pExpression1 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Condition1 clone ( )
  {
    return new Condition1 ( this.expressions [ 0 ].clone ( ) ,
        this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Condition1 )
    {
      Condition1 other = ( Condition1 ) pObject ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( this.expressions [ 1 ]
          .equals ( other.expressions [ 1 ] ) ) ) ;
    }
    return false ;
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
   * Returns the conditional part.
   * 
   * @return the conditional block
   */
  public Expression getE0 ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the <code>true</code> case.
   * 
   * @return the <code>true</code> block.
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 1 ] ;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * Returns the indices of the child {@link Expression}s.
   * 
   * @return The indices of the child {@link Expression}s.
   */
  public int [ ] getExpressionsIndex ( )
  {
    return INDICES_E ;
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
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( )
        + this.expressions [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Condition1 substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE0 = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newE1 = this.expressions [ 1 ].substitute ( pId , pExpression ) ;
    return new Condition1 ( newE0 , newE1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Condition1 substitute ( TypeSubstitution substitution )
  {
    Expression newE0 = this.expressions [ 0 ].substitute ( substitution ) ;
    Expression newE1 = this.expressions [ 1 ].substitute ( substitution ) ;
    return new Condition1 ( newE0 , newE1 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_CONDITION , LATEX_CONDITION1 , pIndent , this.toPrettyString ( )
            .toString ( ) , this.expressions [ 0 ].toPrettyString ( )
            .toString ( ) , this.expressions [ 1 ].toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
        PRIO_CONDITION_E0 ) ;
    builder.addBreak ( ) ;
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
        PRIO_CONDITION_E1 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CONDITION ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_IF ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONDITION_E0 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( PRETTY_THEN ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CONDITION_E1 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
