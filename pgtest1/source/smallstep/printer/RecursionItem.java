/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Pretty printer item for <b>(REC)</b> expressions.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class RecursionItem extends Item {
  /**
   * Creates a new <b>(REC)</b> expression item.
   * 
   * @param id the identifier.
   * @param e the expression item.
   */
  public RecursionItem(String id, Item e) {
    super(0);
    this.id = id;
    this.e = e;
  }
  
  /**
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    LinkedList<Object> items = new LinkedList<Object>();
    items.add("rec " + this.id + ".");
    items.add(this.e);
    return items;
  }

  /**
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 3, 0 };
  }

  private String id;
  private Item e;
}
