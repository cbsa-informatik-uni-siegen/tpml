/*
 * ChoseColorGUI.java
 *
 * Created on 26. Februar 2006, 18:05
 */

package ui;

/**
 *
 * @author  marcell
 */
public class ChoseColorGUI extends javax.swing.JDialog {
    
    /** Creates new form ChoseColorGUI */
    public ChoseColorGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.dialogListener = new javax.swing.event.EventListenerList();
    }
    
    public ChoseColorGUI(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.dialogListener = new javax.swing.event.EventListenerList();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jColorChooser1 = new javax.swing.JColorChooser();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

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
        getContentPane().add(jColorChooser1, gridBagConstraints);

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButtonOk, gridBagConstraints);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButtonCancel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        fireOk();
        this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        fireCancel();
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    public void setColor (java.awt.Color color) {
        jColorChooser1.setColor(color);
    }
    
    public java.awt.Color getColor () {
        return jColorChooser1.getColor();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChoseColorGUI(new javax.swing.JFrame(), true).setVisible(true);
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

    
    private javax.swing.event.EventListenerList dialogListener;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JColorChooser jColorChooser1;
    // End of variables declaration//GEN-END:variables
    
}
