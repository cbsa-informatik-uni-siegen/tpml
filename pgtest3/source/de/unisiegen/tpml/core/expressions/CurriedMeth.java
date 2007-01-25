package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


public class CurriedMeth extends Expression
{
  private String [ ] identifiers ;


  private Expression expression ;


  private Row parentRow ;


  public CurriedMeth ( String [ ] pIdentifiers , Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ;
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "expression is null" ) ;
    }
    if ( pIdentifiers.length < 2 )
    {
      throw new IllegalArgumentException (
          "identifiers must contain atleast two items" ) ;
    }
    this.identifiers = pIdentifiers ;
    this.expression = pExpression ;
  }


  public void parentRow ( Row pRow )
  {
    this.parentRow = pRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Method" ; //$NON-NLS-1$
  }


  public Expression getE ( )
  {
    return this.expression ;
  }


  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.expression.free ( ) ) ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      free.remove ( this.identifiers [ i ] ) ;
    }
    return free ;
  }


  @ Override
  public CurriedMeth clone ( )
  {
    return new CurriedMeth ( this.identifiers.clone ( ) , this.expression
        .clone ( ) ) ;
  }


  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof CurriedMeth )
    {
      CurriedMeth other = ( CurriedMeth ) pObject ;
      return ( ( this.identifiers.equals ( other.identifiers ) ) && ( this.expression
          .equals ( other.expression ) ) ) ;
    }
    return false ;
  }


  @ Override
  public boolean isValue ( )
  {
    return this.expression.isValue ( ) ;
  }


  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expression.hashCode ( ) ;
  }


  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    return new CurriedMeth ( this.identifiers , this.expression.substitute (
        pID , pExpression ) ) ;
  }


  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_CURRIED_METH ) ;
    builder.addKeyword ( "meth" ) ;
    for ( String id : this.identifiers )
    {
      builder.addText ( " " ) ;
      builder.addIdentifier ( id ) ;
    }
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_CURRIED_METH_E ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( ";" ) ;
    return builder ;
  }


  public Row returnParentRow ( )
  {
    return this.parentRow ;
  }
}
