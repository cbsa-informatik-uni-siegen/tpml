package de.unisiegen.tpml.core.expressions;


import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;


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
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * The string for the <b>(EMPTY-LIST)</b> exception.
   */
  private static final String EMPTY_LIST = "empty_list"; //$NON-NLS-1$


  /**
   * The string for the <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  private static final String DIVIDE_BY_ZERO = "divide_by_zero"; //$NON-NLS-1$


  /**
   * String for the case that the name is null.
   */
  private static final String NAME_NULL = "name is null"; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Exn.class );


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_EXN, 1, "\\mbox{\\color{" //$NON-NLS-1$
        + LATEX_COLOR_EXPRESSION + "}{$\\uparrow$\\ #1}}", "name" ) ); //$NON-NLS-1$//$NON-NLS-2$
    return commands;
  }


  /**
   * The <b>(DIVIDE-BY-ZERO)</b> exception.
   * 
   * @return A new <b>(DIVIDE-BY-ZERO)</b> exception.
   */
  public static final Exn newDivideByZero ()
  {
    return new Exn ( DIVIDE_BY_ZERO );
  }


  /**
   * The <b>(EMPTY-LIST)</b> exception.
   * 
   * @return A new <b>(EMPTY-LIST)</b> exception.
   */
  public static final Exn newEmptyList ()
  {
    return new Exn ( EMPTY_LIST );
  }


  /**
   * The name of the exception.
   * 
   * @see #toString()
   */
  private String name;


  /**
   * Allocates a new <code>Exn</code> instance with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the exception.
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  private Exn ( String pName )
  {
    if ( pName == null )
    {
      throw new NullPointerException ( NAME_NULL );
    }
    this.name = pName;
  }


  /**
   * {@inheritDoc} Cloning is not necessary for exceptions and as such, we just
   * return a reference to <code>this</code> here.
   * 
   * @see Expression#clone()
   */
  @Override
  public Exn clone ()
  {
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Exn )
    {
      Exn other = ( Exn ) pObject;
      return this.name.equals ( other.name );
    }
    return false;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption ()
  {
    return CAPTION;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @Override
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = super.getLatexCommands ();
    commands.add ( getLatexCommandsStatic () );
    return commands;
  }


  /**
   * Returns the name of the exception.
   * 
   * @return the name of the exception.
   */
  public String getName ()
  {
    return this.name;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @Override
  public String getPrefix ()
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_EXN;
    }
    return this.prefix;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#hashCode()
   */
  @Override
  public int hashCode ()
  {
    return this.name.hashCode ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#isException()
   */
  @Override
  public boolean isException ()
  {
    return true;
  }


  /**
   * {@inheritDoc} Substitution below exceptions is not possible, so for the
   * <code>Exn</code> class, this method always returns a reference to the
   * exception itself.
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @Override
  public Exn substitute ( @SuppressWarnings ( UNUSED )
  Identifier pId, Expression pExpression )
  {
    if ( pExpression.getIdentifierFreeNotOnlyVariable () )
    {
      throw new NotOnlyFreeVariableException ();
    }
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @Override
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder (
        PRIO_EXN, LATEX_EXN, pIndent, this.toPrettyString ().toString () );
    builder.addText ( "{" //$NON-NLS-1$
        + this.name.replaceAll ( "_", "\\\\_" ) + "}" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  @Override
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this,
          PRIO_EXN );
      this.prettyStringBuilder.addText ( PRETTY_EXCEPTION );
      this.prettyStringBuilder.addText ( PRETTY_SPACE );
      this.prettyStringBuilder.addText ( this.name );
    }
    return this.prettyStringBuilder;
  }
}
