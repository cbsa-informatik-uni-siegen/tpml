package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


/**
 * This class saves and loads the several values, like the CheckBox values
 * replace, binding, selection and autoUpdate. Also the divider location of the
 * JSplitPane.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTPreferences
{
  /**
   * The replace value. The selected expression should be replaced in higher
   * nodes.
   */
  private boolean replace ;


  /**
   * The binding value. The bindings of the selected Identifier should be
   * highlighted in higher nodes.
   */
  private boolean binding ;


  /**
   * TODO
   */
  private boolean unbound ;


  /**
   * The selection value. The selected expression should be highlighted in
   * higher nodes.
   */
  private boolean selection ;


  /**
   * The autoUpdate value. If the user does a SmallStep the outline should be
   * updated automatically.
   */
  private boolean autoUpdate ;


  /**
   * TODO
   */
  private Preferences preferences ;


  /**
   * TODO
   */
  private int dividerLocation ;


  /**
   * TODO
   */
  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    updatePreferences ( ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public int getDividerLocation ( )
  {
    return this.dividerLocation ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean isAutoUpdate ( )
  {
    return this.autoUpdate ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean isBinding ( )
  {
    return this.binding ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean isReplace ( )
  {
    return this.replace ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean isSelection ( )
  {
    return this.selection ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public boolean isUnbound ( )
  {
    return this.unbound ;
  }


  /**
   * TODO
   * 
   * @param pAutoupdate
   */
  public void setAutoUpdate ( boolean pAutoupdate )
  {
    this.autoUpdate = pAutoupdate ;
    this.preferences.putBoolean ( "autoupdate" , pAutoupdate ) ;
  }


  /**
   * TODO
   * 
   * @param pBinding
   */
  public void setBinding ( boolean pBinding )
  {
    this.binding = pBinding ;
    this.preferences.putBoolean ( "bindings" , pBinding ) ;
  }


  /**
   * TODO
   * 
   * @param pDividerLocation
   */
  public void setDividerLocation ( int pDividerLocation )
  {
    this.dividerLocation = pDividerLocation ;
    this.preferences.putInt ( "dividerLocation" , pDividerLocation ) ;
  }


  /**
   * TODO
   * 
   * @param pReplace
   */
  public void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
    this.preferences.putBoolean ( "replace" , pReplace ) ;
  }


  /**
   * TODO
   * 
   * @param pSelection
   */
  public void setSelection ( boolean pSelection )
  {
    this.selection = pSelection ;
    this.preferences.putBoolean ( "selected" , pSelection ) ;
  }


  /**
   * TODO
   * 
   * @param pUnbound
   */
  public void setUnbound ( boolean pUnbound )
  {
    this.unbound = pUnbound ;
    this.preferences.putBoolean ( "unbound" , pUnbound ) ;
  }


  /**
   * TODO
   */
  public void updatePreferences ( )
  {
    this.replace = this.preferences.getBoolean ( "replace" , true ) ;
    this.binding = this.preferences.getBoolean ( "bindings" , true ) ;
    this.unbound = this.preferences.getBoolean ( "unbound" , true ) ;
    this.selection = this.preferences.getBoolean ( "selected" , true ) ;
    this.autoUpdate = this.preferences.getBoolean ( "autoupdate" , true ) ;
    ASTNode.setReplace ( this.replace ) ;
    ASTNode.setBinding ( this.binding ) ;
    ASTNode.setUnbound ( this.unbound ) ;
    ASTNode.setSelection ( this.selection ) ;
    this.dividerLocation = this.preferences.getInt ( "dividerLocation" , 300 ) ;
  }
}
