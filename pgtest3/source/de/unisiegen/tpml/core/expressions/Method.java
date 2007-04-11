package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1067 $
 */
public class Method extends Expression
{
  /**
   * TODO
   * 
   * @see #getId()
   */
  private Identifier id ;


  /**
   * TODO
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression e ;


  /**
   * TODO
   * 
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public Method ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.id = pIdentifier ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Method clone ( )
  {
    return new Method ( this.id.clone ( ) , this.tau == null ? null : this.tau
        .clone ( ) , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Method )
    {
      Method other = ( Method ) pObject ;
      return ( ( this.id.equals ( other.id ) ) && ( this.e.equals ( other.e ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Method" ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #e
   */
  public Expression getE ( )
  {
    return this.e ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #id
   */
  public Identifier getId ( )
  {
    return this.id ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #tau
   */
  public MonoType getTau ( )
  {
    return this.tau ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public int hashCode ( )
  {
    return this.id.hashCode ( ) + this.e.hashCode ( )
        + ( this.tau == null ? 0 : this.tau.hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.e.isValue ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Method substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Method substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE = this.e.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Method ( this.id.clone ( ) , this.tau == null ? null : this.tau
        .clone ( ) , newE ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Method substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new Method ( this.id.clone ( ) , newTau , newE ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_METHOD ) ;
      this.prettyStringBuilder.addKeyword ( "method" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_ID ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_METHOD_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_METHOD_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
