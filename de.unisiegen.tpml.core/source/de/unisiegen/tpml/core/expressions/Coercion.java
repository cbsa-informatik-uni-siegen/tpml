package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.core.types.TypeVariable ;


/**
 * Represents the <b>(COERCION)</b> expression in the expression hierarchy.
 * 
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 */
public final class Coercion extends Expression implements DefaultTypes ,
    DefaultExpressions , SortedChildren
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { 1 , 2 } ;


  /**
   * String for the case that the expression is null.
   */
  private static final String EXPRESSION_NULL = "expression is null" ; //$NON-NLS-1$


  /**
   * String for the case that tau1 is null.
   */
  private static final String TAU1_NULL = "tau1 is null" ; //$NON-NLS-1$


  /**
   * String for the case that tau2 is null.
   */
  private static final String TAU2_NULL = "tau2 is null" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Coercion.class ) ;


  /**
   * The types.
   * 
   * @see #getTypes()
   */
  private MonoType [ ] types ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new coercion.
   * 
   * @param pExpression The body.
   * @param pTau1 The first type.
   * @param pTau2 The second type.
   * @throws NullPointerException If something is null.
   */
  public Coercion ( Expression pExpression , MonoType pTau1 , MonoType pTau2 )
  {
    if ( pExpression == null )
    {
      throw new NullPointerException ( EXPRESSION_NULL ) ;
    }
    if ( pTau1 == null )
    {
      throw new NullPointerException ( TAU1_NULL ) ;
    }
    if ( pTau2 == null )
    {
      throw new NullPointerException ( TAU2_NULL ) ;
    }
    // Expression
    this.expressions = new Expression [ ]
    { pExpression } ;
    this.expressions [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ ]
    { pTau1 , pTau2 } ;
    this.types [ 0 ].setParent ( this ) ;
    this.types [ 1 ].setParent ( this ) ;
    checkTypeVariables ( ) ;
  }


  /**
   * Allocates a new coercion.
   * 
   * @param pExpression The body.
   * @param pTau1 The first type.
   * @param pTau2 The second type.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   * @throws NullPointerException If something is null.
   */
  public Coercion ( Expression pExpression , MonoType pTau1 , MonoType pTau2 ,
      int pParserStartOffset , int pParserEndOffset )
  {
    this ( pExpression , pTau1 , pTau2 ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * Checks if a child type contains {@link TypeVariable}s.
   */
  public void checkTypeVariables ( )
  {
    ArrayList < TypeVariable > list = new ArrayList < TypeVariable > ( ) ;
    list.addAll ( this.types [ 0 ].getTypeVariablesFree ( ) ) ;
    list.addAll ( this.types [ 1 ].getTypeVariablesFree ( ) ) ;
    if ( list.size ( ) > 0 )
    {
      String [ ] message = new String [ list.size ( ) ] ;
      int [ ] startOffset = new int [ list.size ( ) ] ;
      int [ ] endOffset = new int [ list.size ( ) ] ;
      for ( int i = 0 ; i < list.size ( ) ; i ++ )
      {
        message [ i ] = Messages.getString ( "Parser.18" ) ; //$NON-NLS-1$
        startOffset [ i ] = list.get ( i ).getParserStartOffset ( ) ;
        endOffset [ i ] = list.get ( i ).getParserEndOffset ( ) ;
      }
      throw new LanguageParserMultiException ( message , startOffset ,
          endOffset ) ;
    }
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Coercion clone ( )
  {
    return new Coercion ( this.expressions [ 0 ].clone ( ) , this.types [ 0 ]
        .clone ( ) , this.types [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Coercion )
    {
      Coercion other = ( Coercion ) pObject ;
      return ( ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) )
          && ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) && ( this.types [ 1 ]
          .equals ( other.types [ 1 ] ) ) ) ;
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
   * Returns the body of the lambda expression.
   * 
   * @return the bodyof the lambda expression.
   */
  public Expression getE ( )
  {
    return this.expressions [ 0 ] ;
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
    return Coercion.INDICES_E ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_COERCION , 3 ,
        "(#1\\colon\\ #2<\\colon\\ #3)" ) ) ; //$NON-NLS-1$
    for ( LatexCommand command : this.expressions [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.types [ 0 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    for ( LatexCommand command : this.types [ 1 ].getLatexCommands ( ) )
    {
      commands.add ( command ) ;
    }
    return commands ;
  }


  /**
   * Returns the {@link Expression} and the {@link Type}s in the right sorting.
   * 
   * @return The {@link Expression} and the {@link Type}s in the right sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public ExpressionOrType [ ] getSortedChildren ( )
  {
    return new ExpressionOrType [ ]
    { this.expressions [ 0 ] , this.types [ 0 ] , this.types [ 1 ] } ;
  }


  /**
   * Returns the first type..
   * 
   * @return The first type.
   */
  public MonoType getTau1 ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the second type..
   * 
   * @return The second type.
   */
  public MonoType getTau2 ( )
  {
    return this.types [ 1 ] ;
  }


  /**
   * Returns the types.
   * 
   * @return The types.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the indices of the child {@link Type}s.
   * 
   * @return The indices of the child {@link Type}s.
   */
  public int [ ] getTypesIndex ( )
  {
    return Coercion.INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions [ 0 ].hashCode ( ) + this.types [ 0 ].hashCode ( )
        + this.types [ 1 ].hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Coercion substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    Expression newE = this.expressions [ 0 ].substitute ( pId , pExpression ) ;
    return new Coercion ( newE , this.types [ 0 ] , this.types [ 1 ] ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Coercion substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    MonoType newTau1 = this.types [ 0 ].substitute ( pTypeSubstitution ) ;
    MonoType newTau2 = this.types [ 1 ].substitute ( pTypeSubstitution ) ;
    return new Coercion ( newE , newTau1 , newTau2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_COERCION , LATEX_COERCION ) ;
      this.latexStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_E ) ;
      this.latexStringBuilder.addBuilder ( this.types [ 0 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_TAU1 ) ;
      this.latexStringBuilder.addBreak ( ) ;
      this.latexStringBuilder.addBuilder ( this.types [ 1 ]
          .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_TAU2 ) ;
    }
    return this.latexStringBuilder ;
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
          PrettyPrintPriorities.PRIO_COERCION ) ;
      this.prettyStringBuilder.addText ( PRETTY_LPAREN ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_E ) ;
      this.prettyStringBuilder.addText ( PRETTY_COLON ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_TAU1 ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SUBTYPE ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.types [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_COERCION_TAU2 ) ;
      this.prettyStringBuilder.addText ( PRETTY_RPAREN ) ;
    }
    return this.prettyStringBuilder ;
  }
}
