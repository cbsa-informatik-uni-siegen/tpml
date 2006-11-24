package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import javax.swing.event.TreeModelEvent ;
import javax.swing.event.TreeModelListener ;
import de.unisiegen.tpml.core.AbstractProofModel ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;


public class ASTTreeModelListener implements TreeModelListener
{
  public AbstractSyntaxTree abstractSyntaxTree ;


  public AbstractProofModel model ;


  public ASTTreeModelListener ( AbstractSyntaxTree pAbstractSyntaxTree ,
      AbstractProofModel pModel )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
    this.model = pModel ;
  }


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
