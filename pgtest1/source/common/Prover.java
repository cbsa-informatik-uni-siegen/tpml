package common;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class Prover implements TreeModel {

  public boolean isRedoable() {
    // FIXME
    return false;
  }
  
  public boolean isUndoable() {
    // FIXME
    return false;
  }

  public void redo() {
    // FIXME
  }
  
  public void undo() {
    // FIXME
  }
  
  public void translateToCoreSyntax(ProofNode node) {
    // FIXME
  }

  /**
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public ProofNode getRoot() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public ProofNode getChild(Object parent, int index) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount(Object parent) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf(Object node) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild(Object parent, Object child) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener(TreeModelListener l) {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener(TreeModelListener l) {
    // TODO Auto-generated method stub

  }

}
