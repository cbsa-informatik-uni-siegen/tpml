package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
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
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    /*
     * Add all free Identifiers of the Row.
     */
    free.addAll ( this.row.free ( ) ) ;
    /*
     * Remove the Identifier of this ObjectExpr.
     */
    free.remove ( this.identifier ) ;
    /*
     * Add all free Identifiers of all Attributes.
     */
    free.addAll ( this.row.freeVal ( ) ) ;
    return free ;
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
    /*
     * If the Identifier, which should be substituted, is equal to the
     * Identifier of this ObjectExpr, it should only be substituted in
     * Attributes.
     */
    if ( this.identifier.equals ( pId ) )
    {
      Expression [ ] newRowE = this.row.getExpressions ( ).clone ( ) ;
      for ( int i = 0 ; i < newRowE.length ; i ++ )
      {
        if ( newRowE [ i ] instanceof Attribute )
        {
          /*
           * Stop the Attribute rename with the parameter false.
           */
          newRowE [ i ] = newRowE [ i ].substitute ( pId , pExpression , false ) ;
        }
      }
      return new ObjectExpr ( this.identifier , this.tau , new Row ( newRowE ) ) ;
    }
    Set < String > freeObj = this.free ( ) ;
    Set < String > freeE = pExpression.free ( ) ;
    String newId = this.identifier ;
    boolean changes = true ;
    while ( changes )
    {
      /*
       * Only rename the Identifier of this ObjectExpr if the Identifier, which
       * should be substituted is free in this ObjectExpr and the Expression
       * contains the newId.
       */
      changes = false ;
      if ( ( freeE.contains ( newId ) ) && ( freeObj.contains ( pId ) ) )
      {
        newId = newId + "'" ; //$NON-NLS-1$
        changes = true ;
      }
      /*
       * Rename, if an Attribute with the same Identifier exists.
       */
      for ( int i = 0 ; i < this.row.getExpressions ( ).length ; i ++ )
      {
        if ( ( this.row.getExpressions ( i ) instanceof Attribute )
            && ( ( ( Attribute ) this.row.getExpressions ( i ) ).getId ( )
                .equals ( newId ) ) )
        {
          newId = newId + "'" ; //$NON-NLS-1$
          changes = true ;
        }
      }
    }
    /*
     * Only substitute the old Identifier, if the new Identifier is not equal to
     * the old Identifier.
     */
    Row newRow = this.row ;
    if ( ! newId.equals ( this.identifier ) )
    {
      Expression [ ] newRowE = newRow.getExpressions ( ).clone ( ) ;
      for ( int i = 0 ; i < newRowE.length ; i ++ )
      {
        if ( ! ( newRowE [ i ] instanceof Attribute ) )
        {
          /*
           * Stop the Attribute rename with the parameter false.
           */
          newRowE [ i ] = newRowE [ i ].substitute ( this.identifier ,
              new Identifier ( newId ) , false ) ;
        }
      }
      newRow = new Row ( newRowE ) ;
    }
    /*
     * Stop the Attribute rename with the parameter false.
     */
    return new ObjectExpr ( newId , this.tau , newRow.substitute ( pId ,
        pExpression , false ) ) ;
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
    return new ObjectExpr ( this.identifier , newTau , this.row
        .substitute ( pTypeSubstitution ) ) ;
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
    builder.addText ( ")" ) ; //$NON-NLS-1$
    builder.addBreak ( ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addBuilder ( this.row
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
        PRIO_OBJECTEXPR_E ) ;
    builder.addBreak ( ) ;
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addKeyword ( "end" ) ; //$NON-NLS-1$
    return builder ;
  }
}
