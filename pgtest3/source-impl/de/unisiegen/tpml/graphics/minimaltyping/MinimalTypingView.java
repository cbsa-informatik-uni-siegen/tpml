package de.unisiegen.tpml.graphics.minimaltyping ;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import de.unisiegen.tpml.core.ProofGuessException;
import de.unisiegen.tpml.core.ProofNode;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode;
import de.unisiegen.tpml.graphics.AbstractProofView;
import de.unisiegen.tpml.graphics.outline.DefaultOutline;
import de.unisiegen.tpml.graphics.outline.Outline;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the minimal typing interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class MinimalTypingView extends AbstractProofView
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 425214200136389228L ;


  /**
   * The <code>MinimalTyping</code> component.
   */
  protected MinimalTypingComponent component ;


  /**
   * The <code>JScrollPane</code> for the <code>component</code>.
   */
  protected JScrollPane scrollPane ;


  /**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane ;


  /**
   * The {@link Outline} of this view.
   * 
   * @see #getOutline()
   */
  private Outline outline ;


  /**
   * The {@link MinimalTypingProofModel}.
   */
  private MinimalTypingProofModel proofModel ;


  /**
   * Allocates a new {@link MinimalTypingView} for the specified
   * {@link MinimalTypingProofModel}.
   * 
   * @param pMinimalTypingProofModel The {@link MinimalTypingProofModel} for the
   *          <code>MinimalTypingView</code>.
   */
  public MinimalTypingView ( MinimalTypingProofModel pMinimalTypingProofModel )
  {
    super ( ) ;
    this.proofModel = pMinimalTypingProofModel ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new MinimalTypingComponent ( this.proofModel ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( @ SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        MinimalTypingView.this.component
            .setAvailableWidth ( MinimalTypingView.this.scrollPane
                .getViewport ( ).getWidth ( ) ) ;
      }
    } ) ;
    this.outline = new DefaultOutline ( this ) ;
    this.outline.loadExpression ( this.proofModel.getRoot ( )
        .getLastLeaf ( ).getExpression ( ) ,
        Outline.ExecuteInit.MINIMALTYPING ) ;
    JPanel jPanelOutline = this.outline.getPanel ( ) ;
    this.jSplitPane.setLeftComponent ( this.scrollPane ) ;
    this.jSplitPane.setRightComponent ( jPanelOutline ) ;
    this.jSplitPane.setOneTouchExpandable ( true ) ;
    this.jSplitPane.setResizeWeight ( 0.5 ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    this.add ( this.jSplitPane , gridBagConstraints ) ;
  }


  /**
   * Returns the jSplitPane.
   * 
   * @return The jSplitPane.
   * @see #jSplitPane
   */
  public JSplitPane getJSplitPane ( )
  {
    return this.jSplitPane ;
  }


  /**
   * Returns the {@link Outline} of this view.
   * 
   * @return The {@link Outline} of this view.
   */
  public Outline getOutline ( )
  {
    return this.outline ;
  }


  /**
   * Returns the MinimalTypingProofModel.
   * 
   * @return The MinimalTypingProofModel.
   * @see #MinimalTypingProofModel
   */
  public MinimalTypingProofModel getMinimalTypingProofModel ( )
  {
    return this.proofModel ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.ProofView#guess()
   */
  public void guess ( ) throws IllegalStateException , ProofGuessException
  {
    this.component.guess ( ) ;
  }
  
	/**
	 * {@inheritDoc}
	 *
	 * @see de.unisiegen.tpml.graphics.ProofView#setAdvanced(boolean)
	 */
  @Override
	public void setAdvanced(boolean advanced) {
	  super.setAdvanced ( advanced );
	  this.proofModel.setMode(advanced);
	  
	  Enumeration<ProofNode> enumeration = this.proofModel.getRoot().postorderEnumeration();
		while (enumeration.hasMoreElements()) {
			// tell the component belonging to this node, that we have a new advanced state
			MinimalTypingProofNode node = (MinimalTypingProofNode)enumeration.nextElement();
			MinimalTypingNodeComponent component = (MinimalTypingNodeComponent)node.getUserObject();
			component.setAdvanced(advanced);
		}
  }
}
