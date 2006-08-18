package de.unisiegen.tpml.core.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Abstract implementation of the generic <code>Environment</code> interface.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.util.Environment
 */
public abstract class AbstractEnvironment<S, E> implements Environment<S, E> {
  //
  // Inner classes
  //
  
  /**
   * The store mappings, mapping locations to values.
   */
  protected static class Mapping<S, E> {
    private S symbol;
    private E entry;
    
    public Mapping(S symbol, E entry) {
      this.symbol = symbol;
      this.entry = entry;
    }
    
    public S getSymbol() {
      return this.symbol;
    }
    
    public E getEntry() {
      return this.entry;
    }
  }
  
  
  
  //
  // Attributes
  //
  
  /**
   * The mappings, the symbol/entry pairs, within this environment.
   */
  protected LinkedList<Mapping<S, E>> mappings;
  
  
  
  //
  // Constructors
  //
  
  /**
   * Default constructor, creates a new empty environment with no mappings.
   */
  protected AbstractEnvironment() {
    // start with an empty mapping
    this.mappings = new LinkedList<Mapping<S, E>>();
  }
  
  /**
   * Creates a new environment, based on the mappings from the specified <code>environment</code>.
   * 
   * @param environment another <code>AbstractEnvironment</code> to inherit the mappings from.
   *                    
   * @throws NullPointerException if the <code>environment</code> is <code>null</code>.                    
   */
  protected AbstractEnvironment(AbstractEnvironment<S, E> environment) {
    if (environment == null) {
      throw new NullPointerException("environment is null");
    }
    // copy-on-write for the mappings
    this.mappings = environment.mappings;
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see common.Environment#containsSymbol(S)
   */
  public boolean containsSymbol(S symbol) {
    if (symbol == null) {
      throw new NullPointerException("symbol is null");
    }
    for (Mapping<S, E> mapping : this.mappings) {
      if (mapping.getSymbol().equals(symbol)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.Environment#get(S)
   */
  public E get(S symbol) {
    if (symbol == null) {
      throw new NullPointerException("symbol is null");
    }
    for (Mapping<S, E> mapping : this.mappings) {
      if (mapping.getSymbol().equals(symbol)) {
        return mapping.getEntry();
      }
    }
    throw new IllegalArgumentException("symbol is invalid");
  }
  
  /**
   * {@inheritDoc}
   *
   * @see common.Environment#symbols()
   */
  public Enumeration<S> symbols() {
    return new Enumeration<S>() {
      private Iterator<Mapping<S, E>> iterator = AbstractEnvironment.this.mappings.iterator();
      public boolean hasMoreElements() { return this.iterator.hasNext(); }
      public S nextElement() { return this.iterator.next().getSymbol(); } 
    };
  }
  
  
  
  //
  // Base methods
  //
  
  /**
   * Returns the string representation for the mutable store. Should be used for debugging purpose only.
   * 
   * @return the string representation.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (Mapping<S, E> mapping : this.mappings) {
      if (builder.length() > 1)
        builder.append(", ");
      builder.append(mapping.getSymbol());
      builder.append(": ");
      builder.append(mapping.getEntry());
    }
    builder.append(']');
    return builder.toString();
  }
}
