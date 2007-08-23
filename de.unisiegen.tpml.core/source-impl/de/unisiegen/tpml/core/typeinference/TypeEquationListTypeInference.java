package de.unisiegen.tpml.core.typeinference ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommand ;
import de.unisiegen.tpml.core.latex.LatexCommandNames ;
import de.unisiegen.tpml.core.latex.LatexInstruction ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference
 */
public final class TypeEquationListTypeInference implements LatexPrintable ,
    LatexCommandNames
{
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationListTypeInference()
   */
  public static final TypeEquationListTypeInference EMPTY_LIST = new TypeEquationListTypeInference ( ) ;


  /**
   * The first equation in the list.
   */
  private TypeEquationTypeInference first ;


  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeEquationListTypeInference remaining ;


  /**
   * Allocates a new empty equation list.
   * 
   * @see #EMPTY_LIST
   */
  private TypeEquationListTypeInference ( )
  {
    super ( ) ;
  }


  /**
   * Allocates a new equation list, which basicly extends <code>remaining</code>
   * with a new {@link TypeEquationTypeInference} <code>first</code>.
   * 
   * @param pFirst the new {@link TypeEquationTypeInference}.
   * @param pRemaining an existing {@link TypeEquationListTypeInference}
   * @throws NullPointerException if <code>first</code> or
   *           <code>remaining</code> is <code>null</code>.
   */
  private TypeEquationListTypeInference (
      final TypeEquationTypeInference pFirst ,
      final TypeEquationListTypeInference pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "First is null" ) ; //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "Remaining is null" ) ; //$NON-NLS-1$
    }
    this.first = pFirst ;
    this.remaining = pRemaining ;
  }


  /**
   * Allocates a new {@link TypeEquationListTypeInference}, which extends this
   * equation list with a new {@link TypeEquationTypeInference} for
   * <code>left</code> and <code>right</code>.
   * 
   * @param pTypeEquationTypeInference The new equation.
   * @return the extended {@link TypeEquationListTypeInference}.
   * @throws NullPointerException if <code>left</code> or <code>right</code>
   *           is <code>null</code>.
   */
  public TypeEquationListTypeInference extend (
      TypeEquationTypeInference pTypeEquationTypeInference )
  {
    return new TypeEquationListTypeInference ( pTypeEquationTypeInference ,
        this ) ;
  }


  /**
   * get the head of the type equation list
   * 
   * @return TypeEquation first
   */
  public TypeEquationTypeInference getFirst ( )
  {
    return this.first ;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public TreeSet < LatexCommand > getLatexCommands ( )
  {
    TreeSet < LatexCommand > commands = new TreeSet < LatexCommand > ( ) ;
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE , 1 , "\\{#1\\}" , //$NON-NLS-1$
        "teqn1, ... , teqnn" ) ) ; //$NON-NLS-1$
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexCommand command : list.first.getLatexCommands ( ) )
      {
        commands.add ( command ) ;
      }
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public TreeSet < LatexInstruction > getLatexInstructions ( )
  {
    TreeSet < LatexInstruction > instructions = new TreeSet < LatexInstruction > ( ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexInstruction instruction : list.first.getLatexInstructions ( ) )
      {
        instructions.add ( instruction ) ;
      }
    }
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public TreeSet < LatexPackage > getLatexPackages ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      for ( LatexPackage pack : list.first.getLatexPackages ( ) )
      {
        packages.add ( pack ) ;
      }
    }
    return packages ;
  }


  /**
   * return the tail of the type equation list
   * 
   * @return TypeEquationList remaining
   */
  public TypeEquationListTypeInference getRemaining ( )
  {
    return this.remaining ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ( )
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance ( ) , 0 )
        .toLatexString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory , int pIndent )
  {
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( this ,
        0 , LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE , pIndent ) ;
    builder.addBuilderBegin ( ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
      }
      builder.addBuilder ( list.first.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT ) , 0 ) ;
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * Returns the string representation of the equations contained in this list.
   * This method is mainly useful for debugging purposes.
   * 
   * @return the string representation.
   * @see TypeEquationTypeInference#toString()
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString ( )
  {
    final StringBuilder builder = new StringBuilder ( 128 ) ;
    builder.append ( '{' ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.append ( ", " ) ; //$NON-NLS-1$
      }
      builder.append ( list.first ) ;
    }
    builder.append ( '}' ) ;
    return builder.toString ( ) ;
  }
}
