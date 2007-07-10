package de.unisiegen.tpml.graphics.subtyping ;


import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.ComponentAdapter ;
import java.awt.event.ComponentEvent ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JSplitPane ;
import javax.swing.border.LineBorder ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the type checker interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class NewSubTypingView extends AbstractProofView
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 425214200136389228L ;


  /**
   * The <code>SubTyping</code> component.
   */
  protected NewSubTypingComponent component ;


  /**
   * The <code>JScrollPane</code> for the <code>component</code>.
   */
  protected JScrollPane scrollPane ;


  /**
   * The <code>JSplitPane</code> for the <code>component</code>.
   */
  private JSplitPane jSplitPane ;


  private JPanel outlinePanel ;


  /**
   * The first {@link Outline} of this view.
   * 
   * @see #getOutline1()
   */
  private Outline outline1 ;


  /**
   * The second {@link Outline} of this view.
   * 
   * @see #getOutline1()
   */
  private Outline outline2 ;


  /**
   * The {@link SubTypingProofModel}.
   */
  private SubTypingProofModel SubTypingProofModel ;


  /**
   * Allocates a new {@link NewSubTypingView} for the specified
   * {@link SubTypingProofModel}.
   * 
   * @param pSubTypingProofModel The {@link SubTypingProofModel} for the
   *          <code>SubTypingView</code>.
   */
  public NewSubTypingView ( SubTypingProofModel pSubTypingProofModel )
  {
    super ( ) ;
    this.SubTypingProofModel = pSubTypingProofModel ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new NewSubTypingComponent ( this.SubTypingProofModel ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( @ SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        NewSubTypingView.this.component
            .setAvailableWidth ( NewSubTypingView.this.scrollPane
                .getViewport ( ).getWidth ( ) ) ;
      }
    } ) ;
    this.outlinePanel = new JPanel ( new GridBagLayout ( ) ) ;
    this.outline1 = new DefaultOutline ( this ) ;
    this.outline1.load ( this.SubTypingProofModel.getRoot ( ).getLastLeaf ( )
        .getType ( ) , Outline.ExecuteInit.SUBTYPING ) ;
    JPanel jPanelOutline1 = this.outline1.getPanel ( ) ;
    jPanelOutline1.setBorder ( new LineBorder ( Color.BLACK , 1 ) ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 2 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    this.outlinePanel.add ( jPanelOutline1 , gridBagConstraints ) ;
    this.outline2 = new DefaultOutline ( this ) ;
    this.outline2.load ( this.SubTypingProofModel.getRoot ( ).getLastLeaf ( )
        .getType2 ( ) , Outline.ExecuteInit.SUBTYPING ) ;
    JPanel jPanelOutline2 = this.outline2.getPanel ( ) ;
    jPanelOutline2.setBorder ( new LineBorder ( Color.BLACK , 1 ) ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 2 , 0 , 0 ) ;
    gridBagConstraints.gridx = 1 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    this.outlinePanel.add ( jPanelOutline2 , gridBagConstraints ) ;
    this.jSplitPane.setLeftComponent ( this.scrollPane ) ;
    this.jSplitPane.setRightComponent ( this.outlinePanel ) ;
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
   * Returns the first {@link Outline} of this view.
   * 
   * @return The first {@link Outline} of this view.
   */
  public Outline getOutline1 ( )
  {
    return this.outline1 ;
  }


  /**
   * Returns the second {@link Outline} of this view.
   * 
   * @return The second {@link Outline} of this view.
   */
  public Outline getOutline2 ( )
  {
    return this.outline2 ;
  }


  /**
   * Returns the SubTypingProofModel.
   * 
   * @return The SubTypingProofModel.
   * @see #SubTypingProofModel
   */
  public SubTypingProofModel getSubTypingProofModel ( )
  {
    return this.SubTypingProofModel ;
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
