package de.unisiegen.tpml.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.unisiegen.tpml.core.languages.Language;

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
   * The {@link Language} to which the proof rule set belongs.
   * 
   * @see #getLanguage()
   */
  private Language language;
  
  /**
   * The set of {@link ProofRule}s in this rule set.
   * 
   * @see #getRules()
   */
  protected List<ProofRule> rules = new LinkedList<ProofRule>();
  
  

  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractProofRuleSet</code> with the specified <code>language</code>.
   * 
   * @param language the {@link Language} to which the rules in this proof rule set belong.
   * 
   * @throws NullPointerException if <code>language</code> is <code>null</code>.
   */
  protected AbstractProofRuleSet(Language language) {
    if (language == null) {
      throw new NullPointerException("language is null");
    }
    this.language = language;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofRuleSet#getLanguage()
   */
  public Language getLanguage() {
    return this.language;
  }
  
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
   * @see de.unisiegen.tpml.core.ProofRuleSet#contains(de.unisiegen.tpml.core.ProofRule)
   */
  public boolean contains(ProofRule rule) {
    return this.rules.contains(rule);
  }
  
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
  
  
  
  //
  // Rule registration
  //
  
  /**
   * Registers the <code>rule</code> in the proof rule set. If a rule with the same name
   * was already added to the proof rule set, the previous rule is unregistered first.
   * 
   * @param rule the proof rule to register.
   * 
   * @throws NullPointerException if <code>rule</code> is <code>null</code>.
   */
  protected void register(AbstractProofRule rule) {
    if (rule == null) {
      throw new NullPointerException("rule is null");
    }

    // unregister any previous rule with the same name
    this.rules.remove(rule);
    
    // register the new rule
    this.rules.add(rule);
  }
}
