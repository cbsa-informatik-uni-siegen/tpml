package de.unisiegen.tpml.graphics;

import javax.swing.JComponent;
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
		
	}
	
	protected abstract void nodesChanged (TreeModelEvent event);
	
	protected abstract void nodesRemoved (TreeModelEvent event);
	
	protected abstract void treeContentChanged ();
	
	protected abstract void relayout ();
	
	public void setAvailableWidth (int availableWidth) {
		this.availableWidth = availableWidth;
		relayout();
	}
	
}
