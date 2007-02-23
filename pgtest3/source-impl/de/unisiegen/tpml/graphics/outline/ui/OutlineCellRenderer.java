package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.Graphics ;
import javax.swing.JTree ;
import javax.swing.border.LineBorder ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * This class renders a cell in the {@link Outline}.
 * 
 * @author Christian Fehler
 * @version $Rev: 1075 $
 */
public final class OutlineCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * The <code>Font</code>.
   */
  private static final String FONT = "SansSerif" ; //$NON-NLS-1$


  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 3053444302287643623L ;


  /**
   * Initializes the {@link OutlineCellRenderer}.
   */
  public OutlineCellRenderer ( )
  {
    initialize ( ) ;
  }


  /**
   * The <code>Color</code> of the border.
   */
  private static final Color BORDER = new Color ( 235 , 235 , 255 ) ;


  /**
   * Initializes the {@link OutlineCellRenderer}.
   */
  public final void initialize ( )
  {
    this.setIcon ( null ) ;
    this.setLeafIcon ( null ) ;
    this.setOpenIcon ( null ) ;
    this.setClosedIcon ( null ) ;
    this.setDisabledIcon ( null ) ;
    this.setBackground ( Color.WHITE ) ;
    this.setBackgroundNonSelectionColor ( Color.WHITE ) ;
    this.setBackgroundSelectionColor ( Color.WHITE ) ;
    this.setFont ( new Font ( FONT , Font.PLAIN , 14 ) ) ;
    this.setBorderSelectionColor ( Color.BLUE ) ;
    this.setTextSelectionColor ( Color.BLACK ) ;
    this.setTextNonSelectionColor ( Color.BLACK ) ;
    this.setBorder ( new LineBorder ( BORDER ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public final Component getTreeCellRendererComponent ( JTree pTree ,
      Object pValue , boolean pSel , boolean pExpanded , boolean pLeaf ,
      int pRow , boolean pHasFocus )
  {
    super.getTreeCellRendererComponent ( pTree , pValue , pSel , pExpanded ,
        pLeaf , pRow , pHasFocus ) ;
    if ( pSel )
    {
      this.setBorder ( new LineBorder ( Color.BLUE ) ) ;
    }
    else
    {
      this.setBorder ( new LineBorder ( BORDER ) ) ;
    }
    return this ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  protected final void paintComponent ( Graphics pGraphics )
  {
    super.paintComponent ( pGraphics ) ;
    // System.out.println ( this.getParent ( ));
    // System.out.println ( this.getWidth ( ) );
  }
}
