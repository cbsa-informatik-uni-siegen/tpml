package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.Graphics ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultTreeCellRenderer ;


/**
 * This class renders a cell in the <code>Outline</code>.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 3053444302287643623L ;


  /**
   * Initializes the <code>OutlineCellRenderer</code>.
   */
  public OutlineCellRenderer ( )
  {
    this.setIcon ( null ) ;
    this.setLeafIcon ( null ) ;
    this.setOpenIcon ( null ) ;
    this.setClosedIcon ( null ) ;
    this.setDisabledIcon ( null ) ;
    this.setBackground ( Color.WHITE ) ;
    this.setBackgroundNonSelectionColor ( Color.WHITE ) ;
    this.setBackgroundSelectionColor ( new Color ( 225 , 225 , 255 ) ) ;
    this.setFont ( new Font ( "SansSerif" , Font.PLAIN , 14 ) ) ; //$NON-NLS-1$
    this.setBorderSelectionColor ( Color.BLUE ) ;
    this.setTextSelectionColor ( Color.BLACK ) ;
    this.setTextNonSelectionColor ( Color.BLACK ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public Component getTreeCellRendererComponent ( JTree pTree , Object pValue ,
      boolean pSel , boolean pExpanded , boolean pLeaf , int pRow ,
      boolean pHasFocus )
  {
    super.getTreeCellRendererComponent ( pTree , pValue , pSel , pExpanded ,
        pLeaf , pRow , pHasFocus ) ;
    return this ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  protected void paintComponent ( Graphics pGraphics )
  {
    super.paintComponent ( pGraphics ) ;
  }
}
