package de.unisiegen.tpml.graphics.outline.util ;


import java.util.prefs.Preferences ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;


/**
 * This class saves and loads the several values, like the
 * <code>JCheckBox</code> values replace, binding, free, selection and
 * autoUpdate. Also the <code>DividerLocation</code> of the
 * <code>JSplitPane</code>.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlinePreferences
{
  /**
   * The <code>String</code> dividerLocation.
   */
  private static final String DIVIDERLOCATION = "dividerLocation" ; //$NON-NLS-1$


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
   * The free value. The free {@link Identifier}s should be highlighted in all
   * nodes.
   * 
   * @see #isFree()
   * @see #setFree(boolean)
   */
  private boolean free ;


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
   * The highlight source code value. The source code should be highlighted if
   * the user selects a node in the {@link Outline}.
   * 
   * @see #isHighlightSourceCode()
   * @see #setHighlightSourceCode(boolean)
   */
  private boolean highlightSourceCode ;


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
   * it. Set the replace, binding, free and selection value of the
   * {@link OutlineNode}.
   */
  public OutlinePreferences ( )
  {
    this.preferences = Preferences.userNodeForPackage ( Outline.class ) ;
    this.replace = this.preferences.getBoolean ( OutlineUI.REPLACE , false ) ;
    this.binding = this.preferences.getBoolean ( OutlineUI.BINDING , true ) ;
    this.free = this.preferences.getBoolean ( OutlineUI.FREE , true ) ;
    this.selection = this.preferences.getBoolean ( OutlineUI.SELECTION , true ) ;
    this.highlightSourceCode = this.preferences.getBoolean (
        OutlineUI.HIGHLIGHTSOURCECODE , true ) ;
    this.autoUpdate = this.preferences
        .getBoolean ( OutlineUI.AUTOUPDATE , true ) ;
    this.dividerLocation = this.preferences.getInt ( DIVIDERLOCATION , 300 ) ;
    OutlineNode.setReplace ( this.replace ) ;
    OutlineNode.setBinding ( this.binding ) ;
    OutlineNode.setFree ( this.free ) ;
    OutlineNode.setSelection ( this.selection ) ;
  }


  /**
   * Return the <code>DividerLocation</code> as an int value.
   * 
   * @return The <code>DividerLocation</code>.
   * @see #dividerLocation
   * @see #setDividerLocation(int)
   */
  public final int getDividerLocation ( )
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
  public final boolean isAutoUpdate ( )
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
  public final boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * Return true, if the free {@link Identifier}s should be highlighted in all
   * nodes, otherwise false.
   * 
   * @return True, if the free {@link Identifier}s should be highlighted in all
   *         nodes, otherwise false.
   * @see #free
   * @see #setFree(boolean)
   */
  public final boolean isFree ( )
  {
    return this.free ;
  }


  /**
   * Return true, if the highlight source code of the {@link Outline} is
   * enabled, otherwise false.
   * 
   * @return True, if the highlight source code of the {@link Outline} is
   *         enabled, otherwise false.
   * @see #highlightSourceCode
   * @see #setHighlightSourceCode (boolean)
   */
  public final boolean isHighlightSourceCode ( )
  {
    return this.highlightSourceCode ;
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
  public final boolean isReplace ( )
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
  public final boolean isSelection ( )
  {
    return this.selection ;
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
  public final void setAutoUpdate ( boolean pAutoupdate )
  {
    this.autoUpdate = pAutoupdate ;
    this.preferences.putBoolean ( OutlineUI.AUTOUPDATE , pAutoupdate ) ;
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
  public final void setBinding ( boolean pBinding )
  {
    this.binding = pBinding ;
    this.preferences.putBoolean ( OutlineUI.BINDING , pBinding ) ;
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
  public final void setDividerLocation ( int pDividerLocation )
  {
    this.dividerLocation = pDividerLocation ;
    this.preferences.putInt ( DIVIDERLOCATION , pDividerLocation ) ;
  }


  /**
   * Set the free value and writes the value to the <code>Preferences</code>.
   * The free {@link Identifier}s should be highlighted in all nodes.
   * 
   * @param pFree The free value.
   * @see #free
   * @see #isFree()
   */
  public final void setFree ( boolean pFree )
  {
    this.free = pFree ;
    this.preferences.putBoolean ( OutlineUI.FREE , pFree ) ;
  }


  /**
   * Set the highlight source code value and writes the value to the
   * <code>Preferences</code>.
   * 
   * @param pHighlightSourceCode The highlight source code value.
   * @see #highlightSourceCode
   * @see #isHighlightSourceCode()
   */
  public final void setHighlightSourceCode ( boolean pHighlightSourceCode )
  {
    this.highlightSourceCode = pHighlightSourceCode ;
    this.preferences.putBoolean ( OutlineUI.HIGHLIGHTSOURCECODE ,
        pHighlightSourceCode ) ;
  }


  /**
   * Set the replace value and writes the value to the <code>Preferences</code>.
   * The selected {@link Expression} should be replaced in higher nodes.
   * 
   * @param pReplace The replace value.
   * @see #replace
   * @see #isReplace()
   */
  public final void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
    this.preferences.putBoolean ( OutlineUI.REPLACE , pReplace ) ;
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
  public final void setSelection ( boolean pSelection )
  {
    this.selection = pSelection ;
    this.preferences.putBoolean ( OutlineUI.SELECTION , pSelection ) ;
  }
}
