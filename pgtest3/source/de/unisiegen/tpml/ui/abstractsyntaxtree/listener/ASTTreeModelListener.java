package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTTreeModelListener implements TreeModelListener
{
  /**
   * TODO
   */
  public AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * TODO
   */
  public AbstractProofModel model ;


  /**
   * TODO
   * 
   * @param pAbstractSyntaxTree
   * @param pModel
   */
  public ASTTreeModelListener ( AbstractSyntaxTree pAbstractSyntaxTree ,
      AbstractProofModel pModel )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    this.model = pModel ;
  }


  /**
   * TODO
   * 
   * @param pTreeModelEvent
   * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesChanged ( TreeModelEvent pTreeModelEvent )
  {
    if ( pTreeModelEvent.getSource ( ) instanceof SmallStepProofModel )
    {
      this.abstractSyntaxTree.setExpression ( this.model.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_smallstep" ) ;
    }
    else if ( pTreeModelEvent.getSource ( ) instanceof BigStepProofModel )
    {
      this.abstractSyntaxTree.setExpression ( this.model.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_bigstep" ) ;
    }
    else if ( pTreeModelEvent.getSource ( ) instanceof TypeCheckerProofModel )
    {
      this.abstractSyntaxTree.setExpression ( this.model.getRoot ( )
          .getLastLeaf ( ).getExpression ( ) , "change_typechecker" ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pTreeModelEvent
   * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesInserted ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * TODO
   * 
   * @param pTreeModelEvent
   * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
   */
  public void treeNodesRemoved ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  /**
   * TODO
   * 
   * @param pTreeModelEvent
   * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
   */
  public void treeStructureChanged ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
