package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.awt.event.WindowEvent ;
import java.awt.event.WindowListener ;
import javax.swing.JFrame ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTPreferences ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


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
    ASTPreferences aSTPreferences = this.aSTUI.getAbstractSyntaxTree ( )
        .getASTPreferences ( ) ;
    JFrame jFrame = this.aSTUI.getJFrameAbstractSyntaxTree ( ) ;
    aSTPreferences.setWidth ( jFrame.getWidth ( ) ) ;
    aSTPreferences.setHeight ( jFrame.getHeight ( ) ) ;
    aSTPreferences.setPositionX ( jFrame.getX ( ) ) ;
    aSTPreferences.setPositionY ( jFrame.getY ( ) ) ;
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
