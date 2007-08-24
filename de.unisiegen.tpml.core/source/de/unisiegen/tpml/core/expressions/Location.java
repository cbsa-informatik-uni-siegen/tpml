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
 * Instances of this class are used to represent memory locations as returned by
 * the <code>ref</code> operator and used by the <code>:=</code> (Assign)
 * and <code>!</code> (Deref) operators.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see Value
 */
public final class Location extends Value
{
  /**
   * The unused string.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The caption of this {@link Expression}.
   */
  private static final String CAPTION = Expression.getCaption ( Location.class ) ;


  /**
   * The name of the location (uses uppercase letters).
   * 
   * @see #getName()
   */
  private String name ;


  /**
   * Allocates a new <code>Location</code> instance with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the memory location.
   */
  public Location ( String pName )
  {
    this.name = pName ;
  }


  /**
   * Allocates a new <code>Location</code> instance with the specified
   * <code>name</code>.
   * 
   * @param pName the name of the memory location.
   * @param pParserStartOffset The start offset of this {@link Expression} in
   *          the source code.
   * @param pParserEndOffset The end offset of this {@link Expression} in the
   *          source code.
   */
  public Location ( String pName , int pParserStartOffset , int pParserEndOffset )
  {
    this ( pName ) ;
    this.parserStartOffset = pParserStartOffset ;
    this.parserEndOffset = pParserEndOffset ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#clone()
   */
  @ Override
  public Location clone ( )
  {
    return new Location ( this.name ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Expression#equals(Object)
   */
  @ Override
  public boolean equals ( Object pObject )
  {
    if ( pObject instanceof Location )
    {
      Location other = ( Location ) pObject ;
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
    return CAPTION ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  @ Override
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = super.getLatexCommands ( ) ;
    commands
        .add ( new DefaultLatexCommand ( LATEX_LOCATION , 1 , "#1" , "name" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return commands ;
  }


  /**
   * Returns the name of the memory location.
   * 
   * @return the name of the memory location.
   */
  public String getName ( )
  {
    return this.name ;
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
   * {@inheritDoc} For <code>Location</code>s, this method always returns the
   * location itself, because substituting below a location is not possible.
   * 
   * @see Expression#substitute(Identifier, Expression)
   */
  @ Override
  public Location substitute ( @ SuppressWarnings ( UNUSED )
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
          PRIO_LOCATION , LATEX_LOCATION , pIndent , this.toPrettyString ( )
              .toString ( ) ) ;
      this.latexStringBuilder.addText ( "{" //$NON-NLS-1$
          + this.name.replaceAll ( "_" , "\\\\_" ) + "}" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return this.latexStringBuilder ;
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
    if ( this.prettyStringBuilder == null )
    {
      this.prettyStringBuilder = pPrettyStringBuilderFactory.newBuilder ( this ,
          PRIO_LOCATION ) ;
      this.prettyStringBuilder.addText ( this.name ) ;
    }
    return this.prettyStringBuilder ;
  }
}
