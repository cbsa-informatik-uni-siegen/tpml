package de.unisiegen.tpml.core.typeinference ;


import java.util.TreeSet ;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand ;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction ;
import de.unisiegen.tpml.core.latex.DefaultLatexPackage ;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexCommandList ;
import de.unisiegen.tpml.core.latex.LatexInstructionList ;
import de.unisiegen.tpml.core.latex.LatexPackage ;
import de.unisiegen.tpml.core.latex.LatexPrintable ;
import de.unisiegen.tpml.core.latex.LatexString ;
import de.unisiegen.tpml.core.latex.LatexStringBuilder ;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.prettyprinter.PrettyString ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory ;


/**
 * A list of <code>TypeEquation</code>s, in the same fashion as a list in
 * OCaml.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @see de.unisiegen.tpml.core.typeinference.TypeEquationTypeInference
 */
public final class TypeEquationListTypeInference implements PrettyPrintable ,
    LatexPrintable
{
  /**
   * The empty equation list.
   * 
   * @see #TypeEquationListTypeInference()
   */
  public static final TypeEquationListTypeInference EMPTY_LIST = new TypeEquationListTypeInference ( ) ;


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( new DefaultLatexCommand (
        LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE , 1 , "\\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{\\{}#1\\color{" //$NON-NLS-1$
            + LATEX_COLOR_NONE + "}{\\}}" , "teqn1, ... , teqnn" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}" , //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ) ; //$NON-NLS-1$
    return instructions ;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static TreeSet < LatexPackage > getLatexPackagesStatic ( )
  {
    TreeSet < LatexPackage > packages = new TreeSet < LatexPackage > ( ) ;
    packages.add ( new DefaultLatexPackage ( "color" ) ) ; //$NON-NLS-1$
    return packages ;
  }


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
  public LatexCommandList getLatexCommands ( )
  {
    LatexCommandList commands = new LatexCommandList ( ) ;
    commands.add ( getLatexCommandsStatic ( ) ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      commands.add ( list.first ) ;
    }
    return commands ;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ( )
  {
    LatexInstructionList instructions = new LatexInstructionList ( ) ;
    instructions.add ( getLatexInstructionsStatic ( ) ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      instructions.add ( list.first ) ;
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
    for ( LatexPackage pack : getLatexPackagesStatic ( ) )
    {
      packages.add ( pack ) ;
    }
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
    StringBuilder body = new StringBuilder ( ) ;
    body.append ( PRETTY_CLPAREN ) ;
    int count = 0 ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        body.append ( PRETTY_COMMA ) ;
        body.append ( PRETTY_SPACE ) ;
      }
      body.append ( list.first.toPrettyString ( ).toString ( ) ) ;
      count ++ ;
    }
    body.append ( PRETTY_CRPAREN ) ;
    String descriptions[] = new String [ 2 + count ] ;
    descriptions [ 0 ] = this.toPrettyString ( ).toString ( ) ;
    descriptions [ 1 ] = body.toString ( ) ;
    count = 0 ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      descriptions [ 2 + count ] = list.first.toPrettyString ( ).toString ( ) ;
      count ++ ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0 ,
        LATEX_TYPE_EQUATION_LIST_TYPE_INFERENCE , pIndent , descriptions ) ;
    builder.addBuilderBegin ( ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE ) ;
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) ) ;
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ) ; //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_COMMA ) ;
        builder.addText ( LATEX_SPACE ) ;
        builder.addText ( "}" ) ; //$NON-NLS-1$
        builder.addBreak ( ) ;
      }
      builder.addBuilder ( list.first.toLatexStringBuilder (
          pLatexStringBuilderFactory , pIndent + LATEX_INDENT * 2 ) , 0 ) ;
    }
    builder.addBuilderEnd ( ) ;
    return builder ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ( )
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance ( ) )
        .toPrettyString ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = pPrettyStringBuilderFactory.newBuilder (
        this , 0 ) ;
    builder.addText ( PRETTY_CLPAREN ) ;
    for ( TypeEquationListTypeInference list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( PRETTY_COMMA ) ;
        builder.addText ( PRETTY_SPACE ) ;
      }
      builder.addBuilder ( list.first
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ) , 0 ) ;
    }
    builder.addText ( PRETTY_CRPAREN ) ;
    return builder ;
  }


  /**
   * Returns the string representation for this type equation list. This method
   * is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type equation
   *         list.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @ Override
  public final String toString ( )
  {
    return toPrettyString ( ).toString ( ) ;
  }
}
