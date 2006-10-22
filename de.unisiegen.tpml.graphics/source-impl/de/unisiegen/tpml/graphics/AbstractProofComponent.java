package de.unisiegen.tpml.graphics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import de.unisiegen.tpml.core.AbstractProofModel;
import de.unisiegen.tpml.core.languages.LanguageTranslator;

public abstract class AbstractProofComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4038129211367836077L;
	
	
	protected AbstractProofModel			proofModel;
	
	protected LanguageTranslator			translator;
	
	protected boolean									currentlyLayouting;
	
	protected	int											availableWidth;
	
	private Theme											theme;

	
	
	public	AbstractProofComponent (AbstractProofModel proofModel) {
		super ();
		
		this.proofModel 				= proofModel;
		this.currentlyLayouting	= false;
		this.translator 				= this.proofModel.getLanguage().newTranslator();

		this.proofModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged (TreeModelEvent e) {
				AbstractProofComponent.this.nodesChanged(e);
				AbstractProofComponent.this.relayout ();
			}
			public void treeNodesInserted (TreeModelEvent e) {
				AbstractProofComponent.this.nodesInserted (e);
				AbstractProofComponent.this.treeContentChanged ();
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				AbstractProofComponent.this.nodesRemoved (e);
				AbstractProofComponent.this.treeContentChanged ();
			}
			public void treeStructureChanged (TreeModelEvent e) {
				AbstractProofComponent.this.treeContentChanged ();
			}
		});
		
		// reset the layout whenever the font changes
		this.theme = Theme.currentTheme();
		this.theme.addPropertyChangeListener("font", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						resetLayout();
						relayout();
					}
				});
			}
		});
	}
	
	protected abstract void nodesInserted (TreeModelEvent event);
	
	protected abstract void nodesChanged (TreeModelEvent event);
	
	protected abstract void nodesRemoved (TreeModelEvent event);
	
	protected abstract void treeContentChanged ();
	
	protected abstract void relayout ();
	
	protected abstract void resetLayout ();
	
	public void setAvailableWidth (int availableWidth) {
		this.availableWidth = availableWidth;
		relayout();
	}
	
}
