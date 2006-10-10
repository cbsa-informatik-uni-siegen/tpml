package de.unisiegen.tpml.core;

import java.util.EventListener;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.util.beans.Bean;

/**
 * The interface defines a suitable data model for proving various properties about an expression. It is based on
 * the generic {@link javax.swing.tree.TreeModel}.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.AbstractProofModel
 * @see de.unisiegen.tpml.core.ProofNode
 */
public interface ProofModel extends Bean, TreeModel {
  //
  // Accessors
  //
  
  /**
   * Returns <code>true</code> if the proof is finished successfully, otherwise if either the
   * proof is not yet completed or the proof got stuck, <code>false</code> will be returned.
   * 
   * @return <code>true</code> if the proof is finished.
   */
  public boolean isFinished();
  
  /**
   * Returns the {@link Language} for which this proof model was constructed.
   * 
   * @return the {@link Language} for the proof model.
   * 
   * @see Language
   * @see ProofRuleSet#getLanguage()
   */
  public Language getLanguage();
  
  /**
   * Returns the list of {@link ProofRule}s that should be displayed as possible rules
   * in the user interface. This is usually a subset of the list of all available rules
   * for the given proof.
   * 
   * The user interface should query the rules everytime the user opens the menu/list to
   * apply rules, as the list of rules may have changed after an operation.
   * 
   * @return the {@link ProofRule}s to be displayed in the user interface.
   * 
   * @see ProofRuleSet#getRules()
   */
  public ProofRule[] getRules();
  
  
  
  //
  // Undo/Redo
  //
  
  /**
   * Returns <code>true</code> if the model has
   * recorded undo steps that can be redone
   * using the {@link #redo()} operation.
   * 
   * @return <code>true</code> if {@link #redo()}
   *         is possible, <code>false</code> otherwise.
   *
   * @see #isUndoable()
   * @see #redo()         
   */
  public boolean isRedoable();
  
  /**
   * Returns <code>true</code> if the model has
   * recorded proof steps that can be undone using
   * the {@link #undo()} operation.
   * 
   * @return <code>true</code> if {@link #undo()}
   *         is possible, <code>false</code> otherwise.
   *
   * @see #isRedoable()
   * @see #undo()         
   */
  public boolean isUndoable();

  /**
   * Redoes the previously undo change to the proof model. Use
   * {@link #isRedoable()} to test whether there's a change to
   * this model that can be redone.
   * 
   * @throws CannotRedoException if the redo history is empty
   *                             or the change cannot be redone
   *                             for some other reason.
   * 
   * @see #undo()
   */
  public void redo() throws CannotRedoException;
  
  /**
   * Undoes the previous change to the proof model. Use
   * {@link #isUndoable()} to test whether there's a change
   * to this model that can be undone.
   * 
   * @throws CannotUndoException if the undo history is empty
   *                             or the change cannot be undone
   *                             for some other reason.
   * 
   * @see #redo()
   */
  public void undo() throws CannotUndoException;
  
  
  
  //
  // Actions
  //
  
  /**
   * Guesses the next proof step for the specified <code>node</code>.
   * 
   * The <code>node</code> must not be already proven (see the
   * {@link ProofNode#isProven()} method for details), otherwise
   * an {@link IllegalStateException} is thrown.
   * 
   * @param node the {@link ProofNode} for which the next proof step should
   *             be guessed.
   *             
   * @throws IllegalArgumentException if the <code>node</code> is invalid for this model.             
   * @throws IllegalStateException if for some reason <code>node</code> cannot be proven.
   * @throws NullPointerException if <code>node</code> is <code>null</code>.
   * @throws ProofGuessException if the next proof step could not be guessed.
   *
   * @see #prove(ProofRule, ProofNode)
   */
  public void guess(ProofNode node) throws ProofGuessException;
  
  /**
   * Applies the given proof <code>rule</code> to the specified
   * proof <code>node</code>.
   * 
   * The <code>node</code> must not be already proven (see the
   * {@link ProofNode#isProven()} method for details), otherwise
   * an {@link IllegalStateException} is thrown.
   * 
   * @param rule the {@link ProofRule} to apply.
   * @param node the {@link ProofNode} to which the <code>rule</code>
   *             should be applied.
   *             
   * @throw IllegalArgumentException if either the <code>rule</code> is
   *                                 not valid for the model, or the
   *                                 <code>node</code> is invalid for
   *                                 the model.
   * @throws IllegalStateException if for some reason <code>node</code> cannot
   *                               be proven.
   * @throw ProofRuleException if the <code>rule</code> cannot be applied
   *                           to the <code>node</code>.
   *
   * @see #guess(ProofNode)
   */
  public void prove(ProofRule rule, ProofNode node) throws ProofRuleException;
  
