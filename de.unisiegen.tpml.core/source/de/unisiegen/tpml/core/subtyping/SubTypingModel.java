package de.unisiegen.tpml.core.subtyping;


import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.ProofRuleException;


/**
 * Interface for the proof model in the sub typing proof model. This interface
 * is used to realize sub typing and sub typing rec in one view.
 * 
 * @author Benjamin Mies
 */
public interface SubTypingModel extends ProofModel
{

  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#getRules()
   */
  ProofRule [] getRules ();


  /**
   * {@inheritDoc}
   * 
   * @throws ProofRuleException
   * @see de.unisiegen.tpml.core.ProofModel#prove(de.unisiegen.tpml.core.ProofRule,
   *      de.unisiegen.tpml.core.ProofNode)
   */
  public void prove ( ProofRule rule, ProofNode proofNode )
      throws ProofRuleException;


  /**
   * {@inheritDoc}
   * 
   * @throws ProofGuessException
   * @see de.unisiegen.tpml.core.ProofModel#guess(de.unisiegen.tpml.core.ProofNode)
   */
  public void guess ( ProofNode proofNode ) throws ProofGuessException;


  /**
   * {@inheritDoc}
   * 
   * @throws ProofGuessException
   * @see de.unisiegen.tpml.core.ProofModel#complete(de.unisiegen.tpml.core.ProofNode)
   */
  public void complete ( ProofNode proofNode ) throws ProofGuessException;


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ProofModel#getRoot()
   */
  public SubTypingNode getRoot ();


  /**
   * Sets the advanced value.
   * 
   * @param advanced The advanced value.
   */
  public void setAdvanced ( boolean advanced );
}
