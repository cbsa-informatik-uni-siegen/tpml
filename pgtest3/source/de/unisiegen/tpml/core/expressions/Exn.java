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
   * 
   * @return A new <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn newDivideByZero ( )
  {
    return new Exn ( "divide_by_zero" ) ; //$NON-NLS-1$
  }


  /**
   * The <b>(EMPTY-LIST)</b> exception.
   * 
   * @return A new <b>(EMPTY-LIST)</b> exception.
   */
  public static final Exn newEmptyList ( )
  {
    return new Exn ( "empty_list" ) ; //$NON-NLS-1$
  }


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
   */
  private Exn ( final String pName )
  {
    if ( pName == null )
    {
      throw new NullPointerException ( "Name is null" ) ; //$NON-NLS-1$
    }
    this.name = pName ;
  }


  /**
   * {@inheritDoc} Cloning is not necessary for exceptions and as such, we just
   * return a reference to <code>this</code> here.
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
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( final Object pObject )
  {
    if ( pObject instanceof Exn )
    {
      final Exn other = ( Exn ) pObject ;
      return this.name.equals ( other.name ) ;
    }
    return false ;
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
   * TODO
   * 
   * @return TODO
   */
  @ Override
  public String getPrefix ( )
  {
    return Expression.PREFIX_EXN ;
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
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Exn substitute ( final Identifier pId , final Expression pExpression )
  {
    return this.substitute ( pId , pExpression , false ) ;
  }


  /**
   * {@inheritDoc} Substitution below exceptions is not possible, so for the
   * <code>Exn</code> class, this method always returns a reference to the
   * exception itself.
   * 
   * @see Expression#substitute(Identifier, Expression, boolean)
   */
  @ Override
  public Exn substitute ( @ SuppressWarnings ( "unused" )
  final Identifier pId , @ SuppressWarnings ( "unused" )
  final Expression pExpression , @ SuppressWarnings ( "unused" )
  final boolean pAttributeRename )
  {
    return this ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @ Override
  public PrettyStringBuilder toPrettyStringBuilder (
      final PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PrettyPrintPriorities.PRIO_EXN ) ;
      this.prettyStringBuilder.addText ( "\u2191 " + this.name ) ; //$NON-NLS-1$
    }
    return this.prettyStringBuilder ;
  }
}
