package de.unisiegen.tpml.graphics.typechecker;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.graphics.AbstractProofComponent;
import de.unisiegen.tpml.graphics.renderer.EnvironmentRenderer;
import de.unisiegen.tpml.graphics.renderer.PrettyStringRenderer;
import de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer;
import de.unisiegen.tpml.graphics.tree.TreeNodeLayout;


/**
 * Implementation of the graphics repsentation of the TypeChecker<br>
 * <br>
 * The following image shows a usual look of a part of an TypeChckerComponent.
 * It contains a few nodes of the origin expression:
 * <code>let rec f = lambda x. if x = 0 then 1 else x * f (x-1) in f 3</code><br>
 * <img src="../../../../../../images/typechecker.png" /><br>
 * <br>
 * The entire placing of the nodes is done within the method {@link #relayout()}
 * but actualy the layouting is passed over to the
 * {@link de.unisiegen.tpml.graphics.tree.TreeNodeLayout} to place the nodes.
 * <br>
 * <br>
 * The lines and arrows of the tree are rendered using the
 * {@link de.unisiegen.tpml.graphics.renderer.TreeArrowRenderer}, so all nodes
 * within the tree implement the
 * {@link de.unisiegen.tpml.graphics.tree.TreeNodeComponent} interface.<br>
 * <br>
 * The nodes are not stored directly in the <i>TypeCheckerComponent</i>, they
 * are stored using the <i>Userobject</i> provided by the
 * {@link de.unisiegen.tpml.core.ProofNode}.<br>
 * Everytime the content of the tree changes ({@link #treeContentChanged()} is
 * called) the {@link #checkForUserObject(TypeCheckerProofNode)}-method is
 * called. This causes a recursive traversing of the entire tree to check if
 * every node has its corresponding
 * {@link de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent}.<br>
 * <br>
 * When nodes get removed only the userobject of that nodes needs to get
 * release.<br>
 * When nodes get inserted, the first of them is stored in the {@link #jumpNode}
 * so the next time the component gets layouted the {@link #jumpToNodeVisible()}-method
 * is called and the scrollview of the
 * {@link de.unisiegen.tpml.graphics.bigstep.BigStepView} scrolls to a place the
 * stored node gets visible.
 * 
 * @author marcell
 * @author michael
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerView
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerNodeComponent
 * @see de.unisiegen.tpml.graphics.typechecker.TypeCheckerEnterType
 */
