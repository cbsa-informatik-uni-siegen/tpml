package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class Attribute extends Expression
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
   * @see #getNewId()
   */
  private Identifier newId ;


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
  public Attribute ( Identifier pIdentifier , MonoType pTau ,
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
    this.newId = null ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attribute clone ( )
  {
    return new Attribute ( this.id.clone ( ) , this.tau == null ? null
        : this.tau.clone ( ) , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Attribute )
    {
      Attribute other = ( Attribute ) pObject ;
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
    return "Attribute" ; //$NON-NLS-1$
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
   * @see #id
   */
  public Identifier getNewId ( )
  {
    return this.newId ;
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
   * TODO
   * 
   * @param pNewId TODO
   * @see #id
   */
  public void setNewId ( Identifier pNewId )
  {
    this.newId = pNewId ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Attribute substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Attribute substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    Expression newE = this.e.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Attribute ( this.id.clone ( ) , this.tau == null ? null
        : this.tau.clone ( ) , newE ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public Attribute substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new Attribute ( this.id.clone ( ) , newTau , newE ) ;
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
          PRIO_ATTRIBUTE ) ;
      this.prettyStringBuilder.addKeyword ( "val" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_ATTRIBUTE_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_ATTRIBUTE_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addKeyword ( ";" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
