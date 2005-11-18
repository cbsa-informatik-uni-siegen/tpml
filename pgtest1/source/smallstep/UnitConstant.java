package smallstep;

import smallstep.printer.AtomicItem;
import smallstep.printer.Item;

public class UnitConstant extends Constant {
  /**
   * @see smallstep.Expression#getPrettyPrintItem()
   */
  @Override
  public Item getPrettyPrintItem() {
    return new AtomicItem("()");
  }
  
  /**
   * The <b>(UNIT)</b> value.
   */
  public static UnitConstant UNIT = new UnitConstant();
  
  private UnitConstant() {
  }
}
