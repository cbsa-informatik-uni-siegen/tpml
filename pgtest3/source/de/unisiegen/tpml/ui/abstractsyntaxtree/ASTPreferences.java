package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


public class ASTPreferences
{
  private boolean checkedReplace ;


  private boolean checkedBindings ;


  private Preferences preferences ;


  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    String replace = this.preferences.get ( "checkedReplace" , "true" ) ;
    String bindings = this.preferences.get ( "checkedBindings" , "true" ) ;
    if ( replace.equals ( "true" ) )
    {
      this.checkedReplace = true ;
      ASTNode.setReplaceGeneral ( true ) ;
    }
    else
    {
      this.checkedReplace = false ;
      ASTNode.setReplaceGeneral ( false ) ;
    }
    if ( bindings.equals ( "true" ) )
    {
      this.checkedBindings = true ;
      ASTNode.setShowBindings ( true ) ;
    }
    else
    {
      this.checkedBindings = false ;
      ASTNode.setShowBindings ( false ) ;
    }
  }


  public boolean isCheckedBindings ( )
  {
    return this.checkedBindings ;
  }


  public boolean isCheckedReplace ( )
  {
    return this.checkedReplace ;
  }


  public void setCheckedBindings ( boolean pCheckedBindings )
  {
    this.checkedBindings = pCheckedBindings ;
    String bindings = "false" ;
    if ( pCheckedBindings )
    {
      bindings = "true" ;
    }
    this.preferences.put ( "checkedBindings" , bindings ) ;
  }


  public void setCheckedReplace ( boolean pCheckedReplace )
  {
    this.checkedReplace = pCheckedReplace ;
    String replace = "false" ;
    if ( pCheckedReplace )
    {
      replace = "true" ;
    }
    this.preferences.put ( "checkedReplace" , replace ) ;
  }
}
