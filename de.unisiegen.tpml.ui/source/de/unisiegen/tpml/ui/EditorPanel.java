/*
 * EditorPanel.java
 *
 * Created on 17. September 2006, 14:59
 */

package de.unisiegen.tpml.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author  TPPool15
 */
public class EditorPanel extends javax.swing.JPanel {
    
    /** Creates new form EditorPanel */
    public EditorPanel() {
        initComponents();
        // setting the default button states
        nextButton.setEnabled(false);
        smallstepButton.setEnabled(false);
        bigstepButton.setEnabled(false);
        typecheckerButton.setEnabled(false);
        editorComponentListener = new PropertyChangeListener (){
            public void propertyChange(PropertyChangeEvent evt) {
                componentStatusChanged(evt.getPropertyName(), evt.getNewValue());
            }
            };
        initEditor();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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
        actionToolBar.add(nextButton);

        editorToolBar.add(actionToolBar);

        add(editorToolBar, java.awt.BorderLayout.NORTH);

        add(editorPanel, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void typecheckerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typecheckerButtonActionPerformed
// TODO add your handling code here:
        updateComponentStates(typechecker);
        deselectButtons();
        typecheckerButton.setSelected(true);
        editorPanel.removeAll();
        editorPanel.add(typechecker, BorderLayout.CENTER);
        paintAll(getGraphics());
    }//GEN-LAST:event_typecheckerButtonActionPerformed

    private void bigstepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bigstepButtonActionPerformed
// TODO add your handling code here:
        updateComponentStates(bigstep);
        deselectButtons();
        bigstepButton.setSelected(true);
        editorPanel.removeAll();
        editorPanel.add(bigstep, BorderLayout.CENTER);
        paintAll(getGraphics());
    }//GEN-LAST:event_bigstepButtonActionPerformed

    private void smallstepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smallstepButtonActionPerformed
// TODO add your handling code here:
        updateComponentStates(smallstep);
        deselectButtons();
        smallstepButton.setSelected(true);
        editorPanel.removeAll();
        editorPanel.add(smallstep, BorderLayout.CENTER);
        paintAll(getGraphics());
    }//GEN-LAST:event_smallstepButtonActionPerformed

    private void codeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeButtonActionPerformed
// TODO add your handling code here:
        updateComponentStates(code);
        deselectButtons();
        codeButton.setSelected(true);
        editorPanel.removeAll();
        editorPanel.add(code, BorderLayout.CENTER);   
        paintAll(getGraphics());
    }//GEN-LAST:event_codeButtonActionPerformed
    
    
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
    
    private TestEditorComponent code;
    
    private TestEditorComponent smallstep;
    
    private TestEditorComponent bigstep;
    
    private TestEditorComponent typechecker;
    
    private PropertyChangeListener editorComponentListener;
    
        /**
	 * Title displayed in the tab.
	 */
	private String title;
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
        
        //self defined methods
        
        private void componentStatusChanged(String ident, Object newValue){
            try{
                if (ident.equals("nextStatus")){
                    nextButton.setEnabled((Boolean)newValue);
                }else
                if (ident.equals("redoStatus")){
                setRedoStatus((Boolean)newValue);
                }
                else
                    if (ident.equals("saveStatus")){
                    setSaveStatus((Boolean)newValue);
                    }
                    else
                        if (ident.equals("title")){
                        setTitle((String)newValue);
                        }
                        else
                            if (ident.equals("undoStatus")){
                            setUndoStatus((Boolean)newValue);
                            }
            } catch (Exception e){} //No Change we looked for therefor we do nothing.
        }
        
        private void updateComponentStates (AbstractEditorComponent comp){
        setRedoStatus(comp.isRedoStatus());
        setUndoStatus(comp.isUndoStatus());
        setSaveStatus(comp.isSaveStatus());
        nextButton.setEnabled(comp.isNextStatus());
        }
    /** This method is called from within the constructor to
     * initialize the source editor.
     */
    private void initEditor(){
        code = new TestEditorComponent("Code");
        editorPanel.removeAll();
        editorPanel.add(code);
        code.addPropertyChangeListener(editorComponentListener);
        code.setDefaultStates();
        updateComponentStates(code);
        deselectButtons();
        codeButton.setSelected(true);  
        codeButton.setEnabled(true);
        this.repaint();
    }
    
    public void handleSmallStep(){
        smallstep = new TestEditorComponent("SmallStep");
        editorPanel.removeAll();
        editorPanel.add(smallstep);
        smallstep.addPropertyChangeListener(editorComponentListener);
        smallstep.setDefaultStates();
        updateComponentStates(smallstep);
        deselectButtons();
        smallstepButton.setSelected(true);
        smallstepButton.setEnabled(true);
        this.repaint();
    }
    
    public void handleBigStep(){
        bigstep = new TestEditorComponent("BigStep");
        editorPanel.removeAll();
        editorPanel.add(bigstep);
        bigstep.addPropertyChangeListener(editorComponentListener);
        bigstep.setDefaultStates();
        updateComponentStates(bigstep);
        deselectButtons();
        bigstepButton.setSelected(true);
        bigstepButton.setEnabled(true);
        this.repaint();
    }
    
    public void handleTypeChecker(){
        typechecker = new TestEditorComponent("TypeChecker");
        editorPanel.removeAll();
        editorPanel.add(typechecker);
        typechecker.addPropertyChangeListener(editorComponentListener);
        typechecker.setDefaultStates();
        updateComponentStates(typechecker);
        deselectButtons();
        typecheckerButton.setSelected(true);
        typecheckerButton.setEnabled(true);
        this.repaint();
    }
        
    private void deselectButtons (){
    codeButton.setSelected(false);
    smallstepButton.setSelected(false);
    bigstepButton.setSelected(false);
    typecheckerButton.setSelected(false);
    }
        
    	public boolean isRedoStatus() {
		return redoStatus;
	}
	public void setRedoStatus(boolean redoStatus) {
		firePropertyChange("redoStatus", this.redoStatus, redoStatus);
		this.redoStatus = redoStatus;
	}
	public boolean isSaveStatus() {
		return saveStatus;
	}
	public void setSaveStatus(boolean saveStatus) {
		firePropertyChange("saveStatus", this.saveStatus, saveStatus);
		this.saveStatus = saveStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		firePropertyChange("titel", this.title, title);
		this.title = title;
	}
	public boolean isUndoStatus() {
		return undoStatus;
	}
	public void setUndoStatus(boolean undoStatus) {
		firePropertyChange("undoStatus", this.undoStatus, undoStatus);
		this.undoStatus = undoStatus;
	}
}
