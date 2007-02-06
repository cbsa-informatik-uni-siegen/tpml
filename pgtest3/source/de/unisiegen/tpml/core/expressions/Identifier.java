package de.unisiegen.tpml.core.expressions ;


import java.util.Collections ;
import java.util.Set ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Represents an identifier in the expression hierarchy. Identifiers are values
 * wrt the semantics of the various languages.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1056 $
 * @see de.unisiegen.tpml.core.expressions.Value
 */
public class Identifier extends Value
{
  /**
   * The {@link Expression} in which this {@link Identifier} is bounded.
   * 
   * @see #getBoundedExpression()
   * @see #setBoundedExpression(Expression)
   */
  private Expression boundedExpression ;


  /**
   * The start index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #getBoundedStart()
   * @see #setBoundedStart(int)
   */
  private int boundedStart ;


  /**
   * The end index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #getBoundedEnd()
   * @see #setBoundedEnd(int)
   */
  private int boundedEnd ;


  /**
   * The index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #getBoundedIdentifierIndex()
   * @see #setBoundedIdentifierIndex(int)
   */
  private int boundedIdentifierIndex ;


  /**
   * The name of the {@link Identifier}.
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * Allocates a new {@link Identifier} with the given <code>name</code>.
   * 
   * @param pName the name of the identifier.
   */
  public Identifier ( String pName )
  {
    this.name = pName ;
    this.boundedExpression = null ;
    this.boundedStart = - 1 ;
    this.boundedEnd = - 1 ;
    this.boundedIdentifierIndex = - 1 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#clone()
   */
  @ Override
  public Identifier clone ( )
  {
    return new Identifier ( this.name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#equals(java.lang.Object)
   */
  @ Override
  public boolean equals ( Object obj )
  {
    if ( obj instanceof Identifier )
    {
      Identifier other = ( Identifier ) obj ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
  }


  /**
   * Returns a set that contains exacty one element, which is the name of the
   * identifier.
   * 
   * @return a set which contains the name of the identifier.
   * @see #getName()
   * @see de.unisiegen.tpml.core.expressions.Expression#free()
   */
  @ Override
  public Set < String > free ( )
  {
    return Collections.singleton ( this.name ) ;
  }


  /**
   * Returns the end index of this {@link Identifier} in the boundedExpression.
   * 
   * @return The end index of this {@link Identifier} in the boundedExpression.
   * @see #boundedEnd
   * @see #setBoundedEnd(int)
   */
  public int getBoundedEnd ( )
  {
    return this.boundedEnd ;
  }


  /**
   * Returns the {@link Expression} in which this {@link Identifier} is bounded.
   * 
   * @return The {@link Expression} in which this {@link Identifier} is bounded.
   * @see #boundedExpression
   * @see #setBoundedExpression(Expression)
   */
  public Expression getBoundedExpression ( )
  {
    return this.boundedExpression ;
  }


  /**
   * Returns the index of this {@link Identifier} in the boundedExpression.
   * 
   * @return The index of this {@link Identifier} in the boundedExpression.
   * @see #boundedIdentifierIndex
   * @see #setBoundedIdentifierIndex(int)
   */
  public int getBoundedIdentifierIndex ( )
  {
    return this.boundedIdentifierIndex ;
  }


  /**
   * Returns the start index of this {@link Identifier} in the
   * boundedExpression.
   * 
   * @return The start index of this {@link Identifier} in the
   *         boundedExpression.
   * @see #boundedStart
   * @see #setBoundedStart(int)
   */
  public int getBoundedStart ( )
  {
    return this.boundedStart ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Identifier" ; //$NON-NLS-1$
  }


  /**
   * Returns the name of the identifier.
   * 
   * @return the name of the identifier.
   */
  public String getName ( )
  {
    return this.name ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.expressions.Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.name.hashCode ( ) ;
  }


  /**
   * Sets the end index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedEnd The end index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedEnd
   * @see #getBoundedEnd()
   */
  public void setBoundedEnd ( int pBoundedEnd )
  {
    this.boundedEnd = pBoundedEnd ;
  }


  /**
   * Sets the Expression in which this {@link Identifier} is bounded.
   * 
   * @param pBoundedExpression The {@link Expression} in which this
   *          {@link Identifier} is bounded.
   * @see #boundedExpression
   * @see #getBoundedExpression()
   */
  public void setBoundedExpression ( Expression pBoundedExpression )
  {
    this.boundedExpression = pBoundedExpression ;
  }


  /**
   * Sets the index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedIdentifierIndex The index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedIdentifierIndex
   * @see #getBoundedIdentifierIndex()
   */
  public void setBoundedIdentifierIndex ( int pBoundedIdentifierIndex )
  {
    this.boundedIdentifierIndex = pBoundedIdentifierIndex ;
  }


  /**
   * Sets the start index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedStart The start index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedStart
   * @see #getBoundedStart()
   */
  public void setBoundedStart ( int pBoundedStart )
  {
    this.boundedStart = pBoundedStart ;
  }


  /**
   * Returns <code>e</code> if <code>id</code> equals the name of the
   * identifier. Else the identifier itself is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the name of the
   *         identifier, else the identifier itself.
   * @see #getName()
   * @see de.unisiegen.tpml.core.expressions.Expression#substitute(java.lang.String,
   *      de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public Expression substitute ( String id , Expression e )
  {
    if ( id.equals ( getName ( ) ) )
    {
      // we need to clone the expression here to make sure we can distinguish an
      // expression
      // in the pretty printer that is substituted multiple times
      return e.clone ( ) ;
    }
    return this ;
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
    PrettyStringBuilder builder = factory.newBuilder ( this , PRIO_IDENTIFIER ) ;
    builder.addText ( this.name ) ;
    return builder ;
  }
}
