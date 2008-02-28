package de.unisiegen.tpml.core;


import de.unisiegen.tpml.core.languages.Language;


/**
 * A set of <code>ProofRule</code>s.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.ProofRule
 */
public interface ProofRuleSet extends Iterable < ProofRule >
{

  //
  // Accessors
  //

  /**
   * Returns the <code>Language</code> to which the proof rules within this
   * proof rule set belong.
   * 
   * @return the language of the proof rules.
   * @see de.unisiegen.tpml.core.languages.Language
   */
  public Language getLanguage ();


  /**
   * Returns an array with all <code>ProofRule</code>s in this set of proof
   * rules. The returned array is sorted using the sort order defined by the
   * {@link ProofRule}s implementation of the {@link Comparable} interface. The
   * returned array is sorted using the sort order defined by the
   * {@link ProofRule}s implementation of the {@link Comparable} interface.
   * 
   * @return an array with all <code>ProofRule</code>s.
   * @see ProofRule
   */
  public ProofRule [] getRules ();


  //
  // Primitives
  //

  /**
   * Returns <code>true</code> if the proof rule set contains the specified
   * proof <code>rule</code>, <code>false</code> otherwise.
   * 
   * @param rule the <code>ProofRule</code> to look for.
   * @return <code>true</code> if the proof rule set contains the
   *         <code>rule</code>.
   * @see #getRules()
   */
  public boolean contains ( ProofRule rule );
}
