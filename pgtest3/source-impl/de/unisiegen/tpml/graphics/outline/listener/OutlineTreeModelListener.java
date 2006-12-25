package de.unisiegen.tpml.graphics.outline.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.ExpressionProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * Sets the new Expression in the Outline, if a node changed.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTreeModelListener implements TreeModelListener
{
  /**
   * The AbstractOutline.
   */
  public Outline outline ;


  /**
   * The ExpressionProofModel.
   */
  public ExpressionProofModel expressionProofModel ;


  /**
   * Initializes the OutlineTreeModelListener with the given Outline and the
   * ExpressionProofModel.
   * 
   * @param pOutline The Outline.
   * @param pExpressionProofModel The ExpressionProofModel.
   */
  public OutlineTreeModelListener ( Outline pOutline ,
      ExpressionProofModel pExpressionProofModel )
  {
    this.outline = pOutline ;
    this.expressionProofModel = pExpressionProofModel ;
  }


  /**
   * Sets the new Expression in the Outline, if a node changed.
   * 
   * @param pTreeModelEvent The tree model event.
   * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesChanged ( TreeModelEvent pTreeModelEvent )
  {
    Object source = pTreeModelEvent.getSource ( ) ;
    if ( source instanceof SmallStepProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_smallstep" ) ; //$NON-NLS-1$
    }
    else if ( source instanceof BigStepProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_bigstep" ) ; //$NON-NLS-1$
    }
    else if ( source instanceof TypeCheckerProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_typechecker" ) ; //$NON-NLS-1$
    }
  }


  /**
   * A node is inserted.
   * 
   * @param pTreeModelEvent The tree model event.
   * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesInserted ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * A node is removed.
   * 
   * @param pTreeModelEvent The tree model event.
   * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesRemoved ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * The structure has changed.
   * 
   * @param pTreeModelEvent The tree model event.
   * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
   */
  public void treeStructureChanged ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
