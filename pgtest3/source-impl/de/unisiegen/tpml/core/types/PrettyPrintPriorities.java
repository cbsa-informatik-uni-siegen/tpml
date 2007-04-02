package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder ;


/**
 * This interface includes the pretty print priorities, according to the
 * priority grammar, for the various types. It is implemented by the
 * {@link Type} class, so one can easily use the constants in this interface
 * without having to explicitly use this interface each time.
 * 
 * @author Benedikt Meurer
 * @version $Rev:339 $
 * @see Type
 * @see PrettyStringBuilder
 */
interface PrettyPrintPriorities
{
  /**
   * The pretty print priority for object types.
   */
  public static final int PRIO_OBJECT = 2 ;


  /**
   * The pretty print priority for the row type in object types.
   */
  public static final int PRIO_OBJECT_ROW = 2 ;


  /**
   * The pretty print priority for row types.
   */
  public static final int PRIO_ROW = 2 ;


  /**
   * The pretty print priority for the type in row types.
   */
  public static final int PRIO_ROW_TAU = 2 ;


  /**
   * The pretty print priority for primitive types.
   */
  public static final int PRIO_PRIMITIVE = 2 ;


  /**
   * The pretty print priority for type variables.
   */
  public static final int PRIO_TYPE_VARIABLE = 2 ;


  /**
   * The pretty print priority for arrow types.
   */
  public static final int PRIO_ARROW = 0 ;


  /**
   * The pretty print priority for the first type in an arrow type.
   */
  public static final int PRIO_ARROW_TAU1 = PRIO_ARROW + 1 ;


  /**
   * The pretty print priority for the second type in an arrow type.
   */
  public static final int PRIO_ARROW_TAU2 = PRIO_ARROW ;


  /**
   * The pretty print priority for tuple types.
   */
  public static final int PRIO_TUPLE = 1 ;


  /**
   * The pretty print priority for elements in tuple types.
   */
  public static final int PRIO_TUPLE_TAU = PRIO_TUPLE + 1 ;


  /**
   * The pretty print priority for reference types.
   */
  public static final int PRIO_REF = 2 ;


  /**
   * The pretty print priority for the base type of reference types.
   */
  public static final int PRIO_REF_TAU = PRIO_REF ;


  /**
   * The pretty print priority for polymorphic types.
   */
  public static final int PRIO_POLY = 0 ;


  /**
   * The pretty print priority for the tau of polymorphic types.
   */
  public static final int PRIO_POLY_TAU = PRIO_POLY ;


  /**
   * The pretty print priority for list types.
   */
  public static final int PRIO_LIST = 2 ;


  /**
   * The pretty print priority for the base type of list types.
   */
  public static final int PRIO_LIST_TAU = PRIO_LIST ;
}
