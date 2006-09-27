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

	private JEditorPane editor;

	private StyledLanguageDocument document;

	private JScrollPane scrollpane;
	
	/**
	 * The initial content of this file
	 * 
	 */
	private String initialContent;
	
	private Stack<String> undohistory;

	private Stack<String> redohistory;

	private DocumentListener doclistener;

	private boolean nextStatus;

	private boolean redoStatus;

	private boolean undoStatus;

	private boolean changed;

	/**
	 * TODO add documentation here
	 */
	public TextEditorPanel(Language language) {
		if (language == null)
			throw new NullPointerException("language is null");
		setLayout(new BorderLayout());
		initComponents(language);
	}

	private void initComponents(Language language) {
		editor = new JEditorPane();
		document = new StyledLanguageDocument(language);
		scrollpane = new JScrollPane();
		doclistener = new TextDocumentListener();
		initialContent = "";
		undohistory = new Stack<String>();
		redohistory = new Stack<String>();

		scrollpane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setViewportView(editor);

		editor.setEditorKit(new StyledEditorKit());
		editor.setDocument(document);
		editor.setAutoscrolls(false);

		document.addDocumentListener(doclistener);

		undohistory.push("");

		add(scrollpane, BorderLayout.CENTER);
	}

	public boolean isNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(boolean nextStatus) {
		firePropertyChange("nextStatus", this.nextStatus, nextStatus);
		this.nextStatus = nextStatus;
	}

	public boolean isRedoStatus() {
		return redoStatus;
	}

	public void setRedoStatus(boolean redoStatus) {
		firePropertyChange("redoStatus", this.redoStatus, redoStatus);
		this.redoStatus = redoStatus;
	}

	public boolean isUndoStatus() {
		return undoStatus;
	}

	public void setUndoStatus(boolean undoStatus) {
		firePropertyChange("undoStatus", this.undoStatus, undoStatus);
		this.undoStatus = undoStatus;
	}

	public void setDefaultStates() {
		setChanged(false);
		setUndoStatus(false);
		setRedoStatus(false);
		setNextStatus(false);
	}

	public void setChanged(boolean changeStatus) {
		firePropertyChange("changed", this.changed, changeStatus);
		this.changed = changeStatus;
	}

	public boolean isChanged() {
		return changed;
	}

	public String getText() {
		try {
			return document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			logger.error("Cannot get Text from document", e);
		}
		return "";
	}

	public void setText(String text) {
		try {
			initialContent = text;
			
			document.removeDocumentListener(doclistener);

			document.remove(0, document.getLength());
			document.insertString(0, text, null);
			setRedoStatus(false);
			redohistory.clear();
			setUndoStatus(false);
			undohistory.clear();
			
			undohistory.push(text);

			document.addDocumentListener(doclistener);
		} catch (BadLocationException e) {
			logger.error("Cannot set Text of the document", e);
		}

	}

	/**
	 * //TODO add documentation here and clean everything up
	 * 
	 * @author Christoph Fehling
	 * @version $Rev$
	 * 
	 */
	private class TextDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent arg0) {
			try {
				TextEditorPanel.this.setChanged(true);

				String doctext = arg0.getDocument().getText(0,
						arg0.getDocument().getLength());
				if (doctext.endsWith(" ")) {
					undohistory.push(doctext);
					logger.debug("history added: " + doctext);
				}
				setUndoStatus(true);

				setRedoStatus(false);
				redohistory.clear();
			} catch (BadLocationException e) {
				logger.error("Failed to add text to undo history", e);
			}
		}

		public void removeUpdate(DocumentEvent arg0) {
			try {
				TextEditorPanel.this.setChanged(true);

				String doctext = arg0.getDocument().getText(0,
						arg0.getDocument().getLength());
				if (doctext.endsWith(" ")) {
					undohistory.push(doctext);
					logger.debug("history added: " + doctext);
				}
				setUndoStatus(true);

				setRedoStatus(false);
				redohistory.clear();
			} catch (BadLocationException e) {
				logger.error("Failed to add text to undo history", e);
			}
		}

		public void changedUpdate(DocumentEvent arg0) {
		}
	}

	public void handleNext() {
		// TODO Auto-generated method stub

	}

	public void handleRedo() {
		try {
			document.removeDocumentListener(doclistener);
			
			undohistory.push(document.getText(0, document.getLength()));
			document.remove(0, document.getLength());
			document.insertString(0, redohistory.pop(), null);
					
			setUndoStatus(true);
			document.addDocumentListener(doclistener);
			if (redohistory.size() == 0)
				setRedoStatus(false);
		} catch (BadLocationException e) {
			logger.error("Cannot handle an undo", e);
		}
	}

	public void handleUndo() {
		try {
			document.removeDocumentListener(doclistener);

			String doctext = document.getText(0, document.getLength());
			String historytext;

			if (undohistory.peek().equals(initialContent)){
				historytext = undohistory.peek();
				setUndoStatus(false);
			}
			else{
				historytext = undohistory.pop();
			}

			document.remove(0, document.getLength());
			document.insertString(0, historytext, null);

			redohistory.add(doctext);
			setRedoStatus(true);

			document.addDocumentListener(doclistener);
		} catch (BadLocationException e) {
			logger.error("Cannot handle an undo", e);
		}

	}

	public StyledLanguageDocument getDocument() {
		return document;
	}

}
