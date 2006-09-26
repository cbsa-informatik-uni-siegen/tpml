/*
 * MainWindow.java
 *
 * Created on 15. September 2006, 14:56
 */

package de.unisiegen.tpml.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;
import de.unisiegen.tpml.core.languages.NoSuchLanguageException;

/**
 * //TODO add documentation here.
 * 
 * @author Christoph Fehling
 * @version $Rev$
 * 
 */
public class MainWindow extends javax.swing.JFrame {

	/** Creates new form MainWindow */
	public MainWindow() {
		initComponents();
		
		setTitle("TPML " +Versions.UI);
		// TODO clean up the setting of states
		setGeneralStates(false);
		saveItem.setEnabled(false);
		saveButton.setEnabled(false);
		preferencesItem.setEnabled(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MainWindow.this.handleQuit();
			};
		});
		this.editorPanelListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				editorStatusChange(evt.getPropertyName(), evt.getNewValue());
			}
		};
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JMenuBar MainMenuBar;
        javax.swing.JMenu editMenu;
        javax.swing.JSeparator editMenuSeperator;
        javax.swing.JMenu fileMenu;
        javax.swing.JSeparator fileMenuSeperator1;
        javax.swing.JSeparator fileMenuSerpator2;
        javax.swing.JToolBar mainToolbar;
        javax.swing.JButton newButton;
        javax.swing.JMenuItem newItem;
        javax.swing.JButton openButton;
        javax.swing.JMenuItem openItem;
        javax.swing.JMenuItem quitItem;
        javax.swing.JMenu runMenu;

        mainToolbar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        MainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newItem = new javax.swing.JMenuItem();
        openItem = new javax.swing.JMenuItem();
        closeItem = new javax.swing.JMenuItem();
        fileMenuSeperator1 = new javax.swing.JSeparator();
        saveItem = new javax.swing.JMenuItem();
        saveAsItem = new javax.swing.JMenuItem();
        fileMenuSerpator2 = new javax.swing.JSeparator();
        quitItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoItem = new javax.swing.JMenuItem();
        redoItem = new javax.swing.JMenuItem();
        editMenuSeperator = new javax.swing.JSeparator();
        preferencesItem = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        smallstepItem = new javax.swing.JMenuItem();
        bigstepItem = new javax.swing.JMenuItem();
        typecheckerItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("mainframe");
        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/new.png")));
        newButton.setToolTipText("New File");
        newButton.setBorderPainted(false);
        newButton.setOpaque(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(newButton);

        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/open.png")));
        openButton.setToolTipText("Open File");
        openButton.setBorderPainted(false);
        openButton.setOpaque(false);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(openButton);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/save.png")));
        saveButton.setToolTipText("Save File");
        saveButton.setBorderPainted(false);
        saveButton.setOpaque(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(saveButton);

        saveAsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/saveas.png")));
        saveAsButton.setToolTipText("Save File As...");
        saveAsButton.setBorderPainted(false);
        saveAsButton.setOpaque(false);
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(saveAsButton);

        undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/undo.gif")));
        undoButton.setBorderPainted(false);
        undoButton.setOpaque(false);
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(undoButton);

        redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/redo.gif")));
        redoButton.setToolTipText("Redo the last step.");
        redoButton.setBorderPainted(false);
        redoButton.setOpaque(false);
        redoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoButtonActionPerformed(evt);
            }
        });

        mainToolbar.add(redoButton);

        getContentPane().add(mainToolbar, java.awt.BorderLayout.NORTH);

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("FileMnemonic").charAt(0));
        fileMenu.setText("File");
        newItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("NewMnemonic").charAt(0));
        newItem.setText("New");
        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemActionPerformed(evt);
            }
        });

        fileMenu.add(newItem);

        openItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("OpenMnemonic").charAt(0));
        openItem.setText("Open");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openItemActionPerformed(evt);
            }
        });

        fileMenu.add(openItem);

        closeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        closeItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("CloseMnemonic").charAt(0));
        closeItem.setText("Close");
        closeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeItemActionPerformed(evt);
            }
        });

        fileMenu.add(closeItem);

        fileMenu.add(fileMenuSeperator1);

        saveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("SaveMnemonic").charAt(0));
        saveItem.setText("Save");
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveItem);

        saveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("SaveAsMnemonic").charAt(0));
        saveAsItem.setText("Save As...");
        saveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveAsItem);

        fileMenu.add(fileMenuSerpator2);

        quitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("QuitMnemonic").charAt(0));
        quitItem.setText("Quit");
        quitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitItemActionPerformed(evt);
            }
        });

        fileMenu.add(quitItem);

        MainMenuBar.add(fileMenu);

        editMenu.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("EditMnemonic").charAt(0));
        editMenu.setText("Edit");
        undoItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("UndoMnemonic").charAt(0));
        undoItem.setText("Undo");
        undoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoItemActionPerformed(evt);
            }
        });

        editMenu.add(undoItem);

        redoItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("RedoMnemonic").charAt(0));
        redoItem.setText("Redo");
        redoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoItemActionPerformed(evt);
            }
        });

        editMenu.add(redoItem);

        editMenu.add(editMenuSeperator);

        preferencesItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("PreferencesMnemonic").charAt(0));
        preferencesItem.setText("Preferences");
        preferencesItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesItemActionPerformed(evt);
            }
        });

        editMenu.add(preferencesItem);

        MainMenuBar.add(editMenu);

        runMenu.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("RunMnemonic").charAt(0));
        runMenu.setText("Run");
        smallstepItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("SmallStepMnemonic").charAt(0));
        smallstepItem.setText("SmallStep");
        smallstepItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smallstepItemActionPerformed(evt);
            }
        });

        runMenu.add(smallstepItem);

        bigstepItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("BigStepMnemonic").charAt(0));
        bigstepItem.setText("BigStep");
        bigstepItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bigstepItemActionPerformed(evt);
            }
        });

        runMenu.add(bigstepItem);

        typecheckerItem.setMnemonic(java.util.ResourceBundle.getBundle("de/unisiegen/tpml/ui/ui").getString("TypeCheckerMnemonic").charAt(0));
        typecheckerItem.setText("TypeChecker");
        typecheckerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecheckerItemActionPerformed(evt);
            }
        });

        runMenu.add(typecheckerItem);

        MainMenuBar.add(runMenu);

        setJMenuBar(MainMenuBar);

        setBounds(0, 0, 800, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void redoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoItemActionPerformed
// TODO add your handling code here:
        (getActiveEditor()).handleRedo();
    }//GEN-LAST:event_redoItemActionPerformed

        private void undoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoItemActionPerformed
