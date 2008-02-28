package de.unisiegen.tpml.graphics.tree;


import java.awt.Dimension;
import java.awt.Point;

import de.unisiegen.tpml.core.ProofNode;


/**
 * Handles the layouting of a treebased GUI. The TypeChecker- and the
 * BigStep-GUI are handled with this.
 * 
 * @author marcell
 */
public class TreeNodeLayout
{

  /**
   * Contains available width of the layout.
   */
  private int availableWidth;


  /**
   * the singel pages will be filled to avoid deviding the singel nodes while
   * printing
   */
  private int actualPageCounter = 0;


  /**
   * The space between the the singel elements over the sides
   */
  private int spaceUnderPage = 12;


  /**
   * Contains available height of one paper, is Integer.MaxValue if not
   * printing...
   */
  private int availableHeight;


  /**
   * The spacing between the nodes.
   */
  private int spacing;


  public TreeNodeLayout ()
  {
    this.spacing = 10;
  }


  public TreeNodeLayout ( int spacing )
  {
    this.spacing = spacing;
  }


  /**
   * Sets the spacing
   * 
   * @param spacing
   */
  public void setSpacing ( int spacing )
  {
    this.spacing = spacing;
  }


  /**
   * Returns the spacing
   * 
   * @return
   */
  public int getSpacing ()
  {
    return this.spacing;
  }


  /**
   * Places the nodes.
   * 
   * @param root The rootNode of the tree
   * @param posX Left position where the root node should be placed
   * @param posY Top position where the root node should be placed
   * @param pAvailableWidth The maximum width available for the layout.
   * @return
   */
  public Point placeNodes ( ProofNode root, int posX, int posY,
      int pAvailableWidth, int pAvailableHeight )
  {
    this.availableWidth = pAvailableWidth;
    this.availableHeight = pAvailableHeight;
    actualPageCounter = 0;

    return placeNode ( root, posX, posY, new Point ( -1, -1 ) );
  }


  /**
   * Calculates the size for a node and does the placing of the Component.
   * 
   * @param node The node that should be
   * @param posX The left position of the node
   * @param posY The top position of the node
   * @return The right bottom pos of the needed width
   */
  private Point placeNode ( ProofNode node, int posX, int posY,
      Point rightBottomPos )
  {

    TreeNodeComponent nodeComponent = ( TreeNodeComponent ) node
        .getUserObject ();

    if ( nodeComponent == null )
    {
      // this should not happen
      return rightBottomPos;
    }

    // the available width is shrinked by the already spended
    // space by indentating the node
    int availableWidth = this.availableWidth - posX;
    Dimension size = nodeComponent.update ( availableWidth );

    // add the needed height to the tmpPaper, if it gets bigger than
    // availableHeight, pagebreak
    if ( this.actualPageCounter + this.spacing + size.height
        + this.spaceUnderPage > this.availableHeight )
    {
      {
        // add the rest of space on the page to to posY... So the next node will
        // get to next page
        posY += this.availableHeight - this.actualPageCounter;
        // posY += 10;

        // the actualPageCounter restarts
        this.actualPageCounter = 0;
      }
    }

    // add the needed height to the actualPageCounter
    this.actualPageCounter += size.height;

    // do the real positioning of the node
    nodeComponent.setBounds ( posX, posY, size.width, size.height );

    // let some spacing between two nodes
    posY += this.spacing;
    this.actualPageCounter += this.spacing;

    //
    // change the resulting point
    //
    // check if this node widens the resulting area
    if ( posX + size.width > rightBottomPos.x )
    {
      rightBottomPos.x = posX + size.width;
    }

    // we will always go down so the new yPos is always bigger
    rightBottomPos.y = posY + size.height;

    posY += size.height;
    posX += nodeComponent.getIndentationWidth ();

    // delegate the rest to the other nodes
    for ( int i = 0 ; i < node.getChildCount () ; i++ )
    {
      ProofNode pNode = node.getChildAt ( i );

      placeNode ( pNode, posX, posY, rightBottomPos );

      // increment the posY for the next node
      posY = rightBottomPos.y;

    }

    return rightBottomPos;
  }

}
