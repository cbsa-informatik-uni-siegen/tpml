/**
 * 
 */
package smallstep.printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Pretty printer item for atomic elements, which are
 * constants, identifiers and exceptions.
 *
 * @author bmeurer
 * @version $Id$
 */
public final class AtomicItem extends Item {
  /**
   * Creates a new atomic item with the given
   * <code>name</code>.
   * 
   * @param name the name of the atomic item, e.g. the
   *        name of an identifier or the name of the
   *        exception or the value of a constant.
   */
  public AtomicItem(String name) {
    super(2);
    this.nameList.add(name);
  }
  
  /**
   * Returns a list with exactly one item, which is
   * the name of this atomic item.
   * 
   * @return a list with a single item, which is the
   *         name of the atomic item.
   * 
   * @see smallstep.printer.Item#getStructureList()
   */
  @Override
  public List getStructureList() {
    return this.nameList;
  }
  
  /**
   * @see smallstep.printer.Item#getStructurePriorities()
   */
  @Override
  public int[] getStructurePriorities() {
    return new int[] { 3 };
  }

  private List<String> nameList = new LinkedList<String>();
}
