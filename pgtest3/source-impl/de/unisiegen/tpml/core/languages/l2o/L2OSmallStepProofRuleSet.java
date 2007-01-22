package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;


public class L2OSmallStepProofRuleSet extends L2SmallStepProofRuleSet
{
  public L2OSmallStepProofRuleSet ( L2OLanguage language )
  {
    super ( language ) ;
    // Obj
    register ( L2OLanguage.L2O , "OBJ-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "OBJ-SEND" , true ) ; //$NON-NLS-1$
    // Attr
    register ( L2OLanguage.L2O , "ATTR-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RIGHT" , false ) ; //$NON-NLS-1$
    // Meth
    register ( L2OLanguage.L2O , "METH-RIGHT" , false ) ; //$NON-NLS-1$
    // Send
    register ( L2OLanguage.L2O , "SEND-EVAL" , false ) ; //$NON-NLS-1$  
    register ( L2OLanguage.L2O , "SEND-ATTR" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-SKIP" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-EXEC" , true ) ; //$NON-NLS-1$
  }


  public Expression evaluateObjectExpr ( SmallStepProofContext pContext ,
      ObjectExpr pObjectExpr )
  {
    pContext.addProofStep ( getRuleByName ( "OBJ-EVAL" ) , pObjectExpr ) ;
    Expression row = evaluate ( pContext , pObjectExpr.getE ( ) ) ;
    if ( row.isException ( ) )
    {
      return row ;
    }
    return new ObjectExpr ( ( Row ) row ) ;
  }


  public Expression evaluateMessage ( SmallStepProofContext pContext ,
      Message pMessage )
  {
    if ( ! pMessage.getE ( ).isValue ( ) )
    {
      pContext.addProofStep ( getRuleByName ( "SEND-EVAL" ) , pMessage ) ;
      Expression e = evaluate ( pContext , pMessage.getE ( ) ) ;
      if ( e.isException ( ) )
      {
        return e ;
      }
      return new Message ( e , pMessage.getIdentifier ( ) ) ;
    }
    if ( pMessage.getE ( ) instanceof ObjectExpr )
    {
      pContext.addProofStep ( getRuleByName ( "OBJ-SEND" ) , pMessage ) ;
      ObjectExpr o = ( ObjectExpr ) pMessage.getE ( ) ;
      Row r = ( Row ) o.getE ( ) ;
      Expression newRow = r.substitute ( "self" , o.clone ( ) ) ;
      return new Message ( newRow , pMessage.getIdentifier ( ) ) ;
    }
    if ( ( pMessage.getE ( ) instanceof Row )
        && ( ( ( Row ) pMessage.getE ( ) ).getExpressions ( 0 ) instanceof Attr ) )
    {
      Row r = ( Row ) pMessage.getE ( ) ;
      Attr a = ( Attr ) r.getExpressions ( 0 ) ;
      pContext.addProofStep ( getRuleByName ( "SEND-ATTR" ) , a ) ;
      Expression [ ] newE = new Expression [ r.getExpressions ( ).length - 1 ] ;
      for ( int i = 0 ; i < newE.length ; i ++ )
      {
        newE [ i ] = r.getExpressions ( i + 1 ).substitute (
            a.getIdentifier ( ) , a.getE ( ) ) ;
      }
      return new Message ( new Row ( newE ) , pMessage.getIdentifier ( ) ) ;
    }
    if ( ( pMessage.getE ( ) instanceof Row )
        && ( ( ( Row ) pMessage.getE ( ) ).getExpressions ( 0 ) instanceof Meth ) )
    {
      Row r = ( Row ) pMessage.getE ( ) ;
      Meth m = ( Meth ) r.getExpressions ( 0 ) ;
      if ( pMessage.getIdentifier ( ).equals ( m.getName ( ) ) )
      {
        pContext.addProofStep ( getRuleByName ( "SEND-EXEC" ) , m ) ;
        return new Identifier ( "TODO" ) ;
      }
      pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , m ) ;
      Expression [ ] newE = new Expression [ r.getExpressions ( ).length - 1 ] ;
      for ( int i = 0 ; i < newE.length ; i ++ )
      {
        newE [ i ] = r.getExpressions ( i + 1 ).clone ( ) ;
      }
      return new Message ( new Row ( newE ) , pMessage.getIdentifier ( ) ) ;
    }
    return pMessage ;
  }


  public Expression evaluateAttr ( SmallStepProofContext pContext , Attr pAttr )
  {
    if ( pAttr.isValue ( ) )
    {
      pContext.addProofStep ( getRuleByName ( "ATTR-RIGHT" ) , pAttr ) ;
      Expression attrE = evaluate ( pContext , pAttr.getE ( ) ) ;
      if ( attrE.isException ( ) )
      {
        return attrE ;
      }
    }
    else
    {
      pContext.addProofStep ( getRuleByName ( "ATTR-EVAL" ) , pAttr ) ;
      Expression attrE = evaluate ( pContext , pAttr.getE ( ) ) ;
      if ( attrE.isException ( ) )
      {
        return attrE ;
      }
      return new Attr ( pAttr.getIdentifier ( ) , attrE ) ;
    }
    return pAttr ;
  }


  public Expression evaluateMeth ( SmallStepProofContext pContext , Meth pMeth )
  {
    pContext.addProofStep ( getRuleByName ( "METH-RIGHT" ) , pMeth ) ;
    return pMeth ;
  }


  public Expression evaluateCurriedMeth ( SmallStepProofContext pContext ,
      CurriedMeth pCurriedMeth )
  {
    pContext.addProofStep ( getRuleByName ( "METH-RIGHT" ) , pCurriedMeth ) ;
    return pCurriedMeth ;
  }


  public Expression evaluateRow ( SmallStepProofContext pContext , Row pRow )
  {
    for ( int i = 0 ; i < pRow.getExpressions ( ).length ; i ++ )
    {
      // Attr
      if ( pRow.getExpressions ( i ) instanceof Attr )
      {
        if ( pRow.getExpressions ( i ).isValue ( ) )
        {
          Attr attr = ( Attr ) pRow.getExpressions ( i ) ;
          Expression newAttr = evaluate ( pContext , attr ) ;
          if ( newAttr.isException ( ) )
          {
            return newAttr ;
          }
        }
        else
        {
          Attr attr = ( Attr ) pRow.getExpressions ( i ) ;
          Expression newAttr = evaluate ( pContext , attr ) ;
          if ( newAttr.isException ( ) )
          {
            return newAttr ;
          }
          Expression [ ] tmp = new Expression [ pRow.getExpressions ( ).length ] ;
          for ( int j = 0 ; j < tmp.length ; j ++ )
          {
            tmp [ j ] = pRow.getExpressions ( j ).clone ( ) ;
          }
          tmp [ i ] = newAttr ;
          return new Row ( tmp ) ;
        }
      }
      // Meth
      else if ( pRow.getExpressions ( i ) instanceof Meth )
      {
        Meth meth = ( Meth ) pRow.getExpressions ( i ) ;
        Expression newMeth = evaluate ( pContext , meth ) ;
        if ( newMeth.isException ( ) )
        {
          return newMeth ;
        }
      }
      // CurriedMeth
      else if ( pRow.getExpressions ( i ) instanceof CurriedMeth )
      {
        CurriedMeth curriedMeth = ( CurriedMeth ) pRow.getExpressions ( i ) ;
        Expression newCurriedMeth = evaluate ( pContext , curriedMeth ) ;
        if ( newCurriedMeth.isException ( ) )
        {
          return newCurriedMeth ;
        }
      }
    }
    return pRow ;
  }
}
