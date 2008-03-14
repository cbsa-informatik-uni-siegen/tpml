package de.unisiegen.tpml.graphics.outline.listener;


import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode;


/**
 * This class listens for <code>TreeSelectionEvent</code>. It updates the
 * caption of the selected and higher nodes.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public final class OutlineTreeSelectionListener implements
    TreeSelectionListener
{

  /**
   * The {@link DefaultOutline}.
   */
  private DefaultOutline defaultOutline;


  /**
   * Initializes the {@link OutlineTreeSelectionListener} with the given
   * {@link DefaultOutline}.
   * 
   * @param pDefaultOutline The {@link DefaultOutline}.
   */
  public OutlineTreeSelectionListener ( DefaultOutline pDefaultOutline )
  {
    this.defaultOutline = pDefaultOutline;
  }


  /**
   * This method is invoked if a node value has changed.
   * 
   * @param pTreeSelectionEvent The <code>TreeSelectionEvent</code>.
   * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
   */
  public final void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    // Outline Tree
    if ( pTreeSelectionEvent.getSource ().equals (
        this.defaultOutline.getUI ().getJTreeOutline ().getSelectionModel () ) )
    {
      TreePath newTreePath = pTreeSelectionEvent.getPath ();
      if ( newTreePath == null )
      {
        return;
      }
      this.defaultOutline.update ( newTreePath );
      TreePath oldTreePath = pTreeSelectionEvent.getOldLeadSelectionPath ();
      if ( oldTreePath != null )
      {
        Object [] objects = oldTreePath.getPath ();
        for ( int i = 0 ; i < objects.length ; i++ )
        {
          this.defaultOutline.getUI ().getTreeModel ().nodeChanged (
              ( OutlineNode ) objects [ i ] );
        }
      }
    }
  }
}
