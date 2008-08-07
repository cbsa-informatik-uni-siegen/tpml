package de.unisiegen.tpml.core.entities;


import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;


/**
 * takes a type equation list and a type substitution list and delivers us a
 * pretty string in form of '<substitions> CONCAT unify(<equations>)'
 * 
 * @author Christian Uhrhan
 * @version $Id$
 */
public class DefaultUnifyProofExpression implements UnifyProofExpression
{

  /**
   * the type equations we want to render
   */
  private DefaultTypeEquationList equations;


  /**
   * the substitutions we want to render
   */
  private TypeSubstitutionList substitutions;


  /**
   * default constructor
   */
  public DefaultUnifyProofExpression ()
  {
    this.equations = null;
    this.substitutions = null;
  }


  /**
   * @param eqns the type equations
   * @param substs the type substitutions
   */
  public DefaultUnifyProofExpression ( final DefaultTypeEquationList eqns,
      final TypeSubstitutionList substs )
  {
    this.equations = eqns;
    this.substitutions = substs;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.UnifyProofExpression#getTypeEquationList()
   */
  public TypeEquationList getTypeEquationList ()
  {
    return this.equations;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.UnifyProofExpression#getTypeSubstitutionList()
   */
  public TypeSubstitutionList getTypeSubstitutionList ()
  {
    return this.substitutions;
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
    builder.addBuilder ( this.substitutions
        .toPrettyStringBuilder ( prettyStringBuilderFactory ), 0 );
    builder.addText ( PRETTY_SPACE );
    builder.addText ( PRETTY_CONCAT );
    builder.addText ( PRETTY_SPACE );
    builder.addText ( "unify( " ); //$NON-NLS-1$
    if ( this.equations.isEmpty () )
      builder.addText ( PRETTY_EMPTY_SET );
    else
      builder.addBuilder ( this.equations
          .toPrettyStringBuilder ( prettyStringBuilderFactory ), 0 );
    builder.addText ( ") " ); //$NON-NLS-1$
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
   * @see de.unisiegen.tpml.core.entities.UnifyProofExpression#setSubstitutionList(de.unisiegen.tpml.core.typeinference.TypeSubstitutionList)
   */
  public void setSubstitutionList ( final TypeSubstitutionList substitutions )
  {
    this.substitutions = substitutions;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.entities.UnifyProofExpression#setTypeEquationList(de.unisiegen.tpml.core.entities.TypeEquationList)
   */
  public void setTypeEquationList ( final TypeEquationList equations )
  {
    this.equations = ( DefaultTypeEquationList ) equations;
  }

}
