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
import de.unisiegen.tpml.ui.abstractsyntaxtree.AbstractSyntaxTree ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTSplitPaneListener ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.listener.ASTTreeModelListener ;


/**
 * Add documentation here.
 * 
 * @author Marcell Fischbach
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.graphics.AbstractProofView
 */
public class TypeCheckerView extends AbstractProofView
{
  /**
   * 
   */
  private static final long serialVersionUID = - 425214200136389228L ;


  protected TypeCheckerComponent component ;


  protected JScrollPane scrollPane ;


  private JSplitPane jSplitPane ;


  private AbstractSyntaxTree abstractSyntaxTree ;


  public TypeCheckerView ( TypeCheckerProofModel model )
  {
    super ( ) ;
    this.abstractSyntaxTree = new AbstractSyntaxTree ( ) ;
    this.abstractSyntaxTree.setExpression ( model.getRoot ( ).getLastLeaf ( )
        .getExpression ( ) , "first_typechecker" ) ;
    model.addTreeModelListener ( new ASTTreeModelListener (
        this.abstractSyntaxTree , model ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    this.setLayout ( new GridBagLayout ( ) ) ;
    this.scrollPane = new JScrollPane ( ) ;
    this.component = new TypeCheckerComponent ( model ) ;
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


  public AbstractSyntaxTree getAbstractSyntaxTree ( )
  {
    return this.abstractSyntaxTree ;
  }


  public void guess ( ) throws IllegalStateException , ProofGuessException
  {
    this.component.guess ( ) ;
  }
}
