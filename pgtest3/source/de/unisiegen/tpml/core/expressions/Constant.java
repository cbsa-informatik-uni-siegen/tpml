package de.unisiegen.tpml.core.expressions ;


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
   */
  @ Override
  public String getCaption ( )
  {
    return "Constant" ; //$NON-NLS-1$
  }


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
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( String pId , Expression pExpression )
  {
    return substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc} Substitution below constants is not possible, so for
   * <code>Constant</code>s this method will always return the constant
   * itself.
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public final Expression substitute ( @ SuppressWarnings ( "unused" )
  String pId , @ SuppressWarnings ( "unused" )
  Expression pExpression , @ SuppressWarnings ( "unused" )
  boolean pAttributeRename )
  {
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
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , PRIO_CONSTANT ) ;
    builder.addConstant ( this.text ) ;
    return builder ;
  }


  /**
   * {@inheritDoc} This method is overwritten in <code>Constant</code> for
   * optimization reasons, because for example the <code>InfixOperation</code>
   * needs only the string representation of the <code>BinaryOperator</code>,
   * but no pretty print information and as such, it suffices to return the
   * constant's string representation directly here, without going through the
   * pretty printing.
   * 
   * @see Expression#toString()
   */
  @ Override
  public String toString ( )
  {
    return this.text ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Constant )
    {
      Constant other = ( Constant ) pObject ;
      return ( this.text.equals ( other.text ) && getClass ( ).equals (
          other.getClass ( ) ) ) ;
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
    return this.text.hashCode ( ) + getClass ( ).hashCode ( ) ;
  }
}