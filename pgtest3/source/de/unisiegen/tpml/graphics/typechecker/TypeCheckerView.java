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


  public TypeCheckerView ( TypeCheckerProofModel model )
  {
    super ( ) ;
    AbstractSyntaxTree abstractSyntaxTree = AbstractSyntaxTree
        .getNewInstance ( ) ;
    abstractSyntaxTree.setExpression ( model.getRoot ( ).getLastLeaf ( )
        .getExpression ( ) ) ;
    model.addTreeModelListener ( new ASTTreeModelListener ( abstractSyntaxTree ,
        model ) ) ;
    GridBagConstraints gridBagConstraints = new GridBagConstraints ( ) ;
    setLayout ( new GridBagLayout ( ) ) ;
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
    JPanel ast = abstractSyntaxTree.getASTUI ( ).getJPanelMain ( ) ;
    ast.getPreferredSize ( ).getHeight ( ) ;
    this.jSplitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ,
        this.scrollPane , ast ) ;
    this.jSplitPane.setOneTouchExpandable ( true ) ;
    this.jSplitPane.setResizeWeight ( 0.75 ) ;
    gridBagConstraints.fill = GridBagConstraints.BOTH ;
    gridBagConstraints.insets = new Insets ( 0 , 0 , 0 , 0 ) ;
    gridBagConstraints.gridx = 0 ;
    gridBagConstraints.gridy = 0 ;
    gridBagConstraints.weightx = 10 ;
    gridBagConstraints.weighty = 10 ;
    add ( this.jSplitPane , gridBagConstraints ) ;
  }


  public void guess ( ) throws IllegalStateException , ProofGuessException
  {
    this.component.guess ( ) ;
  }
}
