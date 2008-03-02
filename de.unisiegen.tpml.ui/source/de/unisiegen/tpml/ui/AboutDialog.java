package de.unisiegen.tpml.ui;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard;


/**
 * @author TPPool15
 * @author Christian Fehler
 * @version $Rev$
 */
public class AboutDialog extends javax.swing.JDialog
{

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 5452729695850970988L;


  /**
   * Creates new form AboutDialog
   * 
   * @param parent The parent.
   * @param modal The model flag.
   */
  public AboutDialog ( java.awt.Frame parent, boolean modal )
  {
    super ( parent, modal );
    initComponents ();
    this.textLabel.setText ( "TPML " + Versions.UI ); //$NON-NLS-1$
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code
  // ">//GEN-BEGIN:initComponents
  private void initComponents ()
  {
    javax.swing.JLabel authorsLabel;
    javax.swing.JLabel authorsLabelTarget1;
    javax.swing.JLabel authorsLabelTarget2;
    javax.swing.JLabel authorsLabelTarget3;
    javax.swing.JLabel authorsLabelTarget4;
    javax.swing.JLabel authorsLabelTarget6;
    javax.swing.JLabel authorsLabelTarget7;
    javax.swing.JPanel bodyPanel;
    javax.swing.JPanel buttonPanel;
    javax.swing.JLabel copyrightLabel;
    javax.swing.JSeparator dialogSeparator;
    java.awt.GridBagConstraints gridBagConstraints;
    javax.swing.JPanel headerPanel;
    javax.swing.JLabel iconLabel;
    javax.swing.JLabel websiteLabel;
    javax.swing.JLabel websiteLabelTarget;
    headerPanel = new javax.swing.JPanel ();
    this.textLabel = new javax.swing.JLabel ();
    iconLabel = new javax.swing.JLabel ();
    dialogSeparator = new javax.swing.JSeparator ();
    copyrightLabel = new javax.swing.JLabel ();
    bodyPanel = new javax.swing.JPanel ();
    websiteLabel = new javax.swing.JLabel ();
    websiteLabelTarget = new javax.swing.JLabel ();
    authorsLabel = new javax.swing.JLabel ();
    authorsLabelTarget1 = new javax.swing.JLabel ();
    authorsLabelTarget2 = new javax.swing.JLabel ();
    authorsLabelTarget3 = new javax.swing.JLabel ();
    authorsLabelTarget4 = new javax.swing.JLabel ();
    authorsLabelTarget6 = new javax.swing.JLabel ();
    authorsLabelTarget7 = new javax.swing.JLabel ();
    buttonPanel = new javax.swing.JPanel ();
    this.closeButton = new javax.swing.JButton ();
    setDefaultCloseOperation ( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
    setTitle ( java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ) //$NON-NLS-1$
        .getString ( "About" ) ); //$NON-NLS-1$
    headerPanel.setLayout ( new java.awt.GridBagLayout () );
    headerPanel.setBackground ( new java.awt.Color ( 255, 255, 255 ) );
    this.textLabel.setFont ( new java.awt.Font ( "Dialog", 1, 30 ) ); //$NON-NLS-1$
    this.textLabel.setText ( "TPML" ); //$NON-NLS-1$
    this.textLabel.setVerticalAlignment ( javax.swing.SwingConstants.BOTTOM );
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.5;
    gridBagConstraints.insets = new java.awt.Insets ( 12, 48, 0, 48 );
    headerPanel.add ( this.textLabel, gridBagConstraints );
    iconLabel.setIcon ( new javax.swing.ImageIcon ( getClass ().getResource (
        "/de/unisiegen/tpml/ui/icons/UniLogo.gif" ) ) ); //$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    headerPanel.add ( iconLabel, gridBagConstraints );
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    headerPanel.add ( dialogSeparator, gridBagConstraints );
    copyrightLabel.setFont ( new java.awt.Font ( "Dialog", 0, 10 ) ); //$NON-NLS-1$
    copyrightLabel.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "CopyrightText" ) ); //$NON-NLS-1$//$NON-NLS-2$
    copyrightLabel.setVerticalAlignment ( javax.swing.SwingConstants.TOP );
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.5;
    gridBagConstraints.insets = new java.awt.Insets ( 0, 48, 12, 48 );
    headerPanel.add ( copyrightLabel, gridBagConstraints );
    getContentPane ().add ( headerPanel, java.awt.BorderLayout.NORTH );
    bodyPanel.setLayout ( new java.awt.GridBagLayout () );
    bodyPanel.setBorder ( javax.swing.BorderFactory.createEmptyBorder ( 24, 24,
        24, 24 ) );
    websiteLabel.setLabelFor ( websiteLabelTarget );
    websiteLabel.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "WebsiteLabel" ) );//$NON-NLS-1$ //$NON-NLS-2$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 12, 12, 6, 6 );
    bodyPanel.add ( websiteLabel, gridBagConstraints );
    final String webpage = "http://theoinf.math.uni-siegen.de/tpml/";//$NON-NLS-1$
    websiteLabelTarget.setForeground ( Color.BLUE );
    websiteLabelTarget.setText ( webpage );
    websiteLabelTarget.setToolTipText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Webpage.ToolTip" ) );//$NON-NLS-1$ //$NON-NLS-2$
    websiteLabelTarget.addMouseListener ( new MouseListener ()
    {

      public void mouseClicked ( @SuppressWarnings ( "unused" )
      MouseEvent event )
      {
        OutlineClipboard.getInstance ().copy ( webpage );
      }


      public void mouseEntered ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        // TODO Auto-generated method stub
      }


      public void mouseExited ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        // TODO Auto-generated method stub
      }


      public void mousePressed ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        // TODO Auto-generated method stub
      }


      public void mouseReleased ( @SuppressWarnings ( "unused" )
      MouseEvent e )
      {
        // TODO Auto-generated method stub
      }
    } );
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 12, 6, 6, 24 );
    bodyPanel.add ( websiteLabelTarget, gridBagConstraints );
    authorsLabel.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "AuthorsLabel" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 6, 12, 6, 6 );
    bodyPanel.add ( authorsLabel, gridBagConstraints );
    authorsLabelTarget1.setText ( "Benedikt Meurer" );//$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 6, 6, 6, 12 );
    bodyPanel.add ( authorsLabelTarget1, gridBagConstraints );
    authorsLabelTarget2.setText ( "Marcell Fischbach" );//$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 0, 6, 6, 12 );
    bodyPanel.add ( authorsLabelTarget2, gridBagConstraints );
    authorsLabelTarget3.setText ( "Christoph Fehling" ); //$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 0, 6, 6, 12 );
    bodyPanel.add ( authorsLabelTarget3, gridBagConstraints );
    authorsLabelTarget4.setText ( "Christian Fehler" ); //$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 6, 6, 6, 12 );
    bodyPanel.add ( authorsLabelTarget4, gridBagConstraints );
    authorsLabelTarget6.setText ( "Benjamin Mies" );//$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 0, 6, 6, 12 );
    bodyPanel.add ( authorsLabelTarget6, gridBagConstraints );
    authorsLabelTarget7.setText ( "Michael Oeste" );//$NON-NLS-1$
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets ( 0, 6, 12, 12 );
    bodyPanel.add ( authorsLabelTarget7, gridBagConstraints );
    getContentPane ().add ( bodyPanel, java.awt.BorderLayout.CENTER );
    buttonPanel.setLayout ( new java.awt.GridBagLayout () );
    buttonPanel.setBorder ( javax.swing.BorderFactory.createEmptyBorder ( 0,
        10, 10, 10 ) );
    this.closeButton.setMnemonic ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "CloseMnemonic" ).charAt ( 0 ) );//$NON-NLS-1$ //$NON-NLS-2$
    this.closeButton.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Close" ) );//$NON-NLS-1$ //$NON-NLS-2$
    this.closeButton.setFocusable ( false );
    this.closeButton.addActionListener ( new java.awt.event.ActionListener ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void actionPerformed ( java.awt.event.ActionEvent evt )
      {
        closeButtonActionPerformed ( evt );
      }
    } );
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
    buttonPanel.add ( this.closeButton, gridBagConstraints );
    getContentPane ().add ( buttonPanel, java.awt.BorderLayout.SOUTH );
    this.setBounds ( 0, 0, 450, 300 );
    setResizable ( false );
  }// </editor-fold>//GEN-END:initComponents


  /**
   * TODO
   * 
   * @param evt
   */
  private void closeButtonActionPerformed ( @SuppressWarnings ( "unused" )
  java.awt.event.ActionEvent evt )
  {// GEN-FIRST:event_closeButtonActionPerformed
    dispose ();
  }// GEN-LAST:event_closeButtonActionPerformed


  /**
   * TODO
   */
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton closeButton;


  /**
   * TODO
   */
  private javax.swing.JLabel textLabel;
  // End of variables declaration//GEN-END:variables
}
