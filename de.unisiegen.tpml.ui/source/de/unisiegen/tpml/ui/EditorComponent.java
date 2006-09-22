package de.unisiegen.tpml.ui;

/**
 * The interface for every Editor Component (e.g. the code editor and smallstep
 * window.
 * 
 * @author Christoph Fehling
 * 
 */
public interface EditorComponent {
	/**
	 * Status of the next function.
	 * 
	 * @return true if next is enabled.
	 */
	public boolean isNextStatus();

	/**
	 * Sets the status of the next function
	 * 
	 * @param nextStatus
	 *            true if the next function should be enabled
	 */
	public void setNextStatus(boolean nextStatus);

	/**
	 * Status of the redo function.
	 * 
	 * @return true if redo is enabled.
	 */
	public boolean isRedoStatus();

	/**
	 * Sets the status of the redo function
	 * 
	 * @param redoStatus
	 *            true if the redo function should be enabled
	 */
	public void setRedoStatus(boolean redoStatus);

	/**
	 * Status of the save function.
	 * 
	 * @return true if save is enabled.
	 */
	public boolean isSaveStatus();

	/**
	 * Sets the status of the save function
	 * 
	 * @param redoStatus
	 *            true if the save function should be enabled
	 */
	public void setSaveStatus(boolean saveStatus);

	/**
	 * Status of the undo function.
	 * 
	 * @return true if undo is enabled.
	 */
	public boolean isUndoStatus();

	/**
	 * Sets the status of the undo function
	 * 
	 * @param redoStatus
	 *            true if the undo function should be enabled
	 */
	public void setUndoStatus(boolean undoStatus);

	/**
	 * Change status of the editor
	 * 
	 * @return true if the content of the editor was changed
	 */
	public boolean isChangeStatus();

	/**
	 * Sets the change status of the editor
	 * 
	 * @param redoStatus
	 *            true editor content was changed
	 */
	public void setChangeStatus(boolean changeStatus);

	/**
	 * Sets the default states for the editor functions
	 * 
	 */
	public void setDefaultStates();

	/**
	 * execute the next funtion on the component
	 * 
	 */
	public void handleNext();

	/**
	 * execute the redo funtion on the component
	 * 
	 */
	public void handleRedo();

	/**
	 * execute the undo funtion on the component
	 * 
	 */
	public void handleUndo();
}
