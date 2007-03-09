package de.unisiegen.tpml.core.expressions ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * Instances of this class represent exceptions in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Expression
 * @see Expression#isException()
 */
public final class Exn extends Expression
{
  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn DIVIDE_BY_ZERO = new Exn ( "divide_by_zero" ) ; //$NON-NLS-1$


  /**
   * The <b>(EMPTY-LIST)</b> exception.
   */
  public static final Exn EMPTY_LIST = new Exn ( "empty_list" ) ; //$NON-NLS-1$


  /**
   * The name of the exception.
   * 
   * @see #toString()
   */
  private String name ;


  /**
   * Allocates a new <code>Exn</code> instance with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the exception.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   * @see #DIVIDE_BY_ZERO
   */
  private Exn ( String pName )
  {
    if ( pName == null )
    {
      throw new NullPointerException ( "Name is null" ) ; //$NON-NLS-1$
    }
    this.name = pName ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getCaption ( )
  {
    return "Exception" ; //$NON-NLS-1$
  }


  /**
   * Returns the name of the exception.
   * 
   * @return the name of the exception.
   */
  public String getName ( )
  {
    return this.name ;
  }


  /**
   * {@inheritDoc} Cloning is not necessary for exceptions and as such, we just
   * a return a reference to <code>this</code> here.
   * 
   * @see Expression#clone()
   */
  @ Override
  public Exn clone ( )
  {
    return this ;
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
   * {@inheritDoc} Substitution below exceptions is not possible, so for the
   * <code>Exn</code> class, this method always returns a reference to the
   * exception itself.
   * 
   * @see Expression#substitute(String, Expression, boolean)
   */
  @ Override
  public Expression substitute ( @ SuppressWarnings ( "unused" )
  String pId , @ SuppressWarnings ( "unused" )
  Expression pExpression , @ SuppressWarnings ( "unused" )
  boolean pAttributeRename )
  {
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#isException()
   */
  @ Override
  public boolean isException ( )
  {
    return true ;
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
        this , PRIO_EXN ) ;
    builder.addText ( "\u2191 " + this.name ) ; //$NON-NLS-1$
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
    if ( pObject instanceof Exn )
    {
      Exn other = ( Exn ) pObject ;
      return this.name.equals ( other.name ) ;
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
    return this.name.hashCode ( ) ;
  }
}
