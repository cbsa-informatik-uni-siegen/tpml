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
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTSplitPaneListener ;
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
  protected SmallStepComponent component ;


  /**
   * The scroll pane for the <code>component</code>.
   */
  protected JScrollPane scrollPane ;


  /**
   * TODO
   */
  protected JSplitPane jSplitPane ;


  /**
   * TODO
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


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
    this.abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
    this.abstractSyntaxTree.setExpression ( model.getRoot ( ).getLastLeaf ( )
        .getExpression ( ) , "first_smallstep" ) ;
    model.addTreeModelListener ( new ASTTreeModelListener (
        this.abstractSyntaxTree , model ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new SmallStepComponent ( model , isAdvanced ( ) ) ;
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
    JPanel jMainPanel = this.abstractSyntaxTree.getASTUI ( ).getJPanelMain ( ) ;
    jMainPanel.getPreferredSize ( ).getHeight ( ) ;
    this.jSplitPane.setLeftComponent ( this.scrollPane ) ;
    this.jSplitPane.setRightComponent ( jMainPanel ) ;
    this.jSplitPane.setOneTouchExpandable ( true ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    this.add ( this.jSplitPane , gridBagConstraints ) ;
    this.addPropertyChangeListener ( new ASTSplitPaneListener (
        this.jSplitPane , this.abstractSyntaxTree ) ) ;
    jMainPanel.addComponentListener ( new ASTSplitPaneListener (
        this.jSplitPane , this.abstractSyntaxTree ) ) ;
  }


  /**
   * TODO
   * 
   * @return TODO
   */
  public AbstractSyntaxTree getAbstractSyntaxTree ( )
  {
    return this.abstractSyntaxTree ;
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
}
