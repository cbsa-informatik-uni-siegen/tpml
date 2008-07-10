package de.unisiegen.tpml.graphics.bigstepclosure;


import java.util.EventListener;

import de.unisiegen.tpml.core.ProofNode;


/**
 * @author marcell
 * @version $Id: BigStepNodeListener.java 2796 2008-03-14 19:13:11Z fehler $
 */
public interface BigStepClosureNodeListener extends EventListener
{

  /**
   * Called when ever the node changed and the layout needs to get updated.
   * 
   * @param node
   */
  public void nodeChanged ( BigStepClosureNodeComponent node );


  /**
   * Callend when an error occured during guess or an guess tree.
   * 
   * @param node The node where the component should jumpt to
   */
  public void requestJumpToNode ( ProofNode node );
}
