package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Duplication extends Expression
{
  /**
   * TODO
   * 
   * @see #getExpressions()
   * @see #getExpressions(int)
   */
  private Expression [ ] expressions ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  protected String [ ] identifiers ;


  /**
   * TODO
   * 
   * @param pExpression TODO
   * @param pIdentifiers TODO
   * @param pExpressions TODO
   */
  public Duplication ( Expression pExpression , String [ ] pIdentifiers ,
      Expression [ ] pExpressions )
  {
    if ( pIdentifiers.length != pExpressions.length )
    {
      throw new IllegalArgumentException (
          "Identifiers and expression length must be equal" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    /*
     * Delete all Expressions with the same Identifier apart from the last.
     * Example: "self {< a = 1 ; a = 2 ; a = 3 ; b = 4 >}" -> "self {< a = 3 ;
     * b = 4 >}"
     */
    ArrayList < String > id = new ArrayList < String > ( ) ;
    ArrayList < Expression > expr = new ArrayList < Expression > ( ) ;
    for ( int i = pIdentifiers.length - 1 ; i >= 0 ; i -- )
    {
      if ( ! id.contains ( pIdentifiers [ i ] ) )
      {
        id.add ( 0 , pIdentifiers [ i ] ) ;
        expr.add ( 0 , pExpressions [ i ] ) ;
      }
    }
    this.expression = pExpression ;
    this.identifiers = new String [ id.size ( ) ] ;
    this.expressions = new Expression [ expr.size ( ) ] ;
    for ( int i = 0 ; i < id.size ( ) ; i ++ )
    {
      this.identifiers [ i ] = id.get ( i ) ;
      this.expressions [ i ] = expr.get ( i ) ;
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication clone ( )
  {
    Expression [ ] tmpE = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmpE [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new Duplication ( this.expression.clone ( ) , this.identifiers ,
        tmpE ) ;
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
      return ( ( this.expression.equals ( other.expression ) )
          && ( this.expressions.equals ( other.expressions ) ) && ( this.identifiers
          .equals ( other.identifiers ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.expression.free ( ) ) ;
    for ( Expression expr : this.expressions )
    {
      free.addAll ( expr.free ( ) ) ;
    }
    return free ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Duplication" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expression
   */
  public Expression getE ( )
  {
    return this.expression ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #expressions
   * @see #getExpressions(int)
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   * @see #expressions
   * @see #getExpressions()
   */
  public Expression getExpressions ( int pIndex )
  {
    return this.expressions [ pIndex ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #identifiers
   * @see #getIdentifiers(int)
   */
  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * TODO
   * 
   * @param pIndex
   * @return TODO
   * @see #identifiers
   * @see #getIdentifiers()
   */
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expression.hashCode ( ) + this.identifiers.hashCode ( )
        + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    // TODO Problems with SmallSteps
    for ( Expression e : this.expressions )
    {
      if ( ! e.isValue ( ) )
      {
        return false ;
      }
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmp.length ; ++ n )
    {
      tmp [ n ] = this.expressions [ n ].substitute ( pID , pExpression ) ;
    }
    return new Duplication ( this.expression.substitute ( pID , pExpression ) ,
        this.identifiers , tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Duplication substituteAttr ( String pID , Expression pExpression )
  {
    Expression [ ] newExpr = new Expression [ this.expressions.length ] ;
    String [ ] newId = this.identifiers.clone ( ) ;
    for ( int i = 0 ; i < newExpr.length ; i ++ )
    {
      if ( ( this.identifiers [ i ].equals ( pID ) )
          && ( pExpression instanceof Identifier ) )
      {
        newId [ i ] = ( ( Identifier ) pExpression ).getName ( ) ;
      }
      newExpr [ i ] = this.expressions [ i ].substitute ( pID , pExpression ) ;
    }
    return new Duplication ( this.expression.substitute ( pID , pExpression ) ,
        newId , newExpr ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Duplication substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmp [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return ( this.expressions.equals ( tmp ) ) ? this : new Duplication (
        this.expression.clone ( ) , this.identifiers , tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_DUPLICATION ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_DUPLICATION_FIRST_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( "{" ) ; //$NON-NLS-1$
    builder.addKeyword ( "<" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addIdentifier ( this.identifiers [ i ] ) ;
      builder.addText ( " = " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_DUPLICATION_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( "; " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
    }
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ">" ) ; //$NON-NLS-1$
    builder.addKeyword ( "}" ) ; //$NON-NLS-1$
    return builder ;
  }
}
