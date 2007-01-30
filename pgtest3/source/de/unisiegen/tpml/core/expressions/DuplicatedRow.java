package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class DuplicatedRow extends Expression
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
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  protected String [ ] identifiers ;


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   * @param pExpressions TODO
   */
  public DuplicatedRow ( String [ ] pIdentifiers , Expression [ ] pExpressions )
  {
    if ( pIdentifiers.length != pExpressions.length )
    {
      throw new IllegalArgumentException (
          "Identifiers length and expression length must be equal" ) ; //$NON-NLS-1$
    }
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
  public DuplicatedRow clone ( )
  {
    Expression [ ] tmpE = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmpE [ i ] = this.expressions [ i ].clone ( ) ;
    }
    return new DuplicatedRow ( this.identifiers , tmpE ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof DuplicatedRow )
    {
      DuplicatedRow other = ( DuplicatedRow ) pObject ;
      return ( this.expressions.equals ( other.expressions ) && this.identifiers
          .equals ( other.identifiers ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Duplicated-Row" ; //$NON-NLS-1$
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
    return this.identifiers.hashCode ( ) + this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ! e.isValue ( ) )
      {
        return false ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public DuplicatedRow substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int n = 0 ; n < tmp.length ; ++ n )
    {
      tmp [ n ] = this.expressions [ n ].substitute ( pID , pExpression ) ;
    }
    return new DuplicatedRow ( this.identifiers , tmp ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public DuplicatedRow substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmp [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return ( this.expressions.equals ( tmp ) ) ? this : new DuplicatedRow (
        this.identifiers , tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_DUPLICATED_ROW ) ;
    builder.addKeyword ( "{" ) ; //$NON-NLS-1$
    builder.addKeyword ( "<" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addIdentifier ( this.identifiers [ i ] ) ;
      builder.addText ( " = " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_DUPLICATED_ROW_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( ", " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
    }
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ">" ) ; //$NON-NLS-1$
    builder.addKeyword ( "}" ) ; //$NON-NLS-1$
    return builder ;
  }
}
