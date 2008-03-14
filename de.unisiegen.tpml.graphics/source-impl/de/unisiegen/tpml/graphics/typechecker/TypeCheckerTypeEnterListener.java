package de.unisiegen.tpml.graphics.typechecker;


import java.util.EventListener;


/**
 * @author marcell
 * @version $Id$
 */
public interface TypeCheckerTypeEnterListener extends EventListener
{

  /**
   * Called when the user has entered a type.
   * 
   * @param type The string of the type that has been entered.
   */
  public void typeEntered ( String type );


  /**
   * Callend when the user canceled the entering of a type.
   */
  public void canceled ();

}
