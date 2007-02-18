package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Instances of this class represents curried let expressions, which are
 * syntactic sugar for {@link Let} and {@link Lambda}.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1080 $
 * @see Let
 * @see Lambda
 */
public class CurriedLet extends Expression
{
  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  protected String [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   * @see #getTypes(int)
   */
  protected MonoType [ ] types ;


  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  protected Expression e1 ;


  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  protected Expression e2 ;


  /**
   * Allocates a new <code>CurriedLet</code> instance.
   * 
   * @param pIdentifiers an array with atleast two identifiers, where the first
   *          identifier is the name to use for the function and the remaining
   *          identifiers specify the parameters for the function.
   * @param pTypes the types for the <code>identifiers</code>, see
   *          {@link #getTypes()} for an extensive description of the meaning of
   *          <code>types</code>.
   * @param pExpression1 the function body.
   * @param pExpression2 the second expression.
   * @throws IllegalArgumentException if the <code>identifiers</code> array
   *           contains less than two identifiers, or the arity of
   *           <code>identifiers</code> and <code>types</code> doesn't
   *           match.
   * @throws NullPointerException if <code>identifiers</code>,
   *           <code>types</code>, <code>e1</code> or <code>e2</code> is
   *           <code>null</code>.
   */
  public CurriedLet ( String [ ] pIdentifiers , MonoType [ ] pTypes ,
      Expression pExpression1 , Expression pExpression2 )
  {
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    if ( pTypes == null )
    {
      throw new NullPointerException ( "types is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length < 2 )
    {
      throw new IllegalArgumentException (
          "identifiers must contain at least two items" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length != pTypes.length )
    {
      throw new IllegalArgumentException (
          "the arity of identifiers and types must match" ) ; //$NON-NLS-1$
    }
    this.identifiers = pIdentifiers ;
    this.types = pTypes ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Curried-Let" ; //$NON-NLS-1$
  }


  /**
   * Returns the identifiers, where the first identifier is the name of the
   * function and the remaining identifiers name the parameters of the
   * functions, in a curried fashion.
   * 
   * @return the identifiers.
   */
  public String [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns the <code>n</code>th identifier.
   * 
   * @param pIndex the index of the identifier to return.
   * @return the <code>n</code>th identifier.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is invalid.
   */
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * Returns the types for the <code>identifiers</code>.
   * <code>let id (id1:tau1)...(idn:taun): tau = e1 in e2</code> is translated
   * to <code>let id = lambda id1:tau1...lambda idn:taun.e1 in e2</code> which
   * means <code>types[0]</code> is used for <code>identifiers[0]</code> and
   * so on (where <code>types[0]</code> corresponds to <code>tau</code> in
   * the example above, and <code>identifiers[0]</code> to <code>id</code>).
   * Any of the types may be null, in which case the type will be inferred in
   * the type checker, while the <code>types</code> itself may not be
   * <code>null</code>. For recursion (see {@link CurriedLetRec}) the
   * <code>tau</code> is used to build the type of the recursive identifier.
   * 
   * @return the types.
   * @see #getTypes(int)
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * Returns the <code>n</code>th type.
   * 
   * @param pIndex the index of the type.
   * @return the <code>n</code>th type.
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getTypes()
   */
  public MonoType getTypes ( int pIndex )
  {
    return this.types [ pIndex ] ;
  }


  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public CurriedLet clone ( )
  {
    return new CurriedLet ( this.identifiers , this.types , this.e1.clone ( ) ,
        this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    TreeSet < String > freeE2 = new TreeSet < String > ( ) ;
    freeE2.addAll ( this.e2.free ( ) ) ;
    freeE2.remove ( this.identifiers [ 0 ] ) ;
    TreeSet < String > freeE1 = new TreeSet < String > ( ) ;
    freeE1.addAll ( this.e1.free ( ) ) ;
    for ( int n = 1 ; n < this.identifiers.length ; ++ n )
    {
      freeE1.remove ( this.identifiers [ n ] ) ;
    }
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( freeE1 ) ;
    free.addAll ( freeE2 ) ;
    return free ;
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
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public CurriedLet substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType [ ] newTypes = new MonoType [ this.types.length ] ;
    for ( int n = 0 ; n < newTypes.length ; ++ n )
    {
      newTypes [ n ] = ( this.types [ n ] != null ) ? this.types [ n ]
          .substitute ( pTypeSubstitution ) : null ;
    }
    return new CurriedLet ( this.identifiers , newTypes , this.e1
        .substitute ( pTypeSubstitution ) , this.e2
        .substitute ( pTypeSubstitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public CurriedLet substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    // determine the expressions and the identifiers
    String [ ] newIdentifiers = this.identifiers ;
    Expression newE1 = this.e1 ;
    Expression newE2 = this.e2 ;
    // check if we can substitute below e1
    if ( ! Arrays.asList ( newIdentifiers )
        .subList ( 1 , newIdentifiers.length ).contains ( pId ) )
    {
      // bound rename for substituting e in e1
      newIdentifiers = newIdentifiers.clone ( ) ;
      Set < String > freeE = pExpression.free ( ) ;
      for ( int n = 1 ; n < newIdentifiers.length ; ++ n )
      {
        // generate a new unique identifier
        while ( freeE.contains ( newIdentifiers [ n ] ) )
        {
          newIdentifiers [ n ] = newIdentifiers [ n ] + "'" ; //$NON-NLS-1$
        }
        // perform the bound renaming
        newE1 = newE1.substitute ( this.identifiers [ n ] , new Identifier (
            newIdentifiers [ n ] ) , pAttributeRename ) ;
      }
      // substitute in e1 if
      newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    // substitute e2 if id is not bound in e2
    if ( ! this.identifiers [ 0 ].equals ( pId ) )
    {
      newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    // generate the new expression
    return new CurriedLet ( newIdentifiers , this.types , newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifiers [ 0 ] ) ;
    for ( int n = 1 ; n < this.identifiers.length ; ++ n )
    {
      builder.addText ( " " ) ; //$NON-NLS-1$
      if ( this.types [ n ] != null )
      {
        builder.addText ( "(" ) ; //$NON-NLS-1$
      }
      builder.addIdentifier ( this.identifiers [ n ] ) ;
      if ( this.types [ n ] != null )
      {
        builder.addText ( ": " ) ; //$NON-NLS-1$
        builder.addBuilder ( this.types [ n ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
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
    builder.addText ( " = " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e1
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
    builder.addBreak ( ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( "in" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e2
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof CurriedLet
        && getClass ( ).equals ( pObject.getClass ( ) ) )
    {
      CurriedLet other = ( CurriedLet ) pObject ;
      return ( this.identifiers.equals ( other.identifiers )
          && ( ( this.types == null ) ? ( other.types == null ) : Arrays
              .equals ( this.types , other.types ) )
          && this.e1.equals ( other.e1 ) && this.e2.equals ( other.e2 ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers.hashCode ( ) + this.e1.hashCode ( )
        + this.e2.hashCode ( )
        + ( ( this.types != null ) ? this.types.hashCode ( ) : 0 ) ;
  }
}
