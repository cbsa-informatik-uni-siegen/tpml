package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
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
public final class ObjectExpr extends Expression
{
  /**
   * TODO
   * 
   * @see #getE()
   */
  private Row row ;


  /**
   * TODO
   * 
   * @see #getId()
   */
  private String identifier ;


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
   * @param pRow TODO
   */
  public ObjectExpr ( String pIdentifier , MonoType pTau , Row pRow )
  {
    if ( pIdentifier == null )
    {
      throw new NullPointerException ( "Identifier is null" ) ; //$NON-NLS-1$
    }
    if ( pRow == null )
    {
      throw new NullPointerException ( "Expression is null" ) ; //$NON-NLS-1$
    }
    this.identifier = pIdentifier ;
    this.tau = pTau ;
    this.row = pRow ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public ObjectExpr clone ( )
  {
    return new ObjectExpr ( this.identifier , this.tau , this.row.clone ( ) ) ;
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
      return ( this.row.equals ( other.row ) )
          && ( ( this.tau == null ) ? ( other.tau == null ) : ( this.tau
              .equals ( other.tau ) )
              && ( this.identifier.equals ( other.identifier ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.addAll ( this.row.free ( ) ) ;
      this.free.remove ( this.identifier ) ;
    }
    return this.free ;
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
   * @see #row
   */
  public Row getE ( )
  {
    return this.row ;
  }


  /**
   * TODO
   * 
   * @return TODO
   * @see #identifier
   */
  public String getId ( )
  {
    return this.identifier ;
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
    return this.identifier.hashCode ( ) + this.row.hashCode ( )
        + ( this.tau == null ? 0 : this.tau.hashCode ( ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public boolean isValue ( )
  {
    return this.row.isValue ( ) ;
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
   */
  @ Override
  public ObjectExpr substitute ( String pId , Expression pExpression ,
      @ SuppressWarnings ( "unused" )
      boolean pAttributeRename )
  {
    if ( this.identifier.equals ( pId ) )
    {
      return this ;
    }
    Row newRow = this.row ;
    String newId = this.identifier ;
    if ( this.row.free ( ).contains ( pId ) )
    {
      BoundRenaming boundRenaming = new BoundRenaming ( ) ;
      boundRenaming.add ( this.free ( ) ) ;
      boundRenaming.add ( pExpression.free ( ) ) ;
      boundRenaming.add ( pId ) ;
      newId = boundRenaming.newIdentifier ( this.identifier ) ;
      if ( ! this.identifier.equals ( newId ) )
      {
        newRow = newRow.substitute ( this.identifier ,
            new Identifier ( newId ) , false ) ;
      }
    }
    newRow = newRow.substitute ( pId , pExpression , false ) ;
    return new ObjectExpr ( newId , this.tau , newRow ) ;
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
    Row newRow = this.row.substitute ( pTypeSubstitution ) ;
    return new ObjectExpr ( this.identifier , newTau , newRow ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    /*
     * System.out.println ( "Free ObjectExpr:" ) ; for ( String s : free ( ) ) {
     * System.out.print ( s + " " ) ; } System.out.println ( ) ;
     */
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_OBJECTEXPR ) ;
    builder.addKeyword ( "object" ) ; //$NON-NLS-1$
    builder.addText ( " (" ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.identifier ) ;
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder.addBuilder ( this.tau
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
          PRIO_OBJECTEXPR_TAU ) ;
    }
    builder.addText ( ") " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addBuilder ( this.row
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addKeyword ( "end" ) ; //$NON-NLS-1$
    return builder ;
  }
}
