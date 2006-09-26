/*
 * EditorPanel.java
 *
 * Created on 17. September 2006, 14:59
 */

package de.unisiegen.tpml.ui;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.graphics.ProofView;
import de.unisiegen.tpml.graphics.ProofViewFactory;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView;
import de.unisiegen.tpml.ui.editor.TextEditorPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.log4j.Logger;

import proofview.ProofViewComponent;

/**
 * Part of the UI displayed in the tabbed pane. It includes one open file and
 * all {@link de.unisiegen.tpml.ui.EditorComponent}s open for that file.
 * 
 * @author Christoph Fehling
 */
public class EditorPanel extends javax.swing.JPanel {

	/** Creates new form EditorPanel */
	public EditorPanel(Language language) {
		initComponents();
		// setting the default button states
		nextButton.setVisible(false);
		actionToolBar.setVisible(false);
		smallstepButton.setVisible(false);
		bigstepButton.setVisible(false);
		typecheckerButton.setVisible(false);
		//
		this.language = language;
		setFileName("newfile" + num + "." + language.getName());
		num++;
		editorComponentListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				componentStatusChanged(evt.getPropertyName(), evt.getNewValue());
			}
		};
		initEditor();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jSeparator1 = new javax.swing.JSeparator();
        editorToolBar = new javax.swing.JToolBar();
        codeButton = new javax.swing.JToggleButton();
        smallstepButton = new javax.swing.JToggleButton();
        bigstepButton = new javax.swing.JToggleButton();
        typecheckerButton = new javax.swing.JToggleButton();
        actionToolBar = new javax.swing.JToolBar();
        nextButton = new javax.swing.JButton();
        editorPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        codeButton.setText("Code");
        codeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(codeButton);

        smallstepButton.setText("SmallStep");
        smallstepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smallstepButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(smallstepButton);

        bigstepButton.setText("BigStep");
        bigstepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bigstepButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(bigstepButton);

        typecheckerButton.setText("TypeChecker");
        typecheckerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecheckerButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(typecheckerButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/next.png")));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        actionToolBar.add(nextButton);

        editorToolBar.add(actionToolBar);

        add(editorToolBar, java.awt.BorderLayout.NORTH);

        editorPanel.setLayout(new java.awt.BorderLayout());

        add(editorPanel, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

        private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
// TODO add your handling code here:
        activeEditorComponent.handleNext();
        }//GEN-LAST:event_nextButtonActionPerformed

	private void typecheckerButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_typecheckerButtonActionPerformed
		setComponent(typechecker);
		deselectButtons();
		typecheckerButton.setSelected(true);
	}// GEN-LAST:event_typecheckerButtonActionPerformed

	private void bigstepButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bigstepButtonActionPerformed
		setComponent(bigstep);
		deselectButtons();
		bigstepButton.setSelected(true);
	}// GEN-LAST:event_bigstepButtonActionPerformed

	private void smallstepButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_smallstepButtonActionPerformed
		// 
		setComponent(smallstep);
		deselectButtons();
		smallstepButton.setSelected(true);
	}// GEN-LAST:event_smallstepButtonActionPerformed

	private void codeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_codeButtonActionPerformed
		// 
		setComponent(code);
		deselectButtons();
		codeButton.setSelected(true);
	}// GEN-LAST:event_codeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar actionToolBar;
    private javax.swing.JToggleButton bigstepButton;
    private javax.swing.JToggleButton codeButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JToolBar editorToolBar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton nextButton;
    private javax.swing.JToggleButton smallstepButton;
    private javax.swing.JToggleButton typecheckerButton;
    // End of variables declaration//GEN-END:variables

	private static final Logger logger = Logger.getLogger(EditorPanel.class);

	/**
	 * 
	 */
	private TextEditorPanel code;

	private EditorComponent smallstep;

	private EditorComponent bigstep;

	private EditorComponent typechecker;
	
	private EditorComponent activeEditorComponent;

	private PropertyChangeListener editorComponentListener;

	/**
	 * Filename displayed in the tab.
	 */
	private String filename;

	/**
	 * static number counting the new files with default name.
	 */
	static private int num = 0;

	/**
	 * The language used in this Editor.
	 */
	private Language language;

	/**
	 * The file to which this document is saved.
	 */
	private File file;

	/**
	 * Indicated if the file was changed.
	 */
	private boolean changed;

	/**
	 * Indicates the status of the Undo function.
	 */
	private boolean undoStatus;

	/**
	 * Indicates the status of the Redo function.
	 */
	private boolean redoStatus;

	/**
	 * Indicates the status of the Save function.
	 */
	private boolean saveStatus;

	// self defined methods

	/**
	 * Is called when a status of the displayed component in changed
	 * 
	 * @param ident
	 *            name of the changed status
	 * @param newValue
	 *            new value of the status
	 */
	private void componentStatusChanged(String ident, Object newValue) {
		if (ident.equals("nextStatus")) {
			nextButton.setEnabled((Boolean) newValue);
		} else if (ident.equals("redoStatus")) {
			setRedoStatus((Boolean) newValue);
		} else if (ident.equals("saveStatus")) {
			setSaveStatus((Boolean) newValue);
		} else if (ident.equals("title")) {
			setFileName((String) newValue);
		} else if (ident.equals("undoStatus")) {
			setUndoStatus((Boolean) newValue);
		} else if (ident.equals("changeStatus")) {
			setChanged((Boolean) newValue);
		}

	}

	private void updateComponentStates(EditorComponent comp) {
		setRedoStatus(comp.isRedoStatus());
		setUndoStatus(comp.isUndoStatus());
		setSaveStatus(comp.isSaveStatus());
		nextButton.setEnabled(comp.isNextStatus());
	}

	/**
	 * TODO merge these two
	 * 
	 * @param comp
	 */
	private void setComponent(EditorComponent comp) {
		updateComponentStates(comp);
		editorPanel.removeAll();
		editorPanel.add((JComponent) comp, BorderLayout.CENTER);
		activeEditorComponent = comp;
		paintAll(getGraphics());
	}

	private EditorComponent getComponent() {
		return (EditorComponent) editorPanel.getComponent(0);
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * source editor.
	 */
	private void initEditor() {
		code = new TextEditorPanel(language);
		// code = new TestEditorComponent("Code");
		editorPanel.removeAll();
		editorPanel.add((JPanel) code, BorderLayout.CENTER);
		// ((JPanel) code).
		((JPanel) code).addPropertyChangeListener(editorComponentListener);
		code.setDefaultStates();
		updateComponentStates(code);
		deselectButtons();
		codeButton.setSelected(true);
		codeButton.setEnabled(true);
		paintAll(this.getGraphics());
	}

	/**
	 * Starts the Small Step Interpreter.
	 * 
	 */
	public void handleSmallStep() {
		smallstep = new TestEditorComponent("SmallStep");
		activateFunction(smallstepButton, smallstep);
	}

	/**
	 * Starts the Big Step Interpreter.
	 */
	public void handleBigStep() {
		bigstep = new TestEditorComponent("BigStep");
		activateFunction(bigstepButton, bigstep);
	}

	/**
	 * Starts the Type Checker.
	 */
public void handleTypeChecker() {
	try {
		typechecker = new ProofViewComponent(ProofViewFactory.newTypeCheckerView(language.newTypeCheckerProofModel(code.getDocument().getExpression())));
		editorPanel.removeAll();
		activateFunction(typecheckerButton, typechecker);
		paintAll(getGraphics());
		
	} catch (Exception e){
		logger.error("Could not create new TypeCheckerView", e);
	}
	}
	/**
	 * activates one of the following: smallstep, bigstep, typechecker etc.
	 * buttons and special component functions.
	 * 
	 * @param button
	 *            the button to be activated
	 * @param comp
	 *            the component related to that button
	 */
	private void activateFunction(JToggleButton button, EditorComponent comp) {
		comp.setDefaultStates();
		((JComponent) comp).addPropertyChangeListener(editorComponentListener);
		setComponent(comp);
		deselectButtons();
		button.setSelected(true);
		button.setVisible(true);
		if (comp.isNextStatus()) {
			nextButton.setVisible(true);
			actionToolBar.setVisible(true);
		}
	}
	

	/**
	 * Sets the select states of the code, smallstep, bigstep and typechecker
	 * buttons to false.
	 * 
	 */
	private void deselectButtons() {
		codeButton.setSelected(false);
		smallstepButton.setSelected(false);
		bigstepButton.setSelected(false);
		typecheckerButton.setSelected(false);
	}

	/**
	 * Returns the redo status
	 * 
	 * @return true if redo is available
	 */
	public boolean isRedoStatus() {
		return redoStatus;
	}

	/**
	 * Sets the redo status.
	 * 
	 * @param redoStatus
	 *            redo status to be set.
	 */
	public void setRedoStatus(boolean redoStatus) {
		firePropertyChange("redoStatus", this.redoStatus, redoStatus);
		this.redoStatus = redoStatus;
	}

	/**
	 * Returns the save status. TODO merge this with the changestate to remove
	 * redundancy
	 * 
	 * @return true if save is available.
	 */
	public boolean isSaveStatus() {
		return saveStatus;
	}

	/**
	 * Sets the save status.
	 * 
	 * @param saveStatus
	 *            <code>true</code> if the file can / should be saved.
	 */
	public void setSaveStatus(boolean saveStatus) {
		firePropertyChange("saveStatus", this.saveStatus, saveStatus);
		this.saveStatus = saveStatus;
	}

	/**
	 * Returns the file name.
	 * 
	 * @return the file name.
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * Sets the file name.
	 * 
	 * @param filename
	 *            the file name to be set.
	 * 
	 * @exception NullPointerException
	 *                if <code>filename</code> is <code>null</code>
	 */
	public void setFileName(String filename) {
		if (filename == null)
			throw new NullPointerException("filename is null");
		firePropertyChange("filename", this.filename, filename);
		this.filename = filename;
	}

	/**
	 * Returns the file name.
	 * 
	 * @return the file name.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the <code>File</code> for this editor.
	 * 
	 * @param file
	 *            the <code>File</code> to be set.
	 * 
	 * @throws NullPointerException
	 *             if the <code>File</code> is <code>null</code>.
	 */
	public void setFile(File file) {
		if (file == null)
			throw new NullPointerException("File is null");
		this.file = file;
		this.filename = file.getName();
	}

	/**
	 * Returns the language used in this editor.
	 * 
	 * @return the language used.
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * TODO add documentation here
	 * 
	 * @return <code>true</code> if the editor's document was changed.
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * Sets the change status of the editor
	 * 
	 * @param changed
	 *            true if the editor's document was changed.
	 */
	public void setChanged(boolean changed) {
		firePropertyChange("changed", this.changed, changed);
		this.changed = changed;
	}

	public String getEditorText() {
		return code.getText();
	}

	public void setEditorText(String text) {
		code.setText(text);
	}

	/**
	 * Sets the undo status
	 * 
	 * @return true if the undo function is available
	 */
	public boolean isUndoStatus() {
		return undoStatus;
	}

	public void setUndoStatus(boolean undoStatus) {
		firePropertyChange("undoStatus", this.undoStatus, undoStatus);
		this.undoStatus = undoStatus;
	}

	public void handleUndo() {
		getComponent().handleUndo();
	};

	public void handleRedo() {
		getComponent().handleRedo();
	};

	public void handleSave() {
		if (file == null)
			handleSaveAs();
		else
			writeFile();
	};

	public void handleSaveAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(getParent());
		File outfile = chooser.getSelectedFile();
		if (outfile != null) {

			try {
				outfile.createNewFile();
			} catch (IOException e) {

			}
			setFile(outfile);
			setFileName(outfile.getName());
			writeFile();
		}
	}

	private void writeFile() {
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(code.getText().getBytes());
			out.flush();
			out.close();
			setChanged(false);
			setSaveStatus(false);
		} catch (IOException e) {
			logger.error("Could not write to file", e);
			JOptionPane.showMessageDialog(this, "Could not write to file.",
					"Write File", JOptionPane.ERROR_MESSAGE);
		}
	}

}
