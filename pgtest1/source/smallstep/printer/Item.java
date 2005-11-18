/**
 * 
 */
package smallstep.printer;

import java.util.List;
import java.util.ListIterator;

/**
 * Abstract pretty printer item class.
 *
 * @author bmeurer
 * @version $Id$
 */
public abstract class Item {
  /**
   * Protected constructor for derived classes.
   * 
   * @param priority the pretty print priority.
   */
  protected Item(int priority) {
    this.priority = priority;
  }
  
  /**
   * Returns the pretty print priority of this item
   * 
   * @return the pretty print priority of this item
   */
  public final int getPriority() {
    return this.priority;
  }
  
  /**
   * Returns the sub structure list, which contains
   * both strings (which correspond to syntactic
   * tokens) and subitems in the correct order.
   * 
   * @return the sub structure list.
   */
  public abstract List getStructureList();
  
  /**
   * Returns an iterator on the sub structure list.
   * 
   * @return an iterator on the sub structure list.
   */
  public final ListIterator structureListIterator() {
    return getStructureList().listIterator();
  }

  /**
   * Returns the sub structure priorities for the
   * items returned with the substructure list.
   * String tokens in the sub structure list will
   * be returned with a priority of 3, which means
   * they will never be embedded in parenthesis.
   * 
   * @return the priorities for the items in the
   *         sub structure list.
   */
  public abstract int[] getStructurePriorities();
  
  /**
   * Returns the string representation of this item.
   * 
   * @return the string representation of this item.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ListIterator it = structureListIterator();
    String result = "";

    for (int n = 0; it.hasNext(); ++n) {
      Object o = it.next();
      String s = o.toString();
      if (o instanceof Item) {
        Item item = (Item)o;
        if (item.getPriority() < getStructurePriorities()[n])
          s = "(" + s + ")";
      }
      result += s;
    }
    
    return result;
  }
  
  private int priority;
}
