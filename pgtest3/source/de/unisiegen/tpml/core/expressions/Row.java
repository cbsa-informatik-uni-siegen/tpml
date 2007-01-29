package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Row extends Expression
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
   * @param pExpressions TODO
   */
  public Row ( Expression [ ] pExpressions )
  {
    if ( pExpressions == null )
    {
      throw new NullPointerException ( "Expressions is null" ) ; //$NON-NLS-1$
    }
    this.expressions = pExpressions ;
    for ( Expression e : this.expressions )
    {
      if ( e instanceof Attr )
      {
        ( ( Attr ) e ).parentRow ( this ) ;
      }
      if ( e instanceof Meth )
      {
        ( ( Meth ) e ).parentRow ( this ) ;
      }
      if ( e instanceof CurriedMeth )
      {
        ( ( CurriedMeth ) e ).parentRow ( this ) ;
      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row clone ( )
  {
    return new Row ( this.expressions.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Row )
    {
      Row other = ( Row ) pObject ;
      return this.expressions.equals ( other.expressions ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Row" ; //$NON-NLS-1$
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
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.expressions.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    for ( Expression e : this.expressions )
    {
      if ( ( e instanceof Attr ) && ( ! ( ( Attr ) e ).isValue ( ) ) )
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
  public Expression substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = this.expressions.clone ( ) ;
    for ( int i = 0 ; i < tmp.length ; i ++ )
    {
      Expression e = tmp [ i ] ;
      if ( e instanceof Attr )
      {
        // TODO "with a new name ... not only append '"
        Attr attr = ( Attr ) e ;
        // first case id == id'
        if ( attr.getIdentifier ( ).equals ( pID ) )
        {
          tmp [ i ] = new Attr ( attr.getIdentifier ( ) , attr.getE ( )
              .substitute ( pID , pExpression ) ) ;
          break ;
        }
        // second case id != id'
        tmp [ i ] = new Attr ( attr.getIdentifier ( ) + "'" , attr.getE ( ) //$NON-NLS-1$
            .substitute ( pID , pExpression ) ) ;
        for ( int j = i + 1 ; j < tmp.length ; j ++ )
        {
          tmp [ j ] = tmp [ j ].substitute ( attr.getIdentifier ( ) ,
              new Identifier ( attr.getIdentifier ( ) + "'" ) ) ; //$NON-NLS-1$
        }
      }
      else if ( e instanceof Meth )
      {
        Meth meth = ( Meth ) e ;
        tmp [ i ] = new Meth ( meth.getIdentifier ( ) , meth.getE ( )
            .substitute ( pID , pExpression ) ) ;
      }
      else if ( e instanceof CurriedMeth )
      {
        CurriedMeth curriedMeth = ( CurriedMeth ) e ;
        tmp [ i ] = new CurriedMeth ( curriedMeth.getIdentifiers ( ) ,
            curriedMeth.getE ( ).substitute ( pID , pExpression ) ) ;
      }
    }
    return new Row ( tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_ROW ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      builder.addBuilder ( this.expressions [ i ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ROW_E ) ;
      if ( i != this.expressions.length - 1 )
      {
        builder.addText ( " " ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
    }
    return builder ;
  }
}
