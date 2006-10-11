/*
 * TestEditorComponent.java
 *
 * Created on 17. September 2006, 16:50
 */

package de.unisiegen.tpml.ui;


/**
 * For TESTING only remove this from final version
 *
 * @author Christoph Fehling
 * @version $Rev$ 
 *
 */
public class TestEditorComponent extends javax.swing.JPanel implements EditorComponent {
    
    /** Creates new form TestEditorComponent */
    public TestEditorComponent(String name) {
        initComponents();
        infoText.setText(name);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        undotrue = new javax.swing.JButton();
        redotrue = new javax.swing.JButton();
        savetrue = new javax.swing.JButton();
        nexttrue = new javax.swing.JButton();
        changed_true = new javax.swing.JButton();
        infoText = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        undofalse = new javax.swing.JButton();
        redofalse = new javax.swing.JButton();
        savefalse = new javax.swing.JButton();
        nextfalse = new javax.swing.JButton();
        changed_false = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        undotrue.setText("undo_true");
        undotrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undotrueActionPerformed(evt);
            }
        });

        jPanel2.add(undotrue);

        redotrue.setText("redo_true");
        redotrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redotrueActionPerformed(evt);
            }
        });

        jPanel2.add(redotrue);

        savetrue.setText("save_true");
        savetrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savetrueActionPerformed(evt);
            }
        });

        jPanel2.add(savetrue);

        nexttrue.setText("next_true");
        nexttrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nexttrueActionPerformed(evt);
            }
        });

        jPanel2.add(nexttrue);

        changed_true.setText("changed_true");
        changed_true.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changed_trueActionPerformed(evt);
            }
        });

        jPanel2.add(changed_true);

        infoText.setText("jTextField1");
        jPanel2.add(infoText);

        add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        undofalse.setText("undo_false");
        undofalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undofalseActionPerformed(evt);
            }
        });

        jPanel1.add(undofalse);

        redofalse.setText("redo_false");
        redofalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redofalseActionPerformed(evt);
            }
        });

        jPanel1.add(redofalse);

        savefalse.setText("save_false");
        savefalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savefalseActionPerformed(evt);
            }
        });

        jPanel1.add(savefalse);

        nextfalse.setText("next_false");
        nextfalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextfalseActionPerformed(evt);
            }
        });

        jPanel1.add(nextfalse);

        changed_false.setText("changed_false");
        changed_false.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changed_falseActionPerformed(evt);
            }
        });

        jPanel1.add(changed_false);

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void changed_falseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changed_falseActionPerformed
// 
    //    ((EditorPanel)(getParent()).getParent()).setChanged(false);
    }//GEN-LAST:event_changed_falseActionPerformed

    private void changed_trueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changed_trueActionPerformed
// 
    //    ((EditorPanel)(getParent()).getParent()).setChanged(true);
    }//GEN-LAST:event_changed_trueActionPerformed

    private void nextfalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextfalseActionPerformed
// 
        setNextStatus(false);
    }//GEN-LAST:event_nextfalseActionPerformed

    private void savefalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savefalseActionPerformed
// 
        setSaveStatus(false);
    }//GEN-LAST:event_savefalseActionPerformed

    private void savetrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savetrueActionPerformed
// 
        setSaveStatus(true);
    }//GEN-LAST:event_savetrueActionPerformed

    private void redofalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redofalseActionPerformed
// 
        setRedoStatus(false);
    }//GEN-LAST:event_redofalseActionPerformed

    private void redotrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redotrueActionPerformed
// 
        setRedoStatus(true);
    }//GEN-LAST:event_redotrueActionPerformed

    private void undofalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undofalseActionPerformed
// 
        setUndoStatus(false);
    }//GEN-LAST:event_undofalseActionPerformed

    private void nexttrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nexttrueActionPerformed
// 
        setNextStatus(true);

    }//GEN-LAST:event_nexttrueActionPerformed

    private void undotrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undotrueActionPerformed
// 
        setUndoStatus(true);
    }//GEN-LAST:event_undotrueActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changed_false;
    private javax.swing.JButton changed_true;
    private javax.swing.JTextField infoText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton nextfalse;
    private javax.swing.JButton nexttrue;
    private javax.swing.JButton redofalse;
    private javax.swing.JButton redotrue;
    private javax.swing.JButton savefalse;
    private javax.swing.JButton savetrue;
    private javax.swing.JButton undofalse;
    private javax.swing.JButton undotrue;
    // End of variables declaration//GEN-END:variables
    
    private boolean nextStatus;
    private boolean redoStatus;
    private boolean undoStatus;
    private boolean saveStatus;
    private String title;
    
    public boolean isNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(boolean nextStatus) {
        firePropertyChange ("nextStatus", this.nextStatus, nextStatus);
        this.nextStatus = nextStatus;
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
        firePropertyChange("title", this.title, title);
        this.title = title;
    }

    public boolean isUndoStatus() {
        return undoStatus;
    }

    public void setUndoStatus(boolean undoStatus) {
        firePropertyChange("undoStatus", this.undoStatus, undoStatus);
        this.undoStatus = undoStatus;
    }

    public void setDefaultStates() {
        setSaveStatus(false);
        setUndoStatus(false);
        setRedoStatus(false);
        setNextStatus(true);
    }

	public void setChangeStatus(boolean changeStatus) {
		// 
		
	}

	public boolean isChangeStatus() {
		// 
		return false;
	}

    public void handleNext() {
    }

    public void handleRedo() {
    }

    public void handleUndo() {
    }

	public void setAdvanced(boolean status) {
		// 
		
	}
    
}
