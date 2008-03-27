package de.unisiegen.tpml.core.unify;


import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.AbstractProofNode;
import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;
import de.unisiegen.tpml.core.entities.TypeEquationList;
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
 * The heart of unification.
 * 
 * @author Christian Uhrhan
 * @version $Id: UnifyProofNode.java 2792 2008-03-14 12:25:20Z mies $
 * @see de.unisiegen.tpml.core.AbstractProofModel
 */
public class UnifyProofModel extends AbstractProofModel
{

  /**
   * TODO
   * 
   * @param root
   * @param ruleSet
   */
  protected UnifyProofModel ( AbstractProofNode root,
      AbstractProofRuleSet ruleSet )
  {
    super ( root, ruleSet );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void guess ( ProofNode node ) throws ProofGuessException
  {
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  @Override
  public void prove ( ProofRule rule, ProofNode node )
      throws ProofRuleException
  {
  }


  /**
   * TODO
   * 
   * @param node
   * @param substs
   * @param eqns
   */
  public void contextAddProofNode ( UnifyProofNode node,
      TypeSubstitutionList substs, TypeEquationList eqns )
  {

  }


  /**
   * Set the actual side overlap for the exported latex file
   * 
   * @param overlap the side overlapping
   * @see de.unisiegen.tpml.core.ProofModel#setOverlap(int)
   */
  public void setOverlap ( int overlap )
  {
  }


  /**
   * Set the number of pages for the exported latex file
   * 
   * @param pages number of pages
   * @see de.unisiegen.tpml.core.ProofModel#setPages(int)
   */
  public void setPages ( int pages )
  {
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
   * @param latexStringBuilderFactory
   * @param indent
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
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyString()
   */
  public PrettyString toPrettyString ()
  {
    return null;
  }


  /**
   * {@inheritDoc}
   * 
   * @param prettyStringBuilderFactory
   * @see de.unisiegen.tpml.core.prettyprinter.PrettyPrintable#toPrettyStringBuilder(de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory)
   */
  public PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory prettyStringBuilderFactory )
  {
    return null;
  }

}
