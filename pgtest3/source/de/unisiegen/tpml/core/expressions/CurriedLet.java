package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


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
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof CurriedLet )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      CurriedLet other = ( CurriedLet ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( Arrays.equals ( this.types , other.types ) )
          && ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
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
      for ( int i = 1 ; i < this.identifiers.length ; i ++ )
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
    return "Curried-Let" ; //$NON-NLS-1$
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public CurriedLet substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    Expression newE1 = this.e1.clone ( ) ;
    Expression newE2 = this.e2.clone ( ) ;
    boolean substE1 = true ;
    boolean substE2 = true ;
    for ( int i = 1 ; i < this.identifiers.length ; i ++ )
    {
      if ( this.identifiers [ i ].equals ( pId ) )
      {
        substE1 = false ;
        break ;
      }
    }
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      substE2 = false ;
    }
    if ( ( substE1 ) && ( this.e1.free ( ).contains ( pId ) ) )
    {
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        BoundRenaming boundRenaming = new BoundRenaming ( ) ;
        boundRenaming.add ( this.e1.free ( ) ) ;
        boundRenaming.remove ( newIdentifiers [ i ] ) ;
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
          newE1 = newE1.substitute ( newIdentifiers [ i ] , new Identifier (
              newId ) , pAttributeRename ) ;
          newIdentifiers [ i ] = newId ;
        }
      }
      newE1 = newE1.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    if ( ( substE2 ) && ( this.e2.free ( ).contains ( pId ) ) )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.e2.free ( ) ) ;
      boundRenaming.remove ( this.identifiers [ 0 ] ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
      boundRenaming.add ( pId ) ;
      for ( int i = 1 ; i < newIdentifiers.length ; i ++ )
      {
        boundRenaming.add ( this.identifiers [ i ] ) ;
      }
      String newId = boundRenaming.newIdentifier ( this.identifiers [ 0 ] ) ;
      if ( ! this.identifiers [ 0 ].equals ( newId ) )
      {
        newE2 = newE2.substitute ( this.identifiers [ 0 ] , new Identifier (
            newId ) , pAttributeRename ) ;
        newIdentifiers [ 0 ] = newId ;
      }
      newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    }
    return new CurriedLet ( newIdentifiers , this.types , newE1 , newE2 ) ;
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
    for ( int i = 0 ; i < newTypes.length ; i ++ )
    {
      newTypes [ i ] = ( this.types [ i ] == null ) ? null : this.types [ i ]
          .substitute ( pTypeSubstitution ) ;
    }
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return new CurriedLet ( this.identifiers , newTypes , newE1 , newE2 ) ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addIdentifier ( this.identifiers [ 0 ] ) ;
      for ( int n = 1 ; n < this.identifiers.length ; ++ n )
      {
        this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
        if ( this.types [ n ] != null )
        {
          this.prettyStringBuilder.addText ( "(" ) ; //$NON-NLS-1$
        }
        this.prettyStringBuilder.addIdentifier ( this.identifiers [ n ] ) ;
        if ( this.types [ n ] != null )
        {
          this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
          this.prettyStringBuilder.addBuilder ( this.types [ n ]
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_LET_TAU ) ;
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
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.e1
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e2
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
