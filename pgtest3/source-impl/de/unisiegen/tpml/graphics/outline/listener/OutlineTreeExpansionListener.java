package de.unisiegen.tpml.graphics.outline.listener ;


import javax.swing.event.TreeExpansionEvent ;
import javax.swing.event.TreeExpansionListener ;
import de.unisiegen.tpml.graphics.outline.AbstractOutline ;


/**
 * This class listens for <code>TreeExpansionEvent</code>. It updates the
 * breaks of the visible nodes.
 * 
 * @author Christian Fehler
 */
public class OutlineTreeExpansionListener implements TreeExpansionListener
{
  /**
   * The {@link AbstractOutline}.
   */
  private AbstractOutline abstractOutline ;


  /**
   * Initializes the {@link OutlineTreeExpansionListener} with the given
   * {@link AbstractOutline}.
   * 
   * @param pAbstractOutline
   */
  public OutlineTreeExpansionListener ( AbstractOutline pAbstractOutline )
  {
    this.abstractOutline = pAbstractOutline ;
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
        this.abstractOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
    {
      this.abstractOutline.updateBreaks ( ) ;
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
        this.abstractOutline.getOutlineUI ( ).getJTreeOutline ( ) ) )
    {
      this.abstractOutline.updateBreaks ( ) ;
    }
  }
}
