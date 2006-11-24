package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


public class ASTPreferences
{
  private boolean replace ;


  private boolean bindings ;


  private boolean selected ;
  
  
  private boolean autoUpdate ;


  private Preferences preferences ;


  private int width ;


  private int height ;


  private int positionX ;


  private int positionY ;


  private int dividerLocation ;


  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    updatePreferences ( ) ;
  }


  public int getDividerLocation ( )
  {
    return this.dividerLocation ;
  }


  public int getHeight ( )
  {
    return this.height ;
  }


  public int getPositionX ( )
  {
    return this.positionX ;
  }


  public int getPositionY ( )
  {
    return this.positionY ;
  }


  public int getWidth ( )
  {
    return this.width ;
  }


  public boolean isAutoUpdate ( )
  {
    return this.autoUpdate ;
  }


  public boolean isBindings ( )
  {
    return this.bindings ;
  }


  public boolean isReplace ( )
  {
    return this.replace ;
  }


  public boolean isSelected ( )
  {
    return this.selected ;
  }


  public void setAutoUpdate ( boolean pAutoupdate )
  {
    this.autoUpdate = pAutoupdate ;
    this.preferences.putBoolean ( "autoupdate" , pAutoupdate ) ;
  }


  public void setBindings ( boolean pBindings )
  {
    this.bindings = pBindings ;
    this.preferences.putBoolean ( "bindings" , pBindings ) ;
  }


  public void setDividerLocation ( int pDividerLocation )
  {
    this.dividerLocation = pDividerLocation ;
    this.preferences.putInt ( "dividerLocation" , pDividerLocation ) ;
  }


  public void setHeight ( int pHeight )
  {
    this.height = pHeight ;
    this.preferences.putInt ( "jFrameHeight" , pHeight ) ;
  }


  public void setPositionX ( int pPositionX )
  {
    this.positionX = pPositionX ;
    this.preferences.putInt ( "jFramePositionX" , pPositionX ) ;
  }


  public void setPositionY ( int pPositionY )
  {
    this.positionY = pPositionY ;
    this.preferences.putInt ( "jFramePositionY" , pPositionY ) ;
  }


  public void setReplace ( boolean pReplace )
  {
    this.replace = pReplace ;
    this.preferences.putBoolean ( "replace" , pReplace ) ;
  }


  public void setSelected ( boolean pSelected )
  {
    this.selected = pSelected ;
    this.preferences.putBoolean ( "selected" , pSelected ) ;
  }


  public void setWidth ( int pWidth )
  {
    this.width = pWidth ;
    this.preferences.putInt ( "jFrameWidth" , pWidth ) ;
  }


  public void updatePreferences ( )
  {
    this.replace = this.preferences.getBoolean ( "replace" , true ) ;
    this.bindings = this.preferences.getBoolean ( "bindings" , true ) ;
    this.selected = this.preferences.getBoolean ( "selected" , true ) ;
    this.autoUpdate = this.preferences.getBoolean ( "autoupdate" , true ) ;
    ASTNode.setReplace ( this.replace ) ;
    ASTNode.setBinding ( this.bindings ) ;
    ASTNode.setSelection ( this.selected ) ;
    this.width = this.preferences.getInt ( "jFrameWidth" , 600 ) ;
    this.height = this.preferences.getInt ( "jFrameHeight" , 450 ) ;
    this.positionX = this.preferences.getInt ( "jFramePositionX" , 100 ) ;
    this.positionY = this.preferences.getInt ( "jFramePositionY" , 100 ) ;
    this.dividerLocation = this.preferences.getInt ( "dividerLocation" , 300 ) ;
  }
}
