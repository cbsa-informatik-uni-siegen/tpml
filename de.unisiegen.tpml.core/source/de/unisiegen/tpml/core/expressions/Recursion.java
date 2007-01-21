package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Instances of this class are used to represent recursive expressions in the
 * expression hierarchy. The string representation for recursive expressions is
 * <code>rec id.e</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Expression
 */
public final class Recursion extends Expression
{
  //
  // Attributes
  //
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


  //
  // Constructor
  //
  /**
   * Allocates a new <code>Recursion</code> with the <code>id</code> and
   * <code>e</code>.
   * 
   * @param id the identifier.
   * @param tau the type for the <code>id</code> or <code>null</code>.
   * @param e the sub expression.
   * @throws NullPointerException if <code>id</code> or <code>e</code> is
   *           <code>null</code>.
   */
  public Recursion ( String id , MonoType tau , Expression e )
  {
    if ( id == null )
    {
      throw new NullPointerException ( "id is null" ) ;
    }
    if ( e == null )
    {
      throw new NullPointerException ( "e is null" ) ;
    }
    this.id = id ;
    this.tau = tau ;
    this.e = e ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Recursion" ; //$NON-NLS-1$
  }


  //
  // Accessors
  //
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


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Recursion clone ( )
  {
    return new Recursion ( this.id , this.tau , this.e.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
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
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public Recursion substitute ( TypeSubstitution substitution )
  {
    MonoType tau = ( this.tau != null ) ? this.tau.substitute ( substitution )
        : null ;
    return new Recursion ( this.id , tau , this.e.substitute ( substitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Recursion substitute ( String id , Expression e )
  {
    if ( this.id.equals ( id ) )
    {
      return this ;
    }
    else
    {
      // determine the free identifiers for e
      Set < String > free = e.free ( ) ;
      // generate a new unique identifier
      String newId = this.id ;
      while ( free.contains ( newId ) )
        newId = newId + "'" ;
      // perform the bound renaming
      Expression newE = this.e.substitute ( this.id , new Identifier ( newId ) ) ;
      // perform the substitution
      return new Recursion ( newId , this.tau , newE.substitute ( id , e ) ) ;
    }
  }


  //
  // Pretty printing
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_REC ) ;
    builder.addKeyword ( "rec" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.id ) ;
    if ( this.tau != null )
    {
      builder.addText ( ":" ) ;
      builder.addBuilder ( this.tau.toPrettyStringBuilder ( factory ) ,
          PRIO_REC_TAU ) ;
    }
    builder.addText ( "." ) ;
    builder.addBuilder ( this.e.toPrettyStringBuilder ( factory ) , PRIO_REC_E ) ;
    return builder ;
  }


  //
  // Base methods
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof Recursion )
    {
      Recursion other = ( Recursion ) obj ;
      return ( this.id.equals ( other.id ) && this.e.equals ( other.e ) && ( ( this.tau == null ) ? ( other.tau == null )
          : ( this.tau.equals ( other.tau ) ) ) ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.id.hashCode ( )
        + ( ( this.tau != null ) ? this.tau.hashCode ( ) : 0 )
        + this.e.hashCode ( ) ;
  }
}
