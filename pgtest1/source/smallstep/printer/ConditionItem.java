/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Returns the pretty print item for conditional
 * expressions.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class ConditionItem extends Item {
  /**
   * Creates a new conditional pretty printer item.
   * 
   * @param e0 the first expression item.
   * @param e1 the second expression item.
   * @param e2 the third expression item.
   */
  public ConditionItem(Item e0, Item e1, Item e2) {
    super(0);
    this.e0 = e0;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  /**
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    LinkedList<Object> items = new LinkedList<Object>();
    items.add("if ");
    items.add(this.e0);
    items.add(" then ");
    items.add(this.e1);
    items.add(" else ");
    items.add(this.e2);
    return items;
  }

  /**
   * Returns the sub structure priorities for the items
   * returned with the sub structure list. Syntactic
   * tokens such as "if", "then" and "else" will have
   * a priority of 3.
   * 
   * @return the priorities for the structure list.
   * 
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 3, 0, 3, 0, 3, 0 };
  }

  private Item e0;
  private Item e1;
  private Item e2;
}
