package de.unisiegen.tpml.graphics.outline.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.ExpressionProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * Sets the new {@link Expression} in the {@link Outline}, if a node changed.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class OutlineTreeModelListener implements TreeModelListener
{
  /**
   * The unused <code>String</code> for the <code>SuppressWarnings</code>.
   */
  private static final String UNUSED = "unused" ; //$NON-NLS-1$


  /**
   * The {@link Outline}.
   */
  public Outline outline ;


  /**
   * The {@link ExpressionProofModel}.
   */
  public ExpressionProofModel expressionProofModel ;


  /**
   * Initializes the {@link OutlineTreeModelListener} with the given
   * {@link Outline} and the {@link ExpressionProofModel}.
   * 
   * @param pOutline The {@link Outline}.
   * @param pExpressionProofModel The {@link ExpressionProofModel}.
   */
  public OutlineTreeModelListener ( Outline pOutline ,
      ExpressionProofModel pExpressionProofModel )
  {
    this.outline = pOutline ;
    this.expressionProofModel = pExpressionProofModel ;
  }


  /**
   * Sets the new {@link Expression} in the {@link Outline}, if a node changed.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
   */
  public final void treeNodesChanged ( TreeModelEvent pTreeModelEvent )
  {
    Object source = pTreeModelEvent.getSource ( ) ;
    if ( source instanceof SmallStepProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , Outline.CHANGE_SMALLSTEP ) ;
    }
    else if ( source instanceof BigStepProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , Outline.CHANGE_BIGSTEP ) ;
    }
    else if ( source instanceof TypeCheckerProofModel )
    {
      this.outline.loadExpression ( this.expressionProofModel.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , Outline.CHANGE_TYPECHECKER ) ;
    }
  }


  /**
   * A node is inserted.
   * 
   * @param pTreeModelEvent The <code>TreeModelEvent</code>.
   * @see TreeModelListener#treeNodesInserted(TreeModelEvent)
   */
  public final void treeNodesInserted ( @ SuppressWarnings ( UNUSED )
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
  public final void treeNodesRemoved ( @ SuppressWarnings ( UNUSED )
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
  public final void treeStructureChanged ( @ SuppressWarnings ( UNUSED )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
