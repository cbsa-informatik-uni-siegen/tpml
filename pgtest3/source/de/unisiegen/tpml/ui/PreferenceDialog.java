package de.unisiegen.tpml.ui ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Graphics ;
import java.awt.KeyEventDispatcher ;
import java.awt.KeyboardFocusManager ;
import java.awt.event.KeyEvent ;
import java.awt.image.BufferedImage ;
import java.lang.reflect.Method ;
import java.util.ResourceBundle ;
import javax.swing.DefaultListCellRenderer ;
import javax.swing.DefaultListModel ;
import javax.swing.ImageIcon ;
import javax.swing.JColorChooser ;
import javax.swing.JLabel ;
import javax.swing.JList ;
import de.unisiegen.tpml.core.util.StringUtilities ;
import de.unisiegen.tpml.graphics.Theme ;


/**
 * TODO Add documentation here.
 * 
 * @author Benedikt Meurer
 * @author Christoph Fehling
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public class PreferenceDialog extends javax.swing.JDialog
{
  //
  // Constants
  //
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = 6823003123624975539L ;


  //
  // Inner classes
  //
  /**
   * The list model item class for the color list.
   */
  private class ColorItem
  {
    private String id ;


    public ColorItem ( String id )
    {
      if ( id == null )
      {
        throw new NullPointerException ( "id is null" ) ;
      }
      this.id = id ;
    }


    public String getId ( )
    {
      return this.id ;
    }


    public Color getColor ( )
    {
      try
      {
        Method method = Theme.class.getMethod ( "get"
            + StringUtilities.toCamelCase ( this.id ) ) ;
        return ( Color ) method.invoke ( PreferenceDialog.this.theme ) ;
      }
      catch ( RuntimeException e )
      {
        throw e ;
      }
      catch ( Exception e )
      {
        throw new RuntimeException ( e ) ;
      }
    }


    public void setColor ( Color color )
    {
      try
      {
        Method method = Theme.class.getMethod ( "set"
            + StringUtilities.toCamelCase ( this.id ) , Color.class ) ;
        method.invoke ( PreferenceDialog.this.theme , color ) ;
      }
      catch ( RuntimeException e )
      {
        throw e ;
      }
      catch ( Exception e )
      {
        throw new RuntimeException ( e ) ;
      }
    }


    public ImageIcon getIcon ( )
    {
      BufferedImage image = new BufferedImage ( 16 , 10 ,
          BufferedImage.TYPE_INT_RGB ) ;
      Graphics g = image.getGraphics ( ) ;
      g.setColor ( getColor ( ) ) ;
      g.fillRect ( 0 , 0 , 16 , 10 ) ;
      g.setColor ( getColor ( ).darker ( ) ) ;
      g.drawRect ( 0 , 0 , 15 , 9 ) ;
      return new ImageIcon ( image ) ;
    }


    public String getName ( )
    {
      return ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" ).getString (
          this.id ) ;
    }
  }


  /**
   * The renderer for the color list items.
   */
  private static class ColorItemRenderer extends DefaultListCellRenderer
  {
    @ Override
    public Component getListCellRendererComponent ( JList list , Object value ,
        int index , boolean isSelected , boolean cellHasFocus )
    {
      JLabel label = ( JLabel ) super.getListCellRendererComponent ( list ,
          value , index , isSelected , cellHasFocus ) ;
      ColorItem item = ( ColorItem ) value ;
      label.setIcon ( item.getIcon ( ) ) ;
      label.setText ( item.getName ( ) ) ;
      return label ;
    }
  }


  //
  // Attributes
  //
  /**
   * The currently active theme.
   * 
   * @see Theme
   */
  private Theme theme = Theme.currentTheme ( ) ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>PreferencesDialog</code> with the specified
   * <code>parent</code>.
   * 
   * @param parent the parent frame or <code>null</code>.
   * @param modal <code>true</code> to place the dialog modal above
   *          <code>parent</code>.
   */
  public PreferenceDialog ( java.awt.Frame parent , boolean modal )
  {
    super ( parent , modal ) ;
    // initialize the components
    initComponents ( ) ;
    // listen to key events
    KeyboardFocusManager.getCurrentKeyboardFocusManager ( )
        .addKeyEventDispatcher ( new KeyEventDispatcher ( )
        {
          public boolean dispatchKeyEvent ( KeyEvent evt )
          {
            if ( evt.getKeyCode ( ) == KeyEvent.VK_ESCAPE )
            {
              dispose ( ) ;
              return true ;
            }
            else
            {
              return false ;
            }
          }
        } ) ;
    // connect the font button to the theme
    /*
     * FIXME: Windows and custom fonts
     * this.fontButton.setFont(this.theme.getFont());
     * this.fontButton.setText(this.theme.getFont().getName() + " " +
     * this.theme.getFont().getSize());
     * this.theme.addPropertyChangeListener("font", new PropertyChangeListener() {
     * public void propertyChange(PropertyChangeEvent e) { Font font =
     * (Font)e.getNewValue(); PreferenceDialog.this.fontButton.setFont(font);
     * PreferenceDialog.this.fontButton.setText(font.getName() + " " +
     * font.getSize()); } });
     */
    // setup the colors list
    DefaultListModel colorsModel = new DefaultListModel ( ) ;
    colorsModel.addElement ( new ColorItem ( "commentColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "constantColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "environmentColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "expressionColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "keywordColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "ruleColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "underlineColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "typeColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "selectionColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "boundIdColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "bindingIdColor" ) ) ;
    colorsModel.addElement ( new ColorItem ( "freeIdColor" ) ) ;
    this.colorsList.setCellRenderer ( new ColorItemRenderer ( ) ) ;
    this.colorsList.setModel ( colorsModel ) ;
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code
  // ">//GEN-BEGIN:initComponents
  private void initComponents ( )
  {
    javax.swing.JButton closeButton ;
    javax.swing.JLabel colorLabel ;
    javax.swing.JScrollPane colorsScrollPane ;
    java.awt.GridBagConstraints gridBagConstraints ;
    javax.swing.JPanel mainPanel ;
    javax.swing.JPanel southPanel ;
    southPanel = new javax.swing.JPanel ( ) ;
    closeButton = new javax.swing.JButton ( ) ;
    mainPanel = new javax.swing.JPanel ( ) ;
    colorLabel = new javax.swing.JLabel ( ) ;
    colorsScrollPane = new javax.swing.JScrollPane ( ) ;
    colorsList = new javax.swing.JList ( ) ;
    setDefaultCloseOperation ( javax.swing.WindowConstants.DISPOSE_ON_CLOSE ) ;
    setTitle ( java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" )
        .getString ( "Preferences" ) ) ;
    southPanel.setLayout ( new java.awt.GridBagLayout ( ) ) ;
    southPanel.setBorder ( javax.swing.BorderFactory.createEmptyBorder ( 10 ,
        10 , 10 , 10 ) ) ;
    closeButton.setMnemonic ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "CloseMnemonic" ).charAt ( 0 ) ) ;
    closeButton.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Close" ) ) ;
    closeButton.addActionListener ( new java.awt.event.ActionListener ( )
    {
      public void actionPerformed ( java.awt.event.ActionEvent evt )
      {
        closeButtonActionPerformed ( evt ) ;
      }
    } ) ;
    southPanel.add ( closeButton , new java.awt.GridBagConstraints ( ) ) ;
    getContentPane ( ).add ( southPanel , java.awt.BorderLayout.SOUTH ) ;
    mainPanel.setLayout ( new java.awt.GridBagLayout ( ) ) ;
    colorLabel.setText ( java.util.ResourceBundle.getBundle (
        "de/unisiegen/tpml/ui/ui" ).getString ( "Colors" ) ) ;
    gridBagConstraints = new java.awt.GridBagConstraints ( ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST ;
    gridBagConstraints.insets = new java.awt.Insets ( 18 , 18 , 12 , 0 ) ;
    mainPanel.add ( colorLabel , gridBagConstraints ) ;
    colorsList.setModel ( new javax.swing.AbstractListModel ( )
    {
      String [ ] strings =
      { "Item 1" , "Item 2" , "Item 3" , "Item 4" , "Item 5" } ;


      public int getSize ( )
      {
        return strings.length ;
      }


      public Object getElementAt ( int i )
      {
        return strings [ i ] ;
      }
    } ) ;
    colorsList.addMouseListener ( new java.awt.event.MouseAdapter ( )
    {
      public void mouseClicked ( java.awt.event.MouseEvent evt )
      {
        colorsListMouseClicked ( evt ) ;
      }
    } ) ;
    colorsScrollPane.setViewportView ( colorsList ) ;
    gridBagConstraints = new java.awt.GridBagConstraints ( ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 3 ;
    gridBagConstraints.gridwidth = 2 ;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH ;
    gridBagConstraints.ipadx = 300 ;
    gridBagConstraints.weightx = 1.0 ;
    gridBagConstraints.weighty = 1.0 ;
    gridBagConstraints.insets = new java.awt.Insets ( 0 , 30 , 6 , 18 ) ;
    mainPanel.add ( colorsScrollPane , gridBagConstraints ) ;
    getContentPane ( ).add ( mainPanel , java.awt.BorderLayout.CENTER ) ;
    pack ( ) ;
  }// </editor-fold>//GEN-END:initComponents


  private void colorsListMouseClicked ( java.awt.event.MouseEvent evt )
  {// GEN-FIRST:event_colorsListMouseClicked
    // check if this is a double-click
    if ( evt.getClickCount ( ) == 2 )
    {
      ColorItem item = ( ColorItem ) this.colorsList.getSelectedValue ( ) ;
      if ( item != null )
      {
        // let the user select a new color
        Color color = JColorChooser.showDialog ( this ,
            java.util.ResourceBundle.getBundle ( "de/unisiegen/tpml/ui/ui" )
                .getString ( "Select_a_color" ) , item.getColor ( ) ) ;
        if ( color != null )
        {
          // set the new color for the item
          item.setColor ( color ) ;
          // schedule a repaint
          this.colorsList.repaint ( ) ;
        }
      }
    }
  }// GEN-LAST:event_colorsListMouseClicked


  private void closeButtonActionPerformed ( java.awt.event.ActionEvent evt )
  {// GEN-FIRST:event_closeButtonActionPerformed
    dispose ( ) ;
  }// GEN-LAST:event_closeButtonActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JList colorsList ;
  // End of variables declaration//GEN-END:variables
}
