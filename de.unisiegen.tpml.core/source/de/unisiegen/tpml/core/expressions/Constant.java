package de.unisiegen.tpml.core.expressions ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.exceptions.NotOnlyFreeVariableException ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
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
   * The unused string.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * String for the case that the text is null.
   */
  private static final String TEXT_NULL = "text is null" ; //$NON-NLS-1$


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
      throw new NullPointerException ( TEXT_NULL ) ;
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
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands.add ( new DefaultLatexCommand ( LATEX_CONSTANT , 1 , "#1" , "c" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return commands ;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  @ Override
  public String getPrefix ( )
  {
    if ( this.prefix == null )
    {
      this.prefix = PREFIX_CONST ;
    }
    return this.prefix ;
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
  public final Constant substitute ( @ SuppressWarnings ( UNUSED )
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
   * @see Expression#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  @ Override
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    if ( this.latexStringBuilder == null )
    {
      this.latexStringBuilder = pLatexStringBuilderFactory.newBuilder ( this ,
          PRIO_CONSTANT , LATEX_CONSTANT , pIndent , this.toPrettyString ( )
              .toString ( ) ) ;
      this.latexStringBuilder.addText ( "{" //$NON-NLS-1$
          + this.text.replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }
    return this.latexStringBuilder ;
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
