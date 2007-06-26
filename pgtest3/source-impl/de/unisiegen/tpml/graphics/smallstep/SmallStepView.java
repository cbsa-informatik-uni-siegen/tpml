package de.unisiegen.tpml.graphics.smallstep ;


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
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the small step interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 */
public class SmallStepView extends AbstractProofView
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 8529052541636149376L ;


  /**
   * The <code>SmallStep</code> component.
   */
  protected SmallStepComponent component ;


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
   * The {@link SmallStepProofModel}.
   */
  private SmallStepProofModel smallStepProofModel ;


  /**
   * Allocates a new {@link SmallStepView} for the specified
   * {@link SmallStepProofModel}.
   * 
   * @param pSmallStepProofModel The {@link SmallStepProofModel} for the
   *          <code>SmallStepView</code>.
   * @throws NullPointerException If {@link SmallStepProofModel} is
   *           <code>null</code>.
   */
  public SmallStepView ( SmallStepProofModel pSmallStepProofModel )
  {
    if ( pSmallStepProofModel == null )
    {
      throw new NullPointerException ( "model is null" ) ; //$NON-NLS-1$
    }
    this.smallStepProofModel = pSmallStepProofModel ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new SmallStepComponent ( this.smallStepProofModel ,
        isAdvanced ( ) ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( @ SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        SmallStepView.this.component
            .setAvailableWidth ( SmallStepView.this.scrollPane.getViewport ( )
                .getWidth ( ) ) ;
      }
    } ) ;
    this.outline = new DefaultOutline ( this ) ;
    this.outline
        .loadPrettyPrintable ( this.smallStepProofModel.getRoot ( )
            .getLastLeaf ( ).getExpression ( ) ,
            Outline.ExecuteInit.SMALLSTEP ) ;
    JPanel jPanelOutline = this.outline.getPanel ( ) ;
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
   * Returns the smallStepProofModel.
   * 
   * @return The smallStepProofModel.
   * @see #smallStepProofModel
   */
  public SmallStepProofModel getSmallStepProofModel ( )
  {
    return this.smallStepProofModel ;
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
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @ Override
  public void setAdvanced ( boolean advanced )
  {
    super.setAdvanced ( advanced ) ;
    this.component.setAdvanced ( isAdvanced ( ) ) ;
  }
}
