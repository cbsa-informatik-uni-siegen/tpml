package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Instances of this class represent curried let rec expressions in the
 * expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.LetRec
 * @see de.unisiegen.tpml.core.expressions.CurriedLet
 */
public final class CurriedLetRec extends CurriedLet
{
  //
  // Constructor
  //
  /**
   * Allocates a new <code>CurriedLetRec</code> instance.
   * 
   * @param identifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param types the types for the <code>identifiers</code>, see
   *          {@link CurriedLet#getTypes()} for an extensive description.
   * @param e1 the function body.
   * @param e2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> does not
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   * @see CurriedLet#CurriedLet(String[], MonoType[], Expression, Expression)
   */
  public CurriedLetRec ( String [ ] identifiers , MonoType [ ] types ,
      Expression e1 , Expression e2 )
  {
    super ( identifiers , types , e1 , e2 ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Let-Rec" ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.CurriedLet#clone()
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
   * @see de.unisiegen.tpml.core.expressions.CurriedLet#free()
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
   * @see de.unisiegen.tpml.core.expressions.CurriedLet#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public CurriedLetRec substitute ( TypeSubstitution substitution )
  {
    MonoType [ ] types = new MonoType [ this.types.length ] ;
    for ( int n = 0 ; n < types.length ; ++ n )
    {
      types [ n ] = ( this.types [ n ] != null ) ? this.types [ n ]
          .substitute ( substitution ) : null ;
    }
    return new CurriedLetRec ( this.identifiers , types , this.e1
        .substitute ( substitution ) , this.e2.substitute ( substitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.CurriedLet#substitute(java.lang.String,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public CurriedLetRec substitute ( String id , Expression e )
  {
    // determine the expressions and the identifiers
    String [ ] identifiers = this.identifiers ;
    Expression e1 = this.e1 ;
    Expression e2 = this.e2 ;
    // check if we can substitute below e1
    if ( ! Arrays.asList ( identifiers ).contains ( id ) )
    {
      // bound rename for substituting e in e1
      identifiers = identifiers.clone ( ) ;
      Set < String > freeE = e.free ( ) ;
      for ( int n = 0 ; n < identifiers.length ; ++ n )
      {
        // generate a new unique identifier
        while ( freeE.contains ( identifiers [ n ] ) )
          identifiers [ n ] = identifiers [ n ] + "'" ;
        // perform the bound renaming
        e1 = e1.substitute ( this.identifiers [ n ] , new Identifier (
            identifiers [ n ] ) ) ;
      }
      // substitute in e1 if
      e1 = e1.substitute ( id , e ) ;
    }
    // substitute e2 if id is not bound in e2
    if ( ! this.identifiers [ 0 ].equals ( id ) )
      e2 = e2.substitute ( id , e ) ;
    // generate the new expression
    return new CurriedLetRec ( identifiers , this.types , e1 , e2 ) ;
  }


  //
  // Pretty printing
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.CurriedLet#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "rec" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.identifiers [ 0 ] ) ;
    for ( int n = 1 ; n < this.identifiers.length ; ++ n )
    {
      builder.addText ( " " ) ;
      if ( this.types [ n ] != null )
      {
        builder.addText ( "(" ) ;
      }
      builder.addIdentifier ( this.identifiers [ n ] ) ;
      if ( this.types [ n ] != null )
      {
        builder.addText ( ": " ) ;
        builder.addBuilder (
            this.types [ n ].toPrettyStringBuilder ( factory ) , PRIO_LET_TAU ) ;
        builder.addText ( ")" ) ;
      }
    }
    if ( this.types [ 0 ] != null )
    {
      builder.addText ( ": " ) ;
      builder.addBuilder ( this.types [ 0 ].toPrettyStringBuilder ( factory ) ,
          PRIO_LET_TAU ) ;
    }
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.e1.toPrettyStringBuilder ( factory ) ,
        PRIO_LET_E1 ) ;
    builder.addBreak ( ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "in" ) ;
    builder.addText ( " " ) ;
    builder.addBuilder ( this.e2.toPrettyStringBuilder ( factory ) ,
        PRIO_LET_E2 ) ;
    return builder ;
  }
}
