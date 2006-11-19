package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.WindowEvent ;
import java.awt.event.WindowListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI;


public class ASTWindowListener implements WindowListener
{
  private ASTUI aSTUI ;


  public ASTWindowListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  public void windowActivated ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }


  public void windowClosed ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }


  public void windowClosing ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setWidth (
        this.aSTUI.getJFrameAbstractSyntaxTree ( ).getWidth ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setHeight (
        this.aSTUI.getJFrameAbstractSyntaxTree ( ).getHeight ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setPositionX (
        this.aSTUI.getJFrameAbstractSyntaxTree ( ).getX ( ) ) ;
    this.aSTUI.getAbstractSyntaxTree ( ).getASTPreferences ( ).setPositionY (
        this.aSTUI.getJFrameAbstractSyntaxTree ( ).getY ( ) ) ;
  }


  public void windowDeactivated ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }


  public void windowDeiconified ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }


  public void windowIconified ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }


  public void windowOpened ( @ SuppressWarnings ( "unused" )
  WindowEvent pWindowEvent )
  {
    // Do Nothing
  }
}
