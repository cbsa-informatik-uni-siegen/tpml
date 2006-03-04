package typing;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import smallstep.Abstraction;
import smallstep.And;
import smallstep.Application;
import smallstep.Condition;
import smallstep.Constant;
import smallstep.Expression;
import smallstep.Identifier;
import smallstep.InfixOperation;
import smallstep.Let;
import smallstep.LetRec;
import smallstep.Or;
import smallstep.Recursion;

/**
 * The tree of proof nodes required to prove a
 * type judgement.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class ProofTree implements TreeModel {
  /**
   * Allocates a new proof tree with an inital type
   * <code>environment</code> and the <code>expression</code>
   * whose type should be determined.
   * 
   * @param environment the inital type {@link Environment}.
   * @param expression the {@link smallstep.Expression} whose type
   *                   should be determined.
   */
  ProofTree(Environment environment, Expression expression) {
    // allocate the new judgement
    Judgement judgement = new Judgement(environment, expression, newTypeVariable());
    
    // allocate the root node
    this.root = new ProofNode(judgement);
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener(TreeModelListener l) {
    this.listenerList.add(TreeModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener(TreeModelListener l) {
    this.listenerList.remove(TreeModelListener.class, l);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public ProofNode getChild(Object parent, int index) {
    return ((ProofNode)parent).getChildAt(index);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount(Object parent) {
    return ((ProofNode)parent).getChildCount();
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild(Object parent, Object child) {
    return ((ProofNode)parent).getIndex((ProofNode)child);
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public ProofNode getRoot() {
    return this.root;
  }

  /**
   * {@inheritDoc}
   * 
   * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf(Object node) {
    return (getChildCount(node) == 0);
  }

  /**
   * {@inheritDoc}
   *
   * This method is not implemented for the {@link ProofTree} class,
   * and will simply throw and {@link UnsupportedOperationException}
   * exception when being invoked.
   * 
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    throw new UnsupportedOperationException("valueForPathChanged is not supported for ProofTree");
  }
  
  /**
   * Applies <code>rule</code> for the <code>node</code> in
   * this proof tree and returns the new proof tree that is
   * the result of applying <code>rule</code> at <code>node</code>.
   * 
   * The <code>node</code> must be a valid node for the proof tree,
   * and no type rule must have been applied to <code>node</code>
   * already, that is {@link ProofNode#getRule()} must return
   * <code>null</code> for <code>node</code>.
   * 
   * @param rule the {@link Rule} to apply at <code>node</code>
   * @param node the {@link ProofNode} at which to apply <code>rule</code>.
   * 
   * @return the resulting {@link ProofTree}.
   * 
   * @throws IllegalArgumentException if the <code>node</code> is not
   *                                  valid for the tree or the <code>node</code>
   *                                  is already proven.
   * @throws InvalidRuleException if the <code>rule</code> cannot be applied to
   *                              the <code>node</code>. 
   * @throws UnificationException if the unification failed.
   * @throws UnknownIdentifierException if an identifier could not be found in the
   *                                    type environment of a judgement.  
   *                                  
   * @see ProofNode#getRule()                                  
   */
  public ProofTree apply(Rule rule, ProofNode node) throws InvalidRuleException, UnificationException, UnknownIdentifierException {
    // verify that the node isn't already proven
    if (node.getRule() != null)
      throw new IllegalArgumentException("A type rule was already applied for the proof node");
    
    // verify that the node is valid for the tree
    if (this.root != node && !this.root.containsChild(node))
      throw new IllegalArgumentException("The proof node is not valid for the proof tree");

    // determine the judgement
    Judgement judgement = node.getJudgement();
    
    // determine the judgement attributes
    Type tau = judgement.getType();
    Expression expression = judgement.getExpression();
    Environment environment = judgement.getEnvironment();
    
    // allocate the new node as replacement for the node
    ProofNode newNode = new ProofNode(judgement, rule);

    // start with an empty equation list as base for the unification
    EquationList equations = EquationList.EMPTY_LIST;
    
    // backward apply the rule
    if (expression instanceof Constant && rule == Rule.CONST) {
      // generate a new type equation for the judgement type and the constant type
      Type constType = Type.getTypeForExpression(expression);
      equations = equations.extend(tau, constType);
    }
    else if (expression instanceof Identifier && rule == Rule.ID) {
      // generate a new type equation for the judgement type and the identifier type
      Type idType = environment.get(((Identifier)expression).getName());
      equations = equations.extend(tau, idType);
    }
    else if (expression instanceof Application && rule == Rule.APP) {
      // split into tau1 and tau2 for the application
      Type tau2 = newTypeVariable();
      Type tau1 = new ArrowType(tau2, tau);
      
      // generate new sub nodes
      Application application = (Application)expression;
      newNode.addChild(new Judgement(environment, application.getE1(), tau1));
      newNode.addChild(new Judgement(environment, application.getE2(), tau2));
    }
    else if (expression instanceof Condition && rule == Rule.COND) {
      // generate new sub nodes
      Condition condition = (Condition)expression;
      newNode.addChild(new Judgement(environment, condition.getE0(), PrimitiveType.BOOL));
      newNode.addChild(new Judgement(environment, condition.getE1(), tau));
      newNode.addChild(new Judgement(environment, condition.getE2(), tau));
    }
    else if (expression instanceof Abstraction && rule == Rule.ABSTR) {
      // generate new type variables
      Type tau1 = newTypeVariable();
      Type tau2 = newTypeVariable();
      
      // add type equations for tau and tau1->tau2
      equations = equations.extend(tau, new ArrowType(tau1, tau2));
      
      // generate a new sub node
      Abstraction abstraction = (Abstraction)expression;
      newNode.addChild(new Judgement(environment.extend(abstraction.getId(), tau1), abstraction.getE(), tau2));
    }
    else if (expression instanceof Let && rule == Rule.LET) {
      // generate a new type variable
      Type tau1 = newTypeVariable();
      
      // generate new sub nodes
      Let let = (Let)expression;
      newNode.addChild(new Judgement(environment, let.getE1(), tau1));
      newNode.addChild(new Judgement(environment.extend(let.getId(), tau1), let.getE2(), tau));
    }
    else if (expression instanceof LetRec && rule == Rule.LET_REC) {
      // generate a new type variable
      Type tau1 = newTypeVariable();
      
      // generate new sub nodes
      LetRec letRec = (LetRec)expression;
      newNode.addChild(new Judgement(environment, letRec.getE1(), tau1));
      newNode.addChild(new Judgement(environment.extend(letRec.getId(), tau1), letRec.getE2(), tau));
    }
    else if (expression instanceof Recursion && rule == Rule.REC) {
      // generate a new type variable
      Type tau1 = newTypeVariable();
      
      // add equation tau = tau1
      equations = equations.extend(tau, tau1);
      
      // generate new sub node
      Recursion recursion = (Recursion)expression;
      newNode.addChild(new Judgement(environment.extend(recursion.getId(), tau1), recursion.getE(), tau1));
    }
    else if (expression instanceof InfixOperation && rule == Rule.INFIX) {
      // generate two new type variables
      Type tau1 = newTypeVariable();
      Type tau2 = newTypeVariable();
      
      // determine the type of the operator
      InfixOperation operation = (InfixOperation)expression;
      Type opType = Type.getTypeForExpression(operation.getOp());
      
      // add equation tau(op) = tau1 -> tau2 -> tau
      equations = equations.extend(opType, new ArrowType(tau1, new ArrowType(tau2, tau)));
      
      // generate new sub nodes
      newNode.addChild(new Judgement(environment, operation.getE1(), tau1));
      newNode.addChild(new Judgement(environment, operation.getE2(), tau2));
    }
    else if (expression instanceof And && rule == Rule.AND) {
      // add equation tau = bool
      equations = equations.extend(tau, PrimitiveType.BOOL);
      
      // generate new sub nodes
      And and = (And)expression;
      newNode.addChild(new Judgement(environment, and.getE0(), PrimitiveType.BOOL));
      newNode.addChild(new Judgement(environment, and.getE1(), PrimitiveType.BOOL));
    }
    else if (expression instanceof Or && rule == Rule.OR) {
      // add equation tau = bool
      equations = equations.extend(tau, PrimitiveType.BOOL);
      
      // generate new sub nodes
      Or or = (Or)expression;
      newNode.addChild(new Judgement(environment, or.getE0(), PrimitiveType.BOOL));
      newNode.addChild(new Judgement(environment, or.getE1(), PrimitiveType.BOOL));
    }
    else {
      // well, not possible then
      throw new InvalidRuleException(node, rule);
    }
    
    // determine the unificator
    Substitution substitution = equations.unify();
    
    // allocate a root item for the new tree
    ProofNode newRoot = this.root.cloneSubstituteAndReplace(substitution, node, newNode);
    
    // allocate the new tree
    return new ProofTree(newRoot);
  }
  
  /**
   * Returns the {@link Judgement} for the <code>node</code> in this
   * proof tree.
   * 
   * @param node a {@link ProofNode} in this proof tree.
   * 
   * @return the {@link Judgement} for the <code>node</code>.
   */
  public Judgement getJudgementForNode(Object node) {
    return ((ProofNode)node).getJudgement();
  }
  
  /**
   * Returns {@link Rule} for the <code>node</code> in this
   * proof tree.
   * 
   * @param node a {@link ProofNode} in this proof tree.
   * 
   * @return the {@link Rule} for the <code>node</code>.
   */
  public Rule getRuleForNode(Object node) {
    return ((ProofNode)node).getRule();
  }

  // allocates a new tree with the given root 
  private ProofTree(ProofNode root) {
    this.root = root;
  }
  
  // returns a new type variable that isn't currently used
  private TypeVariable newTypeVariable() {
    for (int i = 0;; ++i) {
      String name = "\u03B1" + i;
      if (this.root == null || !this.root.containsTypeVariable(name))
        return new TypeVariable(name);
    }
  }
  
  // member attributes
  private EventListenerList listenerList = new EventListenerList();
  private ProofNode root;
}