  /**
   * Returns <code>true</code> if the expression for the <code>node</code> contains syntactic sugar.
   * If <code>recursive</code> is <code>true</code> and the expression for the <code>node</code> is
   * not syntactic sugar, its sub expressions will also be checked.
   * 
   * @param node the proof node whose expression should be checked for syntactic sugar.
   * 
   * @return <code>true</code> if the expression of the <code>node</code> contains
   *         syntactic sugar according to the language for this model.
   *
   * @throws IllegalArgumentException if the <code>node</code> is invalid for this proof model.
   * @throws NullPointerException if the <code>node</code> is <code>null</code>.
   * 
   * @see #translateToCoreSyntax(ProofNode, boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#containsSyntacticSugar(Expression, boolean)
   */
  public boolean containsSyntacticSugar(ProofNode node, boolean recursive);
  
  /**
   * Translates the expression for the <code>node</code> to core syntax according to
   * the language for this model. If <code>recursive</code> is <code>true</code>,
   * all sub expressions will also be translated to core syntax, otherwise only the
   * outermost expression will be translated.
   * 
   * @param node the proof node whose expression should be translated to core syntax.
   * @param recursive whether to translate the expression recursively.
   * 
   * @throws IllegalArgumentException if the <code>node</code> is invalid for this proof model,
   *                                  or the <code>node</code>'s expression does not contain
   *                                  syntactic sugar.
   * @throws IllegalStateException if any steps were performed on the <code>node</code> already.
   * @throws NullPointerException if the <code>node</code> is <code>null</code>.                               
   * 
   * @see #containsSyntacticSugar(ProofNode, boolean)
   * @see de.unisiegen.tpml.core.languages.LanguageTranslator#translateToCoreSyntax(Expression, boolean)
   */
  public void translateToCoreSyntax(ProofNode node, boolean recursive);
  
  
  
  //
  // Tree queries
  //

  /**
   * Returns the root of the tree. Returns <code>null</code>
   * only if the tree has no nodes.
   * 
   * @return the root of the proof tree.
   * 
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public ProofNode getRoot();

  /**
   * Returns the child of <code>parent</code> at index <code>index</code> in
   * the <code>parent</code>'s child array. <code>parent</code> must be a node
   * previously obtained from this data source. This should not return <code>null</code>
   * if <code>index</code> is a valid index for <code>parent</code> (that is
   * <code>index &gt;= 0 && index &lt; getChildCount(parent)</code>).
   * 
   * @param parent a node in the tree, obtained from this data source.
   * @param index the child index of a child node of <code>parent</code>.
   *  
   * @return the child of <code>parent</code> at index <code>index</code>.
   * 
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public ProofNode getChild(Object parent, int index);

  /**
   * Builds the parents of node up to and including the root node,
   * where the original node is the last element in the returned
   * array. The length of the returned array gives the node's depth
   * in the tree.
   * 
   * @param aNode the {@link TreeNode} to get the path for.
   * 
   * @return the parents of <code>aNode</code> up to and including
   *         the root node.
   */
  public TreeNode[] getPathToRoot(TreeNode aNode);

  
  
  //
  // Events
  //
  
  /**
   * Returns an array of all the tree model listeners registered on this
   * model.
   * 
   * @return all of this model's {@link TreeModelListener}'s or an empty
   *         array if no tree model listeners are currently registered.
   *         
   * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)         
   */
  public TreeModelListener[] getTreeModelListeners();

  /**
   * Returns an array of all the objects currently registered as <code><em>Foo</em>Listener</code>s
   * upon this model. <code><em>Foo</em>Listener</code>s are registered using the
   * <code>add<em>Foo</em>Listener</code> method.
   *
   * You can specify the <code>listenerType</code> argument with a class literal, such as
   * <code><em>Foo</em>Listener.class</code>. For example, you can query a <code>ProofModel</code>
   * <code>m</code> for its tree model listeners with the following code:
   *
   * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
   *
   * If no such listeners exist, this method returns an empty array.
   *
   * @param listenerType the type of listeners requested; this parameter should specify an interface
   *                     that descends from {@link EventListener}.
   *                     
   * @return an array of all objects registered as <code><em>Foo</em>Listener</code>s on this prover,
   *         or an empty array if no such listeners have been added.
   *         
   * @throws ClassCastException if <code>listenerType</code> doesn't specify a class or interface that
   *                            implements {@link EventListener}.
   *
   * @see #getTreeModelListeners()
   */
  public <T extends EventListener> T[] getListeners(Class<T> listenerType); 
}
