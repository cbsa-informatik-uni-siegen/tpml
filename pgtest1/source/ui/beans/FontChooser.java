/*
 * FontChooser.java
 *
 * Created on 27. Januar 2006, 18:56
 */

package ui.beans;

import java.awt.GraphicsEnvironment;
import javax.swing.DefaultListModel;
/**
 *
 * @author  marcell
 */
public class FontChooser extends javax.swing.JPanel {
    
    /** Creates new form FontChooser */
    public FontChooser() {
        initComponents();
        
        initFamilies ();
        
        
    }
    
    private void initFamilies() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontFamilies[] = ge.getAvailableFontFamilyNames();
        
        DefaultListModel listModel = new DefaultListModel();
        for (int i=0; i<fontFamilies.length; i++) {
            listModel.addElement(fontFamilies[i]);
        }
        listFamily.setModel(listModel);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFamily = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listStyle = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listSize = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jLabel1.setText("Family:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(jLabel1, gridBagConstraints);

        listFamily.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Family1", "Family2", "Family3", "Family4" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listFamily.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listFamilyValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(listFamily);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(jScrollPane1, gridBagConstraints);

        jLabel2.setText("Style:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(jLabel2, gridBagConstraints);

        listStyle.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Roman", "Bold", "Oblique", "Bold Oblique" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listStyle.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listStyleValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(listStyle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(jScrollPane2, gridBagConstraints);

        jLabel3.setText("Size:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(jLabel3, gridBagConstraints);

        listSize.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "8", "9", "10", "11", "12", "13", "14", "15", "16", "18", "20", "22", "24", "26", "28", "32", "36", "40" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listSize.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSizeValueChanged(evt);
            }
        });

        jScrollPane3.setViewportView(listSize);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(jScrollPane3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview:"));
        jTextField1.setText("abcdefghijk ABCDEFGHIJK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 366;
        gridBagConstraints.ipady = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void listSizeValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listSizeValueChanged
        // font size chooser changed
// TODO add your handling code here:
        applyFont();
    }//GEN-LAST:event_listSizeValueChanged

    private void listStyleValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listStyleValueChanged
        // font style chooser changed
// TODO add your handling code here:
        applyFont();
    }//GEN-LAST:event_listStyleValueChanged

    private void listFamilyValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listFamilyValueChanged
        // font family chooser changed
// TODO add your handling code here:
        applyFont();
    }//GEN-LAST:event_listFamilyValueChanged
  
    public void setGUIFont (java.awt.Font font) {
        this.guiFont = font;
        listFamily.setSelectedValue(font.getFamily(), true);
        listStyle.setSelectedIndex(font.getStyle());
        listSize.setSelectedValue (new Integer (font.getSize()).toString(), true);
        applyFont ();
    }
    
    public java.awt.Font getGUIFont () {
        return this.guiFont;
    }
    
    private void applyFont () {
        String family = (String)listFamily.getSelectedValue();
        int style = listStyle.getSelectedIndex();
        String stringSize = (String)listSize.getSelectedValue();
        int size = 8;
        try {
            size = Integer.parseInt(stringSize);
        } catch (Exception e) {
            
        }
        this.guiFont = new java.awt.Font (family, style, size);
        this.jTextField1.setFont(this.guiFont);
        this.jTextField1.setText("abcdefghijk ABCDEFGHIJK");
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList listFamily;
    private javax.swing.JList listSize;
    private javax.swing.JList listStyle;
    // End of variables declaration//GEN-END:variables
    private java.awt.Font guiFont;
}
