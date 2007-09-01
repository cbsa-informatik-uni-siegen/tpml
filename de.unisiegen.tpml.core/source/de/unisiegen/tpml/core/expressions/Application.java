package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * Represents the <b>(APP)</b> expression in the expression hierarchy. The
 * string representation for applications is <code>e1 e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 */
public final class Application extends Expression implements DefaultExpressions
{
  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Application.class ) ;


  /**
   * String for the case that e1 is null.
   */
  private static final String E1_NULL = "e1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that e2 is null.
   */
  private static final String E2_NULL = "e2 is null" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { 1 , 2 } ;


  /**
   * The left and right expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new application of <code>e1</code> to <code>e2</code>.
   * 
   * @param pExpression1 the first expression (the operation).
   * @param pExpression2 the second expression (the operand).
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public Application ( Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( E1_NULL ) ;
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( E2_NULL ) ;
    }
    this.expressions = new Expression [ 2 ] ;
    /*
     * Clone the children if they have already a parent and this parent is an
     * instanceof InfixOperation, because if the now needed brackets. Otherwise
     * the latex export does not work.
     */
    if ( ( pExpression1.getParent ( ) != null )
        && ( pExpression1.getParent ( ) instanceof InfixOperation ) )
    {
      this.expressions [ 0 ] = pExpression1.clone ( ) ;
    }
    else
    {
      this.expressions [ 0 ] = pExpression1 ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    if ( ( pExpression2.getParent ( ) != null )
        && ( pExpression2.getParent ( ) instanceof InfixOperation ) )
    {
      this.expressions [ 1 ] = pExpression2.clone ( ) ;
    }
    else
    {
      this.expressions [ 1 ] = pExpression2 ;
    }
    this.expressions [ 1 ].setParent ( this ) ;
  }


  /**
   * Allocates a new application of <code>e1</code> to <code>e2</code>.
   * 
   * @param pExpression1 the first expression (the operation).
   * @param pExpression2 the second expression (the operand).
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException if <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public Application ( Expression pExpression1 , Expression pExpression2 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression1 , pExpression2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Application clone ( )
  {
    return new Application ( this.expressions [ 0 ].clone ( ) ,
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
    if ( pObject instanceof Application )
    {
      Application other = ( Application ) pObject ;
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
   * Returns the first expression of the application.
   * 
   * @return the first expression of the application.
   */
  public Expression getE1 ( )
  {
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the second expression of the application.
   * 
   * @return the second expression of the application.
   */
  public Expression getE2 ( )
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
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_APPLICATION , 2 , "\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}#1\\ #2" , "e1" , "e2" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
   * An <code>Application</code> can be a value if it consists of a binary
   * operator and a value, or if it consists of a <code>UnaryCons</code>
   * operator and a value.
   * 
   * @return <code>true</code> if the application consists of a binary
   *         operator and a value.
   * @see Expression#isValue()
   */
  @ Override
  public boolean isValue ( )
  {
    return ( ( this.expressions [ 0 ] instanceof BinaryOperator || this.expressions [ 0 ] instanceof UnaryCons ) && this.expressions [ 1 ]
        .isValue ( ) ) ;
  }


  /**
   * Substitutes <code>e</code> for <code>id</code> in the two sub
   * expressions of the application.
   * 
   * @param pId the identifier for which to substitute.
   * @param pExpression the expression to substitute for <code>id</code>.
   * @return the resulting expression.
   * @see Expression#substitute(Identifier, Expression )
   */
  @ Override
  public Application substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE1 = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pId , pExpression ) ;
    return new Application ( newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public Application substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE1 = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.expressions [ 1 ].substitute ( pTypeSubstitution ) ;
    return new Application ( newE1 , newE2 ) ;
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
        PRIO_APPLICATION , LATEX_APPLICATION , pIndent , this.toPrettyString ( )
            .toString ( ) , this.expressions [ 0 ].toPrettyString ( )
            .toString ( ) , this.expressions [ 1 ].toPrettyString ( )
            .toString ( ) ) ;
    builder.addBuilder ( this.expressions [ 0 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
        PRIO_APPLICATION_E1 ) ;
    builder.addBuilder ( this.expressions [ 1 ].toLatexStringBuilder (
        pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) ,
        PRIO_APPLICATION_E2 ) ;
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
          PRIO_APPLICATION ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_APPLICATION_E1 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_APPLICATION_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
