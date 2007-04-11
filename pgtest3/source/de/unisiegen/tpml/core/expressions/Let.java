package de.unisiegen.tpml.core.expressions ;


import java.util.ArrayList ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Represents the simple binding mechanism <code>let</code>. The string
 * representation is <code>let id = e1 in e2</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 * @see Lambda
 */
public class Let extends Expression
{
  /**
   * The identifier of the <code>Let</code> expression.
   * 
   * @see #getId()
   */
  protected Identifier id ;


  /**
   * The type for the <code>id</code> (and thereby for <code>e1</code>) or
   * <code>null</code>.
   * 
   * @see #getTau()
   */
  protected MonoType tau ;


  /**
   * The first expression.
   * 
   * @see #getE1()
   */
  protected Expression e1 ;


  /**
   * The second expression.
   * 
   * @see #getE2()
   */
  protected Expression e2 ;


  /**
   * Allocates a new <code>Let</code> with the specified <code>id</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param pId the name of the identifier.
   * @param pTau the type for the <code>id</code> (and thereby for
   *          <code>e1</code>) or <code>null</code>.
   * @param pExpression1 the first expression.
   * @param pExpression2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Let ( Identifier pId , MonoType pTau , Expression pExpression1 ,
      Expression pExpression2 )
  {
    if ( pId == null )
    {
      throw new NullPointerException ( "id is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ; //$NON-NLS-1$
    }
    if ( pExpression2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ; //$NON-NLS-1$
    }
    this.id = pId ;
    this.tau = pTau ;
    this.e1 = pExpression1 ;
    this.e2 = pExpression2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Let clone ( )
  {
    return new Let ( this.id.clone ( ) , this.tau == null ? null : this.tau
        .clone ( ) , this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof Let )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      Let other = ( Let ) pObject ;
      return ( ( this.id.equals ( other.id ) )
          && ( this.e1.equals ( other.e1 ) ) && ( this.e2.equals ( other.e2 ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : this.tau.equals ( other.tau ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public ArrayList < Identifier > free ( )
  {
    if ( this.free == null )
    {
      this.free = new ArrayList < Identifier > ( ) ;
      this.free.addAll ( this.e2.free ( ) ) ;
      while ( this.free.remove ( this.id ) )
      {
        // Remove all Identifiers with the same name
      }
      this.free.addAll ( this.e1.free ( ) ) ;
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
      ArrayList < Identifier > boundedE2 = this.e2.free ( ) ;
      for ( Identifier freeId : boundedE2 )
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
    return "Let" ; //$NON-NLS-1$
  }


  /**
   * Returns the first expression.
   * 
   * @return the first expression.
   */
  public Expression getE1 ( )
  {
    return this.e1 ;
  }


  /**
   * Returns the second expression.
   * 
   * @return the second expression.
   */
  public Expression getE2 ( )
  {
    return this.e2 ;
  }


  /**
   * Returns the identifier of the <code>Let</code> expression.
   * 
   * @return the identifier of the <code>Let</code> expression.
   */
  public Identifier getId ( )
  {
    return this.id ;
  }


  /**
   * Returns the type for the identifier (and thereby the type for
   * <code>e1</code>) or <code>null</code> if no type was specified by the
   * user or the translation to core syntax.
   * 
   * @return the type for <code>id</code> or <code>null</code>.
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
        + this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Let substitute ( Identifier pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Let substitute ( Identifier pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    /*
     * Perform the substitution in e1.
     */
    Expression newE1 = this.e1.substitute ( pId , pExpression ,
        pAttributeRename ) ;
    Expression newE2 = this.e2 ;
    /*
     * Do not substitute in e2 , if the Identifiers are equal.
     */
    if ( this.id.equals ( pId ) )
    {
      return new Let ( this.id.clone ( ) , this.tau == null ? null : this.tau
          .clone ( ) , newE1 , newE2.clone ( ) ) ;
    }
    /*
     * Perform the bound renaming if required.
     */
    ArrayList < Identifier > freeE2 = newE2.free ( ) ;
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( freeE2 ) ;
    boundRenaming.remove ( this.id ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    Identifier newId = boundRenaming.newId ( this.id ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    if ( ! this.id.equals ( newId ) )
    {
      newE2 = newE2.substitute ( this.id , newId , pAttributeRename ) ;
    }
    /*
     * Perform the substitution in e2.
     */
    newE2 = newE2.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Let ( newId , this.tau == null ? null : this.tau.clone ( ) ,
        newE1 , newE2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Let substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE1 = this.e1.substitute ( pTypeSubstitution ) ;
    Expression newE2 = this.e2.substitute ( pTypeSubstitution ) ;
    return new Let ( this.id.clone ( ) , newTau , newE1 , newE2 ) ;
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
     * System.out.println ( "Let:" ) ; System.out.print ( "free: " ) ; for (
     * Identifier tmp : free ( ) ) { System.out.print ( tmp ) ; }
     * System.out.println ( ) ; System.out.print ( "bounded: " ) ; for (
     * Identifier tmp : bounded ( ) ) { System.out.print ( tmp ) ; }
     * System.out.println ( ) ; System.out.println ( ) ;
     */
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LET ) ;
      this.prettyStringBuilder.addKeyword ( "let" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.id
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_LET_TAU ) ;
      }
      this.prettyStringBuilder.addText ( " = " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e1
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E1 ) ;
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBreak ( ) ;
      this.prettyStringBuilder.addKeyword ( "in" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e2
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_LET_E2 ) ;
    }
    return this.prettyStringBuilder ;
  }
}
