package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class L2OSmallStepProofRuleSet extends L2SmallStepProofRuleSet
{
  /**
   * TODO
   * 
   * @param pL2OLanguage TODO
   */
  public L2OSmallStepProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage ) ;
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


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pObjectExpr TODO
   * @return TODO
   */
  public Expression evaluateObjectExpr ( SmallStepProofContext pContext ,
      ObjectExpr pObjectExpr )
  {
    pContext.addProofStep ( getRuleByName ( "OBJ-EVAL" ) , pObjectExpr ) ; //$NON-NLS-1$
    Expression row = evaluate ( pContext , pObjectExpr.getE ( ) ) ;
    if ( row.isException ( ) )
    {
      return row ;
    }
    return pObjectExpr.getIdentifier ( ) == null ? new ObjectExpr ( ( Row ) row )
        : new ObjectExpr ( pObjectExpr.getIdentifier ( ) , ( Row ) row ) ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pMessage TODO
   * @return TODO
   */
  public Expression evaluateMessage ( SmallStepProofContext pContext ,
      Message pMessage )
  {
    if ( ! pMessage.getE ( ).isValue ( ) )
    {
      pContext.addProofStep ( getRuleByName ( "SEND-EVAL" ) , pMessage ) ; //$NON-NLS-1$
      Expression e = evaluate ( pContext , pMessage.getE ( ) ) ;
      if ( e.isException ( ) )
      {
        return e ;
      }
      return new Message ( e , pMessage.getIdentifier ( ) ) ;
    }
    if ( pMessage.getE ( ) instanceof ObjectExpr )
    {
      pContext.addProofStep ( getRuleByName ( "OBJ-SEND" ) , pMessage ) ; //$NON-NLS-1$
      ObjectExpr o = ( ObjectExpr ) pMessage.getE ( ) ;
      Row r = ( Row ) o.getE ( ) ;
      Expression newRow = r.substitute ( "self" , o.clone ( ) ) ; //$NON-NLS-1$
      return new Message ( newRow , pMessage.getIdentifier ( ) ) ;
    }
    if ( pMessage.getE ( ) instanceof Row )
    {
      Expression firstExpr = ( ( Row ) pMessage.getE ( ) ).getExpressions ( 0 ) ;
      if ( firstExpr instanceof Meth )
      {
        Row r = ( Row ) pMessage.getE ( ) ;
        Meth m = ( Meth ) r.getExpressions ( 0 ) ;
        if ( pMessage.getIdentifier ( ).equals ( m.getIdentifier ( ) ) )
        {
          boolean definedLater = false ;
          for ( int i = 1 ; i < r.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = r.getExpressions ( i ) ;
            if ( ( tmp instanceof Meth )
                && ( ( ( Meth ) tmp ).getIdentifier ( ).equals ( pMessage
                    .getIdentifier ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMeth )
                && ( ( ( CurriedMeth ) tmp ).getIdentifiers ( 0 )
                    .equals ( pMessage.getIdentifier ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
          }
          if ( ! definedLater )
          {
            pContext.addProofStep ( getRuleByName ( "SEND-EXEC" ) , m ) ; //$NON-NLS-1$
            return m.getE ( ) ;
          }
        }
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , m ) ; //$NON-NLS-1$
        Expression [ ] newE = new Expression [ r.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newE.length ; i ++ )
        {
          newE [ i ] = r.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newE ) , pMessage.getIdentifier ( ) ) ;
      }
      else if ( firstExpr instanceof CurriedMeth )
      {
        Row r = ( Row ) pMessage.getE ( ) ;
        CurriedMeth cm = ( CurriedMeth ) r.getExpressions ( 0 ) ;
        if ( pMessage.getIdentifier ( ).equals ( cm.getIdentifiers ( 0 ) ) )
        {
          boolean definedLater = false ;
          for ( int i = 1 ; i < r.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = r.getExpressions ( i ) ;
            if ( ( tmp instanceof Meth )
                && ( ( ( Meth ) tmp ).getIdentifier ( ).equals ( pMessage
                    .getIdentifier ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMeth )
                && ( ( ( CurriedMeth ) tmp ).getIdentifiers ( 0 )
                    .equals ( pMessage.getIdentifier ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
          }
          if ( ! definedLater )
          {
            pContext.addProofStep ( getRuleByName ( "SEND-EXEC" ) , cm ) ; //$NON-NLS-1$
            Expression e = cm.getE ( ) ;
            for ( int i = cm.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
            {
              e = new Lambda ( cm.getIdentifiers ( i ) , null , e ) ;
            }
            return e ;
          }
        }
        if ( r.getExpressions ( ).length == 1 )
        {
          return pMessage ;
        }
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , cm ) ; //$NON-NLS-1$
        Expression [ ] newE = new Expression [ r.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newE.length ; i ++ )
        {
          newE [ i ] = r.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newE ) , pMessage.getIdentifier ( ) ) ;
      }
      else if ( firstExpr instanceof Attr )
      {
        Row r = ( Row ) pMessage.getE ( ) ;
        Attr a = ( Attr ) r.getExpressions ( 0 ) ;
        pContext.addProofStep ( getRuleByName ( "SEND-ATTR" ) , a ) ; //$NON-NLS-1$
        Expression [ ] newE = new Expression [ r.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newE.length ; i ++ )
        {
          newE [ i ] = r.getExpressions ( i + 1 ).substitute (
              a.getIdentifier ( ) , a.getE ( ) ) ;
        }
        return new Message ( new Row ( newE ) , pMessage.getIdentifier ( ) ) ;
      }
    }
    return pMessage ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pAttr TODO
   * @return TODO
   */
  public Expression evaluateAttr ( SmallStepProofContext pContext , Attr pAttr )
  {
    if ( pAttr.isValue ( ) )
    {
      pContext.addProofStep ( getRuleByName ( "ATTR-RIGHT" ) , pAttr ) ; //$NON-NLS-1$
      Expression attrE = evaluate ( pContext , pAttr.getE ( ) ) ;
      if ( attrE.isException ( ) )
      {
        return attrE ;
      }
    }
    else
    {
      pContext.addProofStep ( getRuleByName ( "ATTR-EVAL" ) , pAttr ) ; //$NON-NLS-1$
      Expression attrE = evaluate ( pContext , pAttr.getE ( ) ) ;
      if ( attrE.isException ( ) )
      {
        return attrE ;
      }
      return new Attr ( pAttr.getIdentifier ( ) , attrE ) ;
    }
    return pAttr ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pMeth TODO
   * @return TODO
   */
  public Expression evaluateMeth ( SmallStepProofContext pContext , Meth pMeth )
  {
    pContext.addProofStep ( getRuleByName ( "METH-RIGHT" ) , pMeth ) ; //$NON-NLS-1$
    return pMeth ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pCurriedMeth TODO
   * @return TODO
   */
  public Expression evaluateCurriedMeth ( SmallStepProofContext pContext ,
      CurriedMeth pCurriedMeth )
  {
    pContext.addProofStep ( getRuleByName ( "METH-RIGHT" ) , pCurriedMeth ) ; //$NON-NLS-1$
    return pCurriedMeth ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pRow TODO
   * @return TODO
   */
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
