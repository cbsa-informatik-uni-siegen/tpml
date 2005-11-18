/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the Item class for <b>(APP)</b> expressions.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class ApplicationItem extends Item {
  /**
   * Creates a new application item for the pretty printer,
   * with the specified subitems.
   * 
   * @param e1 the first expression item.
   * @param e2 the second expression item.
   */
  public ApplicationItem(Item e1, Item e2) {
    super(1);
    this.e1 = e1;
    this.e2 = e2;
  }
  
  /**
   * Returns the substructure list with the two sub items.
   * 
   * @return the substructure list with the two sub items.
   * 
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    List<Object> items = new LinkedList<Object>();
    items.add(this.e1);
    items.add(" ");
    items.add(this.e2);
    return items;
  }
  
  /**
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 1, 3, 2 };
  }
  
  private Item e1;
  private Item e2;
}
