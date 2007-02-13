package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
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
    register ( L2OLanguage.L2O , "OBJ-UNFOLD" , true ) ; //$NON-NLS-1$
    // Attr
    register ( L2OLanguage.L2O , "ATTR-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RIGHT" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RENAME" , true ) ; //$NON-NLS-1$
    // Meth
    register ( L2OLanguage.L2O , "METH-RIGHT" , false ) ; //$NON-NLS-1$
    // Send
    register ( L2OLanguage.L2O , "SEND-EVAL" , false ) ; //$NON-NLS-1$  
    register ( L2OLanguage.L2O , "SEND-ATTR" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-SKIP" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-EXEC" , true ) ; //$NON-NLS-1$
    // Dupl
    register ( L2OLanguage.L2O , "DUPL-EVAL" , false ) ; //$NON-NLS-1$  
    register ( L2OLanguage.L2O , "DUPL-EXEC" , true ) ; //$NON-NLS-1$
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
    return new ObjectExpr ( pObjectExpr.getId ( ) , pObjectExpr.getTau ( ) ,
        ( Row ) row ) ;
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
      // SEND-EVAL
      pContext.addProofStep ( getRuleByName ( "SEND-EVAL" ) , pMessage ) ; //$NON-NLS-1$
      Expression expr = evaluate ( pContext , pMessage.getE ( ) ) ;
      if ( expr.isException ( ) )
      {
        return expr ;
      }
      return new Message ( expr , pMessage.getId ( ) ) ;
    }
    else if ( pMessage.getE ( ) instanceof ObjectExpr )
    {
      // OBJ-UNFOLD
      pContext.addProofStep ( getRuleByName ( "OBJ-UNFOLD" ) , pMessage ) ; //$NON-NLS-1$
      ObjectExpr objectExpr = ( ObjectExpr ) pMessage.getE ( ) ;
      Row row = objectExpr.getE ( ) ;
      Expression newRow = row.substitute ( objectExpr.getId ( ) , objectExpr
          .clone ( ) ) ;
      return new Message ( newRow , pMessage.getId ( ) ) ;
    }
    else if ( pMessage.getE ( ) instanceof Row )
    {
      Row row = ( Row ) pMessage.getE ( ) ;
      Expression firstRowChild = row.getExpressions ( 0 ) ;
      if ( firstRowChild instanceof Attr )
      {
        // SEND-ATTR
        Attr attr = ( Attr ) row.getExpressions ( 0 ) ;
        pContext.addProofStep ( getRuleByName ( "SEND-ATTR" ) , attr ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).substitute (
              attr.getId ( ) , attr.getE ( ) ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      else if ( firstRowChild instanceof Meth )
      {
        Meth meth = ( Meth ) row.getExpressions ( 0 ) ;
        if ( pMessage.getId ( ).equals ( meth.getId ( ) ) )
        {
          // SEND-EXEC
          boolean definedLater = false ;
          for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = row.getExpressions ( i ) ;
            if ( ( tmp instanceof Meth )
                && ( ( ( Meth ) tmp ).getId ( ).equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMeth )
                && ( ( ( CurriedMeth ) tmp ).getIdentifiers ( 0 )
                    .equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
          }
          if ( ! definedLater )
          {
            pContext.addProofStep ( getRuleByName ( "SEND-EXEC" ) , meth ) ; //$NON-NLS-1$
            return meth.getE ( ) ;
          }
        }
        /*
         * If the row has only one child and the Message Identifier is not equal
         * to the Identifier of the Meth, it can not be skipped.
         */
        if ( row.getExpressions ( ).length == 1 )
        {
          return pMessage ;
        }
        // SEND-SKIP
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , meth ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      else if ( firstRowChild instanceof CurriedMeth )
      {
        CurriedMeth curriedMeth = ( CurriedMeth ) row.getExpressions ( 0 ) ;
        if ( pMessage.getId ( ).equals ( curriedMeth.getIdentifiers ( 0 ) ) )
        {
          // SEND-EXEC
          boolean definedLater = false ;
          for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = row.getExpressions ( i ) ;
            if ( ( tmp instanceof Meth )
                && ( ( ( Meth ) tmp ).getId ( ).equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMeth )
                && ( ( ( CurriedMeth ) tmp ).getIdentifiers ( 0 )
                    .equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
          }
          if ( ! definedLater )
          {
            pContext
                .addProofStep ( getRuleByName ( "SEND-EXEC" ) , curriedMeth ) ; //$NON-NLS-1$
            Expression expr = curriedMeth.getE ( ) ;
            for ( int i = curriedMeth.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
            {
              expr = new Lambda ( curriedMeth.getIdentifiers ( i ) ,
                  curriedMeth.getTypes ( i ) , expr ) ;
            }
            return expr ;
          }
        }
        /*
         * If the row has only one child and the Message Identifier is not equal
         * to the Identifier of the CurriedMeth, it can not be skipped.
         */
        if ( row.getExpressions ( ).length == 1 )
        {
          return pMessage ;
        }
        // SEND-SKIP
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , curriedMeth ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
    }
    return pMessage ;
  }


  /**
   * TODO
   * 
   * @param pContext TODO
   * @param pDuplication TODO
   * @return TODO
   */
  public Expression evaluateDuplication ( SmallStepProofContext pContext ,
      Duplication pDuplication )
  {
    boolean allChildrenAreValues = true ;
    for ( Expression expr : pDuplication.getExpressions ( ) )
    {
      if ( ! expr.isValue ( ) )
      {
        allChildrenAreValues = false ;
        break ;
      }
    }
    if ( ( ! allChildrenAreValues ) && ( pDuplication.getE ( ).isValue ( ) ) )
    {
      // DUPL-EVAL
      pContext.addProofStep ( getRuleByName ( "DUPL-EVAL" ) , pDuplication ) ; //$NON-NLS-1$
      Expression [ ] newDuplicationE = pDuplication.getExpressions ( ).clone ( ) ;
      for ( int i = 0 ; i < newDuplicationE.length ; i ++ )
      {
        if ( ! newDuplicationE [ i ].isValue ( ) )
        {
          newDuplicationE [ i ] = evaluate ( pContext , newDuplicationE [ i ] ) ;
          return new Duplication ( pDuplication.getE ( ).clone ( ) ,
              pDuplication.getIdentifiers ( ) , newDuplicationE ) ;
        }
      }
    }
    if ( ( allChildrenAreValues )
        && ( pDuplication.getE ( ) instanceof ObjectExpr )
        && ( pDuplication.getE ( ).isValue ( ) ) )
    {
      // DUPL-EXEC
      pContext.addProofStep ( getRuleByName ( "DUPL-EXEC" ) , pDuplication ) ; //$NON-NLS-1$
      ObjectExpr objectExpr = ( ObjectExpr ) pDuplication.getE ( ) ;
      Row row = objectExpr.getE ( ) ;
      Expression [ ] newRowE = row.getExpressions ( ).clone ( ) ;
      for ( int i = 0 ; i < newRowE.length ; i ++ )
      {
        if ( newRowE [ i ] instanceof Attr )
        {
          Attr attr = ( Attr ) newRowE [ i ] ;
          for ( int j = 0 ; j < pDuplication.getIdentifiers ( ).length ; j ++ )
          {
            if ( attr.getId ( ).equals ( pDuplication.getIdentifiers ( j ) ) )
            {
              newRowE [ i ] = new Attr ( attr.getId ( ) , attr.getTau ( ) ,
                  pDuplication.getExpressions ( j ) ) ;
            }
          }
        }
      }
      return new ObjectExpr ( objectExpr.getId ( ) , objectExpr.getTau ( ) ,
          new Row ( newRowE ) ) ;
    }
    return pDuplication ;
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
      if ( pRow.getExpressions ( i ) instanceof Attr )
      {
        // Attr
        Attr attr = ( Attr ) pRow.getExpressions ( i ) ;
        if ( ! attr.isValue ( ) )
        {
          // ATTR-EVAL
          pContext.addProofStep ( getRuleByName ( "ATTR-EVAL" ) , attr ) ; //$NON-NLS-1$
          Expression attrE = evaluate ( pContext , attr.getE ( ) ) ;
          if ( attrE.isException ( ) )
          {
            return attrE ;
          }
          Expression [ ] tmp = pRow.getExpressions ( ).clone ( ) ;
          tmp [ i ] = new Attr ( attr.getId ( ) , attr.getTau ( ) , attrE ) ;
          return new Row ( tmp ) ;
        }
        // ATTR-RENAME or ATTR-RIGHT
        boolean attrRename = false ;
        for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
        {
          if ( ( pRow.getExpressions ( j ) instanceof Attr )
              && ( ( Attr ) pRow.getExpressions ( j ) ).getId ( ).equals (
                  attr.getId ( ) ) )
          {
            attrRename = true ;
            break ;
          }
        }
        if ( attrRename )
        {
          // ATTR-RENAME
          pContext.addProofStep ( getRuleByName ( "ATTR-RENAME" ) , attr ) ; //$NON-NLS-1$ 
          Expression [ ] tmp = pRow.getExpressions ( ).clone ( ) ;
          String newId = attr.getId ( ) + "'" ; //$NON-NLS-1$ 
          while ( attrRename )
          {
            attrRename = false ;
            for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
            {
              if ( ( pRow.getExpressions ( j ) instanceof Attr )
                  && ( ( Attr ) pRow.getExpressions ( j ) ).getId ( ).equals (
                      newId ) )
              {
                newId += "'" ;//$NON-NLS-1$ 
                attrRename = true ;
                break ;
              }
            }
          }
          tmp [ i ] = new Attr ( newId , attr.getTau ( ) , attr.getE ( )
              .clone ( ) ) ;
          for ( int j = i + 1 ; j < tmp.length ; j ++ )
          {
            if ( ( tmp [ j ] instanceof Attr )
                && ( ( ( Attr ) tmp [ j ] ).getId ( ).equals ( attr.getId ( ) ) ) )
            {
              break ;
            }
            // Meth
            if ( tmp [ j ] instanceof Meth )
            {
              Meth meth = ( Meth ) tmp [ j ] ;
              if ( meth.getE ( ) instanceof Duplication )
              {
                tmp [ j ] = meth.substituteAttr ( attr.getId ( ) ,
                    new Identifier ( newId ) ) ;
              }
              else
              {
                tmp [ j ] = meth.substitute ( attr.getId ( ) , new Identifier (
                    newId ) ) ;
              }
            }
            // CurriedMeth
            else if ( tmp [ j ] instanceof CurriedMeth )
            {
              CurriedMeth curriedMeth = ( CurriedMeth ) tmp [ j ] ;
              if ( curriedMeth.getE ( ) instanceof Duplication )
              {
                tmp [ j ] = curriedMeth.substituteAttr ( attr.getId ( ) ,
                    new Identifier ( newId ) ) ;
              }
              else
              {
                tmp [ j ] = curriedMeth.substitute ( attr.getId ( ) ,
                    new Identifier ( newId ) ) ;
              }
            }
          }
          return new Row ( tmp ) ;
        }
        // ATTR-RIGHT
        pContext.addProofStep ( getRuleByName ( "ATTR-RIGHT" ) , attr ) ; //$NON-NLS-1$
        Expression attrE = evaluate ( pContext , attr.getE ( ) ) ;
        if ( attrE.isException ( ) )
        {
          return attrE ;
        }
      }
      else
      {
        // Meth or CurriedMeth
        pContext.addProofStep (
            getRuleByName ( "METH-RIGHT" ) , pRow.getExpressions ( i ) ) ; //$NON-NLS-1$
      }
    }
    return pRow ;
  }
}
