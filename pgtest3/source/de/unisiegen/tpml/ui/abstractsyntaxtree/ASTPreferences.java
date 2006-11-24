package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTPreferences
{
  /**
   * TODO
   */
  private boolean replace ;


  /**
   * TODO
   */
  private boolean binding ;


  /**
   * TODO
   */
  private boolean selection ;


  /**
   * TODO
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
   */
  public void updatePreferences ( )
  {
    this.replace = this.preferences.getBoolean ( "replace" , true ) ;
    this.binding = this.preferences.getBoolean ( "bindings" , true ) ;
    this.selection = this.preferences.getBoolean ( "selected" , true ) ;
    this.autoUpdate = this.preferences.getBoolean ( "autoupdate" , true ) ;
    ASTNode.setReplace ( this.replace ) ;
    ASTNode.setBinding ( this.binding ) ;
    ASTNode.setSelection ( this.selection ) ;
    this.dividerLocation = this.preferences.getInt ( "dividerLocation" , 300 ) ;
  }
}
