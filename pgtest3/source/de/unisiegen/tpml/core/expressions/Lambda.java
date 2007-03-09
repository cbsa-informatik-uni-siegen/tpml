package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


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
   * 
   * @see Expression#clone()
   */
  @ Override
  public Expression clone ( )
  {
    return new Lambda ( this.id , this.tau , this.e.clone ( ) ) ;
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
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.addAll ( this.e.free ( ) ) ;
      this.free.remove ( this.id ) ;
    }
    return this.free ;
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
   * Returns the body of the lambda expression.
   * 
   * @return the bodyof the lambda expression.
   */
  public Expression getE ( )
  {
    return this.e ;
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
    if ( this.id.equals ( pId ) )
    {
      return this ;
    }
    Expression newE = this.e ;
    String newId = this.id ;
    if ( this.e.free ( ).contains ( pId ) )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.free ( ) ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
      boundRenaming.add ( pId ) ;
      newId = boundRenaming.newIdentifier ( this.id ) ;
      if ( ! this.id.equals ( newId ) )
      {
        newE = newE.substitute ( this.id , new Identifier ( newId ) ,
            pAttributeRename ) ;
      }
    }
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Lambda ( newId , this.tau , newE ) ;
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
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    /*
     * System.out.println ( "BoundRenaming Lambda:" ) ; for ( String s : free ( ) ) {
     * System.out.print ( s + " " ) ; } System.out.println ( ) ;
     */
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
}
