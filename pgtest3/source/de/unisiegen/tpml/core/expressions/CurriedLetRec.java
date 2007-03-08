package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.Free ;


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
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Let-Rec" ; //$NON-NLS-1$
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
    TreeSet < String > freeE2 = new TreeSet < String > ( ) ;
    freeE2.addAll ( this.e2.free ( ) ) ;
    freeE2.remove ( this.identifiers [ 0 ] ) ;
    TreeSet < String > freeE1 = new TreeSet < String > ( ) ;
    freeE1.addAll ( this.e1.free ( ) ) ;
    for ( String id : this.identifiers )
      freeE1.remove ( id ) ;
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( freeE1 ) ;
    free.addAll ( freeE2 ) ;
    return free ;
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
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
      return this ;
    }
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    Expression newE1 = this.e1.clone ( ) ;
    Expression newE2 = this.e2.clone ( ) ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        return new CurriedLetRec ( this.identifiers , this.types , newE1 ,
            newE2.substitute ( pId , pExpression , pAttributeRename ) ) ;
      }
    }
    for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
    {
      Free free = new Free ( ) ;
      free.add ( this.e1.free ( ) ) ;
      free.remove ( newIdentifiers [ i ] ) ;
      free.add ( pExpression.free ( ) ) ;
      free.add ( pId ) ;
      if ( free.contains ( newIdentifiers [ i ] ) )
      {
        for ( int j = 1 ; j < newIdentifiers.length ; j ++ )
        {
          if ( i != j )
          {
            free.add ( this.identifiers [ j ] ) ;
          }
        }
      }
      String newId = free.newIdentifier ( newIdentifiers [ i ] ) ;
      if ( ! newIdentifiers [ i ].equals ( newId ) )
      {
        newE1 = newE1.substitute ( newIdentifiers [ i ] , new Identifier (
            newId ) , pAttributeRename ) ;
        newIdentifiers [ i ] = newId ;
      }
    }
    Free free = new Free ( ) ;
    free.add ( this.free ( ) ) ;
    free.add ( pExpression.free ( ) ) ;
    free.add ( pId ) ;
    String newId = free.newIdentifier ( this.identifiers [ 0 ] ) ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      newE1 = newE1.substitute ( this.identifiers [ 0 ] , new Identifier (
          newId ) , pAttributeRename ) ;
      newE2 = newE2.substitute ( this.identifiers [ 0 ] , new Identifier (
          newId ) , pAttributeRename ) ;
      newIdentifiers [ 0 ] = newId ;
    }
    newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    return new CurriedLetRec ( newIdentifiers , this.types , newE1 , newE2 ) ;
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
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ;//$NON-NLS-1$
    builder.addText ( " " ) ;//$NON-NLS-1$
    builder.addKeyword ( "rec" ) ;//$NON-NLS-1$
    builder.addText ( " " ) ;//$NON-NLS-1$
    builder.addIdentifier ( this.identifiers [ 0 ] ) ;
    for ( int n = 1 ; n < this.identifiers.length ; ++ n )
    {
      builder.addText ( " " ) ;//$NON-NLS-1$
      if ( this.types [ n ] != null )
      {
        builder.addText ( "(" ) ;//$NON-NLS-1$
      }
      builder.addIdentifier ( this.identifiers [ n ] ) ;
      if ( this.types [ n ] != null )
      {
        builder.addText ( ": " ) ;//$NON-NLS-1$
        builder.addBuilder ( this.types [ n ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
        builder.addText ( ")" ) ;//$NON-NLS-1$
      }
    }
    if ( this.types [ 0 ] != null )
    {
      builder.addText ( ": " ) ;//$NON-NLS-1$
      builder
          .addBuilder ( this.types [ 0 ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
    }
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
    builder.addText ( " " ) ;//$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "in" ) ;//$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    return builder ;
  }
}
