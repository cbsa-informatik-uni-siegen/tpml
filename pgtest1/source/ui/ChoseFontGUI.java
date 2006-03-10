/*
 * ChoseFontGUI.java
 *
 * Created on 26. Februar 2006, 17:54
 */

package ui;

import javax.swing.event.EventListenerList;

/**
 *
 * @author  marcell
 */
public class ChoseFontGUI extends javax.swing.JDialog {
    
    /** Creates new form ChoseFontGUI */
    public ChoseFontGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public ChoseFontGUI(java.awt.Dialog parent, boolean modal) {
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

        fontChooser1 = new ui.beans.FontChooser();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(fontChooser1, gridBagConstraints);

        buttonOk.setText("Ok");
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(buttonOk, gridBagConstraints);

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(buttonCancel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
// TODO add your handling code here:
        fireCancel();
        dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        fireOk();
        dispose();
    }//GEN-LAST:event_buttonOkActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChoseFontGUI(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    public void addDialogListener (DialogListener l) {
        this.dialogListener.add(DialogListener.class, l);
    }
    
    public void fireOk () {
        Object[] listeners = dialogListener.getListenerList();
		
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DialogListener.class) {
                // Lazily create the event:
                ((DialogListener)listeners[i+1]).dialogOk(new java.util.EventObject(this));
             }
        }
        
    }
    
    public void fireCancel () {
        Object[] listeners = dialogListener.getListenerList();
		
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DialogListener.class) {
                // Lazily create the event:
                ((DialogListener)listeners[i+1]).dialogCancel(new java.util.EventObject(this));
             }
        }
    }
    
    public void setGUIFont (java.awt.Font font) {
        fontChooser1.setGUIFont(font);
    }
    
    public java.awt.Font getGUIFont () {
        return fontChooser1.getGUIFont();
    }

    
    private javax.swing.event.EventListenerList dialogListener = new EventListenerList();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private ui.beans.FontChooser fontChooser1;
    // End of variables declaration//GEN-END:variables
    
}
