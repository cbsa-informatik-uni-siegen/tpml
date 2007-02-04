package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class L2OBigStepProofRuleSet extends L2BigStepProofRuleSet
{
  /**
   * TODO
   * 
   * @param pL2OLanguage TODO
   */
  public L2OBigStepProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage ) ;
    registerByMethodName ( L2OLanguage.L2O , "OBJ" , "applyObj" , //$NON-NLS-1$//$NON-NLS-2$
        "updateObj" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateAttr" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" , //$NON-NLS-1$//$NON-NLS-2$
        "updateSend" ) ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void applyObj ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    ObjectExpr objectExpr = ( ObjectExpr ) pNode.getExpression ( ) ;
    pContext.addProofNode ( pNode , objectExpr.getE ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void updateObj ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    boolean allNodesProven = true ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      if ( ! pNode.getChildAt ( i ).isProven ( ) )
      {
        allNodesProven = false ;
        break ;
      }
    }
    if ( allNodesProven )
    {
      Row row = ( Row ) pNode.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      ObjectExpr objectExpr = ( ObjectExpr ) pNode.getExpression ( ) ;
      pContext.setProofNodeResult ( pNode , new ObjectExpr ( objectExpr
          .getId ( ) , objectExpr.getTau ( ) , row ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void applyAttr ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    boolean allValues = true ;
    for ( int i = 0 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attr )
      {
        Attr attr = ( Attr ) row.getExpressions ( i ) ;
        if ( ! attr.isValue ( ) )
        {
          allValues = false ;
          break ;
        }
      }
    }
    if ( allValues )
    {
      throw new IllegalArgumentException (
          "Can not apply ATTR if all attributes are values." ) ; //$NON-NLS-1$
    }
    for ( int i = 0 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attr )
      {
        Attr attr = ( Attr ) row.getExpressions ( i ) ;
        pContext.addProofNode ( pNode , attr.getE ( ) ) ;
      }
    }
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void updateAttr ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    boolean allNodesProven = true ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      if ( ! pNode.getChildAt ( i ).isProven ( ) )
      {
        allNodesProven = false ;
        break ;
      }
    }
    if ( allNodesProven )
    {
      Row row = ( Row ) pNode.getExpression ( ) ;
      Expression [ ] tmp = row.getExpressions ( ).clone ( ) ;
      int nodeCount = 0 ;
      for ( int i = 0 ; i < row.getExpressions ( ).length ; i ++ )
      {
        if ( row.getExpressions ( i ) instanceof Attr )
        {
          Attr attr = ( Attr ) row.getExpressions ( i ) ;
          tmp [ i ] = new Attr ( attr.getId ( ) , attr.getTau ( ) ,
              pNode.getChildAt ( nodeCount ).getResult ( ).getValue ( ) ) ;
          nodeCount ++ ;
        }
      }
      pContext.setProofNodeResult ( pNode , new Row ( tmp ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void applySend ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Message message = ( Message ) pNode.getExpression ( ) ;
    if ( ! message.getE ( ).isValue ( ) )
    {
      pContext.addProofNode ( pNode , message.getE ( ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pNode TODO
   */
  public void updateSend ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pNode.getChildAt ( 0 )
          .getResult ( ).getValue ( ) ;
      pContext.addProofNode ( pNode , new Message ( objectExpr.getE ( ) ,
          ( ( Message ) pNode.getExpression ( ) ).getId ( ) ) ) ;
    }
  }
}
