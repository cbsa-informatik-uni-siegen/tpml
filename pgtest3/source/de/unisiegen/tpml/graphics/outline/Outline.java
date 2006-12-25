package de.unisiegen.tpml.graphics.outline ;


import javax.swing.JPanel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This interface is the main interface of the AbstractOutline. It loads new
 * Expressions, returns the GUI and the preferences.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public interface Outline
{
  /**
   * Disables the AutoUpdate check box and the menu item. Removes the listeners.
   */
  public void disableAutoUpdate ( ) ;


  /**
   * Returns the jMainPanel of the OutlineUI.
   * 
   * @return The jMainPanel of the OutlineUI.
   */
  public JPanel getJPanelOutline ( ) ;


  /**
   * Returns the OutlinePreferences.
   * 
   * @return The OutlinePreferences.
   */
  public OutlinePreferences getOutlinePreferences ( ) ;


  /**
   * This method loads a new Expression into the Outline. It checks if the new
   * Expression is different to the current loaded Expression, if not it does
   * nothing and returns. It does also nothing if the auto update is disabled
   * and the change does not come from a mouse event. In the BigStep and the
   * TypeChecker view it does also nothing if the change does not come from a
   * mouse event.
   * 
   * @param pExpression The new Expression.
   * @param pDescription The description who is calling this method.
   */
  public void loadExpression ( Expression pExpression , String pDescription ) ;
}
