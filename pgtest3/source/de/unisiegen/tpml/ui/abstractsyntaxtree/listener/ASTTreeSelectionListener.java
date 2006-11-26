package de.unisiegen.tpml.ui.abstractsyntaxtree.listener ;


import java.util.LinkedList ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTNode ;
import de.unisiegen.tpml.ui.abstractsyntaxtree.ASTUI ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev$
 */
public class ASTTreeSelectionListener implements TreeSelectionListener
{
  /**
   * TODO
   */
  private ASTUI aSTUI ;


  /**
   * TODO
   * 
   * @param pASTUI
   */
  public ASTTreeSelectionListener ( ASTUI pASTUI )
  {
    this.aSTUI = pASTUI ;
  }


  /**
   * TODO
   * 
   * @param pParent
   * @param pChild
   * @return TODO
   */
  private int childIndex ( DefaultMutableTreeNode pParent ,
      DefaultMutableTreeNode pChild )
  {
    for ( int i = 0 ; i < pParent.getChildCount ( ) ; i ++ )
    {
      if ( pParent.getChildAt ( i ).equals ( pChild ) )
      {
        return i ;
      }
    }
    return ASTNode.NO_BINDING ;
  }


  /**
   * TODO
   * 
   * @param pNode
   */
  private void repaint ( DefaultMutableTreeNode pNode )
  {
    this.aSTUI.nodeChanged ( pNode ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pNode
   */
  public void reset ( DefaultMutableTreeNode pNode )
  {
    ASTNode aSTNode = ( ASTNode ) pNode.getUserObject ( ) ;
    aSTNode.resetCaption ( ) ;
    aSTNode.setReplaceInThisNode ( false ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      reset ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pTreePath
   */
  public void update ( TreePath pTreePath )
  {
    if ( pTreePath == null )
    {
      return ;
    }
    LinkedList < ASTNode > list = new LinkedList < ASTNode > ( ) ;
    for ( int i = 0 ; i < pTreePath.getPathCount ( ) ; i ++ )
    {
      Object tmp = ( ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ i ] )
          .getUserObject ( ) ;
      if ( ( tmp instanceof ASTNode ) )
      {
        list.add ( ( ASTNode ) tmp ) ;
      }
    }
    DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) pTreePath
        .getPath ( ) [ 0 ] ;
    reset ( rootNode ) ;
    ASTNode last = list.getLast ( ) ;
    ASTNode secondlast = null ;
    if ( list.size ( ) >= 2 )
    {
      secondlast = list.get ( list.size ( ) - 2 ) ;
    }
    // No Expression
    if ( last.getASTPair ( ) != null )
    {
      if ( secondlast == null )
      {
        return ;
      }
      // Highlight the selected Identifier
      last.enableSelectedColor ( ) ;
      for ( int i = 0 ; i < list.size ( ) - 1 ; i ++ )
      {
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                secondlast.getExpression ( ) ) ;
        int childIndex = childIndex ( ( DefaultMutableTreeNode ) pTreePath
            .getPath ( ) [ pTreePath.getPathCount ( ) - 2 ] ,
            ( DefaultMutableTreeNode ) pTreePath.getPath ( ) [ pTreePath
                .getPathCount ( ) - 1 ] ) ;
        if ( ! ( secondlast.getExpression ( ) instanceof Lambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLambda )
            && ! ( secondlast.getExpression ( ) instanceof MultiLet )
            && ! ( secondlast.getExpression ( ) instanceof LetRec )
            && ! ( secondlast.getExpression ( ) instanceof Let )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLetRec )
            && ! ( secondlast.getExpression ( ) instanceof CurriedLet )
            && ! ( secondlast.getExpression ( ) instanceof Recursion ) )
        {
          childIndex = ASTNode.NO_BINDING ;
        }
        list.get ( i ).setASTBinding ( last.getASTBinding ( ) ) ;
        list.get ( i ).setReplaceInThisNode ( true ) ;
        list.get ( i )
            .updateCaption (
                prettyAnnotation.getStartOffset ( )
                    + last.getASTPair ( ).getStart ( ) ,
                prettyAnnotation.getStartOffset ( )
                    + last.getASTPair ( ).getEnd ( ) , childIndex ) ;
      }
    }
    // Expression
    else
    {
      for ( int i = 0 ; i < list.size ( ) ; i ++ )
      {
        PrettyAnnotation prettyAnnotation = list.get ( i ).getExpression ( )
            .toPrettyString ( ).getAnnotationForPrintable (
                last.getExpression ( ) ) ;
        if ( i < list.size ( ) - 1 )
        {
          list.get ( i ).setReplaceInThisNode ( true ) ;
        }
        list.get ( i ).updateCaption ( prettyAnnotation.getStartOffset ( ) ,
            prettyAnnotation.getEndOffset ( ) , ASTNode.NO_BINDING ) ;
      }
    }
    repaint ( rootNode ) ;
  }


  /**
   * TODO
   * 
   * @param pTreeSelectionEvent
   * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
   */
  public void valueChanged ( TreeSelectionEvent pTreeSelectionEvent )
  {
    update ( pTreeSelectionEvent.getPath ( ) ) ;
  }
}
