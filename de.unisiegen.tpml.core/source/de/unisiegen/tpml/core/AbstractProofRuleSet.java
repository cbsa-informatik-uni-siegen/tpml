package de.unisiegen.tpml.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for classes implementing the <code>ProofRuleSet</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.ProofRule
 * @see de.unisiegen.tpml.core.ProofRuleSet
 */
public abstract class AbstractProofRuleSet implements ProofRuleSet {
  //
  // Constants
  //
  
  /**
   * An empty array of <code>ProofRule</code>s, used for the {@link #getRules()} implementation.
   * 
   * @see #getRules()
   */
  private static final ProofRule[] EMPTY_ARRAY = new ProofRule[0];
  
  
  
  //
  // Attributes
  //
  
  /**
   * The set of {@link ProofRule}s in this rule set.
   * 
   * @see #getRules()
   */
  protected List<ProofRule> rules = new LinkedList<ProofRule>();
  
  

  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofRuleSet#getRules()
   */
  public ProofRule[] getRules() {
    return this.rules.toArray(EMPTY_ARRAY);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Iterable#iterator()
   */
  public final Iterator<ProofRule> iterator() {
    return new Iterator<ProofRule>() {
      private Iterator<ProofRule> iterator = AbstractProofRuleSet.this.rules.iterator();
      public boolean hasNext() { return this.iterator.hasNext(); }
      public ProofRule next() { return this.iterator.next(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
}
