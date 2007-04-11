package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import java.util.Arrays ;
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
  private Identifier [ ] identifiers ;


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
  public CurriedMethod ( Identifier [ ] pIdentifiers , MonoType [ ] pTypes ,
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
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedMethod ( newIdentifiers , newTypes , this.e.clone ( ) ) ;
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
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.addAll ( this.e.free ( ) ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        while ( this.free.remove ( this.identifiers [ i ] ) )
        {
          // Remove all Identifiers with the same name
        }
      }
    }
    return this.free ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  @ Override
  public ArrayList < ArrayList < Identifier >> getBoundedIdentifiers ( )
  {
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundedE = this.e.free ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        if ( i == 0 )
        {
          this.boundedIdentifiers.add ( new ArrayList < Identifier > ( ) ) ;
        }
        else
        {
          /*
           * An Identifier has no binding, if an Identifier after him has the
           * same name. Example: object method add x x = x ; end.
           */
          boolean hasBinding = true ;
          for ( int j = i + 1 ; j < this.identifiers.length ; j ++ )
          {
            if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
            {
              hasBinding = false ;
              break ;
            }
          }
          ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
          if ( hasBinding )
          {
            for ( Identifier freeId : boundedE )
            {
              if ( this.identifiers [ i ].equals ( freeId ) )
              {
                freeId.setBoundedToExpression ( this ) ;
                freeId.setBoundedToIdentifier ( this.identifiers [ i ] ) ;
                boundedIdList.add ( freeId ) ;
              }
            }
          }
          this.boundedIdentifiers.add ( boundedIdList ) ;
        }
      }
    }
    return this.boundedIdentifiers ;
  }


  /**
   * TODO
   * 
   * @param pIndex TODO
   * @return TODO
   */
  @ Override
  public ArrayList < Identifier > getBoundedIdentifiers ( int pIndex )
  {
    if ( this.boundedIdentifiers == null )
    {
      return getBoundedIdentifiers ( ).get ( pIndex ) ;
    }
    return this.boundedIdentifiers.get ( pIndex ) ;
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
  public Identifier [ ] getIdentifiers ( )
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
  public Identifier getIdentifiers ( int pIndex )
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
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public CurriedMethod substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return this.clone ( ) ;
      }
    }
    Expression newE = this.e ;
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.free ( ) ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
      boundRenaming.add ( pId ) ;
      /*
       * The new Identifier should not be equal to an other Identifier.
       */
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
      Identifier newId = boundRenaming.newId ( newIdentifiers [ i ] ) ;
      /*
       * Search for an Identifier before the current Identifier with the same
       * name. For example: "let a = b in object (self) method add b b = b a ;
       * end".
       */
      for ( int j = 1 ; j < i ; j ++ )
      {
        if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
        {
          newId = newIdentifiers [ j ] ;
        }
      }
      /*
       * Substitute the old Identifier only with the new Identifier, if they are
       * different.
       */
      if ( ! newIdentifiers [ i ].equals ( newId ) )
      {
        newE = newE.substitute ( newIdentifiers [ i ] , newId ,
            pAttributeRename ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedMethod ( newIdentifiers , newTypes , newE ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public CurriedMethod substitute ( TypeSubstitution pTypeSubstitution )
  {
    Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new CurriedMethod ( newIdentifiers , newTypes , this.e
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
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
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
