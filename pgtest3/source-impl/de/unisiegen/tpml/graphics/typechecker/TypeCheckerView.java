package de.unisiegen.tpml.graphics.typechecker ;


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
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeModelListener ;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the type checker interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev: 995 $
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class TypeCheckerView extends AbstractProofView
{
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 425214200136389228L ;


  /**
   * The <code>TypeChecker</code> component.
   */
  protected TypeCheckerComponent component ;


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
   * Allocates a new {@link TypeCheckerView} for the specified
   * {@link TypeCheckerProofModel}.
   * 
   * @param pTypeCheckerProofModel The {@link TypeCheckerProofModel} for the
   *          <code>TypeCheckerView</code>.
   */
  public TypeCheckerView ( TypeCheckerProofModel pTypeCheckerProofModel )
  {
    super ( ) ;
    this.outline = new DefaultOutline ( Outline.Start.TYPECHECKER ) ;
    this.outline.loadExpression ( pTypeCheckerProofModel.getRoot ( )
        .getLastLeaf ( ).getExpression ( ) , Outline.Execute.INIT_TYPECHECKER ) ;
    pTypeCheckerProofModel.addTreeModelListener ( new OutlineTreeModelListener (
        this.outline , pTypeCheckerProofModel ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new TypeCheckerComponent ( pTypeCheckerProofModel ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( @ SuppressWarnings ( "unused" )
      ComponentEvent event )
      {
        TypeCheckerView.this.component
            .setAvailableWidth ( TypeCheckerView.this.scrollPane.getViewport ( )
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
   * Returns the {@link Outline} of this view.
   * 
   * @return The {@link Outline} of this view.
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
