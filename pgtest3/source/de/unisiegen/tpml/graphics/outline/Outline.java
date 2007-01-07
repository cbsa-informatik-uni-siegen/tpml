package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
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
   * Initialized change.
   */
  public static final int INIT = 0 ;


  /**
   * Change by mouse cick.
   */
  public static final int MOUSE_CLICK = 1 ;


  /**
   * Auto change from the <code>SmallStep</code>.
   */
  public static final int CHANGE_SMALLSTEP = 2 ;


  /**
   * Auto change from the <code>BigStep</code>.
   */
  public static final int CHANGE_BIGSTEP = 3 ;


  /**
   * Auto change from the <code>TypeChecker</code>.
   */
  public static final int CHANGE_TYPECHECKER = 4 ;


  /**
   * Disables the auto update <code>JCheckBox</code> and the
   * <code>JMenuItem</code>. Removes the <code>ItemListener</code> and the
   * <code>ActionListener</code>.
   */
  public void disableAutoUpdate ( ) ;


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   */
  public JPanel getJPanelOutline ( ) ;


  /**
   * Returns the {@link OutlinePreferences}.
   * 
   * @return The {@link OutlinePreferences}.
   */
  public OutlinePreferences getOutlinePreferences ( ) ;


  /**
   * This method loads a new {@link Expression} into the <code>Outline</code>.
   * It checks if the new {@link Expression} is different to the current loaded
   * {@link Expression}, if not it does nothing and returns. It does also
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>. In the <code>BigStep</code> and the
   * <code>TypeChecker</code> view it does also nothing if the change does not
   * come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pModus The modus who is calling this method.
   */
  public void loadExpression ( Expression pExpression , int pModus ) ;
}