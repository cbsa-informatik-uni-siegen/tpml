/*
 * FileWizard.java
 *
 * Created on 26. Juli 2006, 19:44
 */

package de.unisiegen.tpml.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageFactory;

/**
 *
 * @author  bmeurer
 */
public class FileWizard extends javax.swing.JDialog {
  //
  // Inner classes
  //

  /**
   * List cell renderer for the JList with the {@link Language}s.
   */
  private static class LanguagesListCellRender extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      Language language = (Language)value;
      label.setText(language.getName() + " (" + language.getTitle() + ")");
      return label;
    }
  }
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>FileWizard</code> instance.
   *
   * @param parent the parent frame.
   * @param model <code>true</code> to display the wizard modal
   *              for the <code>parent</code>.
   */
  public FileWizard(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    
    // determine the list of available languages
    LanguageFactory factory = LanguageFactory.newInstance();
    Language[] available = factory.getAvailableLanguages();

    // setup the list model with the available languages
    DefaultListModel languagesModel = new DefaultListModel();
    for (Language language : available) {
      languagesModel.addElement(language);
    }
    this.languagesList.setModel(languagesModel);
    
    // setup the list cell renderer for the languages
    this.languagesList.setCellRenderer(new LanguagesListCellRender());
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JPanel bodyPanel;
        javax.swing.JPanel buttonsPanel;
        javax.swing.JButton cancelButton;
        javax.swing.JLabel descriptionLabel;
        javax.swing.JScrollPane descriptionScrollPane;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel headerImageLabel;
        javax.swing.JPanel headerPanel;
        javax.swing.JSeparator headerSeparator;
        javax.swing.JLabel headerSubTitleLabel;
        javax.swing.JLabel headerTitleLabel;
        javax.swing.JLabel languagesLabel;
        javax.swing.JScrollPane languagesScrollPane;

        headerPanel = new javax.swing.JPanel();
        headerTitleLabel = new javax.swing.JLabel();
        headerSubTitleLabel = new javax.swing.JLabel();
        headerSeparator = new javax.swing.JSeparator();
        headerImageLabel = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        languagesScrollPane = new javax.swing.JScrollPane();
        languagesList = new javax.swing.JList();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        languagesLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New File...");
        setName("fileWizard");
        headerPanel.setLayout(new java.awt.GridBagLayout());

        headerPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        headerTitleLabel.setFont(new java.awt.Font("Dialog", 1, 24));
        headerTitleLabel.setText("New File...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
        headerPanel.add(headerTitleLabel, gridBagConstraints);

        headerSubTitleLabel.setText("Create an empty source file for a language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        headerPanel.add(headerSubTitleLabel, gridBagConstraints);

        headerSeparator.setForeground(javax.swing.UIManager.getDefaults().getColor("windowBorder"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.weightx = 1.0;
        headerPanel.add(headerSeparator, gridBagConstraints);

        headerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/unisiegen/tpml/ui/icons/new24.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        headerPanel.add(headerImageLabel, gridBagConstraints);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        bodyPanel.setLayout(new java.awt.GridBagLayout());

        languagesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "L0 (Pure untyped \u03bb calculus)", "L1 (Simply typed \u03bb calculus)" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        languagesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        languagesList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                languagesListKeyPressed(evt);
            }
        });
        languagesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                languagesListValueChanged(evt);
            }
        });
        languagesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                languagesListMouseClicked(evt);
            }
        });

        languagesScrollPane.setViewportView(languagesList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        bodyPanel.add(languagesScrollPane, gridBagConstraints);

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        bodyPanel.add(descriptionScrollPane, gridBagConstraints);

        descriptionLabel.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        bodyPanel.add(descriptionLabel, gridBagConstraints);

        languagesLabel.setText("Available languages:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        bodyPanel.add(languagesLabel, gridBagConstraints);

        getContentPane().add(bodyPanel, java.awt.BorderLayout.CENTER);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        buttonsPanel.add(cancelButton, gridBagConstraints);

        okButton.setMnemonic('O');
        okButton.setText("Ok");
        okButton.setEnabled(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        buttonsPanel.add(okButton, gridBagConstraints);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void languagesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_languagesListMouseClicked
// TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            dispose();
        }
    }//GEN-LAST:event_languagesListMouseClicked

    private void languagesListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_languagesListKeyPressed
// TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (language != null){
            dispose();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            language = null;
            dispose();
        }
    }//GEN-LAST:event_languagesListKeyPressed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
// 
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

  private void languagesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_languagesListValueChanged
    language = (Language)this.languagesList.getSelectedValue();
    if (language != null) {
      this.descriptionTextArea.setText(language.getDescription());
      this.okButton.setEnabled(true);
    }
    else {
      this.descriptionTextArea.setText("");
      this.okButton.setEnabled(false);
    }
  }//GEN-LAST:event_languagesListValueChanged

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
      language = null;
      dispose();
  }//GEN-LAST:event_cancelButtonActionPerformed
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JList languagesList;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private Language language;
    
    public Language getLanguage(){
        return language;
    }
}
