package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


public class ASTPreferences
{
  private boolean replace ;


  private boolean bindings ;


  private boolean selected ;


  private Preferences preferences ;


  private int width ;


  private int height ;


  private int positionX ;


  private int positionY ;


  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    this.replace = this.preferences.getBoolean ( "replace" , true ) ;
    this.bindings = this.preferences.getBoolean ( "bindings" , true ) ;
    this.selected = this.preferences.getBoolean ( "selected" , true ) ;
    ASTNode.setReplace ( this.replace ) ;
    ASTNode.setBindings ( this.bindings ) ;
    ASTNode.setSelected ( this.selected ) ;
    this.width = this.preferences.getInt ( "jFrameWidth" , 600 ) ;
    this.height = this.preferences.getInt ( "jFrameHeight" , 450 ) ;
    this.positionX = this.preferences.getInt ( "jFramePositionX" , 100 ) ;
    this.positionY = this.preferences.getInt ( "jFramePositionY" , 100 ) ;
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


  public void setBindings ( boolean pBindings )
  {
    this.bindings = pBindings ;
    this.preferences.putBoolean ( "bindings" , pBindings ) ;
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
}
