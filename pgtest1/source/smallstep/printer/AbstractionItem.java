/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a lambda item expression to the
 * pretty printer.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class AbstractionItem extends Item {
  /**
   * Creates a new lambda item with the given identifier
   * and sub expression item.
   * 
   * @param id the identifier.
   * @param e the expression item.
   */
  public AbstractionItem(String id, Item e) {
    super(0);
    this.id = id;
    this.e = e;
  }
  
  /**
   * Returns the substructure list.
   * 
   * @return the substructure list.
   * 
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    LinkedList<Object> items = new LinkedList<Object>();
    items.add("\u03bb" + this.id + ".");
    items.add(this.e);
    return items;
  }

  /**
   * Returns the priorities for the items in the structure list.
   * 
   * @return the priorities for the items in the structure list.
   * 
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 3, 0 };
  }

  private String id;
  private Item e;
}