public class TypeCheckerComponent extends AbstractProofComponent implements
    Scrollable, Cloneable
{

  /**
   * 
   */
  private static final long serialVersionUID = 5184580585827680414L;


  /**
   * Index that will be incremented during the layouting.
   */
  private int index;


  /**
   * TreeNodeLayout will be used to do the layouting of the tree.
   */
  private TreeNodeLayout treeNodeLayout;


  /**
   * The ProofNode where the scrollView will scroll to when an new node will be
   * inserted.
   */
  private ProofNode jumpNode;


  /**
   * the advaced state
   */
  private boolean advanced;


  /**
   * the constructor
   * 
   * @param model the model
   * @param pAdvanced the adcanced mode
   */
  public TypeCheckerComponent ( TypeCheckerProofModel model, boolean pAdvanced )
  {
    super ( model );

    this.availableHeight = Integer.MAX_VALUE;
    this.advanced = pAdvanced;
    this.treeNodeLayout = new TreeNodeLayout ( 10 );
    this.jumpNode = null;

    setLayout ( null );

    // initialy the tree content has changed
    treeContentChanged ();
  }


  /**
   * sets the spacing of the treeNodeLayout
   * 
   * @param spacing the spacing in pixels
   */
  public void setSpacing ( int spacing )
  {
    this.treeNodeLayout.setSpacing ( spacing );
  }


  /**
   * Guesses the next unprooven node in the tree.
   * 
   * @throws IllegalStateException
   * @throws ProofGuessException
   */
  public void guess () throws IllegalStateException, ProofGuessException
  {
    Enumeration < ProofNode > enumeration = this.proofModel.getRoot ()
        .postorderEnumeration ();
    while ( enumeration.hasMoreElements () )
    {
      ProofNode node = enumeration.nextElement ();
      if ( !node.isProven () )
      {
        this.proofModel.guess ( node );
        return;
      }
    }
    throw new IllegalStateException ( "Unable to find next node" );
  }


  /**
   * Recalculates the layout
   */
  @Override
  protected void relayout ()
  {
    if ( this.currentlyLayouting )
    {
      return;
    }

    this.currentlyLayouting = true;

    SwingUtilities.invokeLater ( new Runnable ()
    {

      @SuppressWarnings ( "synthetic-access" )
      public void run ()
      {
        doRelayout ();
      }
    } );
  }


  protected void doRelayout ()
  {
    TypeCheckerProofNode rootNode = ( TypeCheckerProofNode ) TypeCheckerComponent.this.proofModel
        .getRoot ();

    Point rightBottomPos = TypeCheckerComponent.this.getTreeNodeLayout ()
        .placeNodes ( rootNode, 20, 20,
            TypeCheckerComponent.this.availableWidth,
            TypeCheckerComponent.this.availableHeight );

    // TODO wuzu denn das?
    // lets add some border to the space

    // rightBottomPos.x += 20;
    // rightBottomPos.y += 20;

    Dimension size = new Dimension ( rightBottomPos.x, rightBottomPos.y );

    // set all the sizes needed by the component
    setMaximumSize ( size );
    setMinimumSize ( size );
    setPreferredSize ( size );
    setSize ( size );

    TypeCheckerComponent.this.currentlyLayouting = false;
    TypeCheckerComponent.this.jumpToNodeVisible ();

  }


  /**
   * Causes every {@link PrettyStringRenderer} and {@link EnvironmentRenderer}
   * to recalculate thier layout.
   */
  @Override
  protected void resetLayout ()
  {
    // apply the reset on the root node
    resetUserObject ( ( TypeCheckerProofNode ) this.proofModel.getRoot () );
  }


  /**
   * Checks the entire tree if every {@link TypeCheckerProofNode} contains an
   * Userobject.
   * 
   * @param node
   */
  private void checkForUserObject ( TypeCheckerProofNode node )
  {

    if ( node == null )
    {
      return;
    }
    TypeCheckerNodeComponent nodeComponent = ( TypeCheckerNodeComponent ) node
        .getUserObject ();
    if ( nodeComponent == null )
    {
      // if the node has no userobject it may be new in the
      // tree, so a new TypeCheckerNodeComponent will be created
      // and added to the TypeCheckerProofNode
      nodeComponent = new TypeCheckerNodeComponent ( node,
          ( TypeCheckerProofModel ) this.proofModel, this.translator,
          this.advanced );
      node.setUserObject ( nodeComponent );

      // the newly created nodeComponent is a gui-element so
      // it needs to get added to the gui
      add ( nodeComponent );

      // when the node changes the gui needs to get updated
      nodeComponent.addTypeCheckerNodeListener ( new TypeCheckerNodeListener ()
      {

        public void nodeChanged ( TypeCheckerNodeComponent pNode )
        {
          TypeCheckerComponent.this.relayout ();
        }


        public void requestTypeEnter ( TypeCheckerNodeComponent pNode )
        {
          // empty

        }


        public void requestJumpToNode ( ProofNode pNode )
        {
          TypeCheckerComponent.this.setJumpNode ( pNode );
        }
      } );
    }

    // set the index value for the node
    nodeComponent.setIndex ( this.index );
    ++this.index;

    // the typechecker is a real tree, so its needed to proceed with all
    // children of the node
    for ( int i = 0 ; i < node.getChildCount () ; i++ )
    {
      TypeCheckerProofNode pNode = node.getChildAt ( i );

      checkForUserObject ( pNode );
    }

  }


  /**
   * Causes all userobject from all nodes to reset the layout.<br>
   * <br>
   * Resetting means that every {@link PrettyStringRenderer} and
   * {@link EnvironmentRenderer} recalculates their needed font sizes.
   * 
   * @param node the TypeCheckerProofNode
   */
  private void resetUserObject ( TypeCheckerProofNode node )
  {
    if ( node == null )
    {
      return;
    }

    TypeCheckerNodeComponent nodeComponent = ( TypeCheckerNodeComponent ) node
        .getUserObject ();
    if ( nodeComponent == null )
    {
      return;
    }

    nodeComponent.reset ();

    for ( int i = 0 ; i < node.getChildCount () ; i++ )
    {
      TypeCheckerProofNode pNode = node.getChildAt ( i );

      resetUserObject ( pNode );
    }
  }


  /**
   * Gets called when the content of the tree has changed. If nodes are newly
   * inserted or nodes got removed.<br>
   * <br>
   * The tree will be checked for userobject and than get relayouted.
   */
  @Override
  protected void treeContentChanged ()
  {

    TypeCheckerProofNode rootNode = ( TypeCheckerProofNode ) this.proofModel
        .getRoot ();

    // initiate the index
    this.index = 1;
    checkForUserObject ( rootNode );
    relayout ();

  }


  /**
   * Saves the first of the newly inserted nodes for the node to jump to later.
   */
  @Override
  protected void nodesInserted ( TreeModelEvent event )
  {
    if ( this.jumpNode != null )
    {
      return;
    }

    Object [] children = event.getChildren ();

    if ( children != null )
    {

      // only problem with this could occure when
      // then children[0] element isn't the topmost element
      // in the tree that has been inserted. at this condition
      // that behaviour is undefined
      this.jumpNode = ( ProofNode ) children [ 0 ];
    }
    else
    {
      this.jumpNode = null;
    }
  }


  /**
   * Delegates a {@link TypeCheckerNodeComponent#changeNode()} to every nodes
   * that have changed.
   */
  @Override
  protected void nodesChanged ( TreeModelEvent event )
  {
    Object [] children = event.getChildren ();
    if ( children == null )
    {

      // if the children are null and the path only contains one element
      // this element is the root node.
      if ( event.getPath ().length == 1 )
      {
        TypeCheckerProofNode proofNode = ( TypeCheckerProofNode ) event
            .getPath () [ 0 ];
        TypeCheckerNodeComponent nodeComponent = ( TypeCheckerNodeComponent ) proofNode
            .getUserObject ();
        if ( nodeComponent != null )
        {
          nodeComponent.changeNode ();
        }

      }
      return;
    }
    for ( int i = 0 ; i < children.length ; i++ )
    {
      if ( children [ i ] instanceof ProofNode )
      {
        TypeCheckerProofNode proofNode = ( TypeCheckerProofNode ) children [ i ];

        TypeCheckerNodeComponent nodeComponent = ( TypeCheckerNodeComponent ) proofNode
            .getUserObject ();
        if ( nodeComponent != null )
        {
          nodeComponent.changeNode ();
        }
      }
    }

  }


  /**
   * Removes the userobject from the {@link TypeCheckerProofNode}.
   */
  @Override
  protected void nodesRemoved ( TreeModelEvent event )
  {
    Object [] children = event.getChildren ();
    for ( int i = 0 ; i < children.length ; i++ )
    {
      if ( children [ i ] instanceof ProofNode )
      {
        TypeCheckerProofNode proofNode = ( TypeCheckerProofNode ) children [ i ];

        TypeCheckerNodeComponent nodeComponent = ( TypeCheckerNodeComponent ) proofNode
            .getUserObject ();
        if ( nodeComponent != null )
        {
          remove ( nodeComponent );
          proofNode.setUserObject ( null );
        }
      }
    }
  }


  /**
   * Just renders the tree using the
   * {@link TreeArrowRenderer#renderArrows(ProofNode, int, Graphics)}-Method
   * 
   * @param gc
   */
  @Override
  protected void paintComponent ( Graphics gc )
  {
    gc.setColor ( Color.WHITE );
    gc.fillRect ( 0, 0, getWidth (), getHeight () );

    gc.setColor ( Color.BLACK );
    ProofNode rootNode = this.proofModel.getRoot ();
    TreeArrowRenderer.renderArrows ( rootNode, this.treeNodeLayout
        .getSpacing (), gc );

  }


  /**
   * Scroll the Viewport to the rect of the previously saved node.
   */
  void jumpToNodeVisible ()
  {
    if ( this.jumpNode == null )
    {
      return;
    }

    // get the Component nodes to evaluate the positions
    // on the viewport
    TypeCheckerNodeComponent node = ( TypeCheckerNodeComponent ) this.jumpNode
        .getUserObject ();
    if ( node == null )
    {
      return;
    }

    // get the visible rect to ensure the x coordinate is in the
    // visible area. only vertical scolling is requested
    Rectangle visibleRect = this.getVisibleRect ();

    Rectangle rect = new Rectangle ();
    rect.x = visibleRect.x;
    rect.y = node.getY ();
    rect.width = 1;
    rect.height = node.getHeight ();

    this.scrollRectToVisible ( rect );

    this.jumpNode = null;
  }


  /*
   * Implementation of the Scrollable interface
   */
  public Dimension getPreferredScrollableViewportSize ()
  {
    return getPreferredSize ();
  }


  public int getScrollableBlockIncrement ( Rectangle visibleRect,
      int orientation, int direction )
  {
    // XXX: Dynamic block increment
    return 25;
  }


  public boolean getScrollableTracksViewportHeight ()
  {
    return false;
  }


  public boolean getScrollableTracksViewportWidth ()
  {
    return false;
  }


  public int getScrollableUnitIncrement ( Rectangle visibleRect,
      int orientation, int direction )
  {
    // XXX: Dynamic unit increment
    return 10;
  }


  public TypeCheckerComponent clone ()
  {
    try
    {
      return ( TypeCheckerComponent ) super.clone ();
    }
    catch ( CloneNotSupportedException e )
    {
      return null;
    }
  }


  /**
   * sets the advance mode and updates all active nodes!
   * 
   * @param advancedP the advanced mode
   */
  public void setAdvanced ( boolean advancedP )
  {
    this.advanced = advancedP;

    // update all active nodes
    Enumeration < ProofNode > enumeration = this.proofModel.getRoot ()
        .postorderEnumeration ();
    while ( enumeration.hasMoreElements () )
    {
      // tell the component belonging to this node, that we have a new advanced
      // state
      TypeCheckerProofNode node = ( TypeCheckerProofNode ) enumeration
          .nextElement ();
      TypeCheckerNodeComponent component = ( TypeCheckerNodeComponent ) node
          .getUserObject ();
      component.setAdvanced ( this.advanced );
    }
  }


  /**
   * @return the treeNodeLayout
   */
  public TreeNodeLayout getTreeNodeLayout ()
  {
    return this.treeNodeLayout;
  }


  /**
   * @param pJumpNode the jumpNode to set
   */
  public void setJumpNode ( ProofNode pJumpNode )
  {
    this.jumpNode = pJumpNode;
  }


  @Override
  protected void forcedRelayout ()
  {
    // TODO größe setzen...
    doRelayout ();

  }
}
