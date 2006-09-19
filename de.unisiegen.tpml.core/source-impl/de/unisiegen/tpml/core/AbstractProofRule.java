package de.unisiegen.tpml.core;

/**
 * Abstract base class for classes implementing the <code>ProofRule</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.ProofRule
 */
public abstract class AbstractProofRule implements ProofRule {
  //
  // Attributes
  //
  
  /**
   * The name of the proof rule.
   * 
   * @see #getName()
   */
  private String name;
  
  
  
  //
  // Constructor (protected)
  //
  
  /**
   * Allocates a new <code>AbstractProofRule</code> with the specified <code>name</code>.
   * 
   * @param name the name of the proof rule.
   * 
   * @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  protected AbstractProofRule(String name) {
    if (name == null) {
      throw new NullPointerException("name is null");
    }
    this.name = name;
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.ProofRule#getName()
   */
  public String getName() {
    return this.name;
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * {@inheritDoc}
   *
   * @see #getName()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }
  
  /**
   * {@inheritDoc}
   *
   * Two proof rules are considered equal if both the name and the class of
   * the proof rule are equal.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AbstractProofRule) {
      AbstractProofRule other = (AbstractProofRule)obj;
      return (this.name.equals(other.name) && this.getClass().equals(other.getClass()));
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.name.hashCode() + this.getClass().hashCode();
  }
}
