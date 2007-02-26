package de.unisiegen.tpml.graphics.bigstep ;


import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.ComponentAdapter ;
import java.awt.event.ComponentEvent ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JSplitPane ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeModelListener ;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the big step interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class BigStepView extends AbstractProofView
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 8529052541636149376L ;


  /**
   * The <code>BigStep</code> component.
   */
  protected BigStepComponent component ;


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
   * Allocates a new <code>BigStepView</code> for the specified
   * {@link BigStepProofModel}.
   * 
   * @param pBigStepProofModel The {@link BigStepProofModel} for the
   *          <code>BigStepView</code>.
   */
  public BigStepView ( BigStepProofModel pBigStepProofModel )
  {
    super ( ) ;
    this.outline = new DefaultOutline ( ) ;
    this.outline.disableAutoUpdate ( ) ;
    this.outline.loadExpression ( pBigStepProofModel.getRoot ( ).getLastLeaf ( )
        .getExpression ( ) , Outline.INIT ) ;
    pBigStepProofModel.addTreeModelListener ( new OutlineTreeModelListener (
        this.outline , pBigStepProofModel ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new BigStepComponent ( pBigStepProofModel ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( @ SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        BigStepView.this.component
            .setAvailableWidth ( BigStepView.this.scrollPane.getViewport ( )
                .getWidth ( ) ) ;
      }
    } ) ;
    JPanel jPanelOutline = this.outline.getJPanelOutline ( ) ;
    jPanelOutline.getPreferredSize ( ).getHeight ( ) ;
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
    this.addPropertyChangeListener ( new OutlinePropertyChangeListener (
        this.jSplitPane , this.outline ) ) ;
    jPanelOutline.addComponentListener ( new OutlineComponentListener (
        this.jSplitPane , this.outline ) ) ;
  }


  /**
   * Returns the <code>Outline</code> of this view.
   * 
   * @return The <code>Outline</code> of this view.
   */
  public Outline getOutline ( )
  {
    return this.outline ;
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
}
