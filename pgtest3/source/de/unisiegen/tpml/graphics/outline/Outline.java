package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This interface is the main interface of the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public interface Outline
{
  /**
   * Indicates who loads the new Expression.
   * 
   * @author Christian Fehler
   */
  public enum Execute
  {
    /**
     * Initialized from the <code>Editor</code>.
     */
    INIT_EDITOR ,
    /**
     * Initialized from the <code>SmallStep</code>.
     */
    INIT_SMALLSTEP ,
    /**
     * Initialized from the <code>BigStep</code>.
     */
    INIT_BIGSTEP ,
    /**
     * Initialized from the <code>TypeChecker</code>.
     */
    INIT_TYPECHECKER ,
    /**
     * Change by mouse cick from the <code>Editor</code>.
     */
    MOUSE_CLICK_EDITOR ,
    /**
     * Change by mouse cick from the <code>SmallStep</code>.
     */
    MOUSE_CLICK_SMALLSTEP ,
    /**
     * Change by mouse cick from the <code>BigStep</code>.
     */
    MOUSE_CLICK_BIGSTEP ,
    /**
     * Change by mouse cick from the <code>TypeChecker</code>.
     */
    MOUSE_CLICK_TYPECHECKER ,
    /**
     * Auto change from the <code>Editor</code>.
     */
    AUTO_CHANGE_EDITOR ,
    /**
     * Auto change from the <code>SmallStep</code>.
     */
    AUTO_CHANGE_SMALLSTEP ,
    /**
     * Auto change from the <code>BigStep</code>.
     */
    AUTO_CHANGE_BIGSTEP ,
    /**
     * Auto change from the <code>TypeChecker</code>.
     */
    AUTO_CHANGE_TYPECHECKER
  }


  /**
   * Indicates from where the {@link Outline} is started.
   * 
   * @author Christian Fehler
   */
  public enum Start
  {
    /**
     * Started from the <code>Editor</code>.
     */
    EDITOR ,
    /**
     * Started from the <code>SmallStepper</code>.
     */
    SMALLSTEP ,
    /**
     * Started from the <code>BigStepper</code>.
     */
    BIGSTEP ,
    /**
     * Started from the <code>TypeChecker</code>.
     */
    TYPECHECKER
  }


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
   * This method loads a new {@link Expression} into the {@link Outline}. It
   * checks if the new {@link Expression} is different to the current loaded
   * {@link Expression}, if not it does nothing and returns. It does also
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>. In the <code>BigStep</code> and the
   * <code>TypeChecker</code> view it does also nothing if the change does not
   * come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public void loadExpression ( Expression pExpression , Outline.Execute pExecute ) ;


  /**
   * Repaints the root node and all of its children.
   */
  public void propertyChanged ( ) ;
}
