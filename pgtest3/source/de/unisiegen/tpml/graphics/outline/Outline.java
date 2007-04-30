package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
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
     * Initialized from the <code>Subtyping</code>.
     */
    INIT_SUBTYPING ,
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
     * Change by mouse cick from the <code>Subtyping</code>.
     */
    MOUSE_CLICK_SUBTYPING ,
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
    AUTO_CHANGE_TYPECHECKER ,
    /**
     * Auto change from the <code>Subtyping</code>.
     */
    AUTO_CHANGE_SUBTYPING
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
   * @param pPrettyPrintable The new {@link PrettyPrintable}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public void loadPrettyPrintable ( PrettyPrintable pPrettyPrintable ,
      Outline.Execute pExecute ) ;


  /**
   * Repaints the root node and all of its children.
   */
  public void propertyChanged ( ) ;
}
