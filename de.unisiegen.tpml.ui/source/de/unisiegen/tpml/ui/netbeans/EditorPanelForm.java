/*
 * EditorPanelForm.java
 *
 * Created on 8. August 2007, 09:03
 */

package de.unisiegen.tpml.ui.netbeans;

import de.unisiegen.tpml.graphics.pong.PongView;
import de.unisiegen.tpml.ui.EditorPanel;
import de.unisiegen.tpml.ui.EditorPanelTypes;
import de.unisiegen.tpml.ui.EditorPanelExpression;
import java.awt.Frame;

/**
 *
 * @author  cfehling
 */
public class EditorPanelForm extends javax.swing.JPanel {
    
    /** Creates new form EditorPanelForm */
    public EditorPanelForm(EditorPanel caller) {
        this.caller = caller;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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
        minimalTypingButton = new javax.swing.JToggleButton();
        subTypingButton = new javax.swing.JToggleButton();
        subTypingRecButton = new javax.swing.JToggleButton();
        actionToolBar = new javax.swing.JToolBar();
        nextButton = new javax.swing.JButton();
        pongButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

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
                typeinferenceButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(typeinferenceButton);

        minimalTypingButton.setText("Minimal Typing");
        minimalTypingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimalTypingButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(minimalTypingButton);

        subTypingButton.setText("Sub Typing");
        subTypingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subTypingButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(subTypingButton);

        subTypingRecButton.setText("Sub Typing Rec");
        subTypingRecButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subTypingRecButtonActionPerformed(evt);
            }
        });

        editorToolBar.add(subTypingRecButton);

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
        nextButton.setFocusPainted(false);
        nextButton.setFocusable(false);
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
        pongButton.setFocusPainted(false);
        pongButton.setFocusable(false);
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

    private void subTypingRecButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTypingRecButtonActionPerformed
        ((EditorPanelTypes)caller).selectSubTypingRec();
    }//GEN-LAST:event_subTypingRecButtonActionPerformed

    private void minimalTypingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimalTypingButtonActionPerformed
        ((EditorPanelExpression)caller).selectMinimalTyping();
    }//GEN-LAST:event_minimalTypingButtonActionPerformed

    private void typeinferenceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeinferenceButtonActionPerformed
        ((EditorPanelExpression)caller).selectTypeInference();
    }//GEN-LAST:event_typeinferenceButtonActionPerformed

    private void subTypingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTypingButtonActionPerformed
        ((EditorPanelTypes)caller).selectSubTyping();
    }//GEN-LAST:event_subTypingButtonActionPerformed

    private void pongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pongButtonActionPerformed
        PongView pongView = new PongView((Frame)getTopLevelAncestor());
        pongView.setVisible(true);
    }//GEN-LAST:event_pongButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        caller.getActiveEditorComponent().handleNext();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void typecheckerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typecheckerButtonActionPerformed
	((EditorPanelExpression)caller).selectTypeChecker();
    }//GEN-LAST:event_typecheckerButtonActionPerformed

    private void bigstepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bigstepButtonActionPerformed
        ((EditorPanelExpression)caller).selectBigStep();
    }//GEN-LAST:event_bigstepButtonActionPerformed

    private void smallstepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smallstepButtonActionPerformed
        ((EditorPanelExpression)caller).selectSmallStep();
    }//GEN-LAST:event_smallstepButtonActionPerformed

    private void codeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeButtonActionPerformed
        caller.selectCode();
    }//GEN-LAST:event_codeButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JToggleButton bigstepButton;
    public javax.swing.JToggleButton codeButton;
    public javax.swing.JPanel editorPanel;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JToggleButton minimalTypingButton;
    public javax.swing.JButton nextButton;
    public javax.swing.JButton pongButton;
    public javax.swing.JToggleButton smallstepButton;
    public javax.swing.JToggleButton subTypingButton;
    public javax.swing.JToggleButton subTypingRecButton;
    public javax.swing.JToggleButton typecheckerButton;
    public javax.swing.JToggleButton typeinferenceButton;
    // End of variables declaration//GEN-END:variables
    private EditorPanel caller;
    
    public EditorPanel getCaller(){
        return this.caller;
    }
}
