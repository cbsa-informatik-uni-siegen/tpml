package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet ;


public class L2OBigStepProofRuleSet extends L2BigStepProofRuleSet
{
  public L2OBigStepProofRuleSet ( L2OLanguage language )
  {
    super ( language ) ;
    registerByMethodName ( L2OLanguage.L2O , "OBJ-EVAL" , "applyObjEval" ,
        "updateObjEval" ) ;
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" ,
        "updateAttr" ) ;
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" ,
        "updateSend" ) ;
  }


  public void applyObjEval ( BigStepProofContext context , BigStepProofNode node )
  {
    ObjectExpr objectExpr = ( ObjectExpr ) node.getExpression ( ) ;
    context.addProofNode ( node , objectExpr.getE ( ) ) ;
  }


  public void updateObjEval ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    boolean allNodesProven = true ;
    for ( int i = 0 ; i < node.getChildCount ( ) ; i ++ )
    {
      if ( ! node.getChildAt ( i ).isProven ( ) )
      {
        allNodesProven = false ;
        break ;
      }
    }
    if ( allNodesProven )
    {
      Row row = ( Row ) node.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      context.setProofNodeResult ( node , new ObjectExpr ( row ) ) ;
    }
  }


  public void applyAttr ( BigStepProofContext context , BigStepProofNode node )
  {
    Row row = ( Row ) node.getExpression ( ) ;
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
          "Can not apply ATTR if all attributes are values." ) ;
    }
    for ( int i = 0 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attr )
      {
        Attr attr = ( Attr ) row.getExpressions ( i ) ;
        context.addProofNode ( node , attr.getE ( ) ) ;
      }
    }
  }


  public void updateAttr ( BigStepProofContext context , BigStepProofNode node )
  {
    boolean allNodesProven = true ;
    for ( int i = 0 ; i < node.getChildCount ( ) ; i ++ )
    {
      if ( ! node.getChildAt ( i ).isProven ( ) )
      {
        allNodesProven = false ;
        break ;
      }
    }
    if ( allNodesProven )
    {
      Row row = ( Row ) node.getExpression ( ) ;
      Expression [ ] tmp = row.getExpressions ( ).clone ( ) ;
      int nodeCount = 0 ;
      for ( int i = 0 ; i < row.getExpressions ( ).length ; i ++ )
      {
        if ( row.getExpressions ( i ) instanceof Attr )
        {
          Attr attr = ( Attr ) row.getExpressions ( i ) ;
          tmp [ i ] = new Attr ( attr.getIdentifier ( ) , node.getChildAt (
              nodeCount ).getResult ( ).getValue ( ) ) ;
          nodeCount ++ ;
        }
      }
      context.setProofNodeResult ( node , new Row ( tmp ) ) ;
    }
  }


  public void applySend ( BigStepProofContext context , BigStepProofNode node )
  {
    Message message = ( Message ) node.getExpression ( ) ;
    if ( ! message.getE ( ).isValue ( ) )
    {
      context.addProofNode ( node , message.getE ( ) ) ;
    }
  }


  public void updateSend ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 1 )
        && ( node.getChildAt ( 0 ).isProven ( ) ) )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) node.getChildAt ( 0 ).getResult ( )
          .getValue ( ) ;
      context.addProofNode ( node , new Message ( objectExpr ,
          ( ( Message ) node.getExpression ( ) ).getIdentifier ( ) ) ) ;
    }
  }
}
