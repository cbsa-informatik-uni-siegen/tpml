package de.unisiegen.tpml.ui.editor;

import java.awt.BorderLayout;
import java.util.Stack;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledEditorKit;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;
import de.unisiegen.tpml.ui.EditorComponent;

/**
 * //TODO add documentation here.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * 
 */
public class TextEditorPanel extends JPanel implements EditorComponent {

	private static final Logger logger = Logger
			.getLogger(TextEditorPanel.class);

	/**
	 * The serial version Identifier.
	 */
	private static final long serialVersionUID = -4886621661465144817L;
	
	private JEditorPane	editor;
	
	private StyledLanguageDocument document;
	
	private JScrollPane scrollpane;
	/**
	 * TODO add documentation here
	 * 
	 */
	private boolean changed;

	private Stack<String> undohistory;

	private Stack<String> redohistory;

	private DocumentListener doclistener;

	/**
	 * TODO add documentation here
	 */
	public TextEditorPanel(Language language) {
		if (language == null) throw new NullPointerException("language is null");
		setLayout(new BorderLayout());
		initComponents(language);
	}
	
	private void initComponents (Language language){
		editor = new JEditorPane();
		document = new StyledLanguageDocument(language);
		scrollpane = new JScrollPane();
		doclistener = new TextDocumentListener();
		undohistory = new Stack<String>();
		redohistory = new Stack<String>();
		
		
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setViewportView (editor);	
		
		editor.setEditorKit(new StyledEditorKit());
		editor.setDocument(document);
		editor.setAutoscrolls(false);
		
		document.addDocumentListener(doclistener);

		undohistory.push("");

		add(scrollpane, BorderLayout.CENTER);
	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isNextStatus()
	 */
	public boolean isNextStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setNextStatus(boolean)
	 */
	public void setNextStatus(boolean nextStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
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
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setRedoStatus(boolean)
	 */
	public void setRedoStatus(boolean redoStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
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
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setSaveStatus(boolean)
	 */
	public void setSaveStatus(boolean saveStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
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
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setUndoStatus(boolean)
	 */
	public void setUndoStatus(boolean undoStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#isChangeStatus()
	 */
	public boolean isChangeStatus() {
		return changed;
	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setChangeStatus(boolean)
	 */
	public void setChangeStatus(boolean changeStatus) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#setDefaultStates()
	 */
	public void setDefaultStates() {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleNext()
	 */
	public void handleNext() {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleRedo()
	 */
	public void handleRedo() {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO add documentation here
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.unisiegen.tpml.ui.EditorComponent#handleUndo()
	 */
	public void handleUndo() {
		// TODO Auto-generated method stub

	}

	private class TextDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent arg0) {
			try{
			if (!TextEditorPanel.this.isChangeStatus()) {
				TextEditorPanel.this.setChangeStatus(true);
			}
			String doctext = arg0.getDocument().getText(0,
					arg0.getDocument().getLength());
			undohistory.push(doctext);
			setUndoStatus(true);
			setRedoStatus(false);
			redohistory.clear();
			logger.debug("Text inserted into editor.");
			}
			catch (BadLocationException e){
				logger.debug("Failed to add text to undo history", e);
			}
		}

		public void removeUpdate(DocumentEvent arg0) {
			try{
			if (!TextEditorPanel.this.isChangeStatus()) {
				TextEditorPanel.this.setChangeStatus(true);
			}
			undohistory.push(arg0.getDocument().getText(0,
					arg0.getDocument().getLength()));
			setUndoStatus(true);
			setRedoStatus(false);
			redohistory.clear();
			logger.debug("Text removed from editor.");
			}
			catch (BadLocationException e){
				logger.debug("Failed to add text to undo history", e);
			}
		}

		public void changedUpdate(DocumentEvent arg0) {
		}
	}
}
