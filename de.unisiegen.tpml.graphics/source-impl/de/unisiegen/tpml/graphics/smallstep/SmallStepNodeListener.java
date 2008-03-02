package de.unisiegen.tpml.graphics.smallstep;


import java.util.EventListener;

import de.unisiegen.tpml.core.ProofNode;


/**
 * TODO
 */
public interface SmallStepNodeListener extends EventListener
{

  /**
   * Called when a node has changed.
   * 
   * @param node The node that has changed.
   */
  public void nodeChanged ( SmallStepNodeComponent node );


  /**
   * Called when a entire repaint is requested.
   */
  public void repaintAll ();


  /**
   * Requests the SmallStepComponent to scroll to given node
   * 
   * @param node
   */
  public void requestJumpToNode ( ProofNode node );
}
