package de.unisiegen.tpml.graphics.abstractsyntaxtree.ui ;


import java.util.TimerTask ;
import de.unisiegen.tpml.graphics.abstractsyntaxtree.AbstractSyntaxTree;


/**
 * Invokes the execute method in the AbstractSyntaxTree.
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTTimerTask extends TimerTask
{
  /**
   * The AbstractSyntaxTree.
   */
  private AbstractSyntaxTree abstractSyntaxTree ;


  /**
   * Initilizes the ASTTimerTask.
   * 
   * @param pAbstractSyntaxTree The AbstractSyntaxTree.
   */
  public ASTTimerTask ( AbstractSyntaxTree pAbstractSyntaxTree )
  {
    this.abstractSyntaxTree = pAbstractSyntaxTree ;
  }


  /**
   * Invokes the execute method in the AbstractSyntaxTree.
   * 
   * @see java.util.TimerTask#run()
   */
  @ Override
  public void run ( )
  {
    this.abstractSyntaxTree.execute ( ) ;
  }
}
