/**
 * 
 */
package de.unisiegen.tpml.ui ;


import java.util.EventListener ;


/**
 * @author marcell
 */
public interface SideBarListener extends EventListener
{
  public void markText ( int left , int right ) ;


  public void insertText ( int index , String pText ) ;
}
