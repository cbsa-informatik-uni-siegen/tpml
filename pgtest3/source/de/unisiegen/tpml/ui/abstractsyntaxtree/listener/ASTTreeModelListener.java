package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


public class ASTTreeModelListener implements TreeModelListener
{
  public AbstractSyntaxTree abstractSyntaxTree ;


  public SmallStepProofModel model ;


  public ASTTreeModelListener ( AbstractSyntaxTree pAbstractSyntaxTree ,
      SmallStepProofModel pModel )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    this.model = pModel ;
  }


  public void treeNodesChanged ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    this.abstractSyntaxTree.setExpression ( this.model.getRoot ( )
        .getLastLeaf ( ).getExpression ( ) ) ;
  }


  public void treeNodesInserted ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  public void treeNodesRemoved ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }


  public void treeStructureChanged ( @ SuppressWarnings ( "unused" )
  TreeModelEvent pTreeModelEvent )
  {
    // Do Nothing
  }
}
