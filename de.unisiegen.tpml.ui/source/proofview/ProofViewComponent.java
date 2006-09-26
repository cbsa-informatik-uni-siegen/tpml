/**
 * TODO add documentation here
 * 
 */
package proofview;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.unisiegen.tpml.graphics.ProofView;
import de.unisiegen.tpml.ui.EditorComponent;

/**
 * //TODO add documentation here.
 *
 * @author Christoph Fehling
 * @version $Rev$ 
 *
 */
public class ProofViewComponent extends JComponent implements EditorComponent {
	
	private ProofView view;
	private boolean nextStatus;
    private boolean redoStatus;
    private boolean undoStatus;
    private boolean saveStatus;
	
	public ProofViewComponent(ProofView view){
		setLayout(new BorderLayout());
		this.view = view;
		add((JComponent)view, BorderLayout.CENTER);
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#isNextStatus()
	 */
	public boolean isNextStatus() {
		// TODO Auto-generated method stub
		return nextStatus;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setNextStatus(boolean)
	 */
	public void setNextStatus(boolean nextStatus) {
		firePropertyChange ("nextStatus", this.nextStatus, nextStatus);
		this.nextStatus=nextStatus;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#isRedoStatus()
	 */
	public boolean isRedoStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setRedoStatus(boolean)
	 */
	public void setRedoStatus(boolean redoStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#isSaveStatus()
	 */
	public boolean isSaveStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setSaveStatus(boolean)
	 */
	public void setSaveStatus(boolean saveStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#isUndoStatus()
	 */
	public boolean isUndoStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setUndoStatus(boolean)
	 */
	public void setUndoStatus(boolean undoStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#isChangeStatus()
	 */
	public boolean isChangeStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setChangeStatus(boolean)
	 */
	public void setChangeStatus(boolean changeStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#setDefaultStates()
	 */
	public void setDefaultStates() {
		// TODO Auto-generated method stub
		setNextStatus(true);
		setRedoStatus(false);
		setUndoStatus(false);
		setChangeStatus(false);
		setSaveStatus(false);

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleNext()
	 */
	public void handleNext() {
		// TODO Auto-generated method stub
		try{
		view.guess();
		}
		catch (Exception e){
			//TODO log or whatever...
		}

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleRedo()
	 */
	public void handleRedo() {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here 
	 *
	 * {@inheritDoc}
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleUndo()
	 */
	public void handleUndo() {
		// TODO Auto-generated method stub

	}

}
