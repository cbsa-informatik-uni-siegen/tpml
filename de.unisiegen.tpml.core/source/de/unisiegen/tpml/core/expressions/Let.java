package de.unisiegen.tpml.core.expressions ;


import java.util.Set ;
import java.util.TreeSet ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Represents the simple binding mechanism <code>let</code>. The string
 * representation is <code>let id = e1 in e2</code>.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.expressions.Expression
 * @see de.unisiegen.tpml.core.expressions.Lambda
 */
public class Let extends Expression
{
  //
  // Attributes
  //
  /**
   * The identifier of the <code>Let</code> expression.
   * 
   * @see #getId()
   */
  protected String id ;


  /**
   * The type for the <code>id</code> (and thereby for <code>e1</code>) or
   * <code>null</code>.
   * 
   * @see #getType()
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


  //
  // Constructor
  //
  /**
   * Allocates a new <code>Let</code> with the specified <code>id</code>,
   * <code>e1</code> and <code>e2</code>.
   * 
   * @param id the name of the identifier.
   * @param tau the type for the <code>id</code> (and thereby for
   *          <code>e1</code>) or <code>null</code>.
   * @param e1 the first expression.
   * @param e2 the second expression.
   * @throws NullPointerException if <code>id</code>, <code>e1</code> or
   *           <code>e2</code> is <code>null</code>.
   */
  public Let ( String id , MonoType tau , Expression e1 , Expression e2 )
  {
    if ( id == null )
    {
      throw new NullPointerException ( "id is null" ) ;
    }
    if ( e1 == null )
    {
      throw new NullPointerException ( "e1 is null" ) ;
    }
    if ( e2 == null )
    {
      throw new NullPointerException ( "e2 is null" ) ;
    }
    this.id = id ;
    this.tau = tau ;
    this.e1 = e1 ;
    this.e2 = e2 ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Let" ; //$NON-NLS-1$
  }


  //
  // Accessors
  //
  /**
   * Returns the identifier of the <code>Let</code> expression.
   * 
   * @return the identifier of the <code>Let</code> expression.
   */
  public String getId ( )
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


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Let clone ( )
  {
    return new Let ( this.id , this.tau , this.e1.clone ( ) , this.e2.clone ( ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see expressions.Expression#free()
   */
  @ Override
  public Set < String > free ( )
  {
    TreeSet < String > free = new TreeSet < String > ( ) ;
    free.addAll ( this.e2.free ( ) ) ;
    free.remove ( this.id ) ;
    free.addAll ( this.e1.free ( ) ) ;
    return free ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  @ Override
  public Let substitute ( TypeSubstitution substitution )
  {
    MonoType tau = ( this.tau != null ) ? this.tau.substitute ( substitution )
        : null ;
    return new Let ( this.id , tau , this.e1.substitute ( substitution ) ,
        this.e2.substitute ( substitution ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression substitute ( String id , Expression e )
  {
    Expression e1 = this.e1.substitute ( id , e ) ;
    Expression e2 = this.id.equals ( id ) ? this.e2 : this.e2.substitute ( id ,
        e ) ;
    return ( this.e1 == e1 && this.e2 == e2 ) ? this : new Let ( this.id ,
        this.tau , e1 , e2 ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public @ Override
  PrettyStringBuilder toPrettyStringBuilder ( PrettyStringBuilderFactory factory )
  {
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_LET ) ;
    builder.addKeyword ( "let" ) ;
    builder.addText ( " " ) ;
    builder.addIdentifier ( this.id ) ;
    if ( this.tau != null )
    {
      builder.addText ( ":" ) ;
      builder.addBuilder ( this.tau.toPrettyStringBuilder ( factory ) ,
          PRIO_LET_TAU ) ;
    }
    builder.addText ( " = " ) ;
    builder.addBuilder ( this.e1.toPrettyStringBuilder ( factory ) ,
        PRIO_LET_E1 ) ;
    builder.addBreak ( ) ;
    builder.addText ( " " ) ;
    builder.addKeyword ( "in" ) ;
    builder.addText ( " " ) ;
    builder.addBuilder ( this.e2.toPrettyStringBuilder ( factory ) ,
        PRIO_LET_E2 ) ;
    return builder ;
  }


  //
  // Pretty printing
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof Let && getClass ( ).equals ( obj.getClass ( ) ) )
    {
      Let other = ( Let ) obj ;
      return ( this.id.equals ( other.id ) && this.e1.equals ( other.e1 )
          && this.e2.equals ( other.e2 ) && ( ( this.tau == null ) ? ( other.tau == null )
          : this.tau.equals ( other.tau ) ) ) ;
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
    return this.id.hashCode ( ) + this.e1.hashCode ( ) + this.e2.hashCode ( ) ;
  }
}
