package de.unisiegen.tpml.core.expressions ;


import java.util.Arrays ;
import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents a multi lambda abstract, which takes a single tuple argument as
 * parameter and splits the tuple items to various identifiers.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Lambda
 * @see MultiLet
 * @see Value
 */
public final class MultiLambda extends Value
{
  /**
   * The tuple parameter identifiers.
   * 
   * @see #getIdentifiers()
   * @see #getIdentifiers(int)
   */
  private String [ ] identifiers ;


  /**
   * The type of the <code>identifiers</code> or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * The function body expression.
   * 
   * @see #getE()
   */
  private Expression e ;


  /**
   * Allocates a new <code>MultiLambda</code> expression with the specified
   * <code>identifiers</code> and the function body <code>e</code>.
   * 
   * @param pIdentifiers non-empty set of identifiers.
   * @param pTau the type of the identifiers or <code>null</code>.
   * @param pExpression the function body.
   * @throws IllegalArgumentException if the <code>identifiers</code> list is
   *           empty.
   * @throws NullPointerException if <code>identifiers</code> or
   *           <code>e</code> is <code>null</code>.
   */
  public MultiLambda ( String [ ] pIdentifiers , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifiers == null )
    {
      throw new NullPointerException ( "identifiers is null" ) ; //$NON-NLS-1$
    }
    if ( pIdentifiers.length == 0 )
    {
      throw new IllegalArgumentException ( "identifiers is empty" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "e is null" ) ; //$NON-NLS-1$
    }
    this.identifiers = pIdentifiers ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Multi-Lambda" ; //$NON-NLS-1$
  }


  /**
   * Returns the identifiers for the tuple parameter.
   * 
   * @return the identifiers for the tuple parameter.
   * @see #getIdentifiers()
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
   * @throws ArrayIndexOutOfBoundsException if <code>n</code> is out of
   *           bounds.
   * @see #getIdentifiers()
   */
  public String getIdentifiers ( int pIndex )
  {
    return this.identifiers [ pIndex ] ;
  }


  /**
   * Returns the type of the <code>identifiers</code> or <code>null</code>
   * if no type was specified.
   * 
   * @return the type of the identifiers;
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * Returns the function body expression.
   * 
   * @return the function body expression.
   */
  public Expression getE ( )
  {
    return this.e ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public MultiLambda clone ( )
  {
    return new MultiLambda ( this.identifiers , this.tau , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public TreeSet < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.e.free ( ) ) ;
    free.removeAll ( Arrays.asList ( this.identifiers ) ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Expression substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new MultiLambda ( this.identifiers , newTau , this.e
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    // check if we can substitute below the lambda abstraction
    if ( Arrays.asList ( this.identifiers ).contains ( pId ) )
    {
      return this ;
    }
    // bound rename for substituting e in this.e
    Expression newE = this.e ;
    Set < String > freeE = pExpression.free ( ) ;
    String [ ] newIdentifiers = this.identifiers.clone ( ) ;
    for ( int n = 0 ; n < newIdentifiers.length ; ++ n )
    {
      // generate a new unique identifier
      while ( newE.free ( ).contains ( newIdentifiers [ n ] )
          || freeE.contains ( newIdentifiers [ n ] )
          || newIdentifiers [ n ].equals ( pId ) )
      {
        newIdentifiers [ n ] = newIdentifiers [ n ] + "'" ; //$NON-NLS-1$
      }
      // perform the bound renaming
      newE = newE.substitute ( this.identifiers [ n ] , new Identifier (
          newIdentifiers [ n ] ) , pAttributeRename ) ;
    }
    // perform the substitution
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    // allocate the new multi lambda
    return new MultiLambda ( newIdentifiers , this.tau , newE ) ;
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
        this , PRIO_LAMBDA ) ;
    builder.addKeyword ( "\u03bb" ) ; //$NON-NLS-1$
    builder.addText ( "(" ) ; //$NON-NLS-1$
    for ( int i = 0 ; i < this.identifiers.length ; ++ i )
    {
      if ( i > 0 )
      {
        builder.addText ( ", " ) ; //$NON-NLS-1$
      }
      builder.addIdentifier ( this.identifiers [ i ] ) ;
    }
    builder.addText ( ")" ) ; //$NON-NLS-1$
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LAMBDA_TAU ) ;
    }
    builder.addText ( "." ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LAMBDA_E ) ;
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
    if ( pObject instanceof MultiLambda )
    {
      MultiLambda other = ( MultiLambda ) pObject ;
      return ( ( Arrays.equals ( this.identifiers , other.identifiers ) )
          && ( this.e.equals ( other.e ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
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
    return this.identifiers.hashCode ( )
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 )
        + this.e.hashCode ( ) ;
  }
}
