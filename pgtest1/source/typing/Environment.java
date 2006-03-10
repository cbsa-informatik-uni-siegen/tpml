package typing;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a type environment, which assigns a type to
 * every identifier contained within the type environment.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Environment {
  /**
   * Allocates a new type environment with the specified <code>parent</code>,
   * and the new <code>type</code> for <code>identifier</code>.
   * 
   * @param parent the parent environment.
   * @param identifier the identifier for <code>type</code>.
   * @param type the {@link Type} that should be set for <code>identifier</code>.
   * 
   * @throws IllegalArgumentException if an invalid value is specified for any
   *                                  of the parameters.
   */
  public Environment(Environment parent, String identifier, Type type) {
    // verify that a valid environment is provided
    if (parent == null)
      throw new IllegalArgumentException("You must specify a valid parent environment");
    
    // verify that a valid identifier is provided
    if (identifier == null || identifier.length() == 0)
      throw new IllegalArgumentException("The empty string is not a valid identifier");
    
    // verify that a valid type is provided
    if (type == null)
      throw new IllegalArgumentException("You must specify a valid type");
    
    // initialize attributes
    this.parent = parent;
    this.identifier = identifier;
    this.type = type;
  }
  
  /**
   * Generates the polymorphic closure for the monomorphic
   * type <code>tau</code> in the type environment.
   * 
   * @param tau a mono morphic type. 
   * 
   * @return the polymorphic closure for <code>tau</code>.
   */
  PolyType closure(MonoType tau) {
    // determine the free type variables for tau
    Set<String> freeTau = tau.free();
    
    // determine the free type variable for gamma
    Set<String> freeGamma = free();
    
    // determine the set of free variables in
    // tau without the ones in gamma
    TreeSet<String> free = new TreeSet<String>();
    for (String name : freeTau)
      if (!freeGamma.contains(name))
        free.add(name);
    
    // generate a new polymorphic type
    return new PolyType(free, tau);
  }
  
  /**
   * Extends this type environment with an entry for the pair
   * (<code>identifer</code>,<code>type</code>) and returns the
   * new {@link Environment}.
   *  
   * @param identifier the identifier for <code>type</code>.
   * @param type the {@link Type} that should be set for <code>identifier</code>.
   * 
   * @return the extended environment.
   * 
   * @throws IllegalArgumentException if an invalid value is specified for any
   *                                  of the parameters.
   */
  public Environment extend(String identifier, Type type) {
    return new Environment(this, identifier, type);
  }
  
  /**
   * Returns all free type variables in this type environment,
   * that is the union of all free type variables for the types
   * within this type environment.
   * 
   * @return the set of free type variables in this type
   *         environment.
   */
  public Set<String> free() {
    // check if this is the empty environment
    if (this == EMPTY_ENVIRONMENT)
      return Type.EMPTY_SET;
    
    // determine the free type variables for the parent
    Set<String> freeParent = this.parent.free();
    
    // determine the free type variables for this type
    Set<String> freeType = this.type.free();
    if (freeType == Type.EMPTY_SET)
      return freeParent;
    
    // merge the sets
    TreeSet<String> free = new TreeSet<String>();
    free.addAll(freeParent);
    free.addAll(freeType);
    return free;
  }
  
  /**
   * Looks up the type for the <code>identifier</code> in
   * this type environment.
   * 
   * @param identifier the name of an identifier
   *
   * @return the type for <code>identifier</code>.
   * 
   * @throws IllegalArgumentException if the <code>identifier</code> is not
   *                                  a valid name.
   * @throws UnknownIdentifierException if the <code>identifier</code> is not
   *                                    present in this type environment.
   *
   * @see #identifiers()                                  
   */
  public Type get(String identifier) throws UnknownIdentifierException {
    // verify that a valid identifier is provided
    if (identifier == null || identifier.length() == 0)
      throw new IllegalArgumentException("The empty string is not a valid identifier");
    
    // determine the type for identifier
    Type type = getInternal(identifier);
    
    // check if no type was found
    if (type == null)
      throw new UnknownIdentifierException(this, identifier);
    
    // return the type
    return type;
  }
  
  /**
   * Returns the set of identifiers, which are present in this
   * type environment. For each of the identifiers, a type is
   * registered in the type environment. 
   * 
   * @return the set of identifiers in the type environment.
   * 
   * @see #get(String)
   */
  public Set<String> identifiers() {
    // determine the set of identifiers in the environment
    TreeSet<String> identifiers = new TreeSet<String>();
    for (Environment env = this; env.parent != null; env = env.parent)
      identifiers.add(env.identifier);
    return identifiers;
  }
  
  /**
   * Applies the <code>substitution</code> to all types in
   * the environment and returns the resulting environment.
   * 
   * @param substitution the {@link Substitution}.
   * 
   * @return the resulting {@link Environment}.
   */
  Environment substitute(Substitution substitution) {
    // nothing to substitute in the empty environment
    if (this == EMPTY_ENVIRONMENT)
      return this;
    
    // apply the substitution to the parent
    Environment parent = this.parent.substitute(substitution);

    // apply the substitution to the type
    Type type = this.type.substitute(substitution);
    
    // check anything changed
    if (parent != this.parent || type != this.type)
      return new Environment(parent, this.identifier, type);
    else
      return this;
  }
  
  /**
   * Returns <code>true</code> if <code>obj</code> is an
   * instance of {@link Environment}, the set of identifiers
   * in this environment and <code>obj</code> is the same and
   * for each identifier the types are equal.
   * 
   * @param obj another environment object.
   * 
   * @return <code>true</code> if <code>obj</code> is an
   *         environment and the types are the same.
   * 
   * @see #get(String)
   * @see #identifiers()
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    else if (obj instanceof Environment) {
      Environment env = (Environment)obj;

      // determine the sets of identifiers and compare them
      Set<String> identifiers = identifiers();
      if (!identifiers.equals(env.identifiers()))
        return false;
      
      // verify that the types for all identifiers are the same
      for (String identifier : identifiers) {
        if (!getInternal(identifier).equals(env.getInternal(identifier)))
          return false;
      }
      
      // the environments are the same
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Returns the string representation for the type environment,
   * which contains the type for each identifier in the environment.
   * 
   * @return the string representation for the type environment.
   *
   * @see #get(String)
   * @see #identifiers()
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    // construct the string for the type environment
    StringBuilder builder = new StringBuilder(128);
    builder.append('[');
    
    // add all identifiers for the environment
    for (Environment env = this; env != EMPTY_ENVIRONMENT; env = env.parent) {
      if (env != this)
        builder.append(", ");
      builder.append(env.identifier);
      builder.append(" : ");
      builder.append(env.type);
    }
    
    // finalize the string
    builder.append(']');
    return builder.toString();
  }
  
  /**
   * The empty type environment, shared by all non-empty type environment
   * as absolute parent. 
   */
  public static final Environment EMPTY_ENVIRONMENT = new Environment();
  
  private Environment() {
  }
  
  private Type getInternal(String identifier) {
    if (this == EMPTY_ENVIRONMENT)
      return null;
    else if (this.identifier.equals(identifier))
      return this.type;
    else
      return this.parent.getInternal(identifier);
  }
  
  // member attributes
  private Environment parent;
  private String identifier;
  private Type type;
}
