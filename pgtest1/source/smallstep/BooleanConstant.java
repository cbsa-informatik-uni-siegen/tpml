package smallstep;

import smallstep.printer.AtomicItem;
import smallstep.printer.Item;

public class BooleanConstant extends Constant {
  /**
   * Returns <code>true</code> if the primitive value of
   * this boolean constant is <code>true</code>.
   * 
   * @return the primitive value of the boolean constant.
   */
  public final boolean isTrue() {
    return this.primitive;
  }
  
  /**
   * @see smallstep.Expression#getPrettyPrintItem()
   */
  @Override
  public Item getPrettyPrintItem() {
    return new AtomicItem(isTrue() ? "true" : "false");
  }
  
  /**
   * The <b>(TRUE)</b> expression.
   */
  public static final BooleanConstant TRUE = new BooleanConstant(true);
  
  /**
   * The <b>(FALSE)</b> expression.
   */
  public static final BooleanConstant FALSE = new BooleanConstant(false);

  private BooleanConstant(boolean primitive) {
    this.primitive = primitive;
  }
  
  private boolean primitive;
}
