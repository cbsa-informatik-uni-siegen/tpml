package de.unisiegen.tpml.core;


import de.unisiegen.tpml.core.expressions.Expression;


/**
 * Abstract base class for all classes implementing the
 * {@link de.unisiegen.tpml.core.ExpressionProofModel} interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev$
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.ExpressionProofModel
 */
public abstract class AbstractExpressionProofModel extends AbstractProofModel
    implements ExpressionProofModel
{

  //
  // Constructor (protected)
  //
  /**
   * Allocates a new <code>AbstractExpressionProofModel</code> using the given
   * <code>root</code> item.
   * 
   * @param pRoot the new root item.
   * @param pRuleSet the set of proof rules.
   * @throws NullPointerException if <code>root</code> or <code>ruleSet</code>
   *           is <code>null</code>.
   */
  protected AbstractExpressionProofModel ( AbstractExpressionProofNode pRoot,
      AbstractProofRuleSet pRuleSet )
  {
    super ( pRoot, pRuleSet );
  }


  //
  // Actions
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofModel#containsSyntacticSugar(de.unisiegen.tpml.core.ExpressionProofNode,
   *      boolean)
   */
  public boolean containsSyntacticSugar ( ExpressionProofNode node,
      boolean recursive )
  {
    if ( node == null )
    {
      throw new NullPointerException ( "node is null" ); //$NON-NLS-1$
    }
    if ( !this.root.isNodeRelated ( node ) )
    {
      throw new IllegalArgumentException ( "node is invalid" ); //$NON-NLS-1$
    }
    if ( this.translator == null )
    {
      this.translator = this.ruleSet.getLanguage ().newTranslator ();
    }
    return this.translator.containsSyntacticSugar ( node.getExpression (),
        recursive );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.ExpressionProofModel#translateToCoreSyntax(de.unisiegen.tpml.core.ExpressionProofNode,
   *      boolean)
   */
  public void translateToCoreSyntax ( ExpressionProofNode node,
      boolean recursive )
  {
    // verify that the node actually contains syntactic sugar
    if ( !containsSyntacticSugar ( node, recursive ) )
    {
      throw new IllegalArgumentException (
          "node does not contain syntactic sugar" ); //$NON-NLS-1$
    }
    // verify that no actions were performed on the node
    if ( node.getSteps ().length > 0 )
    {
      throw new IllegalStateException ( "steps have been performed on node" ); //$NON-NLS-1$
    }
    // cast the proof node to the appropriate type
    final AbstractExpressionProofNode abstractNode = ( AbstractExpressionProofNode ) node;
    // translate the expression to core syntax
    final Expression expression = node.getExpression ();
    final Expression coreExpression = this.translator.translateToCoreSyntax (
        expression, recursive ).clone ();
    // create the undoable edit
    UndoableTreeEdit edit = new UndoableTreeEdit ()
    {

      public void redo ()
      {
        // translate the expression of the node to core syntax
        abstractNode.setExpression ( coreExpression );
        nodeChanged ( abstractNode );
      }


      public void undo ()
      {
        // restore the previous expression
        abstractNode.setExpression ( expression );
        nodeChanged ( abstractNode );
      }
    };
    // perform the redo operation
    edit.redo ();
    // and record the edit
    addUndoableTreeEdit ( edit );
  }


  //
  // Tree queries
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#getRoot()
   */
  @Override
  public AbstractExpressionProofNode getRoot ()
  {
    return ( AbstractExpressionProofNode ) super.getRoot ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.AbstractProofModel#getChild(java.lang.Object,
   *      int)
   */
  @Override
  public AbstractExpressionProofNode getChild ( Object parent, int index )
  {
    return ( AbstractExpressionProofNode ) super.getChild ( parent, index );
  }
}
