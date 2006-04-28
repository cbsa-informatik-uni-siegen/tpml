package common;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public abstract class ProofNode implements TreeNode {

  public Expression getExpression() {
    // FIXME
    return null;
  }
  
  public boolean containsSyntacticSugar() {
    // FIXME
    return false;
  }
  
  /**
   * @see javax.swing.tree.TreeNode#getChildAt(int)
   */
  public TreeNode getChildAt(int childIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.swing.tree.TreeNode#getChildCount()
   */
  public int getChildCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see javax.swing.tree.TreeNode#getParent()
   */
  public TreeNode getParent() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
   */
  public int getIndex(TreeNode node) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
  public boolean getAllowsChildren() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see javax.swing.tree.TreeNode#isLeaf()
   */
  public boolean isLeaf() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see javax.swing.tree.TreeNode#children()
   */
  public Enumeration children() {
    // TODO Auto-generated method stub
    return null;
  }

}
