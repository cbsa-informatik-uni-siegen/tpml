package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
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
public final class Identifier extends Value
{
  /**
   * The {@link Expression} in which this {@link Identifier} is bounded.
   * 
   * @see #boundedExpression()
   * @see #boundedExpression(Expression)
   */
  private Expression boundedExpression ;


  /**
   * The start index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #boundedStart()
   * @see #boundedStart(int)
   */
  private int boundedStart ;


  /**
   * The end index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #boundedEnd()
   * @see #boundedEnd(int)
   */
  private int boundedEnd ;


  /**
   * The index of this {@link Identifier} in the boundedExpression.
   * 
   * @see #boundedIdentifierIndex()
   * @see #boundedIdentifierIndex(int)
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
   * Returns the end index of this {@link Identifier} in the boundedExpression.
   * 
   * @return The end index of this {@link Identifier} in the boundedExpression.
   * @see #boundedEnd
   * @see #boundedEnd(int)
   */
  public int boundedEnd ( )
  {
    return this.boundedEnd ;
  }


  /**
   * Sets the end index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedEnd The end index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedEnd
   * @see #boundedEnd()
   */
  public void boundedEnd ( int pBoundedEnd )
  {
    this.boundedEnd = pBoundedEnd ;
  }


  /**
   * Returns the {@link Expression} in which this {@link Identifier} is bounded.
   * 
   * @return The {@link Expression} in which this {@link Identifier} is bounded.
   * @see #boundedExpression
   * @see #boundedExpression(Expression)
   */
  public Expression boundedExpression ( )
  {
    return this.boundedExpression ;
  }


  /**
   * Sets the Expression in which this {@link Identifier} is bounded.
   * 
   * @param pBoundedExpression The {@link Expression} in which this
   *          {@link Identifier} is bounded.
   * @see #boundedExpression
   * @see #boundedExpression()
   */
  public void boundedExpression ( Expression pBoundedExpression )
  {
    this.boundedExpression = pBoundedExpression ;
  }


  /**
   * Returns the index of this {@link Identifier} in the boundedExpression.
   * 
   * @return The index of this {@link Identifier} in the boundedExpression.
   * @see #boundedIdentifierIndex
   * @see #boundedIdentifierIndex(int)
   */
  public int boundedIdentifierIndex ( )
  {
    return this.boundedIdentifierIndex ;
  }


  /**
   * Sets the index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedIdentifierIndex The index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedIdentifierIndex
   * @see #boundedIdentifierIndex()
   */
  public void boundedIdentifierIndex ( int pBoundedIdentifierIndex )
  {
    this.boundedIdentifierIndex = pBoundedIdentifierIndex ;
  }


  /**
   * Returns the start index of this {@link Identifier} in the
   * boundedExpression.
   * 
   * @return The start index of this {@link Identifier} in the
   *         boundedExpression.
   * @see #boundedStart
   * @see #boundedStart(int)
   */
  public int boundedStart ( )
  {
    return this.boundedStart ;
  }


  /**
   * Sets the start index of this {@link Identifier} in the boundedExpression.
   * 
   * @param pBoundedStart The start index of this {@link Identifier} in the
   *          boundedExpression.
   * @see #boundedStart
   * @see #boundedStart()
   */
  public void boundedStart ( int pBoundedStart )
  {
    this.boundedStart = pBoundedStart ;
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
  public TreeSet < String > free ( )
  {
    if ( this.free == null )
    {
      this.free = new TreeSet < String > ( ) ;
      this.free.add ( this.name ) ;
    }
    return this.free ;
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
   * Returns <code>e</code> if <code>id</code> equals the name of the
   * identifier. Else the identifier itself is returned.
   * 
   * @return <code>e</code> if <code>id</code> equals the name of the
   *         identifier, else the identifier itself.
   * @see #getName()
   */
  @ Override
  public Expression substitute ( String id , Expression e ,
      @ SuppressWarnings ( "unused" )
      boolean pAttributeRename )
  {
    if ( id.equals ( getName ( ) ) )
    {
      /*
       * We need to clone the expression here to make sure we can distinguish an
       * expression in the pretty printer that is substituted multiple times
       */
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
