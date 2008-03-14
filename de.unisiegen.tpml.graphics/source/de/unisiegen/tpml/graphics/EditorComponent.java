package de.unisiegen.tpml.graphics;


import javax.swing.JComponent;


/**
 * The interface for every Editor Component (e.g. the code editor and smallstep
 * window.
 * 
 * @author Christoph Fehling
 * @version $Id$
 */
public interface EditorComponent
{

  //
  // Accessors
  //

  /**
   * Status of the next function.
   * 
   * @return true if next is enabled.
   */
  public boolean isNextStatus ();


  /**
   * Returns <code>true</code> if <i>Pong</i> can be played.
   * 
   * @return <code>true</code> if <i>Pong</i> can be played.
   */
  public boolean isPongStatus ();


  /**
   * Status of the redo function.
   * 
   * @return true if redo is enabled.
   */
  public boolean isRedoStatus ();


  /**
   * Status of the undo function.
   * 
   * @return true if undo is enabled.
   */
  public boolean isUndoStatus ();


  //
  // Primitives
  //

  /**
   * Sets the default states for the editor functions
   */
  public void setDefaultStates ();


  /**
   * execute the next funtion on the component
   */
  public void handleNext ();


  /**
   * execute the redo funtion on the component
   */
  public void handleRedo ();


  /**
   * execute the undo funtion on the component
   */
  public void handleUndo ();


  /**
   * Sets the Mode (Advanced / Beginner for the Component.
   * 
   * @param status true if mode shall be advanced.
   */
  public void setAdvanced ( boolean status );


  /**
   * TODO
   * 
   * @return TODO
   */
  public JComponent getPrintPart ();
}
