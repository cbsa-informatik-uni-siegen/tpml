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
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTComponentListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTPropertyChangeListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTTreeModelListener ;


/**
 * Add documentation here.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class BigStepView extends AbstractProofView
{
  /**
   * 
   */
  private static final long serialVersionUID = - 8529052541636149376L ;


  /**
   * TODO
   */
  protected BigStepComponent component ;


  /**
   * TODO
   */
  protected JScrollPane scrollPane ;


  /**
   * TODO
   */
  private JSplitPane jSplitPane ;


  /**
   * TODO
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * TODO
   * 
   * @param model
   */
  public BigStepView ( BigStepProofModel model )
  {
    super ( ) ;
    this.abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
    // Disable AutoUpdate, remove Listener and deselect
    this.abstractSyntaxTree.getASTUI ( ).getJCheckBoxAutoUpdate ( ).setEnabled (
        false ) ;
    this.abstractSyntaxTree.getASTUI ( ).getJCheckBoxAutoUpdate ( )
        .removeItemListener (
            this.abstractSyntaxTree.getASTUI ( ).getJCheckBoxAutoUpdate ( )
                .getItemListeners ( ) [ 0 ] ) ;
    this.abstractSyntaxTree.getASTUI ( ).getJCheckBoxAutoUpdate ( )
        .setSelected ( false ) ;
    this.abstractSyntaxTree.getASTUI ( ).getJMenuItemAutoUpdate ( ).setEnabled (
        false ) ;
    this.abstractSyntaxTree.getASTUI ( ).getJMenuItemAutoUpdate ( )
        .removeActionListener (
            this.abstractSyntaxTree.getASTUI ( ).getJMenuItemAutoUpdate ( )
                .getActionListeners ( ) [ 0 ] ) ;
    this.abstractSyntaxTree.getASTUI ( ).getJMenuItemAutoUpdate ( )
        .setSelected ( false ) ;
    this.abstractSyntaxTree.loadExpression ( model.getRoot ( )
        .getLastLeaf ( ).getExpression ( ) , "first_bigstep" ) ;
    model.addTreeModelListener ( new ASTTreeModelListener (
        this.abstractSyntaxTree , model ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new BigStepComponent ( model ) ;
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
    JPanel jMainPanel = this.abstractSyntaxTree.getASTUI ( ).getJPanelMain ( ) ;
    jMainPanel.getPreferredSize ( ).getHeight ( ) ;
    this.jSplitPane.setLeftComponent ( this.scrollPane ) ;
    this.jSplitPane.setRightComponent ( jMainPanel ) ;
    this.jSplitPane.setOneTouchExpandable ( true ) ;
    this.jSplitPane.setResizeWeight ( 0.5 ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    this.add ( this.jSplitPane , gridBagConstraints ) ;
    this.addPropertyChangeListener ( new ASTPropertyChangeListener (
        this.jSplitPane , this.abstractSyntaxTree ) ) ;
    jMainPanel.addComponentListener ( new ASTComponentListener (
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


  /**
   * Guesses the first node with the tree that is not already prooven.
   * 
   * @throws IllegalStateException
   * @throws ProofGuessException
   */
  public void guess ( ) throws IllegalStateException , ProofGuessException
  {
    this.component.guess ( ) ;
  }
}
