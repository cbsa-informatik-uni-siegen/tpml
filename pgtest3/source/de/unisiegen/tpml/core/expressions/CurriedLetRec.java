package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.interfaces.BoundedIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Instances of this class represent curried let rec expressions in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see LetRec
 * @see CurriedLet
 */
public final class CurriedLetRec extends CurriedLet implements
    BoundedIdentifiers , DefaultTypes , ChildrenExpressions
{
  /**
   * Allocates a new <code>CurriedLetRec</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link CurriedLet#getTypes()} for an extensive description.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> does not
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   * @see CurriedLet#CurriedLet(Identifier[], MonoType[], Expression,
   *      Expression)
   */
  public CurriedLetRec ( final Identifier [ ] pIdentifiers ,
      final MonoType [ ] pTypes , final Expression pExpression1 ,
      final Expression pExpression2 )
  {
    super ( pIdentifiers , pTypes , pExpression1 , pExpression2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#clone()
   */
  @ Override
  public CurriedLetRec clone ( )
  {
    final Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    final MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedLetRec ( newIdentifiers , newTypes ,
        this.expressions [ 0 ].clone ( ) , this.expressions [ 1 ].clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#free()
   */
  @ Override
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      final ArrayList < Identifier > freeE1 = new ArrayList < Identifier > ( ) ;
      final ArrayList < Identifier > freeE2 = new ArrayList < Identifier > ( ) ;
      freeE1.addAll ( this.expressions [ 0 ].free ( ) ) ;
      for ( Identifier element : this.identifiers )
      {
        while ( freeE1.remove ( element ) )
        {
          // Remove all Identifiers with the same name
        }
      }
      freeE2.addAll ( this.expressions [ 1 ].free ( ) ) ;
      while ( freeE2.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
      this.free.addAll ( freeE1 ) ;
      this.free.addAll ( freeE2 ) ;
    }
    return this.free ;
  }


  /**
   * Returns a list of lists of in this {@link Expression} bounded
   * {@link Identifier}s.
   * 
   * @return A list of lists of in this {@link Expression} bounded
   *         {@link Identifier}s.
   */
  @ Override
  public ArrayList < ArrayList < Identifier >> getBoundedIdentifiers ( )
  {
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      final ArrayList < Identifier > boundedE1 = this.expressions [ 0 ].free ( ) ;
      final ArrayList < Identifier > boundedE2 = this.expressions [ 1 ].free ( ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        if ( i == 0 )
        {
          /*
           * Check, if the bindings should be searched in E1 and E2 or only in
           * E2. Example: let x x = x in x.
           */
          boolean searchInE1 = true ;
          for ( int j = 1 ; j < this.identifiers.length ; j ++ )
          {
            if ( this.identifiers [ 0 ].equals ( this.identifiers [ j ] ) )
            {
              searchInE1 = false ;
              break ;
            }
          }
          final ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
          if ( searchInE1 )
          {
            for ( final Identifier freeId : boundedE1 )
            {
              if ( this.identifiers [ i ].equals ( freeId ) )
              {
                freeId.setBoundedToExpression ( this ) ;
                freeId.setBoundedToIdentifier ( this.identifiers [ i ] ) ;
                boundedIdList.add ( freeId ) ;
              }
            }
          }
          for ( final Identifier freeId : boundedE2 )
          {
            if ( this.identifiers [ i ].equals ( freeId ) )
            {
              freeId.setBoundedToExpression ( this ) ;
              freeId.setBoundedToIdentifier ( this.identifiers [ i ] ) ;
              boundedIdList.add ( freeId ) ;
            }
          }
          this.boundedIdentifiers.add ( boundedIdList ) ;
        }
        else
        {
          /*
           * An Identifier has no binding, if an Identifier after him has the
           * same name. Example: let rec f x x = x + 1 in f 1 2.
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
          final ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
          if ( hasBinding )
          {
            for ( final Identifier freeId : boundedE1 )
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
   * Returns the <code>pIndex</code>th list of in this {@link Expression}
   * bounded {@link Identifier}s.
   * 
   * @param pIndex The index of the list of {@link Identifier}s to return.
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
  @ Override
  public ArrayList < Identifier > getBoundedIdentifiers ( final int pIndex )
  {
    if ( this.boundedIdentifiers == null )
    {
      return this.getBoundedIdentifiers ( ).get ( pIndex ) ;
    }
    return this.boundedIdentifiers.get ( pIndex ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Let-Rec" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public CurriedLetRec substitute ( final Identifier pId ,
      final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public CurriedLetRec substitute ( final Identifier pId ,
      final Expression pExpression , final boolean pAttributeRename )
  {
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this.clone ( ) ;
    }
    final Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    Expression newE1 = this.expressions [ 0 ].clone ( ) ;
    Expression newE2 = this.expressions [ 1 ] ;
    boolean sameIdAs0 = false ;
    boolean substInE1 = true ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        substInE1 = false ;
        break ;
      }
    }
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( this.identifiers [ 0 ] ) )
      {
        sameIdAs0 = true ;
        break ;
      }
    }
    if ( substInE1 )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        final BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.expressions [ 0 ].free ( ) ) ;
        boundRenaming.remove ( newIdentifiers [ i ] ) ;
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
         * name. For example: "let a = b in let rec f b b = b a in f".
         */
        for ( int j = 1 ; j < i ; j ++ )
        {
          if ( this.identifiers [ i ].equals ( this.identifiers [ j ] ) )
          {
            newId = newIdentifiers [ j ] ;
          }
        }
        /*
         * Substitute the old Identifier only with the new Identifier, if they
         * are different.
         */
        if ( ! newIdentifiers [ i ].equals ( newId ) )
        {
          newE1 = newE1.substitute ( newIdentifiers [ i ] , newId ,
              pAttributeRename ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
    }
    final BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.free ( ) ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    if ( ! sameIdAs0 )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        boundRenaming.add ( this.identifiers [ i ] ) ;
      }
    }
    final Identifier newId = boundRenaming.newId ( this.identifiers [ 0 ] ) ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      if ( ! sameIdAs0 )
      {
        newE1 = newE1.substitute ( this.identifiers [ 0 ] , newId ,
            pAttributeRename ) ;
        newIdentifiers [ 0 ] = newId ;
      }
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , newId ,
          pAttributeRename ) ;
      newIdentifiers [ 0 ] = newId ;
    }
    if ( ( ! sameIdAs0 ) || ( substInE1 ) )
    {
      newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    /*
     * Perform the substitution in e2.
     */
    newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    final MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .clone ( ) ;
    }
    return new CurriedLetRec ( newIdentifiers , newTypes , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(TypeSubstitution)
   */
  @ Override
  public CurriedLetRec substitute ( final TypeSubstitution pTypeSubstitution )
  {
    final Identifier [ ] newIdentifiers = new Identifier [ this.identifiers.length ] ;
    for ( int i = 0 ; i < newIdentifiers.length ; i ++ )
    {
      newIdentifiers [ i ] = this.identifiers [ i ].clone ( ) ;
    }
    final MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    return new CurriedLetRec ( newIdentifiers , newTypes ,
        this.expressions [ 0 ].substitute ( pTypeSubstitution ) ,
        this.expressions [ 1 ].substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "rec" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_ID ) ;
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
      {
        this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ;//$NON-NLS-1$
        }
        this.prettyStringBuilder.addBuilder ( this.identifiers [ i ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PrettyPrintPriorities.PRIO_ID ) ;
        if ( this.types [ i ] != null )
        {
          this.prettyStringBuilder.addText ( ": " ) ;//$NON-NLS-1$
          this.prettyStringBuilder.addBuilder ( this.types [ i ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PrettyPrintPriorities.PRIO_LET_TAU ) ;
          this.prettyStringBuilder.addText ( ")" ) ;//$NON-NLS-1$
        }
      }
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ;//$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PrettyPrintPriorities.PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 1 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
