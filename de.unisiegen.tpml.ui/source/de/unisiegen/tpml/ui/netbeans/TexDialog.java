/*
 * PdfDialog.java
 *
 * Created on 8. August 2007, 16:34
 */

package de.unisiegen.tpml.ui.netbeans;

import javax.swing.JOptionPane;

/**
 *
 * @author  cfehling
 */
public class TexDialog extends javax.swing.JDialog {
    
    /** Creates new form PdfDialog */
    public TexDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        filechooser = new javax.swing.JFileChooser();
        overlappingTextField = new javax.swing.JTextField();
        allCheckBox = new javax.swing.JCheckBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PDF Printing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jSeparator1, gridBagConstraints);

        jLabel1.setText("Overlapping:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        filechooser.setDialogTitle("PDF Printing");
        filechooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        filechooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filechooserActionPerformed(evt);
            }
        });
        filechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                filechooserPropertyChange(evt);
            }
        });
        filechooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filechooserMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(filechooser, gridBagConstraints);

        overlappingTextField.setText("0");
        overlappingTextField.setPreferredSize(new java.awt.Dimension(30, 19));
        overlappingTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overlappingTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(overlappingTextField, gridBagConstraints);

        allCheckBox.setSelected(true);
        allCheckBox.setText("Export all");
        allCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 10;
        getContentPane().add(allCheckBox, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void overlappingTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overlappingTextFieldActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_overlappingTextFieldActionPerformed

    private void filechooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filechooserMouseClicked
// TODO add your handling code here:
    }//GEN-LAST:event_filechooserMouseClicked

    private void filechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_filechooserPropertyChange
// TODO add your handling code here:
    }//GEN-LAST:event_filechooserPropertyChange

    private void filechooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filechooserActionPerformed
// TODO add your handling code here:
        if (evt.getActionCommand().equals("CancelSelection")){
            this.cancelled = true;
            this.dispose();
        } else
            if (evt.getActionCommand().equals("ApproveSelection")){
                this.all = allCheckBox.isSelected();
                this.overlapping = overlappingTextField.getText();
                try{
                    int test = Integer.parseInt(overlappingTextField.getText());
                    overlappingInt = test;
                }
                catch (NumberFormatException e)
                {
                  //TODO Multilanguage
                    JOptionPane.showMessageDialog(this,"The Overlapping-Value must be an integer!");
                    return;
                }
                this.dispose();
            }
    }//GEN-LAST:event_filechooserActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allCheckBox;
    public javax.swing.JFileChooser filechooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField overlappingTextField;
    // End of variables declaration//GEN-END:variables
    
    public boolean all;
    public boolean cancelled;
    public String overlapping;
    public int overlappingInt;
 
}