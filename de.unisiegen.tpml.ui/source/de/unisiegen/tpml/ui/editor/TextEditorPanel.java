package de.unisiegen.tpml.ui.editor;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.graphics.StyledLanguageDocument;
import de.unisiegen.tpml.graphics.StyledLanguageEditor;
import de.unisiegen.tpml.ui.EditorComponent;
import de.unisiegen.tpml.ui.SideBar;
import de.unisiegen.tpml.ui.SideBarListener;

/**
 * //TODO add documentation here. TODO this thing really needs cleaning up
 * especially the cut copy paste functions...
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * 
 */
public class TextEditorPanel extends JPanel implements EditorComponent, ClipboardOwner {

	private static final Logger logger = Logger.getLogger(TextEditorPanel.class);

	/**
	 * The serial version Identifier.
	 */
	private static final long serialVersionUID = -4886621661465144817L;

	private StyledLanguageEditor editor;

	private StyledLanguageDocument document;

	private JScrollPane scrollpane;
	
	private SideBar			sideBar;

	/**
	 * The initial content of this file
	 * 
	 */
	private String initialContent;

	private String currentContent;

	private Stack<String> undohistory;

	private Stack<String> redohistory;

	private DocumentListener doclistener;

	private boolean nextStatus;

	private boolean redoStatus;

	private boolean undoStatus;

	private boolean changed;

	private JPopupMenu popup;

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
		editor = new StyledLanguageEditor();

		document = new StyledLanguageDocument(language);
		
		JPanel compoundPanel = new JPanel ();
		compoundPanel.setLayout(new BorderLayout ());
		
		this.scrollpane = new JScrollPane();
		compoundPanel.add(this.scrollpane, BorderLayout.CENTER);
		
		this.sideBar = new SideBar (this.scrollpane,
																this.document,
																this.editor);
		this.sideBar.addSibeBarListener(new SideBarListener() {
			public void markText (int left, int right) {
				TextEditorPanel.this.selectErrorText(left, right);
			}
		});
		compoundPanel.add(this.sideBar, BorderLayout.WEST);
		

		doclistener = new TextDocumentListener();
		initialContent = "";
		currentContent = "";
		undohistory = new Stack<String>();
		redohistory = new Stack<String>();

		scrollpane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setViewportView(editor);

		editor.setDocument(document);
		editor.setAutoscrolls(false);

		document.addDocumentListener(doclistener);

		undohistory.push("");

		// the popup menu and listeners
		popup = new JPopupMenu();
		MenuListener menulistener = new MenuListener();
		JMenuItem copyItem = new JMenuItem("Copy");
		JMenuItem cutItem = new JMenuItem("Cut");
		JMenuItem pasteItem = new JMenuItem("Paste");
		copyItem.addActionListener(menulistener);
		copyItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/copy16.gif")));
		cutItem.addActionListener(menulistener);
		cutItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/cut16.gif")));
		pasteItem.addActionListener(menulistener);
		pasteItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/paste16.gif")));
		popup.add(copyItem);
		popup.add(cutItem);
		popup.add(pasteItem);
		editor.addMouseListener(new PopupListener());

		add(compoundPanel, BorderLayout.CENTER);
	}
	
	private void selectErrorText (int left, int right) {
		this.editor.select(left, right);
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

	public String getSelectedText() {
		return editor.getSelectedText();
	}

	public void insertText(String text) {
		try {
			document.insertString(editor.getCaretPosition(), text, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeSelectedText() {
		int start = editor.getSelectionStart();
		int end = editor.getSelectionEnd();
		try {
			if (start < end) {
				document.remove(start, (end-start));
			} else {
				document.remove(end, (start-end));
			}
		} catch (BadLocationException e) {
			logger.error("Cannot remove text from document", e);
		}
	}

	public void setText(String text) {
		try {
			initialContent = text;
			currentContent = text;

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

	public StyledLanguageEditor getEditor() {
		return editor;
	}

	/**
	 * //TODO add documentation here and clean everything up
	 * 
	 * @author Christoph Fehling
	 * @version $Rev$
	 * 
	 */

	public void handleNext() {
		// this function is not implemented here

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

			if (undohistory.peek().equals(initialContent)) {
				historytext = undohistory.peek();
				setUndoStatus(false);
			} else {
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

	public void handleCut() {
		Clipboard clipboard = getToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(getSelectedText());
		clipboard.setContents(stringSelection, this);
		removeSelectedText();
	}

	public void handleCopy() {
		Clipboard clipboard = getToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(getSelectedText());
		clipboard.setContents(stringSelection, this);
	}

	public void handlePaste() {
		Clipboard clipboard = getToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				insertText((String) contents
						.getTransferData(DataFlavor.stringFlavor));
			} catch (UnsupportedFlavorException ex) {
				logger.error("Can not paste from clipboard", ex);
			} catch (IOException ex) {
				logger.error("Can not paste from clipboard", ex);
			}
		}
	}

	public StyledLanguageDocument getDocument() {
		return document;
	}

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
				currentContent = doctext;
			} catch (BadLocationException e) {
				logger.error("Failed to add text to undo history", e);
			}
		}

		public void removeUpdate(DocumentEvent arg0) {
			try {
				TextEditorPanel.this.setChanged(true);
				undohistory.push(currentContent);
				setRedoStatus(false);
				setUndoStatus(true);
				redohistory.clear();
				currentContent = (String) arg0.getDocument().getText(0,
						arg0.getDocument().getLength());

				// TextEditorPanel.this.setChanged(true);
				//
				// String doctext = arg0.getDocument().getText(0,
				// arg0.getDocument().getLength());
				// //if (doctext.endsWith(" ")) {
				// undohistory.push(doctext);
				// logger.debug("history added: " + doctext);
				// //}
				// setUndoStatus(true);
				//
				// setRedoStatus(false);
				// redohistory.clear();
			} catch (BadLocationException e) {
				logger.error("Failed to add text to undo history", e);
			}
		}

		public void changedUpdate(DocumentEvent arg0) {
		}
	}

	private class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	private class MenuListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			String command = evt.getActionCommand();

			if (command.equals("Copy")) {
				handleCopy();
			} else if (command.equals("Cut")) {
				handleCut();
			} else if (command.equals("Paste")) {
				handlePaste();
			}
		}
	}

	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// we do not care so we do nothing

	}

	public void setAdvanced(boolean status) {
		// the editor does not have an advanced mode so this is ignored.
		
	}

}
