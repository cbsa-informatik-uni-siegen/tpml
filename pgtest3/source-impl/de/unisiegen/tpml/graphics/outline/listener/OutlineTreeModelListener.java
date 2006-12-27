package de.unisiegen.tpml.graphics.outline.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.ExpressionProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * Sets the new <code>Expression</code> in the <code>Outline</code>, if a
 * node changed.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTreeModelListener implements TreeModelListener
{
  /**
   * The <code>Outline</code>.
   */
  public Outline outline ;


  /**
   * The <code>ExpressionProofModel</code>.
   */
  public ExpressionProofModel expressionProofModel ;


  /**
   * Initializes the <code>OutlineTreeModelListener</code> with the given
   * <code>Outline</code> and the <code>ExpressionProofModel</code>.
   * 
   * @param pOutline The <code>Outline</code>.
   * @param pExpressionProofModel The <code>ExpressionProofModel</code>.
   */
  public OutlineTreeModelListener ( Outline pOutline ,
      ExpressionProofModel pExpressionProofModel )
  {
    this.outline = pOutline ;
    this.expressionProofModel = pExpressionProofModel ;
  }


  /**
   * Sets the new <code>Expression</code> in the <code>Outline</code>, if a
   * node changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
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
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesInserted(TreeModelEvent)
   */
  public void treeNodesInserted ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * A node is removed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesRemoved(TreeModelEvent)
   */
  public void treeNodesRemoved ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * The structure has changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
   */
  public void treeStructureChanged ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
