package de.unisiegen.tpml.ui;


import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JPanel;

import de.unisiegen.tpml.graphics.EditorComponent;


/**
 * TODO
 */
public interface EditorPanel
{

  /**
   * TODO
   * 
   * @param b
   */
  void setAdvanced ( boolean b );


  /**
   * TODO
   * 
   * @param editorPanelListener
   */
  void addPropertyChangeListener ( PropertyChangeListener editorPanelListener );


  /**
   * TODO
   * 
   * @param b
   */
  void setTexteditor ( boolean b );


  /**
   * TODO
   */
  void handleCopy ();


  /**
   * TODO
   */
  void handleCut ();


  /**
   * TODO
   */
  void handlePaste ();


  /**
   * TODO
   */
  void handleRedo ();


  /**
   * TODO
   */
  void handleUndo ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean handleSaveAs ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean handleSave ();


  /**
   * TODO
   * 
   * @return TODO
   */
  File getFile ();


  /**
   * TODO
   * 
   * @param name
   */
  void setFileName ( String name );


  /**
   * TODO
   * 
   * @param string
   */
  void setEditorText ( String string );


  /**
   * TODO
   * 
   * @param file
   */
  void setFile ( File file );


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean isRedoStatus ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean isUndoStatus ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean isSaveStatus ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean isTexteditor ();


  /**
   * TODO
   * 
   * @return TODO
   */
  String getFileName ();


  /**
   * TODO
   * 
   * @return TODO
   */
  boolean shouldBeSaved ();


  /**
   * TODO
   */
  public void handlePrint ();


  /**
   * TODO
   */
  public void handleLatexExport ();


  /**
   * TODO
   * 
   * @return TODO
   */
  public EditorComponent getActiveEditorComponent ();


  /**
   * TODO
   */
  public void selectCode ();


  /**
   * TODO
   * 
   * @return TODO
   */
  public JPanel getPanel ();
}
