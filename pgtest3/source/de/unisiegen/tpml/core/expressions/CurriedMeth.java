package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public class CurriedMeth extends Expression
{
  /**
   * TODO
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private String [ ] identifiers ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @see #parentRow(Row) ;
   * @see #returnParentRow() ;
   */
  private Row parentRow ;


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   * @param pExpression TODO
   */
  public CurriedMeth ( String [ ] pIdentifiers , Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length < 2 )
    {
      throw new IllegalArgumentException (
          "Identifiers must contain at least two items" ) ; //$NON-NLS-1$
    }
    this.identifiers = pIdentifiers ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMeth clone ( )
  {
    return new CurriedMeth ( this.identifiers.clone ( ) , this.expression
        .clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
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


  /**
   * {@inheritDoc}
   */
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


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Method" ; //$NON-NLS-1$
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
   * @param pIndex TODO
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
    return this.identifiers.hashCode ( ) + this.expression.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.expression.isValue ( ) ;
  }


  /**
   * TODO
   * 
   * @param pRow TODO
   * @see #parentRow
   * @see #returnParentRow()
   */
  public void parentRow ( Row pRow )
  {
    this.parentRow = pRow ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #parentRow
   * @see #parentRow(Row)
   */
  public Row returnParentRow ( )
  {
    return this.parentRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Expression substitute ( String pID , Expression pExpression )
  {
    return new CurriedMeth ( this.identifiers , this.expression.substitute (
        pID , pExpression ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_CURRIED_METH ) ;
    builder.addKeyword ( "meth" ) ; //$NON-NLS-1$
    for ( String id : this.identifiers )
    {
      builder.addText ( " " ) ; //$NON-NLS-1$
      builder.addIdentifier ( id ) ;
    }
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_CURRIED_METH_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ";" ) ; //$NON-NLS-1$
    return builder ;
  }
}
