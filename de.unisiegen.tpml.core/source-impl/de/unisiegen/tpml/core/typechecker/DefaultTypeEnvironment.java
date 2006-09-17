package de.unisiegen.tpml.core.typechecker;

import java.util.Enumeration;

import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.util.AbstractEnvironment;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 */
final class DefaultTypeEnvironment extends AbstractEnvironment<String, Type> implements TypeEnvironment {
  //
  // Constructor (package)
  //
  
  /**
   * Allocates a new empty <code>DefaultTypeEnvironment</code>.
   * 
   * @see AbstractEnvironment#AbstractEnvironment()
   */
  DefaultTypeEnvironment() {
    super();
  }
  
  /**
   * Allocates a new <code>DefaultTypeEnvironment</code> based on the <code>environment</code>.
   * 
   * @param environment the type environment from which to copy the mappings.
   * 
   * @throws NullPointerException if <code>environment</code> is <code>null</code>.
   * 
   * @see AbstractEnvironment#AbstractEnvironment(AbstractEnvironment)
   */
  DefaultTypeEnvironment(DefaultTypeEnvironment environment) {
    super(environment);
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeEnvironment#containsIdentifier(java.lang.String)
   */
  public boolean containsIdentifier(String identifier) {
    return containsSymbol(identifier);
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeEnvironment#identifiers()
   */
  public Enumeration<String> identifiers() {
    return symbols();
  }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.typechecker.TypeEnvironment#extend(java.lang.String, de.unisiegen.tpml.core.types.Type)
   */
  public TypeEnvironment extend(String identifier, Type type) {
    DefaultTypeEnvironment environment = new DefaultTypeEnvironment(this);
    environment.put(identifier, type);
    return environment;
  }
}
