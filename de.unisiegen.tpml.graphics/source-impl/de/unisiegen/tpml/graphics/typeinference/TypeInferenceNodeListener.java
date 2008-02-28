package de.unisiegen.tpml.graphics.typeinference;


import java.util.EventListener;

import de.unisiegen.tpml.core.ProofNode;


/**
 * interface as a <code>EventListener</code> (NodeListener) for the
 * TypeInference
 * 
 * @author michael
 */
public interface TypeInferenceNodeListener extends EventListener
{

  /**
   * Called when a node has changed.
   * 
   * @param node The node that has changed.
   */
  public void nodeChanged ( TypeInferenceNodeComponent node );


  /**
   * Called when a entire repaint is requested.
   */
  public void repaintAll ();


  /**
   * Requests the TypeInferenceComponent to scroll to given node
   * 
   * @param node the node to sroll to
   */
  public void requestJumpToNode ( ProofNode node );
}
