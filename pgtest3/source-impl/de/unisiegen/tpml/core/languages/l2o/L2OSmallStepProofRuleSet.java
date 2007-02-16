package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Method ;
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
    // Attribute
    register ( L2OLanguage.L2O , "ATTR-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RIGHT" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RENAME" , true ) ; //$NON-NLS-1$
    // Method
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
      /*
       * If the row has zero children it gets stuck.
       */
      if ( row.getExpressions ( ).length == 0 )
      {
        return pMessage ;
      }
      Expression firstRowChild = row.getExpressions ( 0 ) ;
      if ( firstRowChild instanceof Attribute )
      {
        // SEND-ATTR
        Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
        pContext.addProofStep ( getRuleByName ( "SEND-ATTR" ) , attribute ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).substitute (
              attribute.getId ( ) , attribute.getE ( ) ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      else if ( firstRowChild instanceof Method )
      {
        Method meth = ( Method ) row.getExpressions ( 0 ) ;
        if ( pMessage.getId ( ).equals ( meth.getId ( ) ) )
        {
          // SEND-EXEC
          boolean definedLater = false ;
          for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = row.getExpressions ( i ) ;
            if ( ( tmp instanceof Method )
                && ( ( ( Method ) tmp ).getId ( ).equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMethod )
                && ( ( ( CurriedMethod ) tmp ).getIdentifiers ( 0 )
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
        // SEND-SKIP
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , meth ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      else if ( firstRowChild instanceof CurriedMethod )
      {
        CurriedMethod curriedMeth = ( CurriedMethod ) row.getExpressions ( 0 ) ;
        if ( pMessage.getId ( ).equals ( curriedMeth.getIdentifiers ( 0 ) ) )
        {
          // SEND-EXEC
          boolean definedLater = false ;
          for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
          {
            Expression tmp = row.getExpressions ( i ) ;
            if ( ( tmp instanceof Method )
                && ( ( ( Method ) tmp ).getId ( ).equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
            if ( ( tmp instanceof CurriedMethod )
                && ( ( ( CurriedMethod ) tmp ).getIdentifiers ( 0 )
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
      ObjectExpr objectExpr = ( ObjectExpr ) pDuplication.getE ( ) ;
      Row row = objectExpr.getE ( ) ;
      Expression [ ] newRowE = row.getExpressions ( ).clone ( ) ;
      boolean found ;
      for ( int i = 0 ; i < pDuplication.getIdentifiers ( ).length ; i ++ )
      {
        found = false ;
        for ( int j = 0 ; j < newRowE.length ; j ++ )
        {
          if ( newRowE [ j ] instanceof Attribute )
          {
            Attribute attribute = ( Attribute ) newRowE [ j ] ;
            if ( attribute.getId ( )
                .equals ( pDuplication.getIdentifiers ( i ) ) )
            {
              newRowE [ j ] = new Attribute ( attribute.getId ( ) , attribute
                  .getTau ( ) , pDuplication.getExpressions ( i ) ) ;
              found = true ;
            }
          }
        }
        if ( ! found )
        {
          return pDuplication ;
        }
      }
      pContext.addProofStep ( getRuleByName ( "DUPL-EXEC" ) , pDuplication ) ; //$NON-NLS-1$
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
      // Attribute
      if ( pRow.getExpressions ( i ) instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) pRow.getExpressions ( i ) ;
        /*
         * If the Attribute is not yet a value the ATTR-EVAL rule has to be
         * applied.
         */
        if ( ! attribute.isValue ( ) )
        {
          pContext.addProofStep ( getRuleByName ( "ATTR-EVAL" ) , attribute ) ; //$NON-NLS-1$
          Expression attrE = evaluate ( pContext , attribute.getE ( ) ) ;
          if ( attrE.isException ( ) )
          {
            return attrE ;
          }
          Expression [ ] tmp = pRow.getExpressions ( ).clone ( ) ;
          tmp [ i ] = new Attribute ( attribute.getId ( ) ,
              attribute.getTau ( ) , attrE ) ;
          return new Row ( tmp ) ;
        }
        /*
         * Check, if the current Attribute has to be renamed with the rule
         * ATTR-RENAME. It has to be renamed, if in the rest of the Row an
         * Attribute with the same Identifier exists.
         */
        boolean attrRename = false ;
        for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
        {
          if ( ( pRow.getExpressions ( j ) instanceof Attribute )
              && ( ( Attribute ) pRow.getExpressions ( j ) ).getId ( ).equals (
                  attribute.getId ( ) ) )
          {
            attrRename = true ;
            break ;
          }
        }
        /*
         * Rename the current Attribute and all its bindings in the rest of the
         * Row.
         */
        if ( attrRename )
        {
          // ATTR-RENAME
          pContext.addProofStep ( getRuleByName ( "ATTR-RENAME" ) , attribute ) ; //$NON-NLS-1$ 
          Expression [ ] tmp = pRow.getExpressions ( ).clone ( ) ;
          String newId = attribute.getId ( ) + "'" ; //$NON-NLS-1$ 
          while ( attrRename )
          {
            attrRename = false ;
            for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
            {
              if ( ( pRow.getExpressions ( j ) instanceof Attribute )
                  && ( ( Attribute ) pRow.getExpressions ( j ) ).getId ( )
                      .equals ( newId ) )
              {
                newId += "'" ;//$NON-NLS-1$ 
                attrRename = true ;
                break ;
              }
            }
          }
          tmp [ i ] = new Attribute ( newId , attribute.getTau ( ) , attribute
              .getE ( ).clone ( ) ) ;
          for ( int j = i + 1 ; j < tmp.length ; j ++ )
          {
            if ( ( tmp [ j ] instanceof Attribute )
                && ( ( ( Attribute ) tmp [ j ] ).getId ( ).equals ( attribute
                    .getId ( ) ) ) )
            {
              break ;
            }
            // Method or CurriedMethod
            tmp [ j ] = tmp [ j ].substitute ( attribute.getId ( ) ,
                new Identifier ( newId ) , true ) ;
          }
          return new Row ( tmp ) ;
        }
        /*
         * Apply the rule ATTR-RIGHT.
         */
        pContext.addProofStep ( getRuleByName ( "ATTR-RIGHT" ) , attribute ) ; //$NON-NLS-1$
        Expression attrE = evaluate ( pContext , attribute.getE ( ) ) ;
        if ( attrE.isException ( ) )
        {
          return attrE ;
        }
      }
      // Method or CurriedMethod
      else
      {
        /*
         * Apply the rule METH-RIGHT.
         */
        pContext.addProofStep (
            getRuleByName ( "METH-RIGHT" ) , pRow.getExpressions ( i ) ) ; //$NON-NLS-1$
      }
    }
    return pRow ;
  }
}
