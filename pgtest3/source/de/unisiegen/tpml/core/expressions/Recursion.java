package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


/**
 * Instances of this class are used to represent recursive expressions in the
 * expression hierarchy. The string representation for recursive expressions is
 * <code>rec id.e</code>.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1092 $
 * @see Expression
 */
public final class Recursion extends Expression
{
  /**
   * The identifier for the recursion.
   * 
   * @see #getId()
   */
  private String id ;


  /**
   * The type for the <code>id</code> or <code>null</code>.
   * 
   * @see #getTau()
   */
  private MonoType tau ;


  /**
   * The subexpression for the recursion.
   * 
   * @see #getE()
   */
  private Expression e ;


  /**
   * Allocates a new <code>Recursion</code> with the <code>id</code> and
   * <code>e</code>.
   * 
   * @param pId the identifier.
   * @param pTau the type for the <code>id</code> or <code>null</code>.
   * @param pExpression the sub expression.
   * @throws NullPointerException if <code>id</code> or <code>e</code> is
   *           <code>null</code>.
   */
  public Recursion ( String pId , MonoType pTau , Expression pExpression )
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
  public Recursion clone ( )
  {
    return new Recursion ( this.id , this.tau == null ? null : this.tau
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
    if ( pObject instanceof Recursion )
    {
      Recursion other = ( Recursion ) pObject ;
      return ( ( this.id.equals ( other.id ) ) && ( this.e.equals ( other.e ) ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
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
    return "Recursion" ; //$NON-NLS-1$
  }


  /**
   * Returns the recursion body.
   * 
   * @return the recursion body.
   */
  public Expression getE ( )
  {
    return this.e ;
  }


  /**
   * Returns the identifier for the recursion.
   * 
   * @return the identifier for the recursion.
   */
  public String getId ( )
  {
    return this.id ;
  }


  /**
   * Returns the type for the identifier or <code>null</code> if type
   * inference should be used.
   * 
   * @return the type for the identifier or <code>null</code>.
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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Recursion substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Recursion substitute ( String pId , Expression pExpression ,
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
    String newId = this.id ;
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( this.free ( ) ) ;
    boundRenaming.add ( pExpression.free ( ) ) ;
    boundRenaming.add ( pId ) ;
    newId = boundRenaming.newIdentifier ( this.id ) ;
    /*
     * Substitute the old Identifier only with the new Identifier, if they are
     * different.
     */
    Expression newE = this.e ;
    if ( ! this.id.equals ( newId ) )
    {
      newE = newE.substitute ( this.id , new Identifier ( newId ) ,
          pAttributeRename ) ;
    }
    /*
     * Perform the substitution.
     */
    newE = newE.substitute ( pId , pExpression , pAttributeRename ) ;
    return new Recursion ( newId ,
        this.tau == null ? null : this.tau.clone ( ) , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Recursion substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau == null ) ? null : this.tau
        .substitute ( pTypeSubstitution ) ;
    Expression newE = this.e.substitute ( pTypeSubstitution ) ;
    return new Recursion ( this.id , newTau , newE ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_REC ) ;
      this.prettyStringBuilder.addKeyword ( "rec" ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addText ( " " ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addIdentifier ( this.id ) ;
      if ( this.tau != null )
      {
        this.prettyStringBuilder.addText ( ": " ) ; //$NON-NLS-1$
        this.prettyStringBuilder.addBuilder ( this.tau
            .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
            PRIO_REC_TAU ) ;
      }
      this.prettyStringBuilder.addText ( "." ) ; //$NON-NLS-1$
      this.prettyStringBuilder.addBuilder ( this.e
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_REC_E ) ;
    }
    return this.prettyStringBuilder ;
  }
}
