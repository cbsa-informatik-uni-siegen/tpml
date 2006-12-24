package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.Graphics ;
import javax.swing.JTree ;
import javax.swing.tree.DefaultTreeCellRenderer ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * TODO
   */
  private static final long serialVersionUID = 3053444302287643623L ;


  /**
   * TODO
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
   * TODO
   * 
   * @param pTree
   * @param pValue
   * @param pSel
   * @param pExpanded
   * @param pLeaf
   * @param pRow
   * @param pHasFocus
   * @return TODO
   * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
   *      java.lang.Object, boolean, boolean, boolean, int, boolean)
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
   * TODO
   * 
   * @param pGraphics
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @ Override
  protected void paintComponent ( Graphics pGraphics )
  {
    super.paintComponent ( pGraphics ) ;
  }
}
