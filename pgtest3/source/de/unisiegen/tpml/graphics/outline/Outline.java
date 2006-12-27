package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This interface is the main interface of the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public interface Outline
{
  /**
   * Disables the auto update <code>JCheckBox</code> and the
   * <code>JMenuItem</code>. Removes the <code>ItemListener</code> and the
   * <code>ActionListener</code>.
   */
  public void disableAutoUpdate ( ) ;


  /**
   * Returns the <code>JPanel</code> of the <code>OutlineUI</code>.
   * 
   * @return The <code>JPanel</code> of the <code>OutlineUI</code>.
   */
  public JPanel getJPanelOutline ( ) ;


  /**
   * Returns the <code>OutlinePreferences</code>.
   * 
   * @return The <code>OutlinePreferences</code>.
   */
  public OutlinePreferences getOutlinePreferences ( ) ;


  /**
   * This method loads a new <code>Expression</code> into the
   * <code>Outline</code>. It checks if the new <code>Expression</code> is
   * different to the current loaded <code>Expression</code>, if not it does
   * nothing and returns. It does also nothing if the auto update is disabled
   * and the change does not come from a <code>MouseEvent</code>. In the
   * <code>BigStep</code> and the <code>TypeChecker</code> view it does also
   * nothing if the change does not come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new <code>Expression</code>.
   * @param pDescription The description who is calling this method.
   */
  public void loadExpression ( Expression pExpression , String pDescription ) ;
}
