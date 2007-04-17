package de.unisiegen.tpml.graphics.outline.listener ;


import javax.swing.event.TreeExpansionEvent ;
import javax.swing.event.TreeExpansionListener ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;


/**
 * This class listens for <code>TreeExpansionEvent</code>. It updates the
 * breaks of the visible nodes.
 * 
 * @author Christian Fehler
 */
public final class OutlineTreeExpansionListener implements
    TreeExpansionListener
{
  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline ;


  /**
   * Initializes the {@link OutlineTreeExpansionListener} with the given
   * {@link DefaultOutline}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineTreeExpansionListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline ;
  }


  /**
   * This method is invoked if a node is collapsed.
   * 
   * @param pTreeExpansionEvent The <code>TreeExpansionEvent</code>.
   * @see TreeExpansionListener#treeCollapsed(TreeExpansionEvent)
   */
  public final void treeCollapsed ( TreeExpansionEvent pTreeExpansionEvent )
  {
    if ( pTreeExpansionEvent.getSource ( ).equals (
        this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
    {
      this.defaultOutline.updateBreaks ( ) ;
    }
  }


  /**
   * This method is invoked if a node is expanded.
   * 
   * @param pTreeExpansionEvent The <code>TreeExpansionEvent</code>.
   * @see TreeExpansionListener#treeExpanded(TreeExpansionEvent)
   */
  public final void treeExpanded ( TreeExpansionEvent pTreeExpansionEvent )
  {
    if ( pTreeExpansionEvent.getSource ( ).equals (
        this.defaultOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
    {
      this.defaultOutline.updateBreaks ( ) ;
    }
  }
}