// TODO add your handling code here:
        (getActiveEditor()).handleUndo();
        }//GEN-LAST:event_undoItemActionPerformed

	private void openItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openItemActionPerformed
		// 
		handleOpen();
	}// GEN-LAST:event_openItemActionPerformed

	private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openButtonActionPerformed
		handleOpen();
	}// GEN-LAST:event_openButtonActionPerformed

	private void preferencesItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_preferencesItemActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_preferencesItemActionPerformed

	private void newItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newItemActionPerformed
		// 
		handleNew();
	}// GEN-LAST:event_newItemActionPerformed

	private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_tabbedPaneStateChanged
		// 
		updateEditorStates((EditorPanel) tabbedPane.getSelectedComponent());
	}// GEN-LAST:event_tabbedPaneStateChanged

	private void typecheckerItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_typecheckerItemActionPerformed
		// 
		(getActiveEditor()).handleTypeChecker();
	}// GEN-LAST:event_typecheckerItemActionPerformed

	private void bigstepItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bigstepItemActionPerformed
		// 
		(getActiveEditor()).handleBigStep();
	}// GEN-LAST:event_bigstepItemActionPerformed

	private void smallstepItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_smallstepItemActionPerformed
		// 
		(getActiveEditor()).handleSmallStep();
	}// GEN-LAST:event_smallstepItemActionPerformed

	private void quitItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_quitItemActionPerformed
		// 
		handleQuit();
	}// GEN-LAST:event_quitItemActionPerformed

	private void saveAsItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAsItemActionPerformed
		// 
		getActiveEditor().handleSaveAs();
	}// GEN-LAST:event_saveAsItemActionPerformed

	private void saveItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveItemActionPerformed
		// 
		getActiveEditor().handleSave();
	}// GEN-LAST:event_saveItemActionPerformed

	private void closeItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_closeItemActionPerformed
		// 
		handleClose();
	}// GEN-LAST:event_closeItemActionPerformed

	private void redoButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_redoButtonActionPerformed
		// 
		(getActiveEditor()).handleRedo();
	}// GEN-LAST:event_redoButtonActionPerformed

	private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_undoButtonActionPerformed
		// 
		(getActiveEditor()).handleUndo();
	}// GEN-LAST:event_undoButtonActionPerformed

	private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAsButtonActionPerformed
		// 
		getActiveEditor().handleSaveAs();
	}// GEN-LAST:event_saveAsButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		// 
		getActiveEditor().handleSave();
	}// GEN-LAST:event_saveButtonActionPerformed

	private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newButtonActionPerformed
		// 
		handleNew();
	}// GEN-LAST:event_newButtonActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem bigstepItem;
    private javax.swing.JMenuItem closeItem;
    private javax.swing.JMenuItem preferencesItem;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoItem;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JMenuItem saveAsItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JMenuItem smallstepItem;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JMenuItem typecheckerItem;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoItem;
    // End of variables declaration//GEN-END:variables
	private PropertyChangeListener editorPanelListener;

	private static final Logger logger = Logger.getLogger(MainWindow.class);

	// Self-defined methods:

	private void setGeneralStates(boolean state) {
		smallstepItem.setEnabled(state);
		bigstepItem.setEnabled(state);
		typecheckerItem.setEnabled(state);
		saveAsItem.setEnabled(state);
		saveAsButton.setEnabled(state);
		closeItem.setEnabled(state);

		setUndoState(state);
		setRedoState(state);
	}

	private void editorStatusChange(String ident, Object newValue) {
		try {
			if (ident.equals("redoStatus")) {
				setRedoState((Boolean) newValue);
			} else if (ident.equals("saveStatus")) {
				setSaveState((Boolean) newValue);
			} else if (ident.equals("filename")) {
				tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),
						(String) newValue);
			} else if (ident.equals("undoStatus")) {
				setUndoState((Boolean) newValue);
			} else if (ident.equals("changed")) {
				setChangeState((Boolean) newValue);
				setSaveState((Boolean) newValue);
			}
		} catch (Exception e) {
		} // This was no Change we look for therefore we do nothing.
		// TODO add correct handling

	}

	private void updateEditorStates(EditorPanel editor) {
		if (getActiveEditor() == null)
			setGeneralStates(false);
		else {
			setRedoState(editor.isRedoStatus());
			setUndoState(editor.isUndoStatus());
			setSaveState(editor.isSaveStatus());
			setChangeState(editor.isChanged());
		}
	}

	private void setRedoState(Boolean state) {
		redoButton.setEnabled(state);
		redoItem.setEnabled(state);
	}

	private void setUndoState(Boolean state) {
		undoButton.setEnabled(state);
		undoItem.setEnabled(state);

	}

	private void setSaveState(Boolean state) {
		saveButton.setEnabled(state);
		saveItem.setEnabled(state);
	}

	private void setChangeState(Boolean state) {
		if (state) {
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), "*"
					+ ((EditorPanel) tabbedPane.getSelectedComponent())
							.getFileName());

		} else {
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),
					((EditorPanel) tabbedPane.getSelectedComponent())
							.getFileName());
		}
	}

	private EditorPanel getActiveEditor() {
		return (EditorPanel) tabbedPane.getSelectedComponent();
	}

	private void handleNew() {
		FileWizard wizard = new FileWizard(this, true);
		wizard.setLocationRelativeTo(this);
		wizard.setVisible(true);
		Language language = wizard.getLanguage();

		if (language == null)
			return;

		EditorPanel newEditorPanel = new EditorPanel(language);
		tabbedPane.add(newEditorPanel);
		tabbedPane.setSelectedComponent(newEditorPanel);
		newEditorPanel.addPropertyChangeListener(editorPanelListener);
		setGeneralStates(true);
		updateEditorStates(newEditorPanel);
	}

	private void handleOpen() {
		// TODO clean this up a little bit
		try {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == chooser.APPROVE_OPTION) {
				File infile = chooser.getSelectedFile();
				LanguageFactory langfactory = LanguageFactory.newInstance();
				Language language = langfactory.getLanguageByFile(infile);
				if (infile == null)
					return;

				StringBuffer buffer = new StringBuffer();
				FileInputStream in = new FileInputStream(infile);
				int onechar;

				while ((onechar = in.read()) != -1)
					buffer.append((char) onechar);

				if (infile == null)
					return;

				EditorPanel newEditorPanel = new EditorPanel(language);
				tabbedPane.add(newEditorPanel);
				tabbedPane.setSelectedComponent(newEditorPanel);

				newEditorPanel.setFileName(infile.getName());
				newEditorPanel.setEditorText(buffer.toString());
				newEditorPanel.setFile(infile);
				newEditorPanel.addPropertyChangeListener(editorPanelListener);
				setGeneralStates(true);
				updateEditorStates(newEditorPanel);
			}

		} catch (NoSuchLanguageException e) {
			logger.error("Language does not exist.", e);
			JOptionPane.showMessageDialog(this, "File is not supported.",
					"Open File", JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			logger.error("File specified could not be found", e);
			JOptionPane.showMessageDialog(this, "File could not be found.",
					"Open File", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			logger.error("Could not read from the file specified", e);
			JOptionPane.showMessageDialog(this,
					"Could not read from the file.", "Open File",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void handleQuit() {
		while (getActiveEditor() != null) {
			if (!handleClose())
				return;
		}
		System.exit(0);
	}

	/**
	 * TODO add documentation here
	 * 
	 * @return true if the active editor was closed
	 */
	/**
	 * TODO add documentation here
	 * 
	 * @return true if the active editor could be closed.
	 */
	private boolean handleClose() {
		// TODO clean this up a little bit
		EditorPanel selectedEditor = getActiveEditor();
		if (selectedEditor.isChanged()) {
			// Custom button text
			Object[] options = { "Yes", "No", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, selectedEditor
					.getFileName()
					+ " contains unsaved changes. Do you want to save?",
					"Save File", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
			switch (n) {
			case 0: // Save Changes
				logger.debug("Close dialog: YES");
				boolean success = selectedEditor.handleSave();
				if (success) {
					tabbedPane.remove(tabbedPane.getSelectedIndex());
					this.repaint();
				}
				return success;

			case 1: // Do not save changes
				logger.debug("Close dialog: NO");
				tabbedPane.remove(tabbedPane.getSelectedIndex());
				this.repaint();
				return true;

			case 2: // Cancelled.
				logger.debug("Close dialog: CANCEL");
				return false;

			default:
				return false;
			}
		} else {
			tabbedPane.remove(tabbedPane.getSelectedIndex());
			this.repaint();
			return true;
		}
	}
}
