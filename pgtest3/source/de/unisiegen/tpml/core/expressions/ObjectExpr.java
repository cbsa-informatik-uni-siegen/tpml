package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.identifiers.BoundedId ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public final class ObjectExpr extends Expression implements BoundedId
{
  /**
   * TODO
   * 
   * @see #getE()
   */
  private Expression e ;


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
   * @param pIdentifier TODO
   * @param pTau TODO
   * @param pExpression TODO
   */
  public ObjectExpr ( Identifier pIdentifier , MonoType pTau ,
      Expression pExpression )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression == null )
    {
      throw new NullPointerException ( "Row is null" ) ; //$NON-NLS-1$
    }
    if ( ! ( pExpression instanceof Row ) )
    {
      throw new IllegalArgumentException ( "The Expression has to be a Row" ) ; //$NON-NLS-1$
    }
    this.id = pIdentifier ;
    this.tau = pTau ;
    this.e = pExpression ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.id.clone ( ) , this.tau == null ? null
        : this.tau.clone ( ) , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof ObjectExpr )
    {
      ObjectExpr other = ( ObjectExpr ) pObject ;
      return ( this.e.equals ( other.e ) )
          && ( ( this.tau == null ) ? ( other.tau == null ) : ( this.tau
              .equals ( other.tau ) )
              && ( this.id.equals ( other.id ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
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
   * Returns a list of in this {@link Expression} bounded {@link Identifier}s.
   * 
   * @return A list of in this {@link Expression} bounded {@link Identifier}s.
   */
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
    return "Object" ; //$NON-NLS-1$
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
  public ObjectExpr substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr substitute ( Identifier pId , Expression pExpression ,
      @ SuppressWarnings ( "unused" )
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
      newE = newE.substitute ( this.id , newId , false ) ;
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression , false ) ;
    return new ObjectExpr ( newId ,
        this.tau == null ? null : this.tau.clone ( ) , newE ) ;
  }


  /**
   * TODO
   * 
   * @param pTypeSubstitution TODO
   * @return TODO
   */
  @ Override
  public ObjectExpr substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new ObjectExpr ( this.id.clone ( ) , newTau , newE ) ;
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
          PRIO_OBJECTEXPR ) ;
      this.prettyStringBuilder.addKeyword ( "object" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " (" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_OBJECTEXPR_TAU ) ;
      }
      this.prettyStringBuilder.addText ( ") " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECTEXPR_E ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "end" ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
