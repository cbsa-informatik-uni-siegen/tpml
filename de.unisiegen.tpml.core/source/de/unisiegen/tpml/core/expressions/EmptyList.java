package de.unisiegen.tpml.core.expressions;

/**
 * The constant for the empty list, represented as <code>[]</code>, which must be handled
 * differently from the <code>List</code> expressions, which are syntactic sugar.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 *
 * @see de.unisiegen.tpml.core.expressions.Constant
 */
public final class EmptyList extends Constant {
  //
  // Constant
  //
  
  /**
   * The single instance of the <code>EmptyList</code> class.
   * 
   * @see #EmptyList()
   */
  public static final EmptyList EMPTY_LIST = new EmptyList();
  
  
  
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>EmptyList</code> instance.
   * 
   * @see #EMPTY_LIST
   */
  private EmptyList() {
    super("[]");
  }
}
