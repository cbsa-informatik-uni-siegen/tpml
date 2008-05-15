package de.unisiegen.tpml.core.typeinference;


import de.unisiegen.tpml.core.entities.TypeEquation;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;


/**
 * List of collected {@link TypeSubstitution}s needed in type inference
 * algorithm to perform the {@link TypeEquation}s.
 * 
 * @author Benjamin Mies
 * @author Christian Fehler
 * @author Christian Uhrhan
 * @version $Id$
 */
public class TypeSubstitutionList implements PrettyPrintable, LatexPrintable
{

  /**
   * empty type substitution list
   */
  public static final TypeSubstitutionList EMPTY_LIST = new TypeSubstitutionList ();


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_SUBSTITUTION_LIST, 1,
        "\\color{" + LATEX_COLOR_NONE + "}{\\{}#1\\color{" //$NON-NLS-1$//$NON-NLS-2$
            + LATEX_COLOR_NONE + "}{\\}}", "tsub1, ... , tsubn" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( new DefaultLatexInstruction ( "\\definecolor{" //$NON-NLS-1$
        + LATEX_COLOR_NONE + "}{rgb}{0.0,0.0,0.0}", //$NON-NLS-1$
        LATEX_COLOR_NONE + ": color of normal text" ) ); //$NON-NLS-1$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.COLOR );
    return packages;
  }


  /**
   * The first TypeSubstitution in the list.
   */
  private DefaultTypeSubstitution first;


  /**
   * The remaining equations or <code>null</code>.
   */
  private TypeSubstitutionList remaining;


  /**
   * Allocates a new, empty<code>TypeSubstitutionList</code> .
   */
  private TypeSubstitutionList ()
  {
    super ();
  }


  /**
   * Allocates a new <code>TypeSubstitutionList</code> .
   * 
   * @param pFirst the first type substitution of the new list
   * @param pRemaining the remaining list of substitutions
   */
  private TypeSubstitutionList ( final DefaultTypeSubstitution pFirst,
      final TypeSubstitutionList pRemaining )
  {
    if ( pFirst == null )
    {
      throw new NullPointerException ( "first is null" ); //$NON-NLS-1$
    }
    if ( pRemaining == null )
    {
      throw new NullPointerException ( "remaining is null" ); //$NON-NLS-1$
    }
    this.first = pFirst;
    this.remaining = pRemaining;
  }


  /**
   * add a new type substitution to this list
   * 
   * @param s DefaultTypeSubstitution to add
   * @return extended list of type substitutions
   */
  public TypeSubstitutionList extend ( final DefaultTypeSubstitution s )
  {
    return new TypeSubstitutionList ( s, this );
  }


  /**
   * applies a type substitution to all substitutions in the list
   * 
   * @param s the substitution we want to apply to all substitutions in the list
   * @return substituted list
   */
  public TypeSubstitutionList substitute ( final DefaultTypeSubstitution s )
  {
    TypeSubstitutionList result = TypeSubstitutionList.EMPTY_LIST;

    for ( TypeSubstitutionList l = this ; l != EMPTY_LIST ; l = l.remaining )
      result = l.extend ( ( DefaultTypeSubstitution ) l.first.substitute ( s ) );

    return result;
  }


  /**
   * get the head of this type substitution list
   * 
   * @return first element of this list
   */
  public DefaultTypeSubstitution getFirst ()
  {
    return this.first;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      commands.add ( list.first );
    }
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      instructions.add ( list.first );
    }
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      packages.add ( list.first );
    }
    return packages;
  }


  /**
   * get the tail of the type substitution list
   * 
   * @return the remaing list of substitutions
   */
  public TypeSubstitutionList getRemaining ()
  {
    return this.remaining;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public final LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent )
  {
    StringBuilder body = new StringBuilder ();
    body.append ( PRETTY_CLPAREN );
    int count = 0;
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        body.append ( PRETTY_COMMA );
        body.append ( PRETTY_SPACE );
      }
      body.append ( list.first.toPrettyString ().toString () );
      count++ ;
    }
    body.append ( PRETTY_CRPAREN );
    String descriptions[] = new String [ 2 + count ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body.toString ();
    count = 0;
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      descriptions [ 2 + count ] = list.first.toPrettyString ().toString ();
      count++ ;
    }
    LatexStringBuilder builder = pLatexStringBuilderFactory.newBuilder ( 0,
        LATEX_TYPE_SUBSTITUTION_LIST, pIndent, descriptions );
    builder.addBuilderBegin ();
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
        builder.addText ( DefaultLatexStringBuilder.getIndent ( pIndent
            + LATEX_INDENT ) );
        builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
        builder.addText ( LATEX_COMMA );
        builder.addText ( LATEX_SPACE );
        builder.addText ( "}" ); //$NON-NLS-1$
        builder.addBreak ();
      }
      builder.addBuilder ( list.first.toLatexStringBuilder (
          pLatexStringBuilderFactory, pIndent + LATEX_INDENT * 2 ), 0 );
    }
    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
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
        this, 0 );
    builder.addText ( PRETTY_CLPAREN );
    for ( TypeSubstitutionList list = this ; list != EMPTY_LIST ; list = list.remaining )
    {
      if ( list != this )
      {
        builder.addText ( PRETTY_COMMA );
        builder.addText ( PRETTY_SPACE );
      }
      builder.addBuilder ( list.first
          .toPrettyStringBuilder ( pPrettyStringBuilderFactory ), 0 );
    }
    builder.addText ( PRETTY_CRPAREN );
    return builder;
  }


  /**
   * Returns the string representation for this type substitution list. This
   * method is mainly used for debugging.
   * 
   * @return The pretty printed string representation for this type substitution
   *         list.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
