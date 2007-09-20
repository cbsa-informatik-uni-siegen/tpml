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
        pageCOuntLabel = new javax.swing.JLabel();
        pageCountTextField = new javax.swing.JTextField();

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
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
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(filechooser, gridBagConstraints);

        overlappingTextField.setText("0");
        overlappingTextField.setToolTipText("please enter the overlapping in mm");
        overlappingTextField.setPreferredSize(new java.awt.Dimension(30, 19));
        overlappingTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overlappingTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        getContentPane().add(overlappingTextField, gridBagConstraints);

        allCheckBox.setSelected(true);
        allCheckBox.setText("Export all");
        allCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(allCheckBox, gridBagConstraints);

        pageCOuntLabel.setText("Pagecount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        getContentPane().add(pageCOuntLabel, gridBagConstraints);

        pageCountTextField.setText("5");
        pageCountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageCountTextFieldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        getContentPane().add(pageCountTextField, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pageCountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageCountTextFieldActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_pageCountTextFieldActionPerformed

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
                    if (test <0 || test > 50)
                    {
                        throw new NumberFormatException();
                    }
                    overlappingInt = test;
                }
                catch (NumberFormatException e)
                {
                  //TODO Multilanguage
                    JOptionPane.showMessageDialog(this,"The Overlapping-Value must be an integer between 0 and 50!");
                    return;
                }
                
                try {
                    int test = Integer.parseInt(pageCountTextField.getText());
                    if (test <0 || test > 15)
                    {
                        throw new NumberFormatException();
                    }
                    pagecount = test;
                    
                }
                catch (NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(this,"The pagecount value must be an integer between 1 and 15");
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
    public javax.swing.JTextField overlappingTextField;
    private javax.swing.JLabel pageCOuntLabel;
    public javax.swing.JTextField pageCountTextField;
    // End of variables declaration//GEN-END:variables
    
    /**
     * true if the tpml.tex shohld be indluded
     */
    public boolean all;
    /**
     * true if the dialog is canceld
     */
    public boolean cancelled;
    public String overlapping;
    /**
     * the overlapping enterd by the user
     */
    public int overlappingInt;
    /**
     * the pagecount enterd by the user
     */
    public int pagecount;
    
 
}
