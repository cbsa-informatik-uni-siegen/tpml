package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.exceptions.LanguageParserMultiException ;
import de.unisiegen.tpml.core.interfaces.BoundIdentifiers ;
import de.unisiegen.tpml.core.interfaces.ChildrenExpressions ;
import de.unisiegen.tpml.core.interfaces.DefaultTypes ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
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
public final class Lambda extends Value implements BoundIdentifiers ,
    DefaultTypes , ChildrenExpressions
{
  /**
   * Indeces of the child {@link Expression}s.
   */
  private static final int [ ] INDICES_E = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Identifier}s.
   */
  private static final int [ ] INDICES_ID = new int [ ]
  { - 1 } ;


  /**
   * Indeces of the child {@link Type}s.
   */
  private static final int [ ] INDICES_TYPE = new int [ ]
  { - 1 } ;


  /**
   * The list of identifiers.
   * 
   * @see #getIdentifiers()
   */
  private Identifier [ ] identifiers ;


  /**
   * The types for the identifiers, where the assignment is as follows:
   * 
   * @see #getTypes()
   */
  private MonoType [ ] types ;


  /**
   * The expression.
   */
  private Expression [ ] expressions ;


  /**
   * Allocates a new lambda abstraction with the specified identifier
   * <code>id</code> and the given body <code>e</code>.
   * 
   * @param pIdentifier the identifier of the lambda parameter.
   * @param pTau the type for the parameter or <code>null</code>.
   * @param pExpression the body.
   * @throws NullPointerException if either <code>id</code> or <code>e</code>
   *           is <code>null</code>.
   */
  public Lambda ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "id is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "e is null" ) ; //$NON-NLS-1$
    }
    // Identifier
    this.identifiers = new Identifier [ 1 ] ;
    this.identifiers [ 0 ] = pIdentifier ;
    if ( this.identifiers [ 0 ].getParent ( ) != null )
    {
      // this.identifiers [ 0 ] = this.identifiers [ 0 ].clone ( ) ;
    }
    this.identifiers [ 0 ].setParent ( this ) ;
    // Type
    this.types = new MonoType [ 1 ] ;
    this.types [ 0 ] = pTau ;
    if ( this.types [ 0 ] != null )
    {
      if ( this.types [ 0 ].getParent ( ) != null )
      {
        // this.types [ 0 ] = this.types [ 0 ].clone ( ) ;
      }
      this.types [ 0 ].setParent ( this ) ;
    }
    // Expression
    this.expressions = new Expression [ 1 ] ;
    this.expressions [ 0 ] = pExpression ;
    if ( this.expressions [ 0 ].getParent ( ) != null )
    {
      // this.expressions [ 0 ] = this.expressions [ 0 ].clone ( ) ;
    }
    this.expressions [ 0 ].setParent ( this ) ;
    checkDisjunction ( ) ;
  }


  /**
   * TODO
   */
  public void checkDisjunction ( )
  {
    ArrayList < Identifier > allIdentifiers = this.expressions [ 0 ]
        .getIdentifiersAll ( ) ;
    ArrayList < Identifier > negativeIdentifiers = new ArrayList < Identifier > ( ) ;
    for ( Identifier allId : allIdentifiers )
    {
      if ( ( this.identifiers [ 0 ].equals ( allId ) )
          && ( allId.getSet ( ).equals ( Identifier.Set.ATTRIBUTE ) ) )
      {
        negativeIdentifiers.add ( allId ) ;
      }
    }
    negativeIdentifiers.add ( this.identifiers [ 0 ] ) ;
    LanguageParserMultiException.throwExceptionDisjunction ( negativeIdentifiers ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Lambda clone ( )
  {
    return new Lambda ( this.identifiers [ 0 ].clone ( ) ,
        this.types [ 0 ] == null ? null : this.types [ 0 ].clone ( ) ,
        this.expressions [ 0 ].clone ( ) ) ;
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
      return ( ( this.identifiers [ 0 ].equals ( other.identifiers [ 0 ] ) )
          && ( this.expressions [ 0 ].equals ( other.expressions [ 0 ] ) ) && ( ( this.types [ 0 ] == null ) ? ( other.types [ 0 ] == null )
          : ( this.types [ 0 ].equals ( other.types [ 0 ] ) ) ) ) ;
    }
    return false ;
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
    return this.expressions [ 0 ] ;
  }


  /**
   * Returns the sub expressions.
   * 
   * @return the sub expressions.
   */
  public Expression [ ] getExpressions ( )
  {
    return this.expressions ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getExpressionsIndex ( )
  {
    return Lambda.INDICES_E ;
  }


  /**
   * Returns the identifier of the parameter for the lambda expression.
   * 
   * @return the identifier of the parameter for the lambda expression.
   */
  public Identifier getId ( )
  {
    return this.identifiers [ 0 ] ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public Identifier [ ] getIdentifiers ( )
  {
    return this.identifiers ;
  }


  /**
   * Returns a list of in this {@link Expression} bound {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bound {@link Identifier}s.
   */
  public ArrayList < ArrayList < Identifier >> getIdentifiersBound ( )
  {
    if ( this.boundIdentifiers == null )
    {
      this.boundIdentifiers = new ArrayList < ArrayList < Identifier >> ( 1 ) ;
      ArrayList < Identifier > boundIdList = new ArrayList < Identifier > ( ) ;
      ArrayList < Identifier > boundE = this.expressions [ 0 ]
          .getIdentifiersFree ( ) ;
      for ( Identifier freeId : boundE )
      {
        if ( this.identifiers [ 0 ].equals ( freeId ) )
        {
          freeId.setBoundTo ( this , this.identifiers [ 0 ] ) ;
          boundIdList.add ( freeId ) ;
        }
      }
      this.boundIdentifiers.add ( boundIdList ) ;
    }
    return this.boundIdentifiers ;
  }


  /**
   * Returns the free (unbound) identifiers of the lambda abstraction. The free
   * (unbound) identifiers of the lambda abstraction are determined by querying
   * the free identifiers of the <code>e</code> subexpression, and removing
   * the identifier <code>id</code> from the returned set.
   * 
   * @return the free identifiers for the lambda abstraction.
   * @see #getId()
   * @see #getE()
   * @see Expression#getIdentifiersFree()
   */
  @ Override
  public ArrayList < Identifier > getIdentifiersFree ( )
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ( ) ;
      this.identifiersFree.addAll ( this.expressions [ 0 ]
          .getIdentifiersFree ( ) ) ;
      while ( this.identifiersFree.remove ( this.identifiers [ 0 ] ) )
      {
        // Remove all Identifiers with the same name
      }
    }
    return this.identifiersFree ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getIdentifiersIndex ( )
  {
    return Lambda.INDICES_ID ;
  }


  /**
   * Returns the type for the parameter or <code>null</code>.
   * 
   * @return the type for the parameter or <code>null</code>.
   */
  public MonoType getTau ( )
  {
    return this.types [ 0 ] ;
  }


  /**
   * Returns the types for the <code>identifiers</code>.
   * 
   * @return the types.
   */
  public MonoType [ ] getTypes ( )
  {
    return this.types ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int [ ] getTypesIndex ( )
  {
    return Lambda.INDICES_TYPE ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.identifiers [ 0 ].hashCode ( )
        + ( ( this.types [ 0 ] == null ) ? 0 : this.types [ 0 ].hashCode ( ) )
        + this.expressions [ 0 ].hashCode ( ) ;
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
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Lambda substitute ( Identifier pId , Expression pExpression )
  {
    /*
     * Do not substitute, if the Identifiers are equal.
     */
    if ( this.identifiers [ 0 ].equals ( pId ) )
    {
      return this ;
    }
    /*
     * Perform the bound renaming if required.
     */
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pExpression.getIdentifiersFree ( ) ) ;
    boundRenaming.add ( pId ) ;
    Identifier newId = boundRenaming.newId ( this.identifiers [ 0 ] ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE = this.expressions [ 0 ] ;
    if ( ! this.identifiers [ 0 ].equals ( newId ) )
    {
      newE = newE.substitute ( this.identifiers [ 0 ] , newId ) ;
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression ) ;
    return new Lambda ( newId , this.types [ 0 ] , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Lambda substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.types [ 0 ] == null ) ? null : this.types [ 0 ]
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.expressions [ 0 ].substitute ( pTypeSubstitution ) ;
    return new Lambda ( this.identifiers [ 0 ] , newTau , newE ) ;
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
          PrettyPrintPriorities.PRIO_LAMBDA ) ;
      this.prettyStringBuilder.addKeyword ( "\u03bb" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.identifiers [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_ID ) ;
      if ( this.types [ 0 ] != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.types [ 0 ]
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PrettyPrintPriorities.PRIO_LAMBDA_TAU ) ;
      }
      this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.expressions [ 0 ]
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PrettyPrintPriorities.PRIO_LAMBDA_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
