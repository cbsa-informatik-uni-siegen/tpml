package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents the <b>(ABSTR)</b> expression in the expression hierarchy, which
 * is used for lambda abstractions. The string representation for lambda
 * abstraction is <code>lambda id.e</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Application
 * @see Expression
 * @see Value
 */
public final class Lambda extends Value
{
  /**
   * The identifier of the abstraction parameter.
   * 
   * @see #getId()
   */
  private String id ;


  /**
   * The type of the parameter or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * The expression of the abstraction body.
   * 
   * @see #getE()
   */
  private Expression e ;


  /**
   * Allocates a new lambda abstraction with the specified identifier
   * <code>id</code> and the given body <code>e</code>.
   * 
   * @param pId the identifier of the lambda parameter.
   * @param pTau the type for the parameter or <code>null</code>.
   * @param pExpression the body.
   * @throws NullPointerException if either <code>id</code> or <code>e</code>
   *           is <code>null</code>.
   */
  public Lambda ( String pId , MonoType pTau , Expression pExpression )
  {
    if ( pId == null )
    {
      throw new NullPointerException ( "id is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "e is null" ) ; //$NON-NLS-1$
    }
    this.id = pId ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Lambda" ; //$NON-NLS-1$
  }


  /**
   * Returns the identifier of the parameter for the lambda expression.
   * 
   * @return the identifier of the parameter for the lambda expression.
   */
  public String getId ( )
  {
    return this.id ;
  }


  /**
   * Returns the type for the parameter or <code>null</code>.
   * 
   * @return the type for the parameter or <code>null</code>.
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * Returns the body of the lambda expression.
   * 
   * @return the bodyof the lambda expression.
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
  public Expression clone ( )
  {
    return new Lambda ( this.id , this.tau , this.e.clone ( ) ) ;
  }


  /**
   * Returns the free (unbound) identifiers of the lambda abstraction. The free
   * (unbound) identifiers of the lambda abstraction are determined by querying
   * the free identifiers of the <code>e1</code> subexpression, and removing
   * the identifier <code>id</code> from the returned set.
   * 
   * @return the free identifiers for the lambda abstraction.
   * @see #getId()
   * @see #getE()
   * @see Expression#free()
   */
  @ Override
  public Set < String > free ( )
  {
    // determine the free identifiers of e1, and
    // make sure it doesn't contain our id
    Set < String > freeE = this.e.free ( ) ;
    if ( freeE.contains ( this.id ) )
    {
      // allocate a new set without the identifier
      TreeSet < String > free = new TreeSet < String > ( freeE ) ;
      free.remove ( this.id ) ;
      return free ;
    }
    // we can just reuse the free set
    return freeE ;
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
    return new Lambda ( this.id , newTau , this.e
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
   * Substitutes <code>e</code> for <code>id</code> within the lambda
   * expression, performing a bound rename if necessary to avoid altering the
   * binding of existing identifiers within the sub expression.
   * 
   * @param pId the identifier for which to substitute.
   * @param pExpression the expression to substitute for <code>id</code>.
   * @return the resulting expression.
   * @see #getId()
   * @see #getE()
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Lambda substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    // check if the identifier is the same as our identifier,
    // in which case no substitution is performed below the
    // lambda expression
    if ( this.id.equals ( pId ) )
    {
      return this ;
    }
    // determine the free identifiers of this lambda
    Set < String > free = free ( ) ;
    // determine the free identifiers for e
    Set < String > freeE = pExpression.free ( ) ;
    // generate a new unique identifier
    String newId = this.id ;
    while ( free.contains ( newId ) || freeE.contains ( newId )
        || newId.equals ( pId ) )
    {
      newId = newId + "'" ; //$NON-NLS-1$
    }
    // perform the bound renaming (if required)
    Expression newE = ( this.id == newId ) ? this.e : this.e.substitute (
        this.id , new Identifier ( newId ) , pAttributeRename ) ;
    // perform the substitution for e1
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    // reuse this abstraction object if possible
    return ( this.e == newE ) ? this : new Lambda ( newId , this.tau , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_LAMBDA ) ;
    builder.addKeyword ( "\u03bb" ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.id ) ;
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
    if ( pObject instanceof Lambda )
    {
      Lambda other = ( Lambda ) pObject ;
      return ( ( this.id.equals ( other.id ) ) && ( this.e.equals ( other.e ) ) && ( ( this.tau == null ) ? ( other.tau == null )
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
    return this.id.hashCode ( )
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 )
        + this.e.hashCode ( ) ;
  }
}
