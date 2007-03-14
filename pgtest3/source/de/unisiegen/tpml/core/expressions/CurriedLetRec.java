package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
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
public final class CurriedLetRec extends CurriedLet
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
   * @see CurriedLet#CurriedLet(String[], MonoType[], Expression, Expression)
   */
  public CurriedLetRec ( String [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression1 , Expression pExpression2 )
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
    return new CurriedLetRec ( this.identifiers , this.types ,
        this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      TreeSet < String > freeE1 = new TreeSet < String > ( ) ;
      TreeSet < String > freeE2 = new TreeSet < String > ( ) ;
      freeE1.addAll ( this.e1.free ( ) ) ;
      for ( int i = 0 ; i < this.identifiers.length ; i ++ )
      {
        freeE1.remove ( this.identifiers [ i ] ) ;
      }
      freeE2.addAll ( this.e2.free ( ) ) ;
      freeE2.remove ( this.identifiers [ 0 ] ) ;
      this.free.addAll ( freeE1 ) ;
      this.free.addAll ( freeE2 ) ;
    }
    return this.free ;
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public CurriedLetRec substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(String, Expression, boolean)
   */
  @ Override
  public CurriedLetRec substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this.clone ( ) ;
    }
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    Expression newE1 = this.e1.clone ( ) ;
    Expression newE2 = this.e2 ;
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
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.e1.free ( ) ) ;
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
        String newId = boundRenaming.newIdentifier ( newIdentifiers [ i ] ) ;
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
          newE1 = newE1.substitute ( newIdentifiers [ i ] , new Identifier (
              newId ) , pAttributeRename ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
    }
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
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
    String newId = boundRenaming.newIdentifier ( this.identifiers [ 0 ] ) ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      if ( ! sameIdAs0 )
      {
        newE1 = newE1.substitute ( this.identifiers [ 0 ] , new Identifier (
            newId ) , pAttributeRename ) ;
        newIdentifiers [ 0 ] = newId ;
      }
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , new Identifier (
          newId ) , pAttributeRename ) ;
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
    return new CurriedLetRec ( newIdentifiers , this.types , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#substitute(TypeSubstitution)
   */
  @ Override
  public CurriedLetRec substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int n = 0 ; n < newTypes.length ; ++ n )
    {
      newTypes [ n ] = ( this.types [ n ] != null ) ? this.types [ n ]
          .substitute ( pTypeSubstitution ) : null ;
    }
    return new CurriedLetRec ( this.identifiers , newTypes , this.e1
        .substitute ( pTypeSubstitution ) , this.e2
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see CurriedLet#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( "rec" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addIdentifier ( this.identifiers [ 0 ] ) ;
      for ( int n = 1 ; n < this.identifiers.length ; ++ n )
      {
        this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
        if ( this.types [ n ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ;//$NON-NLS-1$
        }
        this.prettyStringBuilder.addIdentifier ( this.identifiers [ n ] ) ;
        if ( this.types [ n ] != null )
        {
          this.prettyStringBuilder.addText ( ": " ) ;//$NON-NLS-1$
          this.prettyStringBuilder.addBuilder ( this.types [ n ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
          this.prettyStringBuilder.addText ( ")" ) ;//$NON-NLS-1$
        }
      }
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ;//$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.e1
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ;//$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e2
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
