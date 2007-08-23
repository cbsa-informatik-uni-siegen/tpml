package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.interfaces.DefaultExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ExpressionOrType ;
import de.unisiegen.tpml.core.interfaces.SortedChildren ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Instances of this class represent duplication expressions,
 * 
 * @author Christian Fehler
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Duplication extends Expression implements
    DefaultIdentifiers , DefaultExpressions , SortedChildren
{
  /**
   * String for the case that the identifiers are null.
   */
  private static final String IDENTIFIERS_NULL = "identifiers is null" ; //$NON-NLS-1$


  /**
   * String for the case that one identifier are null.
   */
  private static final String IDENTIFIER_NULL = "one identifier is null" ; //$NON-NLS-1$


  /**
   * String for the case that the expressions are null.
   */
  private static final String EXPRESSIONS_NULL = "expressions is null" ; //$NON-NLS-1$


  /**
   * String for the case that one expression are null.
   */
  private static final String EXPRESSION_NULL = "one expression is null" ; //$NON-NLS-1$


  /**
   * The identifier has the wrong set.
   */
  private static final String WRONG_SET = "the set of the identifier has to be 'attribute'" ; //$NON-NLS-1$


  /**
   * String for the case that the arity of identifiers and expressions doesn´t
   * match.
   */
  private static final String ARITY = "the arity of identifiers and expressions must match" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression
      .getCaption ( Duplication.class ) ;


  /**
   * The string of an self identifier.
   */
  private static final String SELF = "self" ; //$NON-NLS-1$


  /**
   * Indeces of the child {@link Expression}s.
   */
  private int [ ] indicesE ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private int [ ] indicesId ;


  /**
   * The expressions.
   * 
   * @see #getExpressions()
   */
  private Expression [ ] expressions ;


  /**
   * The identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * Allocates a new {@link Duplication}.
   * 
   * @param pIdentifiers The {@link Identifier}.
   * @param pExpressions The child {@link Expression}.
   */
  public Duplication ( Identifier [ ] pIdentifiers , Expression [ ] pExpressions )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( IDENTIFIERS_NULL ) ;
    }
    for ( Identifier id : pIdentifiers )
    {
      if ( id == null )
      {
        throw new NullPointerException ( IDENTIFIER_NULL ) ;
      }
      if ( ! Identifier.Set.ATTRIBUTE.equals ( id.getSet ( ) ) )
      {
        throw new IllegalArgumentException ( WRONG_SET ) ;
      }
    }
    if ( pExpressions == null )
    {
      throw new NullPointerException ( EXPRESSIONS_NULL ) ;
    }
    for ( Expression e : pExpressions )
    {
      if ( e == null )
      {
        throw new NullPointerException ( EXPRESSION_NULL ) ;
      }
    }
    if ( pIdentifiers.length != pExpressions.length )
    {
      throw new IllegalArgumentException ( ARITY ) ;
    }
    // Identifier
    this.identifiers = pIdentifiers ;
    this.indicesId = new int [ this.identifiers.length ] ;
    // Expression
    this.expressions = pExpressions ;
    this.indicesE = new int [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      // Identifier
      this.identifiers [ i ].setParent ( this ) ;
      this.indicesId [ i ] = i + 1 ;
      // Expression
      this.expressions [ i ].setParent ( this ) ;
      this.indicesE [ i ] = i + 1 ;
    }
  }


  /**
   * Allocates a new {@link Duplication}.
   * 
   * @param pIdentifiers The {@link Identifier}.
   * @param pExpressions The child {@link Expression}.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Duplication ( Identifier [ ] pIdentifiers ,
      Expression [ ] pExpressions , int pParserStartOffset ,
      int pParserEndOffset )
  {
    this ( pIdentifiers , pExpressions ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication clone ( )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].clone ( ) ;
    }
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    return new Duplication ( newIdentifiers , newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Duplication )
    {
      Duplication other = ( Duplication ) pObject ;
      return ( ( Arrays.equals ( this.expressions , other.expressions ) ) && ( Arrays
          .equals ( this.identifiers , other.identifiers ) ) ) ;
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
   * Returns the sub {@link Expression}s.
   * 
   * @return the sub {@link Expression}s.
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
    return this.indicesE ;
  }


  /**
   * Returns the {@link Identifier}s of this {@link Expression}.
   * 
   * @return The {@link Identifier}s of this {@link Expression}.
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.add ( new Identifier ( SELF , Identifier.Set.SELF ) ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.identifiersFree.addAll ( this.expressions [ i ]
            .getIdentifiersFree ( ) ) ;
        this.identifiersFree.addAll ( this.identifiers [ i ]
            .getIdentifiersFree ( ) ) ;
      }
    }
    return this.identifiersFree ;
  }


  /**
   * Returns the indices of the child {@link Identifier}s.
   * 
   * @return The indices of the child {@link Identifier}s.
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return this.indicesId ;
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
    commands.add ( new DefaultLatexCommand ( LATEX_DUPLICATION , 1 ,
        "\\{<#1>\\}" , "a1 = e1 ; ... ; an = en" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    for ( Identifier id : this.identifiers )
    {
      for ( LatexCommand command : id.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    for ( Expression child : this.expressions )
    {
      for ( LatexCommand command : child.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns the {@link Identifier}s and {@link Expression}s in the right
   * sorting.
   * 
   * @return The {@link Identifier}s and {@link Expression}s in the right
   *         sorting.
   * @see SortedChildren#getSortedChildren()
   */
  public ExpressionOrType [ ] getSortedChildren ( )
  {
    ExpressionOrType [ ] result = new ExpressionOrType [ this.identifiers.length
        + this.expressions.length ] ;
    for ( int i = 0 ; i < this.identifiers.length + this.expressions.length ; i ++ )
    {
      if ( i % 2 == 0 )
      {
        result [ i ] = this.identifiers [ i / 2 ] ;
      }
      else
      {
        result [ i ] = this.expressions [ i / 2 ] ;
      }
    }
    return result ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Expression substitute ( Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    if ( ( Identifier.Set.SELF.equals ( pId.getSet ( ) ) )
        && ( pExpression instanceof ObjectExpr ) )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pExpression ;
      Row row = objectExpr.getRow ( ) ;
      Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
      BoundRenaming < Identifier > boundRenaming = new BoundRenaming < Identifier > ( ) ;
      boundRenaming.add ( row.getIdentifiersFree ( ) ) ;
      for ( Expression e : this.expressions )
      {
        boundRenaming.add ( e.getIdentifiersFree ( ) ) ;
      }
      for ( Identifier id : this.identifiers )
      {
        boundRenaming.add ( id ) ;
      }
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        Identifier newId = boundRenaming
            .newIdentifier ( this.identifiers [ i ] ) ;
        boundRenaming.add ( newId ) ;
        newIdentifiers [ i ] = newId ;
        newIdentifiers [ i ].setSet ( Identifier.Set.VARIABLE ) ;
      }
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        row = row
            .substituteRow ( this.identifiers [ i ] , newIdentifiers [ i ] ) ;
      }
      Expression result = new ObjectExpr ( objectExpr.getId ( ) , objectExpr
          .getTau ( ) , row ) ;
      for ( int i = this.expressions.length - 1 ; i >= 0 ; i -- )
      {
        result = new Let ( newIdentifiers [ i ].clone ( ) , null ,
            this.expressions [ i ].substitute ( pId , pExpression ) , result ) ;
      }
      return result ;
    }
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ].substitute ( pId ,
          pExpression ) ;
    }
    return new Duplication ( this.identifiers , newExpressions ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Duplication substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] newExpressions = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      newExpressions [ i ] = this.expressions [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new Duplication ( this.identifiers , newExpressions ) ;
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
          PRIO_DUPLICATION , LATEX_DUPLICATION ) ;
      this.latexStringBuilder.addBuilderBegin ( ) ;
      this.latexStringBuilder.addText ( LATEX_SPACE ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.latexStringBuilder.addBuilder ( this.identifiers [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory ) , PRIO_ID ) ;
        this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        this.latexStringBuilder.addText ( LATEX_EQUAL ) ;
        this.latexStringBuilder.addText ( LATEX_SPACE ) ;
        this.latexStringBuilder.addBuilder ( this.expressions [ i ]
            .toLatexStringBuilder ( pLatexStringBuilderFactory ) ,
            PRIO_DUPLICATION_E ) ;
        if ( i != this.expressions.length - 1 )
        {
          this.latexStringBuilder.addText ( LATEX_SEMI ) ;
          this.latexStringBuilder.addText ( LATEX_SPACE ) ;
          this.latexStringBuilder.addCanBreakHere ( ) ;
        }
      }
      // Only one space for '{< >}'
      if ( this.expressions.length > 0 )
      {
        this.latexStringBuilder.addText ( LATEX_SPACE ) ;
      }
      this.latexStringBuilder.addBuilderEnd ( ) ;
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
          PRIO_DUPLICATION ) ;
      this.prettyStringBuilder.addText ( PRETTY_DUPLBEGIN ) ;
      this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      for ( int i = 0 ; i < this.expressions.length ; i ++ )
      {
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addText ( PRETTY_EQUAL ) ;
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
        this.prettyStringBuilder.addBuilder ( this.expressions [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_DUPLICATION_E ) ;
        if ( i != this.expressions.length - 1 )
        {
          this.prettyStringBuilder.addText ( PRETTY_SEMI ) ;
          this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
          this.prettyStringBuilder.addBreak ( ) ;
        }
      }
      // Only one space for '{< >}'
      if ( this.expressions.length > 0 )
      {
        this.prettyStringBuilder.addText ( PRETTY_SPACE ) ;
      }
      this.prettyStringBuilder.addText ( PRETTY_DUPLEND ) ;
    }
    return this.prettyStringBuilder ;
  }
}
