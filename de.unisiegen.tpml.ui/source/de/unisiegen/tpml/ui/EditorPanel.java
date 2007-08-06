/*
 * EditorPanel.java
 *
 * Created on 17. September 2006, 14:59
 */

package de.unisiegen.tpml.ui;

import java.beans.PropertyChangeListener;
import java.io.File;


public interface EditorPanel  {

	void setAdvanced ( boolean b );

	void addPropertyChangeListener ( PropertyChangeListener editorPanelListener );

	void setTexteditor ( boolean b );

	void handleCopy ( );

	void handleCut ( );

	void handlePaste ( );

	void handleRedo ( );

	void handleUndo ( );

	boolean handleSaveAs ( );

	boolean handleSave ( );

	File getFile ( );

	void setFileName ( String name );

	void setEditorText ( String string );

	void setFile ( File file );

	boolean isRedoStatus ( );

	boolean isUndoStatus ( );

	boolean isTexteditor ( );

	String getFileName ( );

	boolean shouldBeSaved ( );

	public void handlePrint();

}
