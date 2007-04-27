package de.unisiegen.tpml.graphics.outline.ui ;


import java.awt.Color ;
import java.awt.Component ;
import java.awt.Graphics ;
import javax.swing.JTree ;
import javax.swing.border.LineBorder ;
import javax.swing.tree.DefaultTreeCellRenderer ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;


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
  // private static final String FONT = "SansSerif" ; //$NON-NLS-1$
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = 3053444302287643623L ;


  /**
   * The <code>Color</code> of the border.
   */
  private static final Color BORDER = new Color ( 235 , 235 , 255 ) ;


  /**
   * The <code>Color</code> of the border as a hex string.
   */
  private static final String colorString = OutlineNode.getHTMLColor ( BORDER ) ;


  /**
   * The white font in HTML code.
   */
  private static final String WHITE_FONT = "<font color=\"#FFFFFF\">" ; //$NON-NLS-1$


  /**
   * The font begin in HTML code.
   */
  private static final String FONT_BEGIN = "<font color=\"#" ; //$NON-NLS-1$


  /**
   * The font end in HTML code.
   */
  private static final String FONT_END = "\">" ; //$NON-NLS-1$


  /**
   * Initializes the {@link OutlineCellRenderer}.
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
    this.setBackgroundSelectionColor ( BORDER ) ;
    this.setFont ( Theme.currentTheme ( ).getFont ( ) ) ;
    this.setBorderSelectionColor ( Color.BLUE ) ;
    this.setTextSelectionColor ( Color.BLACK ) ;
    this.setTextNonSelectionColor ( Color.BLACK ) ;
    this.setBorder ( new LineBorder ( Color.WHITE ) ) ;
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
    OutlineNode outlineNode = ( OutlineNode ) pValue ;
    if ( ( outlineNode.hasBreaks ( ) ) && ( pSel ) )
    {
      outlineNode.setCaptionHTML ( outlineNode.getCaptionHTML ( ).replaceAll (
          WHITE_FONT , FONT_BEGIN + colorString + FONT_END ) ) ;
      this.setBorder ( new LineBorder ( Color.BLUE ) ) ;
    }
    else if ( ( outlineNode.hasBreaks ( ) ) && ( ! pSel ) )
    {
      this.setBorder ( new LineBorder ( BORDER ) ) ;
    }
    else if ( ( ! outlineNode.hasBreaks ( ) ) && ( pSel ) )
    {
      this.setBorder ( new LineBorder ( Color.BLUE ) ) ;
    }
    else if ( ( ! outlineNode.hasBreaks ( ) ) && ( ! pSel ) )
    {
      this.setBorder ( new LineBorder ( Color.WHITE ) ) ;
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
  }
}
