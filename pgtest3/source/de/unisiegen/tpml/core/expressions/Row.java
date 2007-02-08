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
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    TreeSet < String > bounded = new TreeSet < String > ( ) ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      if ( this.expressions [ i ] instanceof Attr )
      {
        Attr expr = ( Attr ) this.expressions [ i ] ;
        TreeSet < String > freeExpr = new TreeSet < String > ( ) ;
        freeExpr.addAll ( expr.free ( ) ) ;
        freeExpr.removeAll ( bounded ) ;
        free.addAll ( freeExpr ) ;
        bounded.add ( expr.getId ( ) ) ;
      }
      else if ( this.expressions [ i ] instanceof Meth )
      {
        Meth expr = ( Meth ) this.expressions [ i ] ;
        TreeSet < String > freeExpr = new TreeSet < String > ( ) ;
        freeExpr.addAll ( expr.free ( ) ) ;
        freeExpr.removeAll ( bounded ) ;
        free.addAll ( freeExpr ) ;
      }
      else if ( this.expressions [ i ] instanceof CurriedMeth )
      {
        CurriedMeth expr = ( CurriedMeth ) this.expressions [ i ] ;
        TreeSet < String > freeExpr = new TreeSet < String > ( ) ;
        freeExpr.addAll ( expr.free ( ) ) ;
        freeExpr.removeAll ( bounded ) ;
        free.addAll ( freeExpr ) ;
      }
    }
    return free ;
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
    ArrayList < String > attrNames = new ArrayList < String > ( ) ;
    for ( Expression e : this.expressions )
    {
      if ( e instanceof Attr )
      {
        Attr attr = ( Attr ) e ;
        if ( ! attr.isValue ( ) )
        {
          return false ;
        }
        if ( attrNames.contains ( attr.getId ( ) ) )
        {
          return false ;
        }
        attrNames.add ( attr.getId ( ) ) ;
      }
    }
    return true ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Row substitute ( String pID , Expression pExpression )
  {
    Expression [ ] tmp = this.expressions.clone ( ) ;
    for ( int i = 0 ; i < tmp.length ; i ++ )
    {
      Expression e = tmp [ i ] ;
      if ( e instanceof Attr )
      {
        Attr attr = ( Attr ) e ;
        // first case id == id'
        if ( attr.getId ( ).equals ( pID ) )
        {
          tmp [ i ] = new Attr ( attr.getId ( ) , attr.getTau ( ) , attr
              .getE ( ).substitute ( pID , pExpression ) ) ;
          break ;
        }
        // second case id != id'
        TreeSet < String > freeRow = new TreeSet < String > ( ) ;
        TreeSet < String > freeE = new TreeSet < String > ( ) ;
        freeE.addAll ( pExpression.free ( ) ) ;
        for ( int j = i + 1 ; j < tmp.length ; j ++ )
        {
          Set < String > freeRowE = tmp [ j ].free ( ) ;
          freeRow.addAll ( freeRowE ) ;
        }
        String newId = attr.getId ( ) ;
        while ( ( freeE.contains ( newId ) ) && ( freeRow.contains ( pID ) ) )
        {
          newId = newId + "'" ; //$NON-NLS-1$
        }
        tmp [ i ] = new Attr ( newId , attr.getTau ( ) , attr.getE ( )
            .substitute ( pID , pExpression ) ) ;
        if ( ! attr.getId ( ).equals ( newId ) )
        {
          for ( int j = i + 1 ; j < tmp.length ; j ++ )
          {
            tmp [ j ] = tmp [ j ].substitute ( attr.getId ( ) , new Identifier (
                newId ) ) ;
          }
        }
      }
      else if ( e instanceof Meth )
      {
        Meth meth = ( Meth ) e ;
        tmp [ i ] = new Meth ( meth.getId ( ) , meth.getTau ( ) , meth.getE ( )
            .substitute ( pID , pExpression ) ) ;
      }
      else if ( e instanceof CurriedMeth )
      {
        CurriedMeth curriedMeth = ( CurriedMeth ) e ;
        tmp [ i ] = new CurriedMeth ( curriedMeth.getIdentifiers ( ) ,
            curriedMeth.getTypes ( ) , curriedMeth.getE ( ).substitute ( pID ,
                pExpression ) ) ;
      }
    }
    return new Row ( tmp ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Row substitute ( TypeSubstitution pTypeSubstitution )
  {
    Expression [ ] tmp = new Expression [ this.expressions.length ] ;
    for ( int i = 0 ; i < this.expressions.length ; i ++ )
    {
      tmp [ i ] = this.expressions [ i ].substitute ( pTypeSubstitution ) ;
    }
    return ( this.expressions.equals ( tmp ) ) ? this : new Row ( tmp ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    /*
     * System.out.println ( "Free Row:" ) ; for ( String s : free ( ) ) {
     * System.out.print ( s + " " ) ; } System.out.println ( ) ;
     */
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
