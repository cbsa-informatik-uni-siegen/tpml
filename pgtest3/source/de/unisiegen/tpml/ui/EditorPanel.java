/*
 * EditorPanel.java
 *
 * Created on 17. September 2006, 14:59
 */

package de.unisiegen.tpml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.graphics.ProofViewFactory;
import de.unisiegen.tpml.graphics.pong.PongView;
import de.unisiegen.tpml.ui.editor.TextEditorPanel;
import de.unisiegen.tpml.ui.proofview.ProofViewComponent;

/**
 * Part of the UI displayed in the tabbed pane. It includes one open file and
 * all {@link de.unisiegen.tpml.ui.EditorComponent}s open for that file.
 * 
 * @author Christoph Fehling
 */
public class EditorPanel extends javax.swing.JPanel {

	/**
	 * The serial version UID
	 * 
	 */
	private static final long serialVersionUID = -272175525193942130L;

	/** Creates new form EditorPanel */
	public EditorPanel(Language language, MainWindow window) {
		initComponents();

		this.window = window;
		// setting the default button states
		nextButton.setVisible(false);
                pongButton.setVisible(false);
		smallstepButton.setVisible(false);
		bigstepButton.setVisible(false);
		typecheckerButton.setVisible(false);
		typeinferenceButton.setVisible(false);
		//There will be no Subtypingbutton because it has no sourcecode
		//finished setting the default states

                // hack to get consistent heights
                codeButton.setPreferredSize(new Dimension(codeButton.getPreferredSize().width, pongButton.getPreferredSize().height));
                smallstepButton.setPreferredSize(new Dimension(smallstepButton.getPreferredSize().width, pongButton.getPreferredSize().height));
                bigstepButton.setPreferredSize(new Dimension(bigstepButton.getPreferredSize().width, pongButton.getPreferredSize().height));
                typecheckerButton.setPreferredSize(new Dimension(typecheckerButton.getPreferredSize().width, pongButton.getPreferredSize().height));
                typeinferenceButton.setPreferredSize(new Dimension(typeinferenceButton.getPreferredSize().width, pongButton.getPreferredSize().height));
                //There will be no SubTypingButton
                //TODO vielleicht auch machen müssen
		
		this.language = language;
		
		//TODO PREFERENCES get this from the preferences
		setAdvanced(false);
		
		setFileName("newfile" + num + "." + language.getName());
		num++;
		editorComponentListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				componentStatusChanged(evt.getPropertyName(), evt.getNewValue());
			}
		};
		initEditor();


		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(java.awt.event.ComponentEvent evt) {
				code.getEditor().requestFocus();
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JToolBar actionToolBar;
        javax.swing.JToolBar editorToolBar;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JPanel toolBarPanel;

        jSeparator1 = new javax.swing.JSeparator();
        editorPanel = new javax.swing.JPanel();
        toolBarPanel = new javax.swing.JPanel();
        editorToolBar = new javax.swing.JToolBar();
        codeButton = new javax.swing.JToggleButton();
        smallstepButton = new javax.swing.JToggleButton();
        bigstepButton = new javax.swing.JToggleButton();
        typecheckerButton = new javax.swing.JToggleButton();
        typeinferenceButton = new javax.swing.JToggleButton();
        actionToolBar = new javax.swing.JToolBar();
        nextButton = new javax.swing.JButton();
        pongButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabChange(evt);
            }
        });

        editorPanel.setLayout(new java.awt.BorderLayout());

        add(editorPanel, java.awt.BorderLayout.CENTER);

        toolBarPanel.setLayout(new java.awt.GridBagLayout());

        editorToolBar.setFloatable(false);
        codeButton.setText("Source");
        codeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(codeButton);

        smallstepButton.setText("Small Step");
        smallstepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smallstepButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(smallstepButton);

        bigstepButton.setText("Big Step");
        bigstepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bigstepButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(bigstepButton);

        typecheckerButton.setText("Type Checker");
        typecheckerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecheckerButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(typecheckerButton);
        
        typeinferenceButton.setText("Type Inference");
        typeinferenceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	typinferenceButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(typeinferenceButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.weighty = 1.0;
        toolBarPanel.add(editorToolBar, gridBagConstraints);

        actionToolBar.setFloatable(false);
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/next24.png")));
        nextButton.setToolTipText("Guess");
        nextButton.setBorderPainted(false);
        nextButton.setOpaque(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        actionToolBar.add(nextButton);

        pongButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/pong16.gif")));
        pongButton.setToolTipText(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("PongTooltip"));
        pongButton.setBorderPainted(false);
        pongButton.setOpaque(false);
        pongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pongButtonActionPerformed(evt);
            }
        });

        actionToolBar.add(pongButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        toolBarPanel.add(actionToolBar, gridBagConstraints);

        add(toolBarPanel, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents

    private void pongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pongButtonActionPerformed
        PongView pongView = new PongView((Frame)getTopLevelAncestor());
        pongView.setVisible(true);
    }//GEN-LAST:event_pongButtonActionPerformed

        private void tabChange(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabChange
// TODO add your handling code here:
        }//GEN-LAST:event_tabChange

	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nextButtonActionPerformed
		// 
		activeEditorComponent.handleNext();
	}// GEN-LAST:event_nextButtonActionPerformed

	private void typecheckerButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_typecheckerButtonActionPerformed
		setTexteditor(false);
		setComponent(typechecker);
		deselectButtons();
		typecheckerButton.setSelected(true);
	}// GEN-LAST:event_typecheckerButtonActionPerformed
	
	private void typinferenceButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_typecheckerButtonActionPerformed
		setTexteditor(false);
		setComponent(typeinference);
		deselectButtons();
		typeinferenceButton.setSelected(true);
	}// GEN-LAST:event_typecheckerButtonActionPerformed


	private void bigstepButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bigstepButtonActionPerformed
		setTexteditor(false);
		setComponent(bigstep);
		deselectButtons();
		bigstepButton.setSelected(true);
	}// GEN-LAST:event_bigstepButtonActionPerformed
	
	//TODO Brauchen wir den auch für den Subtyping

	private void smallstepButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_smallstepButtonActionPerformed
		// 
		setTexteditor(false);
		setComponent(smallstep);
		deselectButtons();
		smallstepButton.setSelected(true);
	}// GEN-LAST:event_smallstepButtonActionPerformed

	private void codeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_codeButtonActionPerformed
		// 
		setTexteditor(true);
		setComponent(code);
		deselectButtons();
		codeButton.setSelected(true);
		code.getEditor().requestFocus();
	}// GEN-LAST:event_codeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bigstepButton;
    private javax.swing.JToggleButton codeButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton pongButton;
    private javax.swing.JToggleButton smallstepButton;
    private javax.swing.JToggleButton typecheckerButton;
    private javax.swing.JToggleButton typeinferenceButton;
    // End of variables declaration//GEN-END:variables

	private static final Logger logger = Logger.getLogger(EditorPanel.class);

	private MainWindow window;

	/**
	 * 
	 */
	private TextEditorPanel code;

	private EditorComponent smallstep;

	private EditorComponent bigstep;

	private EditorComponent typechecker;
	
	private EditorComponent typeinference;
	
	private EditorComponent subtyping;

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
	
	private boolean advanced;

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
	//private boolean changed;

	/**
	 * Indicates the status of the Undo function.
	 */
	private boolean undoStatus;

	/**
	 * Indicates the status of the Redo function.
	 */
	private boolean redoStatus;

	/**
	 * Indicated if the displayed component is a text editor.
	 * 
	 */
	private boolean texteditor;

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
			this.nextButton.setEnabled((Boolean) newValue);
		} else if (ident.equals("pongStatus")) {
                        this.pongButton.setVisible((Boolean) newValue);
		} else if (ident.equals("redoStatus")) {
			setRedoStatus((Boolean) newValue);
		} else if (ident.equals("title")) {
			setFileName((String) newValue);
		} else if (ident.equals("undoStatus")) {
			setUndoStatus((Boolean) newValue);
		} else if (ident.equals("changed")) {
			//setChanged((Boolean) newValue);
			setUndoStatus((Boolean) newValue);
		}

	}

	private void updateComponentStates(EditorComponent comp) {
		setRedoStatus(comp.isRedoStatus());
		setUndoStatus(comp.isUndoStatus());
		this.nextButton.setEnabled(comp.isNextStatus());
                this.nextButton.setVisible(comp != this.code);
                this.pongButton.setVisible(comp.isPongStatus());
	}

	/**
	 * Sets the Component shown in the Editor Panel.
	 * 
	 * @param comp
	 */
	private void setComponent(EditorComponent comp) {
		
		editorPanel.removeAll();
		editorPanel.add((JComponent) comp, BorderLayout.CENTER);
		activeEditorComponent = comp;
		updateComponentStates(comp);
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

		editorPanel.removeAll();
		editorPanel.add((JPanel) code, BorderLayout.CENTER);
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
		setTexteditor(false);
		try {
			SmallStepProofModel model = language.newSmallStepProofModel(code
					.getDocument().getExpression());
			smallstep = new ProofViewComponent(ProofViewFactory
					.newSmallStepView(model), model);
			editorPanel.removeAll();
			activateFunction(smallstepButton, smallstep);
			smallstep.setAdvanced(this.advanced);
			paintAll(getGraphics());
			

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotSmallStep")+
					"\n"+e.getMessage()+".",
					"Small Step", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Starts the Big Step Interpreter.
	 */
	public void handleBigStep() {
		setTexteditor(false);
		try {
			BigStepProofModel model = language.newBigStepProofModel(code
					.getDocument().getExpression());
			bigstep = new ProofViewComponent(ProofViewFactory
					.newBigStepView(model), model);
			editorPanel.removeAll();
			activateFunction(bigstepButton, bigstep);
			bigstep.setAdvanced(this.advanced);
			paintAll(getGraphics());
			

		} catch (Exception e) {
			logger.error("Could not create new BigStepView", e);
			JOptionPane.showMessageDialog(this,
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotBigStep")+
					"\n"+e.getMessage()+".", "Big Step",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Starts the Type Checker.
	 */
	public void handleTypeChecker() {
		setTexteditor(false);
		try {
			TypeCheckerProofModel model = language
					.newTypeCheckerProofModel(code.getDocument()
							.getExpression());
			typechecker = new ProofViewComponent(ProofViewFactory
					.newTypeCheckerView(model), model);
			editorPanel.removeAll();
			activateFunction(typecheckerButton, typechecker);
			typechecker.setAdvanced(this.advanced);
			paintAll(getGraphics());
			

		} catch (Exception e) {
			logger.error("Could not create new TypeCheckerView", e);
			JOptionPane.showMessageDialog(this,
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotTypeChecker")+
					"\n"+e.getMessage()+".",
					"TypeChecker", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Starts the TypeInference.
	 */
	public void handleTypInference() {
		setTexteditor(false);
		
		try {
			TypeInferenceProofModel model = language.newTypeInferenceProofModel(code.getDocument()
							.getExpression());
			//typechecker = new ProofViewComponent(ProofViewFactory
			//		.newTypeCheckerView(model), model);
			
			typeinference = new ProofViewComponent(ProofViewFactory.newTypeInferenceView(model), model);
			editorPanel.removeAll();
			//activateFunction(typecheckerButton, typechecker);
			activateFunction(typeinferenceButton, typeinference);
			typeinference.setAdvanced(this.advanced);
			paintAll(getGraphics());
			

		} catch (Exception e) {
			logger.error("Could not create new TypeCheckerView", e);
			JOptionPane.showMessageDialog(this,
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotTypeChecker")+
					"\n"+e.getMessage()+".",
					"TypeChecker", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Starts the Subtyping.
	 */
	public void handleSubtyping() {
		setTexteditor(false);
		
		try {
			SubTypingProofModel model = language.newSubTypingProofModel(null, null, this.advanced);
			//typechecker = new ProofViewComponent(ProofViewFactory
			//		.newTypeCheckerView(model), model);
			subtyping = new ProofViewComponent(ProofViewFactory.newSubtypingView(model), model);
			editorPanel.removeAll();
			//activateFunction(typecheckerButton, typechecker);
			//
			activateFunction(null, subtyping);
			subtyping.setAdvanced(this.advanced);
			paintAll(getGraphics());
			

		} catch (Exception e) {
			logger.error("Could not create new TypeCheckerView", e);
			JOptionPane.showMessageDialog(this,
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotTypeChecker")+
					"\n"+e.getMessage()+".",
					"TypeChecker", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void handleCut() {
		this.code.handleCut();
	}

	public void handleCopy() {
		this.code.handleCopy();
	}

	public void handlePaste() {
		this.code.handlePaste();
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
		if (button != null)
		{
			button.setSelected(true);
			button.setVisible(true);
	    		nextButton.setVisible(true);
			
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
		typeinferenceButton.setSelected(false);
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
		boolean oldRedoStatus = this.redoStatus;
		this.redoStatus = redoStatus;
		firePropertyChange("redoStatus", oldRedoStatus, redoStatus);
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
		String oldFilename = this.filename;
		this.filename = filename;
		firePropertyChange("filename", oldFilename, filename);
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
		//if (this.file != null) window.removeRecentlyUsed(this.file);
		this.file = file;
		window.addRecentlyUsed(new HistoryItem (this.file));
		setFileName(file.getName());
		
	}

	/**
	 * Returns the language used in this editor.
	 * 
	 * @return the language used.
	 */
	public Language getLanguage() {
		return language;
	}

//	/**
//	 *  add documentation here
//	 * 
//	 * @return <code>true</code> if the editor's document was changed.
//	 */
////	public boolean isChanged() {
////		return this.changed;
////	}

	/**
	 * Sets the change status of the editor
	 * 
	 * @param changed
	 *            true if the editor's document was changed.
	 */
//	public void setChanged(boolean changed) {
//		firePropertyChange("changed", this.changed, changed);
//		this.changed = changed;
//	}

	public boolean isTexteditor() {
		return this.texteditor;
	}

	public void setTexteditor(boolean texteditor) {
		firePropertyChange("texteditor", this.texteditor, texteditor);
		logger.debug("Texteditor is active");
		this.texteditor = texteditor;
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
		if (this.undoStatus != undoStatus) {
			logger.debug("UndoStatus of EditorPanel set to "+undoStatus);
			boolean oldUndoStatus = this.undoStatus;
			this.undoStatus = undoStatus;
			firePropertyChange("undoStatus", oldUndoStatus, undoStatus);
			if (this.isTexteditor()) firePropertyChange("changed", oldUndoStatus, undoStatus);
		}
	}
	
	public void setAdvanced(boolean state){
		if (bigstep != null) bigstep.setAdvanced(state);
		if (smallstep != null) smallstep.setAdvanced(state);
		if (typechecker != null) typechecker.setAdvanced(state);
		if (typeinference != null) typeinference.setAdvanced(state);
		if (subtyping != null) subtyping.setAdvanced(state);
		this.advanced = state;
	}
	
	public boolean isAdvaced (){
		return this.advanced;
	}
	
	public boolean shouldBeSaved(){
		return code.isUndoStatus();
	}

	public void handleUndo() {
		getComponent().handleUndo();
	};

	public void handleRedo() {
		getComponent().handleRedo();
	};

	public boolean handleSave() {
		if (file == null)
			return handleSaveAs();
		else
			return writeFile();
	};

	/**
	 * Saves the active editor component.
	 * 
	 * @return true if the file could be saved.
	 */
	public boolean handleSaveAs() {
		// setup the file chooser
		final LanguageFactory factory = LanguageFactory.newInstance();
		PreferenceManager prefmanager = PreferenceManager.get();
		JFileChooser chooser = new JFileChooser(prefmanager.getWorkingPath());
		chooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				try {
					factory.getLanguageByFile(f);
					return true;
				} catch (NoSuchLanguageException e) {
					return false;
				}
			}

			@Override
			public String getDescription() {
				Language[] languages = factory.getAvailableLanguages();
				StringBuilder builder = new StringBuilder(128);
				builder.append("Source Files (");
				for (int n = 0; n < languages.length; ++n) {
					if (n > 0) {
						builder.append("; ");
					}
					builder.append("*.");
					builder.append(languages[n].getName().toLowerCase());
				}
				builder.append(')');
				return builder.toString();
			}
		});
		chooser.setAcceptAllFileFilterUsed(false);
		prefmanager.setWorkingPath(chooser.getCurrentDirectory().getAbsolutePath());
		
		// determine the file name
		File outfile;
		for (;;) {
			// run the dialog
			int n = chooser.showSaveDialog(getParent());
			
			if (n != JFileChooser.APPROVE_OPTION) {
				logger.debug("Save as dialog cancelled");
				return false;
			}

			// check the extension
			File f = chooser.getSelectedFile();
			String name = f.getName();
			int i = name.lastIndexOf('.');
			if (i > 0 && i < name.length()) {
				if (!name.substring(i + 1).equalsIgnoreCase(
						this.language.getName())) {
					JOptionPane.showMessageDialog(this,
							java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("FileMustEndWith")+" \"."
									+ this.language.getName().toLowerCase()
									+ "\".", java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("Save"), JOptionPane.ERROR_MESSAGE);
					continue;
				}
			} else {
				name = name + "." + this.language.getName().toLowerCase();
			}

			// try to create the new file
			try {
				outfile = new File(f.getParent(), name);
				if (!outfile.createNewFile()) {
					// TODO: Christoph, this doesn't work propertly!
					int j = JOptionPane
							.showConfirmDialog(
									this,
									java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("The_File")+" \""
											+ outfile.getName()
											+ "\" " +java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("alreadyExists"),
									java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("Overwrite"),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
					if (j == JFileChooser.CANCEL_OPTION) {
						logger.debug("Cancelled overwrite of \""
								+ outfile.getName() + "\"");
						return false;
					} else if (j == JOptionPane.NO_OPTION) {
						// next try
						continue;
					}
				}

				// save to the new file
				setFile(outfile);
				setFileName(outfile.getName());
				return writeFile();
			} catch (IOException e) {
				logger.error("Selected file could not be created.", e);
				JOptionPane.showMessageDialog(this,
						java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("FileCantBeCreated"), java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("Save"),
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	}

	/**
	 * Writes content of the source panel to a specified file.
	 * 
	 * @return true if the file could be written
	 */
	private boolean writeFile() {
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(code.getText().getBytes());
			out.flush();
			out.close();

			// TODO: Christoph, what about this one?
      code.clearHistory();
                        
			return true;
		} catch (IOException e) {
			logger.error("Could not write to file", e);
			JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CouldNotWriteToFile"),
					java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("WriteFile"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
