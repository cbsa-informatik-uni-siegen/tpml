package de.unisiegen.tpml.ui;

import javax.swing.JPanel;

public interface AbstractEditorComponent {
   
    public boolean isNextStatus();
    
    public void setNextStatus(boolean nextStatus);
    
       	public boolean isRedoStatus();
	public void setRedoStatus(boolean redoStatus);
	public boolean isSaveStatus();
	public void setSaveStatus(boolean saveStatus);
	public String getTitle();
	public void setTitle(String title);
	public boolean isUndoStatus();
	public void setUndoStatus(boolean undoStatus);
        
       public void setDefaultStates();
}
