package de.unisiegen.tpml.ui.abstractsyntaxtree ;


import java.util.prefs.Preferences ;


public class ASTPreferences
{
  private boolean checkedReplace ;


  private boolean checkedBindings ;


  private boolean checkedSelected ;


  private Preferences preferences ;


  public ASTPreferences ( )
  {
    this.preferences = Preferences
        .userNodeForPackage ( AbstractSyntaxTree.class ) ;
    String replace = this.preferences.get ( "checkedReplace" , "true" ) ;
    String bindings = this.preferences.get ( "checkedBindings" , "true" ) ;
    String selected = this.preferences.get ( "checkedSelected" , "true" ) ;
    if ( selected.equals ( "true" ) )
    {
      this.checkedSelected = true ;
      ASTNode.setCheckedSelected ( true ) ;
    }
    else
    {
      this.checkedSelected = false ;
      ASTNode.setCheckedSelected ( false ) ;
    }
    if ( replace.equals ( "true" ) )
    {
      this.checkedReplace = true ;
      ASTNode.setCheckedReplace ( true ) ;
    }
    else
    {
      this.checkedReplace = false ;
      ASTNode.setCheckedReplace ( false ) ;
    }
    if ( bindings.equals ( "true" ) )
    {
      this.checkedBindings = true ;
      ASTNode.setCheckedBindings ( true ) ;
    }
    else
    {
      this.checkedBindings = false ;
      ASTNode.setCheckedBindings ( false ) ;
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


  public boolean isCheckedSelected ( )
  {
    return this.checkedSelected ;
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


  public void setCheckedSelected ( boolean pCheckedSelected )
  {
    this.checkedSelected = pCheckedSelected ;
    String selected = "false" ;
    if ( pCheckedSelected )
    {
      selected = "true" ;
    }
    this.preferences.put ( "checkedSelected" , selected ) ;
  }
}
