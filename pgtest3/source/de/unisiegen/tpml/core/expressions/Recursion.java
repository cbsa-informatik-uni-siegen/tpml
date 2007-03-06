package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.util.Free ;


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
   */
  @ Override
  public String getCaption ( )
  {
    return "Recursion" ; //$NON-NLS-1$
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
   * Returns the recursion body.
   * 
   * @return the recursion body.
   */
  public Expression getE ( )
  {
    return this.e ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Recursion clone ( )
  {
    return new Recursion ( this.id , this.tau , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#free()
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.e.free ( ) ) ;
    free.remove ( this.id ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#substitute(TypeSubstitution)
   */
  @ Override
  public Recursion substitute ( TypeSubstitution pTypeSubstitution )
  {
    MonoType newTau = ( this.tau != null ) ? this.tau
        .substitute ( pTypeSubstitution ) : null ;
    return new Recursion ( this.id , newTau , this.e
        .substitute ( pTypeSubstitution ) ) ;
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
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Recursion substitute ( String pId , Expression pExpression ,
      boolean pAttributeRename )
  {
    if ( this.id.equals ( pId ) )
    {
      return this ;
    }
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.free ( ) ) ;
    free.addAll ( pExpression.free ( ) ) ;
    free.add ( pId ) ;
    String newId = Free.newIdentifier ( this.id , free ) ;
    Expression newE = this.e ;
    if ( ! this.id.equals ( newId ) )
    {
      newE = this.e.substitute ( this.id , new Identifier ( newId ) ,
          pAttributeRename ) ;
    }
    return new Recursion ( newId , this.tau , newE.substitute ( pId ,
        pExpression , pAttributeRename ) ) ;
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
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_REC ) ;
    builder.addKeyword ( "rec" ) ; //$NON-NLS-1$
    builder.addText ( " " ) ; //$NON-NLS-1$
    builder.addIdentifier ( this.id ) ;
    if ( this.tau != null )
    {
      builder.addText ( ": " ) ; //$NON-NLS-1$
      builder
          .addBuilder ( this.tau
              .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) ,
              PRIO_REC_TAU ) ;
    }
    builder.addText ( "." ) ; //$NON-NLS-1$
    builder.addBuilder ( this.e
        .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , PRIO_REC_E ) ;
    return builder ;
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
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.id.hashCode ( )
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 )
        + this.e.hashCode ( ) ;
  }
}
