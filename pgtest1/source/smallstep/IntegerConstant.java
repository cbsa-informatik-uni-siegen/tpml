package smallstep;

import smallstep.printer.AtomicItem;
import smallstep.printer.Item;

public class IntegerConstant extends smallstep.Constant {
  /**
   * Allocates a new integer constant with the given
   * <code>number</code>.
   * 
   * @param number the integer number.
   */
  public IntegerConstant(int number) {
    this.number = number;
  }
  
  /**
   * Returns the number represented by this integer constant.
   * @return the number represented by this integer constant.
   */
  public int getNumber() {
    return this.number;
  }
  
  /**
   * @see smallstep.Expression#getPrettyPrintItem()
   */
  @Override
  public Item getPrettyPrintItem() {
    return new AtomicItem(Integer.toString(this.number));
  }

  private int number;
}
