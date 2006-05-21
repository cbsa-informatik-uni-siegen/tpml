package expressions;

/**
 * Represents the unit constant.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class UnitConstant extends Constant {
  //
  // Constants
  //
  
  /**
   * The single <code>UnitConstant</code> instance, which
   * represents the unit value, written as <code>()</code>.
   */
  public static UnitConstant UNIT = new UnitConstant();
  
  
  
  //
  // Constructor (private)
  //
  
  private UnitConstant() {
    super("()");
  }
}
