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
   * The {@link BigStepProofModel}.
   */
  private BigStepProofModel bigStepProofModel ;


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
    this.bigStepProofModel = pBigStepProofModel ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new BigStepComponent ( this.bigStepProofModel ) ;
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
    this.outline = new DefaultOutline ( this ) ;
    this.outline.loadPrettyPrintable ( this.bigStepProofModel.getRoot ( )
        .getLastLeaf ( ).getExpression ( ) , Outline.Execute.INIT_BIGSTEP ) ;
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
  }


  /**
   * Returns the bigStepProofModel.
   * 
   * @return The bigStepProofModel.
   * @see #bigStepProofModel
   */
  public BigStepProofModel getBigStepProofModel ( )
  {
    return this.bigStepProofModel ;
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
