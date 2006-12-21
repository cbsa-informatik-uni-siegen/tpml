package de.unisiegen.tpml.core.unification;

import javax.swing.ListModel;

import de.unisiegen.tpml.core.CannotRedoException;
import de.unisiegen.tpml.core.CannotUndoException;
import de.unisiegen.tpml.core.util.beans.Bean;

public interface UnifyListModel extends Bean, ListModel
{
	public void undo() throws CannotUndoException;
	
	public void redo() throws CannotRedoException;
	
	public void guess();
	
	
}
