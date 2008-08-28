package de.unisiegen.tpml.core.entities;


import java.util.ArrayList;
import java.util.Iterator;

import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.DefaultLatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.typechecker.UnificationException;


/**
 * A list of <code>TypeEquation</code>s.
 */
public class DefaultTypeEquationList implements TypeEquationList
{

  /**
   * list of type equations
   */
  private ArrayList < TypeEquation > equations;


  /**
   * The start offset of this {@link DefaultTypeEquationList} in the source
   * code.
   * 
   * @see #getParserStartOffset()
   * @see #setParserStartOffset(int)
   */
  protected int parserStartOffset = -1;


  /**
   * The end offset of this {@link DefaultTypeEquationList} in the source code.
   * 
   * @see #getParserEndOffset()
   * @see #setParserEndOffset(int)
   */
  protected int parserEndOffset = -1;


  /**
   * Allocates a new <code>DefaultTypeEquationList</code>
   */
  public DefaultTypeEquationList ()
  {
    this.equations = new ArrayList < TypeEquation > ();
  }


  /**
   * Allocates a new <code>DefaultTypeEquationList</code> with the specified
   * equation list
   * 
   * @param eqns list of type equations
   */
  private DefaultTypeEquationList ( ArrayList < TypeEquation > eqns )
  {
    this.equations = eqns;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#isEmpty()
   */
  public boolean isEmpty ()
  {
    return this.equations.isEmpty ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#extend(de.unisiegen.tpml.core.entities.TypeEquation)
   */
  public TypeEquationList extend ( TypeEquation typeEquation )
  {
    ArrayList < TypeEquation > result = new ArrayList < TypeEquation > ();
    result.addAll ( this.equations );
    result.add ( typeEquation );
    return new DefaultTypeEquationList ( result );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#getFirst()
   */
  public TypeEquation getFirst ()
  {
    if ( isEmpty () )
      return null;
    return this.equations.get ( 0 );
  }


  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   * @see #parserStartOffset
   * @see #setParserStartOffset(int)
   */
  public int getParserStartOffset ()
  {
    return this.parserStartOffset;
  }


  /**
   * Sets the parser start offset.
   * 
   * @param pParserStartOffset The new parser start offset.
   * @see #getParserStartOffset()
   * @see #parserStartOffset
   */
  public void setParserStartOffset ( int pParserStartOffset )
  {
    this.parserStartOffset = pParserStartOffset;
  }


  /**
   * Sets the parser end offset.
   * 
   * @param pParserEndOffset The new parser end offset.
   * @see #getParserEndOffset()
   * @see #parserEndOffset
   */
  public void setParserEndOffset ( int pParserEndOffset )
  {
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   * @see #parserEndOffset
   * @see #setParserEndOffset(int)
   */
  public int getParserEndOffset ()
  {
    return this.parserEndOffset;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#getRemaining()
   */
  public TypeEquationList getRemaining ()
  {
    if ( isEmpty () )
    {
      return new DefaultTypeEquationList ( new ArrayList < TypeEquation > () );
    }

    ArrayList < TypeEquation > result = new ArrayList < TypeEquation > ();
    result.addAll ( this.equations );
    result.remove ( 0 );

    return new DefaultTypeEquationList ( result );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#substitute(de.unisiegen.tpml.core.typechecker.TypeSubstitution)
   */
  public TypeEquationList substitute ( TypeSubstitution s )
  {
    ArrayList < TypeEquation > result = new ArrayList < TypeEquation > ();

    for ( TypeEquation it : this.equations )
    {
      result.add ( it.substitute ( s ) );
    }

    return new DefaultTypeEquationList ( result );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#unify()
   */
  public TypeSubstitution unify () throws UnificationException
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    PrettyStringBuilder builder = prettyStringBuilderFactory.newBuilder ( this,
        0 );
    builder.addText ( PRETTY_CLPAREN );

    for ( TypeEquation it : this.equations )
    {
      if ( it != getFirst () )
      {
        builder.addText ( PRETTY_COMMA );
        builder.addText ( PRETTY_SPACE );
      }
      builder.addBuilder ( it
          .toPrettyStringBuilder ( prettyStringBuilderFactory ), 0 );
    }
    builder.addText ( PRETTY_CRPAREN );
    return builder;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_TYPE_EQUATION_LIST, 1,
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
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    for ( TypeEquation equation : this.equations )
    {
      commands.add ( equation );
    }
    return commands;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    for ( TypeEquation equation : this.equations )
    {
      instructions.add ( equation );
    }
    return instructions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    for ( TypeEquation equation : this.equations )
    {
      packages.add ( equation );
    }
    return packages;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexStringBuilder(de.unisiegen.tpml.core.latex.LatexStringBuilderFactory,
   *      int)
   */
  public LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory latexStringBuilderFactory, int indent )
  {
    StringBuilder body = new StringBuilder ();
    body.append ( PRETTY_CLPAREN );
    int count = 0;
    for ( TypeEquation equation : this.equations )
    {
      body.append ( PRETTY_COMMA );
      body.append ( PRETTY_SPACE );
      body.append ( equation.toPrettyString ().toString () );
      count++ ;
    }

    body.append ( PRETTY_CRPAREN );
    String descriptions[] = new String [ 2 + count ];
    descriptions [ 0 ] = this.toPrettyString ().toString ();
    descriptions [ 1 ] = body.toString ();
    count = 0;
    for ( TypeEquation equation : this.equations )
    {
      descriptions [ 2 + count ] = equation.toPrettyString ().toString ();
      count++ ;
    }

    LatexStringBuilder builder = latexStringBuilderFactory.newBuilder ( 0,
        LATEX_TYPE_EQUATION_LIST, indent, descriptions );
    builder.addBuilderBegin ();
    for ( TypeEquation equation : this.equations )
    {
      builder.addText ( LATEX_LINE_BREAK_SOURCE_CODE );
      builder.addText ( DefaultLatexStringBuilder.getIndent ( indent
          + LATEX_INDENT ) );
      builder.addText ( "\\color{" + LATEX_COLOR_NONE + "}{" ); //$NON-NLS-1$ //$NON-NLS-2$
      builder.addText ( LATEX_COMMA );
      builder.addText ( LATEX_SPACE );
      builder.addText ( "}" ); //$NON-NLS-1$
      builder.addBreak ();
      builder.addBuilder ( equation.toLatexStringBuilder (
          latexStringBuilderFactory, indent + LATEX_INDENT * 2 ), 0 );
    }

    builder.addBuilderEnd ();
    return builder;
  }


  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Iterable#iterator()
   */
  public Iterator < TypeEquation > iterator ()
  {
    return this.equations.iterator ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.TypeEquationList#size()
   */
  public int size ()
  {
    return this.equations.size ();
  }

}
