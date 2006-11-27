package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


/**
 * This class saves and loads the several values, like the CheckBox values
 * replace, binding, unbound, selection and autoUpdate. Also the divider
 * location of the JSplitPane.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTPreferences
{
  /**
   * The replace value. The selected expression should be replaced in higher
   * nodes.
   * 
   * @see #isReplace()
   * @see #setReplace(boolean)
   */
  private boolean replace ;


  /**
   * The binding value. The bindings of the selected Identifier should be
   * highlighted in higher nodes.
   * 
   * @see #isBinding()
   * @see #setBinding(boolean)
   */
  private boolean binding ;


  /**
   * The unbound value. The unbounded Identifiers should be highlighted in all
   * nodes.
   * 
   * @see #isUnbound()
   * @see #setUnbound(boolean)
   */
  private boolean unbound ;


  /**
   * The selection value. The selected expression should be highlighted in
   * higher nodes.
   * 
   * @see #isSelection()
   * @see #setSelection(boolean)
   */
  private boolean selection ;


  /**
   * The auto update value. If the user does a SmallStep the outline should be
   * updated automatically.
   * 
   * @see #isAutoUpdate()
   * @see #setAutoUpdate(boolean)
   */
  private boolean autoUpdate ;


  /**
   * The preferences
   */
  private Preferences preferences ;


  /**
   * The divider location as an int value.
   * 
   * @see #getDividerLocation()
   * @see #setDividerLocation(int)
   */
  private int dividerLocation ;


  /**
   * Initialize the preferences. Load the last saved values from it. Set the
   * replace, binding, unbound and selection value of the ASTNode.
   */
  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    this.replace = this.preferences.getBoolean ( "replace" , true ) ;
    this.binding = this.preferences.getBoolean ( "bindings" , true ) ;
    this.unbound = this.preferences.getBoolean ( "unbound" , true ) ;
    this.selection = this.preferences.getBoolean ( "selected" , true ) ;
    this.autoUpdate = this.preferences.getBoolean ( "autoupdate" , true ) ;
    this.dividerLocation = this.preferences.getInt ( "dividerLocation" , 300 ) ;
    ASTNode.setReplace ( this.replace ) ;
    ASTNode.setBinding ( this.binding ) ;
    ASTNode.setUnbound ( this.unbound ) ;
    ASTNode.setSelection ( this.selection ) ;
  }


  /**
   * Return the divider location as an int value.
   * 
   * @return The divider location.
   * @see #dividerLocation
   * @see #setDividerLocation(int)
   */
  public int getDividerLocation ( )
  {
    return this.dividerLocation ;
  }


  /**
   * Return true, if the auto update of the outline is enabled, otherwise false.
   * 
   * @return True, if the auto update of the outline is enabled, otherwise
   *         false.
   * @see #autoUpdate
   * @see #setAutoUpdate(boolean)
   */
  public boolean isAutoUpdate ( )
  {
    return this.autoUpdate ;
  }


  /**
   * Return true, if the bindings of the selected Identifier should be
   * highlighted in higher nodes, otherwise false.
   * 
   * @return True, if the bindings of the selected Identifier should be
   *         highlighted in higher nodes, otherwise false.
   * @see #binding
   * @see #setBinding(boolean)
   */
  public boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * Return true, if the selected expression should be replaced in higher nodes,
   * otherwise false.
   * 
   * @return True, if the selected expression should be replaced in higher
   *         nodes, otherwise false.
   * @see #replace
   * @see #setReplace(boolean)
   */
  public boolean isReplace ( )
  {
    return this.replace ;
  }


  /**
   * Return true, if the selected expression should be highlighted in higher
   * nodes, otherwise false.
   * 
   * @return True, if the selected expression should be highlighted in higher
   *         nodes, otherwise false.
   * @see #selection
   * @see #setSelection(boolean)
   */
  public boolean isSelection ( )
  {
    return this.selection ;
  }


  /**
   * Return true, if the unbounded Identifiers should be highlighted in all
   * nodes, otherwise false.
   * 
   * @return True, if the unbounded Identifiers should be highlighted in all
   *         nodes, otherwise false.
   * @see #unbound
   * @see #setUnbound(boolean)
   */
  public boolean isUnbound ( )
  {
    return this.unbound ;
  }


  /**
   * Set the auto update value and writes the value to the preferences. If the
   * user does a SmallStep the outline should be updated automatically.
   * 
   * @param pAutoupdate The auto update value.
   * @see #autoUpdate
   * @see #isAutoUpdate()
   */
  public void setAutoUpdate ( boolean pAutoupdate )
  {
    this.autoUpdate = pAutoupdate ;
    this.preferences.putBoolean ( "autoupdate" , pAutoupdate ) ;
  }


  /**
   * Set the binding value and writes the value to the preferences. The bindings
   * of the selected Identifier should be highlighted in higher nodes.
   * 
   * @param pBinding The binding value.
   * @see #binding
   * @see #isBinding()
   */
  public void setBinding ( boolean pBinding )
  {
    this.binding = pBinding ;
    this.preferences.putBoolean ( "bindings" , pBinding ) ;
  }


  /**
   * Set the divider location value and writes the value to the preferences. The
   * divider location as an int value.
   * 
   * @param pDividerLocation The divider location value.
   * @see #dividerLocation
   * @see #getDividerLocation()
   */
  public void setDividerLocation ( int pDividerLocation )
  {
    this.dividerLocation = pDividerLocation ;
    this.preferences.putInt ( "dividerLocation" , pDividerLocation ) ;
  }


  /**
   * Set the replace value and writes the value to the preferences. The selected
   * expression should be replaced in higher nodes.
   * 
   * @param pReplace The replace value.
   * @see #replace
   * @see #isReplace()
   */
  public void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
    this.preferences.putBoolean ( "replace" , pReplace ) ;
  }


  /**
   * Set the selection value and writes the value to the preferences. The
   * selected expression should be highlighted in higher nodes.
   * 
   * @param pSelection The selection value.
   * @see #selection
   * @see #isSelection()
   */
  public void setSelection ( boolean pSelection )
  {
    this.selection = pSelection ;
    this.preferences.putBoolean ( "selected" , pSelection ) ;
  }


  /**
   * Set the unbound value and writes the value to the preferences. The
   * unbounded Identifiers should be highlighted in all nodes.
   * 
   * @param pUnbound The unbound value.
   * @see #unbound
   * @see #isUnbound()
   */
  public void setUnbound ( boolean pUnbound )
  {
    this.unbound = pUnbound ;
    this.preferences.putBoolean ( "unbound" , pUnbound ) ;
  }
}
