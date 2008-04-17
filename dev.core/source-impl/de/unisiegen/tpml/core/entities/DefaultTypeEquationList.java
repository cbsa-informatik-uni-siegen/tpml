package de.unisiegen.tpml.core.entities;


import java.util.ArrayList;
import java.util.Iterator;

import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
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
    for ( TypeEquation it : this.equations )
    {
      it.substitute ( s );
    }

    return this;
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
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexCommands()
   */
  public LatexCommandList getLatexCommands ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexInstructions()
   */
  public LatexInstructionList getLatexInstructions ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#getLatexPackages()
   */
  public LatexPackageList getLatexPackages ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.latex.LatexPrintable#toLatexString()
   */
  public LatexString toLatexString ()
  {
    return null;
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
    return null;
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

}
