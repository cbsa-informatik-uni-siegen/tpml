package de.unisiegen.tpml.graphics.outline.util ;


import java.util.prefs.Preferences ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.OutlineNode ;


/**
 * This class saves and loads the several values, like the
 * <code>JCheckBox</code> values replace, binding, unbound, selection and
 * autoUpdate. Also the <code>DividerLocation</code> of the
 * <code>JSplitPane</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlinePreferences
{
  /**
   * The replace value. The selected {@link Expression} should be replaced in
   * higher nodes.
   * 
   * @see #isReplace()
   * @see #setReplace(boolean)
   */
  private boolean replace ;


  /**
   * The binding value. The bindings of the selected {@link Identifier} should
   * be highlighted in higher nodes.
   * 
   * @see #isBinding()
   * @see #setBinding(boolean)
   */
  private boolean binding ;


  /**
   * The unbound value. The unbounded {@link Identifier}s should be highlighted
   * in all nodes.
   * 
   * @see #isUnbound()
   * @see #setUnbound(boolean)
   */
  private boolean unbound ;


  /**
   * The selection value. The selected {@link Expression} should be highlighted
   * in higher nodes.
   * 
   * @see #isSelection()
   * @see #setSelection(boolean)
   */
  private boolean selection ;


  /**
   * The auto update value. If the user does a <code>SmallStep</code> the
   * {@link Outline} should be updated automatically.
   * 
   * @see #isAutoUpdate()
   * @see #setAutoUpdate(boolean)
   */
  private boolean autoUpdate ;


  /**
   * The <code>Preferences</code>.
   */
  private Preferences preferences ;


  /**
   * The <code>DividerLocation</code> as an int value.
   * 
   * @see #getDividerLocation()
   * @see #setDividerLocation(int)
   */
  private int dividerLocation ;


  /**
   * Initialize the <code>Preferences</code>. Load the last saved values from
   * it. Set the replace, binding, unbound and selection value of the
   * {@link OutlineNode}.
   */
  public OutlinePreferences ( )
  {
    this.preferences = Preferences.userNodeForPackage ( AbstractOutline.class ) ;
    this.replace = this.preferences.getBoolean ( "replace" , true ) ; //$NON-NLS-1$
    this.binding = this.preferences.getBoolean ( "bindings" , true ) ; //$NON-NLS-1$
    this.unbound = this.preferences.getBoolean ( "unbound" , true ) ; //$NON-NLS-1$
    this.selection = this.preferences.getBoolean ( "selected" , true ) ; //$NON-NLS-1$
    this.autoUpdate = this.preferences.getBoolean ( "autoupdate" , true ) ; //$NON-NLS-1$
    this.dividerLocation = this.preferences.getInt ( "dividerLocation" , 300 ) ; //$NON-NLS-1$
    OutlineNode.setReplace ( this.replace ) ;
    OutlineNode.setBinding ( this.binding ) ;
    OutlineNode.setUnbound ( this.unbound ) ;
    OutlineNode.setSelection ( this.selection ) ;
  }


  /**
   * Return the <code>DividerLocation</code> as an int value.
   * 
   * @return The <code>DividerLocation</code>.
   * @see #dividerLocation
   * @see #setDividerLocation(int)
   */
  public int getDividerLocation ( )
  {
    return this.dividerLocation ;
  }


  /**
   * Return true, if the auto update of the {@link Outline} is enabled,
   * otherwise false.
   * 
   * @return True, if the auto update of the {@link Outline} is enabled,
   *         otherwise false.
   * @see #autoUpdate
   * @see #setAutoUpdate(boolean)
   */
  public boolean isAutoUpdate ( )
  {
    return this.autoUpdate ;
  }


  /**
   * Return true, if the bindings of the selected {@link Identifier} should be
   * highlighted in higher nodes, otherwise false.
   * 
   * @return True, if the bindings of the selected {@link Identifier} should be
   *         highlighted in higher nodes, otherwise false.
   * @see #binding
   * @see #setBinding(boolean)
   */
  public boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * Return true, if the selected {@link Expression} should be replaced in
   * higher nodes, otherwise false.
   * 
   * @return True, if the selected {@link Expression} should be replaced in
   *         higher nodes, otherwise false.
   * @see #replace
   * @see #setReplace(boolean)
   */
  public boolean isReplace ( )
  {
    return this.replace ;
  }


  /**
   * Return true, if the selected {@link Expression} should be highlighted in
   * higher nodes, otherwise false.
   * 
   * @return True, if the selected {@link Expression} should be highlighted in
   *         higher nodes, otherwise false.
   * @see #selection
   * @see #setSelection(boolean)
   */
  public boolean isSelection ( )
  {
    return this.selection ;
  }


  /**
   * Return true, if the unbounded {@link Identifier}s should be highlighted in
   * all nodes, otherwise false.
   * 
   * @return True, if the unbounded {@link Identifier}s should be highlighted
   *         in all nodes, otherwise false.
   * @see #unbound
   * @see #setUnbound(boolean)
   */
  public boolean isUnbound ( )
  {
    return this.unbound ;
  }


  /**
   * Set the auto update value and writes the value to the
   * <code>Preferences</code>. If the user does a <code>SmallStep</code>
   * the {@link Outline} should be updated automatically.
   * 
   * @param pAutoupdate The auto update value.
   * @see #autoUpdate
   * @see #isAutoUpdate()
   */
  public void setAutoUpdate ( boolean pAutoupdate )
  {
    this.autoUpdate = pAutoupdate ;
    this.preferences.putBoolean ( "autoupdate" , pAutoupdate ) ; //$NON-NLS-1$
  }


  /**
   * Set the binding value and writes the value to the <code>Preferences</code>.
   * The bindings of the selected {@link Identifier} should be highlighted in
   * higher nodes.
   * 
   * @param pBinding The binding value.
   * @see #binding
   * @see #isBinding()
   */
  public void setBinding ( boolean pBinding )
  {
    this.binding = pBinding ;
    this.preferences.putBoolean ( "bindings" , pBinding ) ; //$NON-NLS-1$
  }


  /**
   * Set the <code>DividerLocation</code> value and writes the value to the
   * <code>Preferences</code>. The <code>DividerLocation</code> as an int
   * value.
   * 
   * @param pDividerLocation The <code>DividerLocation</code> value.
   * @see #dividerLocation
   * @see #getDividerLocation()
   */
  public void setDividerLocation ( int pDividerLocation )
  {
    this.dividerLocation = pDividerLocation ;
    this.preferences.putInt ( "dividerLocation" , pDividerLocation ) ; //$NON-NLS-1$
  }


  /**
   * Set the replace value and writes the value to the <code>Preferences</code>.
   * The selected {@link Expression} should be replaced in higher nodes.
   * 
   * @param pReplace The replace value.
   * @see #replace
   * @see #isReplace()
   */
  public void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
    this.preferences.putBoolean ( "replace" , pReplace ) ; //$NON-NLS-1$
  }


  /**
   * Set the selection value and writes the value to the
   * <code>Preferences</code>. The selected {@link Expression} should be
   * highlighted in higher nodes.
   * 
   * @param pSelection The selection value.
   * @see #selection
   * @see #isSelection()
   */
  public void setSelection ( boolean pSelection )
  {
    this.selection = pSelection ;
    this.preferences.putBoolean ( "selected" , pSelection ) ; //$NON-NLS-1$
  }


  /**
   * Set the unbound value and writes the value to the <code>Preferences</code>.
   * The unbounded {@link Identifier}s should be highlighted in all nodes.
   * 
   * @param pUnbound The unbound value.
   * @see #unbound
   * @see #isUnbound()
   */
  public void setUnbound ( boolean pUnbound )
  {
    this.unbound = pUnbound ;
    this.preferences.putBoolean ( "unbound" , pUnbound ) ; //$NON-NLS-1$
  }
}
