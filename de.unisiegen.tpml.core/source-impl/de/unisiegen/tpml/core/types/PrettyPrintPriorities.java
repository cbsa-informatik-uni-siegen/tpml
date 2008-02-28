package de.unisiegen.tpml.core.types;


import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;


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
   * The pretty print priority for type names.
   */
  public static final int PRIO_TYPE_NAME = 6;


  /**
   * The pretty print priority for rec types.
   */
  public static final int PRIO_REC_TYPE = 0;


  /**
   * The pretty print priority for the body of rec types.
   */
  public static final int PRIO_REC_TYPE_TAU = PRIO_REC_TYPE;


  /**
   * The pretty print priority for the type name of rec types.
   */
  public static final int PRIO_REC_TYPE_TYPE_NAME = 0;


  /**
   * The pretty print priority for identifiers like "< add: int ; >".
   */
  public static final int PRIO_ID = 0;


  /**
   * The pretty print priority for object types.
   */
  public static final int PRIO_OBJECT = 0;


  /**
   * The pretty print priority for the row type in object types.
   */
  public static final int PRIO_OBJECT_ROW = 0;


  /**
   * The pretty print priority for class types.
   */
  public static final int PRIO_CLASS = 0;


  /**
   * The pretty print priority for the type in class types.
   */
  public static final int PRIO_CLASS_TAU = 0;


  /**
   * The pretty print priority for the phi in class types.
   */
  public static final int PRIO_CLASS_PHI = 0;


  /**
   * The pretty print priority for row types.
   */
  public static final int PRIO_ROW = 0;


  /**
   * The pretty print priority for the type in row types.
   */
  public static final int PRIO_ROW_TAU = 0;


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


  /**
   * The pretty print priority for reference types.
   */
  public static final int PRIO_REF = 2;


  /**
   * The pretty print priority for the base type of reference types.
   */
  public static final int PRIO_REF_TAU = PRIO_REF;


  /**
   * The pretty print priority for polymorphic types.
   */
  public static final int PRIO_POLY = 0;


  /**
   * The pretty print priority for the tau of polymorphic types.
   */
  public static final int PRIO_POLY_TAU = PRIO_POLY;


  /**
   * The pretty print priority for list types.
   */
  public static final int PRIO_LIST = 2;


  /**
   * The pretty print priority for the base type of list types.
   */
  public static final int PRIO_LIST_TAU = PRIO_LIST;
}
