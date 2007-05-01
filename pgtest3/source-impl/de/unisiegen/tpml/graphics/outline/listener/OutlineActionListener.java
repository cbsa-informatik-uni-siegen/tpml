package de.unisiegen.tpml.graphics.outline.listener ;


import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlineClipboard ;


/**
 * This class listens for <code>ActionEvents</code>. It implements the
 * methods for closing, collapsing and expanding the current node and all nodes.
 * 
 * @author Christian Fehler
 * @version $Rev: 995 $
 */
public final class OutlineActionListener implements ActionListener
{
  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initializes the <code>OutlineActionListener</code>.
   * 
   * @param pDefaultOutline The <code>DefaultOutline</code>.
   */
  public OutlineActionListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * This method is invoked if a <code>JMenuItem</code> is pushed.
   * 
   * @param pActionEvent The <code>ActionEvent</code>
   * @see ActionListener#actionPerformed(ActionEvent)
   */
  public final void actionPerformed ( ActionEvent pActionEvent )
  {
    String actionCommand = pActionEvent.getActionCommand ( ) ;
    if ( OutlineUI.CLOSE.equals ( actionCommand ) )
    {
      close ( ) ;
    }
    else if ( OutlineUI.CLOSEALL.equals ( actionCommand ) )
    {
      closeAll ( ) ;
    }
    else if ( OutlineUI.EXPAND.equals ( actionCommand ) )
    {
      expand ( ) ;
    }
    else if ( OutlineUI.EXPANDALL.equals ( actionCommand ) )
    {
      expandAll ( ) ;
    }
    else if ( OutlineUI.COLLAPSE.equals ( actionCommand ) )
    {
      collapse ( ) ;
    }
    else if ( OutlineUI.COLLAPSEALL.equals ( actionCommand ) )
    {
      collapseAll ( ) ;
    }
    else if ( OutlineUI.COPY.equals ( actionCommand ) )
    {
      copy ( ) ;
    }
    else if ( OutlineUI.SELECTION.equals ( actionCommand ) )
    {
      this.defaultOutline.getOutlineItemListener ( ).update (
          this.defaultOutline.getOutlineUI ( ).getJMenuItemSelection ( )
              .isSelected ( ) , null , OutlineUI.SELECTION ) ;
    }
    else if ( OutlineUI.BINDING.equals ( actionCommand ) )
    {
      this.defaultOutline.getOutlineItemListener ( ).update (
          this.defaultOutline.getOutlineUI ( ).getJMenuItemBinding ( )
              .isSelected ( ) , null , OutlineUI.BINDING ) ;
    }
    else if ( OutlineUI.FREE.equals ( actionCommand ) )
    {
      this.defaultOutline.getOutlineItemListener ( ).update (
          this.defaultOutline.getOutlineUI ( ).getJMenuItemFree ( )
              .isSelected ( ) , null , OutlineUI.FREE ) ;
    }
    else if ( OutlineUI.REPLACE.equals ( actionCommand ) )
    {
      this.defaultOutline.getOutlineItemListener ( ).update (
          this.defaultOutline.getOutlineUI ( ).getJMenuItemReplace ( )
              .isSelected ( ) , null , OutlineUI.REPLACE ) ;
    }
    else if ( OutlineUI.AUTOUPDATE.equals ( actionCommand ) )
    {
      this.defaultOutline.getOutlineItemListener ( ).update (
          this.defaultOutline.getOutlineUI ( ).getJMenuItemAutoUpdate ( )
              .isSelected ( ) , null , OutlineUI.AUTOUPDATE ) ;
    }
  }


  /**
   * Closes the selected node.
   */
  public final void close ( )
  {
    if ( this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getSelectionRows ( ) == null )
    {
      return ;
    }
    for ( int i = this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getRowCount ( ) - 1 ; i >= this.defaultOutline.getOutlineUI ( )
        .getJTreeOutline ( ).getSelectionRows ( ) [ 0 ] ; i -- )
    {
      this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Closes all nodes.
   */
  public final void closeAll ( )
  {
    for ( int i = this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getRowCount ( ) - 1 ; i >= 0 ; i -- )
    {
      this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).collapseRow ( i ) ;
    }
  }


  /**
   * Collapses the selected node.
   */
  public final void collapse ( )
  {
    if ( this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getSelectionRows ( ) == null )
    {
      return ;
    }
    this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).collapseRow (
        this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
            .getSelectionRows ( ) [ 0 ] ) ;
  }


  /**
   * Collapses all nodes.
   */
  public final void collapseAll ( )
  {
    this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).collapseRow ( 0 ) ;
  }


  /**
   * Copies the selection into the {@link OutlineClipboard}.
   */
  public final void copy ( )
  {
    OutlineNode outlineNode = ( OutlineNode ) this.defaultOutline
        .getOutlineUI ( ).getJTreeOutline ( ).getSelectionPath ( )
        .getLastPathComponent ( ) ;
    if ( outlineNode != null )
    {
      OutlineClipboard.getInstance ( ).copy ( outlineNode.getPrettyString ( ) ) ;
    }
  }


  /**
   * Expands the selected node.
   */
  public final void expand ( )
  {
    expandTreePath ( this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getSelectionPath ( ) ) ;
  }


  /**
   * Expands all nodes.
   */
  public final void expandAll ( )
  {
    int i = 0 ;
    while ( i < this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( )
        .getRowCount ( ) )
    {
      this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).expandRow ( i ) ;
      i ++ ;
    }
  }


  /**
   * This method expands the given <code>TreePath</code>.
   * 
   * @param pTreePath The <code>TreePath</code>, which should be expand.
   */
  private final void expandTreePath ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      return ;
    }
    OutlineNode outlineNode = ( OutlineNode ) pTreePath.getLastPathComponent ( ) ;
    for ( int i = 0 ; i < outlineNode.getChildCount ( ) ; i ++ )
    {
      expandTreePath ( pTreePath.pathByAddingChild ( outlineNode
          .getChildAt ( i ) ) ) ;
    }
    this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ).expandPath (
        pTreePath ) ;
  }
}
