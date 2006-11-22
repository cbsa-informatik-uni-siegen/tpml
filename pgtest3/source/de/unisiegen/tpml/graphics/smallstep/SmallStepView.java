package de.unisiegen.tpml.graphics.smallstep ;


import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.event.ComponentAdapter ;
import java.awt.event.ComponentEvent ;
import javax.swing.JScrollPane ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTTreeModelListener ;


/**
 * The implementation of the {@link de.unisiegen.tpml.graphics.ProofView}
 * interface for the small step interpreter user interface.
 * 
 * @author Marcell Fischbach
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 * @see de.unisiegen.tpml.graphics.smallstep.SmallStepComponent
 */
public class SmallStepView extends AbstractProofView
{
  //
  // Constants
  //
  /**
   * The unique serialization identifier for this class.
   */
  private static final long serialVersionUID = - 8529052541636149376L ;


  //
  // Attributes
  //
  /**
   * The small step component.
   */
  private SmallStepComponent component ;


  /**
   * The scroll pane for the <code>component</code>.
   */
  private JScrollPane scrollPane ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>SmallStepView</code> for the specified
   * <code>model</code>.
   * 
   * @param model the proof model for the small step view.
   * @throws NullPointerException if <code>model</code> is <code>null</code>.
   */
  public SmallStepView ( SmallStepProofModel model )
  {
    if ( model == null )
    {
      throw new NullPointerException ( "model is null" ) ;
    }
    AbstractSyntaxTree abstractSyntaxTree = AbstractSyntaxTree
        .getNewInstance ( ) ;
    abstractSyntaxTree.setExpression ( model.getRoot ( ).getLastLeaf ( )
        .getExpression ( ) ) ;
    model.addTreeModelListener ( new ASTTreeModelListener ( abstractSyntaxTree ,
        model ) ) ;
    GridBagLayout gridBagLayout = new GridBagLayout ( ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    setLayout ( gridBagLayout ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new SmallStepComponent ( model , isAdvanced ( ) ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    add ( this.scrollPane , gridBagConstraints ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 1 , 1 , 1 , 1 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 7 ;
    gridBagConstraints.weightx = 0 ;
    gridBagConstraints.weighty = 0 ;
    add ( abstractSyntaxTree.getASTUI ( ).getJPanelMain ( ) ,
        gridBagConstraints ) ;
    this.scrollPane.setViewportView ( this.component ) ;
    this.scrollPane.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollPane.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( ComponentEvent event )
      {
        SmallStepView.this.component
            .setAvailableWidth ( SmallStepView.this.scrollPane.getViewport ( )
                .getWidth ( ) ) ;
      }
    } ) ;
  }


  //
  // Accessors
  //
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


  //
  // Primitives
  //
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
