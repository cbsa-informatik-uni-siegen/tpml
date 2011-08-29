/*
 * SettingsGUI.java
 *
 * Created on 20. Januar 2006, 15:36
 */

package ui;

import java.awt.Color;
import java.awt.Font;
import java.util.prefs.*;
import javax.swing.DefaultComboBoxModel;

import ui.renderer.AbstractRenderer;
/**
 *
 * @author  marcell
 */
public class SettingsGUI extends javax.swing.JDialog {

	public SettingsGUI() {
		super();
		ThemeManager manager = ThemeManager.get();
		
        applyToRenderers();
	}
    /** Creates new form SettingsGUI */
    public SettingsGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Preferences prefs = Preferences.userNodeForPackage(SettingsGUI.class);
        checkBoxSSUnderline.setSelected(prefs.getBoolean("ssUnderlineExpressions", true));
        checkBoxSSJustAxioms.setSelected(prefs.getBoolean("ssJustAxioms", true));
        
        initializeThemes ();
    }
    
    public SettingsGUI(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Preferences prefs = Preferences.userNodeForPackage(SettingsGUI.class);
        checkBoxSSUnderline.setSelected(prefs.getBoolean("ssUnderlineExpressions", true));
        checkBoxSSJustAxioms.setSelected(prefs.getBoolean("ssJustAxioms", true));
        
        initializeThemes ();
    }
    
    private void initializeThemes () {
        ThemeManager manager = ThemeManager.get();
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i=0; i<manager.getNumberOfThemes(); i++) {
            model.addElement(manager.getTheme(i).getName());
        }
        this.jThemesComboBox.setModel(model);
        selectTheme (manager.getCurrentThemeIndex());
    }
    
    private void selectTheme (int idx) {
        ThemeManager manager = ThemeManager.get();
        this.jThemesComboBox.setSelectedIndex(idx);
        this.theme = manager.getTheme(idx);
        int listIndex = this.itemList.getSelectedIndex();
        this.itemList.setListData(this.theme.getItemNames());
        if (listIndex == -1) {
            listIndex = 0;
        }
        this.itemList.setSelectedIndex(listIndex);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        checkBoxSSUnderline = new javax.swing.JCheckBox();
        checkBoxSSJustAxioms = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        buttonNew = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jThemesComboBox = new javax.swing.JComboBox();
        jButtonColor = new javax.swing.JButton();
        jButtonFont = new javax.swing.JButton();
        jFontLabel = new javax.swing.JLabel();
        jPanelColor = new javax.swing.JPanel();
        buttonClose = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        checkBoxSSUnderline.setText("UnderlineExpressions");
        checkBoxSSUnderline.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxSSUnderline.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkBoxSSUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxSSUnderlineActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        jPanel1.add(checkBoxSSUnderline, gridBagConstraints);

        checkBoxSSJustAxioms.setText("Evaluate metarules");
        checkBoxSSJustAxioms.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxSSJustAxioms.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkBoxSSJustAxioms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxSSJustAxiomsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        jPanel1.add(checkBoxSSJustAxioms, gridBagConstraints);

        jTabbedPane1.addTab("SmallStep", jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        itemList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        itemList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                itemListValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(itemList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Theme"));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        buttonNew.setText("New");
        buttonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewActionPerformed(evt);
            }
        });

        jPanel4.add(buttonNew);

        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        jPanel4.add(buttonDelete);

        jPanel3.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jThemesComboBox.setEditable(true);
        jThemesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jThemesComboBoxActionPerformed(evt);
            }
        });

        jPanel3.add(jThemesComboBox, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel2.add(jPanel3, gridBagConstraints);

        jButtonColor.setText("Color");
        jButtonColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonColorActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jButtonColor, gridBagConstraints);

        jButtonFont.setText("Font");
        jButtonFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFontActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel2.add(jButtonFont, gridBagConstraints);

        jFontLabel.setText("abcdefghijk ABCDEFGHIJK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jFontLabel, gridBagConstraints);

        jPanelColor.setBackground(new java.awt.Color(0, 51, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanelColor, gridBagConstraints);

        jTabbedPane1.addTab("Themes", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        buttonClose.setText("Close");
        buttonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(buttonClose, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jThemesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jThemesComboBoxActionPerformed
// TODO add your handling code here:
        int idx = this.jThemesComboBox.getSelectedIndex();
        ThemeManager manager = ThemeManager.get();
        if (idx == -1) {
            String name = (String)this.jThemesComboBox.getSelectedItem();
            if (manager.getCurrentThemeIndex() != 0) {
                manager.getCurrentTheme().setName (name);
                initializeThemes();
                applySettings();
            }
        }
        else {
            selectTheme (idx);
            manager.setCurrentThemeIndex(idx);
            applySettings();
        }
    }//GEN-LAST:event_jThemesComboBoxActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
// TODO add your handling code here:
        ThemeManager.get().removeCurrentTheme();
        initializeThemes ();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewActionPerformed
// TODO add your handling code here:
        ThemeManager.get().addNewTheme();
        initializeThemes();
    }//GEN-LAST:event_buttonNewActionPerformed

    private void itemListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_itemListValueChanged
// TODO add your handling code here:
        int idx = this.itemList.getSelectedIndex();
        if (idx == -1) {
            return;
        }
        if (theme.hasItemFont(idx)) {
        	java.awt.Font font = theme.getItemFont(idx);
        	this.jFontLabel.setEnabled(true);
        	this.jButtonFont.setEnabled(true);
            this.jFontLabel.setFont(font);
        }
        else {
        	this.jFontLabel.setEnabled(false);
        	this.jButtonFont.setEnabled(false);
        }
        java.awt.Color color = theme.getItemColor(idx);
        this.jPanelColor.setBackground(color);
    }//GEN-LAST:event_itemListValueChanged

    private void jButtonFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFontActionPerformed
        int idx = this.itemList.getSelectedIndex();
        java.awt.Font font = theme.getItemFont(idx);
        
        ChoseFontGUI gui = new ChoseFontGUI (this, true);
        gui.setGUIFont(font);
        gui.addDialogListener(new DialogListener() {
           public void dialogOk(java.util.EventObject o) {
               handleFontChanged ((ChoseFontGUI)o.getSource());
           } 
           public void dialogCancel(java.util.EventObject o) {
               
           }
        });
        gui.setVisible (true);

    }//GEN-LAST:event_jButtonFontActionPerformed

    private void jButtonColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonColorActionPerformed
        ChoseColorGUI gui = new ChoseColorGUI (this, true);
        int idx = this.itemList.getSelectedIndex();
        java.awt.Color color = theme.getItemColor(idx);
        gui.setColor(color);
        gui.addDialogListener(new DialogListener() {
            public void dialogOk(java.util.EventObject o) {
                handleColorChanged ((ChoseColorGUI)o.getSource());
            }
            public void dialogCancel(java.util.EventObject o) { 
                
            }
        });
        gui.setVisible (true);

    }//GEN-LAST:event_jButtonColorActionPerformed

    private void handleFontChanged (ChoseFontGUI gui) {
        this.jFontLabel.setFont(gui.getGUIFont());
        int idx = this.itemList.getSelectedIndex();
        theme.setItemFont(idx, gui.getGUIFont());
        applySettings();
    }
    
    private void handleColorChanged (ChoseColorGUI gui) {
        this.jPanelColor.setBackground(gui.getColor ());
        int idx = this.itemList.getSelectedIndex();
        theme.setItemColor(idx, gui.getColor());
        applySettings();
    }
    
    private void applySettings () {
        try {
            Preferences prefs = Preferences.userNodeForPackage(SettingsGUI.class);
            prefs.putBoolean("ssUnderlineExpressions", checkBoxSSUnderline.isSelected());
            prefs.putBoolean("ssJustAxioms", checkBoxSSJustAxioms.isSelected());
            ThemeManager manager = ThemeManager.get();
            manager.storeThemes(prefs);
            prefs.flush ();
           
            
            applyToRenderers();
            
        }
        catch (Exception e) {
            System.out.println("error flushing preferences");
        }
        
        
        
    }

    public void applyToRenderers() {
        // apply the theme into the Expression Renderer
    	ThemeManager manager = ThemeManager.get();
        Theme theme = manager.getCurrentTheme();
        Font fnt 	= theme.getItemFont(Theme.TYPE_CONSTANT);
        Color col	= theme.getItemColor(Theme.TYPE_CONSTANT);
        AbstractRenderer.setConstantStyle(fnt, getFontMetrics (fnt), col);
        fnt = theme.getItemFont(Theme.TYPE_EXPRESSION);
        col	= theme.getItemColor(Theme.TYPE_EXPRESSION);
        AbstractRenderer.setTextStyle(fnt, getFontMetrics (fnt), col);
        fnt = theme.getItemFont(Theme.TYPE_KEYWORD);
        col	= theme.getItemColor(Theme.TYPE_KEYWORD);
        AbstractRenderer.setKeywordStyle(fnt, getFontMetrics (fnt), col);
        col	= theme.getItemColor(Theme.TYPE_UNDERLINE);
        AbstractRenderer.setUnderlineColor(col);
        
        fnt = theme.getItemFont(Theme.ENV_IDENTIFIER);
        col = theme.getItemColor(Theme.ENV_IDENTIFIER);
        AbstractRenderer.setEnvironmentStyle(fnt, getFontMetrics (fnt), col);

    }
    private void checkBoxSSJustAxiomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxSSJustAxiomsActionPerformed
// TODO add your handling code here:
        applySettings();
    }//GEN-LAST:event_checkBoxSSJustAxiomsActionPerformed

    private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseActionPerformed
// TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_buttonCloseActionPerformed

    private void checkBoxSSUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxSSUnderlineActionPerformed
// TODO add your handling code here:
        applySettings();
    }//GEN-LAST:event_checkBoxSSUnderlineActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SettingsGUI(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonNew;
    private javax.swing.JCheckBox checkBoxSSJustAxioms;
    private javax.swing.JCheckBox checkBoxSSUnderline;
    private javax.swing.JList itemList;
    private javax.swing.JButton jButtonColor;
    private javax.swing.JButton jButtonFont;
    private javax.swing.JLabel jFontLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelColor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox jThemesComboBox;
    // End of variables declaration//GEN-END:variables
    
    private Theme theme;
}