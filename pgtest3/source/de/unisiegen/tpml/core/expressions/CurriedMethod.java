package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public final class CurriedMethod extends Expression
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
  private Expression e ;


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
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod clone ( )
  {
    return new CurriedMethod ( this.identifiers , this.types , this.e.clone ( ) ) ;
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
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( Arrays.equals ( this.types , other.types ) ) && ( this.e
          .equals ( other.e ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.addAll ( this.e.free ( ) ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.free.remove ( this.identifiers [ i ] ) ;
      }
    }
    return this.free ;
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
   * @see #e
   */
  public Expression getE ( )
  {
    return this.e ;
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
    return this.identifiers.hashCode ( ) + this.e.hashCode ( )
        + this.types.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.e.isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public CurriedMethod substitute ( String pId , Expression pExpression )
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
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this.clone ( ) ;
      }
    }
    Expression newE = this.e ;
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    if ( this.e.free ( ).contains ( pId ) )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.free ( ) ) ;
        boundRenaming.add ( pExpression.free ( ) ) ;
        boundRenaming.add ( pId ) ;
        if ( boundRenaming.contains ( newIdentifiers [ i ] ) )
        {
          for ( int j = 1 ; j < newIdentifiers.length ; j ++ )
          {
            if ( i != j )
            {
              boundRenaming.add ( newIdentifiers [ j ] ) ;
            }
          }
        }
        String newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] ) ;
        for ( int j = 1 ; j < i ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ] ;
          }
        }
        if ( ! newIdentifiers [ i ].equals ( newId ) )
        {
          newE = newE.substitute ( newIdentifiers [ i ] , new Identifier (
              newId ) , pAttributeRename ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
    }
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    return new CurriedMethod ( newIdentifiers , this.types , newE ) ;
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
    return new CurriedMethod ( this.identifiers , newTypes , this.e
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CURRIED_METHOD ) ;
      this.prettyStringBuilder.addKeyword ( "method" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addIdentifier ( this.identifiers [ 0 ] ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addIdentifier ( this.identifiers [ i ] ) ;
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBuilder ( this.types [ i ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_CURRIED_METHOD_TAU ) ;
          this.prettyStringBuilder.addText ( ")" ) ; //$NON-NLS-1$
        }
      }
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_CURRIED_METHOD_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
