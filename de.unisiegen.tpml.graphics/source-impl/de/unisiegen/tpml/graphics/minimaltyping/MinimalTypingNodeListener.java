package de.unisiegen.tpml.graphics.minimaltyping;


import java.util.EventListener;

import de.unisiegen.tpml.core.ProofNode;


/**
 * Listener for MinimalTypingNodes
 * 
 * @author Benjamin Mies
 */
public interface MinimalTypingNodeListener extends EventListener
{

  /**
   * Called when the given node has changed.
   * 
   * @param node The node that has changed.
   */
  public void nodeChanged ( MinimalTypingNodeComponent node );


  /**
   * Called when the user has selected the "enter type" from the menu.
   * 
   * @param node
   */
  public void requestTypeEnter ( MinimalTypingNodeComponent node );


  /**
   * Callend when an error occured during guess or an guess tree.
   * 
   * @param node The node where the component should jumpt to
   */
  public void requestJumpToNode ( ProofNode node );
}
