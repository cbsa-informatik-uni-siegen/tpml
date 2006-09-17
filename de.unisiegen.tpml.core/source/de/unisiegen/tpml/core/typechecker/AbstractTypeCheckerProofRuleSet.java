package de.unisiegen.tpml.core.typechecker;

import de.unisiegen.tpml.core.AbstractProofRuleSet;
import de.unisiegen.tpml.core.languages.Language;

/**
 * Abstract base class for type checker proof rule sets.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.AbstractProofRuleSet
 */
public abstract class AbstractTypeCheckerProofRuleSet extends AbstractProofRuleSet {
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractTypeCheckerProofRuleSet</code> for the specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the type checker proof rules in this set belong.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  protected AbstractTypeCheckerProofRuleSet(Language language) {
    super(language);
  }
}
