package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Abstract class to represent a constant expression (only values can be
 * constants).
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Value
 */
public abstract class Constant extends Value
{
  /**
   * The text representation of the constant.
   * 
   * @see #getText()
   * @see #toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  protected String text ;


  /**
   * Allocates a new <code>Constant</code> with the string representation
   * given in <code>text</code>.
   * 
   * @param pText the string representation of the constant.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  protected Constant ( String pText )
  {
    if ( pText == null )
    {
      throw new NullPointerException ( "text is null" ) ; //$NON-NLS-1$
    }
    this.text = pText ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public abstract Constant clone ( ) ;


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( ( pObject instanceof Constant )
        && ( this.getClass ( ).equals ( pObject.getClass ( ) ) ) )
    {
      Constant other = ( Constant ) pObject ;
      return this.text.equals ( other.text ) ;
    }
    return false ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public abstract String getCaption ( ) ;


  /**
   * Returns the text representation for this constant, just like
   * {@link #toString()}.
   * 
   * @return the text representation.
   * @see #toString()
   */
  public String getText ( )
  {
    return this.text ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @ Override
  public int hashCode ( )
  {
    return this.text.hashCode ( ) + getClass ( ).hashCode ( ) ;
  }


  /**
   * {@inheritDoc} Substitution below constants is not possible, so for
   * <code>Constant</code>s this method will always return the constant
   * itself.
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public final Constant substitute ( @ SuppressWarnings ( "unused" )
  Identifier pId , Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable ( ) )
    {
      throw new NotOnlyFreeVariableException ( ) ;
    }
    return this ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_CONSTANT ) ;
      this.prettyStringBuilder.addConstant ( this.text ) ;
    }
    return this.prettyStringBuilder ;
  }
}
