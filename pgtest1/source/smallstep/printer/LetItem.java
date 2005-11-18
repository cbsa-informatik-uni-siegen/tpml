/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a <b>(LET)</b> item expression to the
 * pretty printer.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class LetItem extends Item {
  /**
   * Creates a new item expression for <b>(LET)</b>s.
   * 
   * @param id the identifier.
   * @param e1 the first expression item.
   * @param e2 the second expression item.
   */
  public LetItem(String id, Item e1, Item e2) {
    super(0);
    this.id = id;
    this.e1 = e1;
    this.e2 = e2;
  }
  
  /**
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    LinkedList<Object> items = new LinkedList<Object>();
    items.add("let " + this.id + " = ");
    items.add(this.e1);
    items.add(" in ");
    items.add(this.e2);
    return items;
  }

  /**
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 3, 0, 3, 0 };
  }

  private String id;
  private Item e1;
  private Item e2;
}
