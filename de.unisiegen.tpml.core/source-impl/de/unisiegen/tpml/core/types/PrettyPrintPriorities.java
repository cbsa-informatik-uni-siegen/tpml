package de.unisiegen.tpml.core.types;

/**
 * This interface includes the pretty print priorities, according to the priority grammar, for the various
 * types. It is implemented by the {@link de.unisiegen.tpml.core.types.Type} class, so one can easily use
 * the constants in this interface without having to explicitly use this interface each time.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.types.Type
 * @see de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder
 */
interface PrettyPrintPriorities {
  /**
   * The pretty print priority for primitive types.
   */
  public static final int PRIO_PRIMITIVE = 2;
  
  /**
   * The pretty print priority for type variables.
   */
  public static final int PRIO_TYPE_VARIABLE = 2;
  
  /**
   * The pretty print priority for arrow types.
   */
  public static final int PRIO_ARROW = 0;
  
  /**
   * The pretty print priority for the first type in an arrow type.
   */
  public static final int PRIO_ARROW_TAU1 = PRIO_ARROW + 1;
  
  /**
   * The pretty print priority for the second type in an arrow type.
   */
  public static final int PRIO_ARROW_TAU2 = PRIO_ARROW;
  
  /**
   * The pretty print priority for tuple types.
   */
  public static final int PRIO_TUPLE = 1;
  
  /**
   * The pretty print priority for elements in tuple types.
   */
  public static final int PRIO_TUPLE_TAU = PRIO_TUPLE + 1;
}
