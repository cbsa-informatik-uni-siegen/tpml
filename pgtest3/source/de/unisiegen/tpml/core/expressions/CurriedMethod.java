package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public class CurriedMethod extends Expression
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
   * @see #getTypes()
   * @see #getTypes(int)
   */
  private MonoType [ ] types ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression expression ;


  /**
   * TODO
   * 
   * @param pIdentifiers TODO
   * @param pTypes TODO
   * @param pExpression TODO
   */
  public CurriedMethod ( String [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "Identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( "Types is null" ) ; //$NON-NLS-1$
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
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException (
          "The arity of Identifiers and Types must match" ) ; //$NON-NLS-1$
    }
    this.identifiers = pIdentifiers ;
    this.types = pTypes ;
    this.expression = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod clone ( )
  {
    return new CurriedMethod ( this.identifiers.clone ( ) ,
        this.types.clone ( ) , this.expression.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof CurriedMethod )
    {
      CurriedMethod other = ( CurriedMethod ) pObject ;
      return ( ( this.identifiers.equals ( other.identifiers ) )
          && ( this.types.equals ( other.types ) ) && ( this.expression
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
   * TODO
   * 
   * @return TODO
   * @see #types
   * @see #getTypes(int)
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   * @see #types
   * @see #getTypes()
   */
  public MonoType getTypes ( int pIndex )
  {
    return this.types [ pIndex ] ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.expression.hashCode ( )
        + this.types.hashCode ( ) ;
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
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    return new CurriedMethod ( this.identifiers , this.types , this.expression
        .substitute ( pId , pExpression , pAttributeRename ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new CurriedMethod ( this.identifiers , newTypes , this.expression
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_CURRIED_METHOD ) ;
    builder.addKeyword ( "method" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifiers [ 0 ] ) ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      builder.addText ( " " ) ; //$NON-NLS-1$
      if ( this.types [ i ] != null )
      {
        builder.addText ( "(" ) ; //$NON-NLS-1$
      }
      builder.addIdentifier ( this.identifiers [ i ] ) ;
      if ( this.types [ i ] != null )
      {
        builder.addText ( ": " ) ; //$NON-NLS-1$
        builder.addBuilder ( this.types [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_CURRIED_METHOD_TAU ) ;
        builder.addText ( ")" ) ; //$NON-NLS-1$
      }
    }
    if ( this.types [ 0 ] != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder
          .addBuilder ( this.types [ 0 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
    }
    builder.addText ( " =" ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.expression
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_CURRIED_METHOD_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( ";" ) ; //$NON-NLS-1$
    return builder ;
  }
}
