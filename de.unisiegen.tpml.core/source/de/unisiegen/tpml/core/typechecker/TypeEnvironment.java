package de.unisiegen.tpml.core.typechecker;

import java.util.Enumeration;
import java.util.Set;

import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.PolyType;
import de.unisiegen.tpml.core.types.Type;
import de.unisiegen.tpml.core.types.TypeVariable;
import de.unisiegen.tpml.core.util.Environment;

/**
 * Base interface to the type environments used within the type checker.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.util.Environment
 */
public interface TypeEnvironment extends Environment<String, Type> {
  //
  // Primitives
  //
  
  /**
   * Returns the polymorphic closure of <code>tau</code> in this type environment.
   * 
   * @param tau the monomorphic type.
   * 
   * @return the polymorphic closure of <code>tau</code>.
   * 
   * @throws NullPointerException if <code>tau</code> is <code>null</code>.
   * 
   * @see PolyType
   */
  public PolyType closure(MonoType tau);
  
  /**
   * Returns <code>true</code> if the type environment contains a mapping for the <code>identifier</code>.
   * 
   * @param identifier an identifier.
   * 
   * @return <code>true</code> if the <code>identifier</code> exists in this type environment.
   * 
   * @throws NullPointerException if <code>identifier</code> is <code>null</code>.
   * 
   * @see Environment#containsSymbol(S)
   */
  public boolean containsIdentifier(String identifier);
  
  /**
   * Returns the free type variables of all types in this type environment.
   * 
   * @return the set of free type variables in this type environment.
   * 
   * @see Type#free()
   */
  public Set<TypeVariable> free();
  
  /**
   * Returns the identifiers for which mappings exist within this type environment.
   * 
   * @return the identifiers present in this type environment.
   * 
   * @see Environment#symbols()
   */
  public Enumeration<String> identifiers();
  
  /**
   * Extends this type environment with an entry for the pair (<code>identifier</code>,<code>type</code>)
   * and returns the new {@link TypeEnvironment}.
   * 
   * @param identifier the identifier for the <code>type</code>.
   * @param type the {@link Type} that should be set for <code>identifier</code>.
   * 
   * @return the extended type environment.
   * 
   * @throws NullPointerException if <code>identifier</code> or <code>type</code> is <code>null</code>.
   */
  public TypeEnvironment extend(String identifier, Type type);
  
  /**
   * Applies the substitution <code>s</code> to all types in this type environment and returns the
   * new <code>TypeEnvironment</code>, which may be equal to this type environment if <code>s</code>
   * did not change any of the types within.
   * 
   * @param s the {@link TypeSubstitution} to apply.
   * 
   * @return the resulting {@link TypeEnvironment}.
   * 
   * @throws NullPointerException if <code>s</code> is <code>null</code>.
   * 
   * @see Type#substitute(TypeSubstitution)
   */
  public TypeEnvironment substitute(TypeSubstitution s);
}
