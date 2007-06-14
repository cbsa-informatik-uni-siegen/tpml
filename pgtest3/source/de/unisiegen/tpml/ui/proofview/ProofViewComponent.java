package de.unisiegen.tpml.ui.proofview;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.CannotRedoException;
import de.unisiegen.tpml.core.CannotUndoException;
import de.unisiegen.tpml.core.ProofModel;
import de.unisiegen.tpml.graphics.ProofView;
import de.unisiegen.tpml.ui.EditorComponent;

/**
 * Editor Component that displays a Proof. 
 * It unites undo / redo functions of the model (core) and visualization.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * 
 */
public class ProofViewComponent extends JComponent implements EditorComponent {
	//
	// Constants
	//
	
	/**
	 * The {@link Logger} for this class.
	 */
	private static final Logger logger = Logger.getLogger(ProofViewComponent.class);
	
	/**
	 * The unique serialization identifier.
	 */
	private static final long serialVersionUID = 8218146393722855647L;

	
	
	//
	// Attributes
	//

	/**
	 * TODO Add documentation here.
	 */
	private ProofView view;

	/**
	 * TODO Add documentation here.
	 */
	private ProofModel model;

	/**
	 * TODO Add documentation here.
	 */
	private boolean nextStatus;

	/**
	 * TODO Add documentation here.
	 */
	private boolean pongStatus;

	/**
	 * TODO Add documentation here.
	 */
	private boolean redoStatus;

	/**
	 * TODO Add documentation here.
	 */
	private boolean undoStatus;
	
	
	
	//
	// Constructor
	//
	
	/**
	 * TODO Add documentation here.
	 */
	public ProofViewComponent(ProofView view, ProofModel model) {
		if (model == null || view == null) {
			throw new NullPointerException("model or view are null");
		}
		setLayout(new BorderLayout());
		this.view = view;
		this.model = model;

		this.model.addPropertyChangeListener(new ModelChangeListener());
		add((JComponent) view, BorderLayout.CENTER);
	}

	/**
	 * sets the model of the ProofViewComponent
	 * @param m the modle
	 */
	public void setModel (ProofModel m)
	{
		model = m;
		this.model.addPropertyChangeListener(new ModelChangeListener());
		add((JComponent) view, BorderLayout.CENTER);
		setDefaultStates ( );
		//setPongStatus ( false );
		
		
		//setPongStatus ( false );
		//setRedoStatus ( false );
		//setUndoStatus ( false );
		
	}
	
	
	//
	// Accessors
	//
	
	/**
	 * Returns the Next Status of the Component.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isNextStatus()
	 */
	public boolean isNextStatus() {
		return this.nextStatus;
	}

	/**
	 * Sets the Next Status of the Component.
	 *
	 * @param nextStatus the new setting for the <code>nextStatus</code> property.
	 * 
	 * @see #isNextStatus()
	 */
	private void setNextStatus(boolean nextStatus) {
		if (this.nextStatus != nextStatus) {
			boolean oldNextStatus = this.nextStatus;
			this.nextStatus = nextStatus;
			firePropertyChange("nextStatus", oldNextStatus, nextStatus);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#isPongStatus()
	 */
	public boolean isPongStatus() {
		return this.pongStatus;
	}
	
	/**
	 * Sets the Pong Status of the Component.
	 * 
	 * @param pongStatus the new setting for the <code>pongStatus</code> property.
	 * 
	 * @see #isPongStatus()
	 */
	public void setPongStatus(boolean pongStatus) {
		if (this.pongStatus != pongStatus) {
			boolean oldPongStatus = this.pongStatus;
			this.pongStatus = pongStatus;
			firePropertyChange("pongStatus", oldPongStatus, pongStatus);
		}
	}

	/**
	 * Returns the Redo Status of the Component.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isRedoStatus()
	 */
	public boolean isRedoStatus() {
		return this.redoStatus;
	}

	/**
	 * Sets the Redo Status of the Component.
	 * 
	 * @param redoStatus the new setting for the <code>redoStatus</code> property.
	 *
	 * @see #isRedoStatus()
	 */
	private void setRedoStatus(boolean redoStatus) {
		if (this.redoStatus != redoStatus) {
			boolean oldRedoStatus = this.redoStatus;
			this.redoStatus = redoStatus;
			firePropertyChange("redoStatus", oldRedoStatus, redoStatus);
		}
	}

	/**
	 * Returns the Undo Status of the Component.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isUndoStatus()
	 */
	public boolean isUndoStatus() {
		return this.undoStatus;
	}

	/**
	 * Sets the Undo Status of the Component.
	 *
	 * @param undoStatus the new setting for the <code>undoStatus</code> property.
	 * 
	 * @see #isUndoStatus()
	 */
	private void setUndoStatus(boolean undoStatus) {
		if (this.undoStatus != undoStatus) {
			boolean oldUndoStatus = this.undoStatus;
			this.undoStatus = undoStatus;
			firePropertyChange("undoStatus", oldUndoStatus, undoStatus);
		}
	}

	
	
	//
	// Primitives
	//
	
	/**
	 * Sets the Default States of the Component's functions.
	 * Attention: For now the NextStatus is alsways enabled.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setDefaultStates()
	 */
	public void setDefaultStates() {
		setNextStatus(!this.model.isFinished());
		setRedoStatus(this.model.isRedoable());
		setUndoStatus(this.model.isUndoable());
	}

	/**
	 * Executes the next function on the view.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleNext()
	 */
	public void handleNext() {
		try {
			this.view.guess();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getTopLevelAncestor(), MessageFormat.format(java.util.ResourceBundle.getBundle(
			"de/unisiegen/tpml/ui/ui").getString("NodeComponent.5"), e.getMessage()), java.util.ResourceBundle.getBundle(
			"de/unisiegen/tpml/ui/ui").getString("NodeComponent.6"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	/**
	 * Executes the redo function on the view.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleRedo()
	 */
	public void handleRedo() {
		try {
			this.model.redo();
		} catch (CannotRedoException e) {
			logger.error("Can not redo on this model", e);
		}
	}

	/**
	 * Executes the undo function on the view.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleUndo()
	 */
	public void handleUndo() {
		try {
			this.model.undo();
		} catch (CannotUndoException e) {
			logger.error("Can not undo on this model", e);
		}

	}

	/**
	 * Handles Property Changes fired by the model.
	 * it supports:	cheating, undoable, redoable, and finished.
	 */
	private class ModelChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			// determine the name of the changed property
			String propertyName = evt.getPropertyName().intern();
			
			// the undo/redo/next stati
			if (propertyName == "undoable") {
				setUndoStatus((Boolean) evt.getNewValue());
			} else if (propertyName == "redoable") {
				setRedoStatus((Boolean) evt.getNewValue());
			} else if (propertyName == "finished") {
				setNextStatus(!(Boolean) evt.getNewValue());
			}
			
			// the pong status
			if (propertyName == "cheating" || propertyName == "finished") {
				setPongStatus(!ProofViewComponent.this.model.isCheating() && ProofViewComponent.this.model.isFinished());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.ui.EditorComponent#setAdvanced(boolean)
	 */
	public void setAdvanced(boolean status) {
		this.view.setAdvanced(status);
	}

	public ProofModel getModel()
	{
		return this.model;
		
	}
}
