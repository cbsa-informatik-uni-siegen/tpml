package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
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
  private Identifier id ;


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
  public Lambda ( Identifier pId , MonoType pTau , Expression pExpression )
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
  public Lambda clone ( )
  {
    return new Lambda ( this.id.clone ( ) , this.tau == null ? null : this.tau
        .clone ( ) , this.e.clone ( ) ) ;
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
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.addAll ( this.e.free ( ) ) ;
      while ( this.free.remove ( this.id ) )
      {
        // Remove all Identifiers with the same name
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
  public ArrayList < Identifier > getBoundedId ( )
  {
    if ( this.boundedIdentifiers == null )
    {
      this.boundedIdentifiers = new ArrayList < ArrayList < Identifier >> ( ) ;
      ArrayList < Identifier > boundedIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundedE = this.e.free ( ) ;
      for ( Identifier freeId : boundedE )
      {
        if ( this.id.equals ( freeId ) )
        {
          freeId.setBoundedToExpression ( this ) ;
          freeId.setBoundedToIdentifier ( this.id ) ;
          boundedIdList.add ( freeId ) ;
        }
      }
      this.boundedIdentifiers.add ( boundedIdList ) ;
    }
    return this.boundedIdentifiers.get ( 0 ) ;
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
  public Identifier getId ( )
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
        + ( ( this.tau == null ) ? 0 : this.tau.hashCode ( ) )
        + this.e.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Lambda substitute ( Identifier pId , Expression pExpression )
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
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Lambda substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    if ( this.id.equals ( pId ) )
    {
      return this.clone ( ) ;
    }
    /*
     * Perform the bound renaming if required.
     */
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.free ( ) ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    Identifier newId = boundRenaming.newId ( this.id ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE = this.e ;
    if ( ! this.id.equals ( newId ) )
    {
      newE = newE.substitute ( this.id , newId , pAttributeRename ) ;
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Lambda ( newId , this.tau == null ? null : this.tau.clone ( ) ,
        newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Lambda substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new Lambda ( this.id.clone ( ) , newTau , newE ) ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LAMBDA ) ;
      this.prettyStringBuilder.addKeyword ( "\u03bb" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LAMBDA_TAU ) ;
      }
      this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_LAMBDA_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
