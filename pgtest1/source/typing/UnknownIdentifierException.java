package typing;

/**
 * Represents an exception that is thrown whenever an identifier could
 * not be looked up in a given {@link typing.Environment}.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnknownIdentifierException extends Exception {
  /**
   * Constructs a new <code>UnknownIdentifierException</code> object
   * with the given <code>environment</code> and <code>identifier</code>.
   * 
   * @param environment the type environment on which the lookup failed.
   * @param identifier the identifier that could not be looked up.
   */
  UnknownIdentifierException(Environment environment, String identifier) {
    super("The identifier " + identifier + " was not found in the type environment");
    this.environment = environment;
    this.identifier = identifier;
  }
  
  /**
   * Returns the type environment that caused the exception to be thrown.
   * 
   * @return the type environment that caused the exception to be thrown.
   */
  public Environment getEnvironment() {
    return this.environment;
  }
  
  /**
   * Returns the identifier that could not be looked up in the type
   * environment and thereby caused the exception to be thrown.
   * 
   * @return the identifier that failed to lookup.
   */
  public String getIdentifier() {
    return this.identifier;
  }
  
  // member attributes
  private Environment environment;
  private String identifier;
  
  // unique serialization id
  private static final long serialVersionUID = 1598690001448334161L;
}
