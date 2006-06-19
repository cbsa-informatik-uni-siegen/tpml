package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyledEditorKit;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import ui.annotations.EditorActionInfo;
import ui.newgui.DefaultEditorAction;
import ui.newgui.EditorAction;
import ui.newgui.EditorComponent;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class SourceFile extends JPanel implements EditorComponent {
	private JPanel mypanel;

	static private int num = 0;

	private JScrollPane scrollPane;

	private JEditorPane editorPane;

	private String filename;

	private String title;

	// TODO merge the two lists
	private LinkedList<DefaultEditorAction> actions;

	private Hashtable myactions;

	private boolean modified;

	private Stack<String> undohistory;

	private Stack<String> redohistory;

	private DocumentListener doclistener;

	public SourceFile() {
		mypanel = new JPanel();
		actions = new LinkedList<DefaultEditorAction>();
		myactions = new Hashtable();
		undohistory = new Stack<String>();
		redohistory = new Stack<String>();

		undohistory.push("");

		doclistener = new DocumentListener() {
			public void insertUpdate(DocumentEvent arg0) {
				if (!SourceFile.this.isModified()) {
					SourceFile.this.setModified(true);
					SourceFile.this.setFilename("*" + filename);
				}
				//System.out.println("Part of document added.");
				try {
					String doctext = arg0.getDocument().getText(0,
							arg0.getDocument().getLength());
					// if (doctext.length() - history.peek().length() > 5
					// || doctext.length() - history.peek().length() < -5) {
					undohistory.push(doctext);
					// }
					setActionStatus("Undo", true);
					setActionStatus("Redo", false);
					redohistory.clear();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (!SourceFile.this.isModified()) {
					SourceFile.this.setModified(true);
					SourceFile.this.setFilename("*" + filename);
				}
				//System.out.println("Part of document removed.");
				try {
					undohistory.push(arg0.getDocument().getText(0,
							arg0.getDocument().getLength()));
					setActionStatus("Undo", true);
					setActionStatus("Redo", false);
					redohistory.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void changedUpdate(DocumentEvent arg0) {
				if (!SourceFile.this.isModified()) {
					SourceFile.this.setModified(true);
					SourceFile.this.setFilename("*" + filename);
				}
			}
		};

		this.setLayout(new BorderLayout());
		this.title = "Source";
		this.filename = "newfile" + num + ".ml";
		num++;
		this.scrollPane = new JScrollPane();
		this.editorPane = new JEditorPane();
		this.editorPane.setName(filename);
		this.editorPane.setAutoscrolls(false);
		this.editorPane.setEditorKit(new StyledEditorKit());
		this.editorPane.setDocument(new MLStyledDocument());
		this.scrollPane.setViewportView(editorPane);
		this.scrollPane.setName(filename);
		((MLStyledDocument) editorPane.getDocument())
				.addDocumentListener(doclistener);
		this.add(scrollPane, BorderLayout.CENTER);
		generateActions();
		setActionStatus("Undo", false);
		// setActionStatus("Redo", false);
	}

	@EditorActionInfo(name = "Undo", icon = "none")
	public void handleUndo() {
		try {
			getDocument().removeDocumentListener(doclistener);
			if (undohistory.peek().equals("")) {
				setActionStatus("Undo", false);
				redohistory.push(getDocument().getText(0,
						getDocument().getLength()));
				getDocument().remove(0, getDocument().getLength());
				getDocument().insertString(0, undohistory.peek(), null);
			} else {
				if (undohistory.peek().equals(
						getDocument().getText(0, getDocument().getLength()))) {
					undohistory.pop();
					handleUndo();
					return;
				} else {
					redohistory.push(getDocument().getText(0,
							getDocument().getLength()));
					getDocument().remove(0, getDocument().getLength());
					getDocument().insertString(0, undohistory.pop(), null);
				}
			}
			getDocument().addDocumentListener(doclistener);
			setActionStatus("Redo", true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EditorActionInfo(name = "Redo", icon = "none")
	public void handleRedo() {
		try {
			if (redohistory.size() > 0) {
				getDocument().removeDocumentListener(doclistener);
				getDocument().remove(0, getDocument().getLength());
				getDocument().insertString(0, redohistory.pop(), null);
				getDocument().addDocumentListener(doclistener);
				undohistory.push(getDocument().getText(0, getDocument().getLength()));
				setActionStatus("Undo", true);
				if (redohistory.size() == 0)
					setActionStatus("Redo", false);
			} else {
				setActionStatus("Redo", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateActions() {
		Class me = this.getClass();
		Method[] methods = me.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method tmp = methods[i];
			EditorActionInfo actioninfo = tmp
					.getAnnotation(EditorActionInfo.class);
			if (actioninfo != null) {
				DefaultEditorAction newaction = new DefaultEditorAction();
				newaction.setTitle(actioninfo.name());
				newaction.setEnabled(true);
				if (!actioninfo.icon().equals("none")) {
					// newaction.setIcon();
				}
				newaction.setGroup(1);
				newaction.setActionlistener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						try {
							tmp.invoke(SourceFile.this);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Add Handling!!
						}
					}
				});
				actions.add(newaction);
				myactions.put(tmp.getName().substring(6), newaction);
			}
		}
	}

	public void setName(String name) {
		this.filename = name;
		this.scrollPane.setName(this.filename);
	}

	public String getFilename() {
		return this.filename;
	}

	public Component getComponent() {
		return this.scrollPane;
	}

	public MLStyledDocument getDocument() {
		return (MLStyledDocument) editorPane.getDocument();
	}

	public List<EditorAction> getActions() {
		List<EditorAction> tmp = new LinkedList<EditorAction>();
		tmp.addAll(actions);
		return tmp;
	}

	public String getTitle() {

		return title;
	}

	public Component getDisplay() {
		return getComponent();
	}

	public void setFilename(String filename) {
		String filenameold = this.filename;
		this.filename = filename;
		firePropertyChange("filename", filenameold, filename);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		boolean modifiedold = this.modified;
		this.modified = modified;
		firePropertyChange("modified", modifiedold, modified);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setActionStatus(String name, boolean status) {
		((DefaultEditorAction) myactions.get(name)).setEnabled(status);
	}

}
