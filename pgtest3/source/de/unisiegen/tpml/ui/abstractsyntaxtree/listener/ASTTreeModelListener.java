package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


/**
 * Sets the new Expression in the AbstractSyntaxTree, if a node changed.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTTreeModelListener implements TreeModelListener
{
  /**
   * The AbstractSyntaxTree.
   */
  public AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * The AbstractProofModel.
   */
  public AbstractProofModel abstractProofModel ;


  /**
   * Initializes the ASTTreeModelListener with the given AbstractSyntaxTree and
   * the AbstractProofModel.
   * 
   * @param pAbstractSyntaxTree The AbstractSyntaxTree.
   * @param pAbstractProofModel The AbstractProofModel.
   */
  public ASTTreeModelListener ( AbstractSyntaxTree pAbstractSyntaxTree ,
      AbstractProofModel pAbstractProofModel )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    this.abstractProofModel = pAbstractProofModel ;
  }


  /**
   * Sets the new Expression in the AbstractSyntaxTree, if a node changed.
   * 
   * @param pTreeModelEvent The tree model event.
   * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesChanged ( TreeModelEvent pTreeModelEvent )
  {
    Object source = pTreeModelEvent.getSource ( ) ;
    if ( source instanceof SmallStepProofModel )
    {
      this.abstractSyntaxTree.loadNewExpression ( this.abstractProofModel
          .getRoot ( ).getLastLeaf ( ).getExpression ( ) , "change_smallstep" ) ;
    }
    else if ( source instanceof BigStepProofModel )
    {
      this.abstractSyntaxTree.loadNewExpression ( this.abstractProofModel
          .getRoot ( ).getLastLeaf ( ).getExpression ( ) , "change_bigstep" ) ;
    }
    else if ( source instanceof TypeCheckerProofModel )
    {
      this.abstractSyntaxTree
          .loadNewExpression ( this.abstractProofModel.getRoot ( )
              .getLastLeaf ( ).getExpression ( ) , "change_typechecker" ) ;
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
