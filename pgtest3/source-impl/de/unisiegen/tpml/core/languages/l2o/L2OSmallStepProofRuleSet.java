package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
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
    // Attr
    register ( L2OLanguage.L2O , "ATTR-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RENAME" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "ATTR-RIGHT" , false ) ; //$NON-NLS-1$
    // Meth
    register ( L2OLanguage.L2O , "METH-RIGHT" , false ) ; //$NON-NLS-1$
    // Obj
    register ( L2OLanguage.L2O , "OBJ-EVAL" , false ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "OBJ-UNFOLD" , true ) ; //$NON-NLS-1$
    // Send
    register ( L2OLanguage.L2O , "SEND-ATTR" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-SKIP" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-EXEC" , true ) ; //$NON-NLS-1$
    register ( L2OLanguage.L2O , "SEND-EVAL" , false ) ; //$NON-NLS-1$  
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
    /*
     * If the Expression is an ObjectExpr, we can only perform OBJ-EVAL.
     */
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
      /*
       * If the child Expression of the Message is not yet a value, we have to
       * perform SEND-EVAL.
       */
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
      /*
       * If the child Expression of the Message is an ObjectExpr and the
       * ObjectExpr is a value, we have to perform OBJ-UNFOLD.
       */
      pContext.addProofStep ( getRuleByName ( "OBJ-UNFOLD" ) , pMessage ) ; //$NON-NLS-1$
      ObjectExpr objectExpr = ( ObjectExpr ) pMessage.getE ( ) ;
      Row row = objectExpr.getE ( ) ;
      Expression newRow = row.substitute ( objectExpr.getId ( ) , objectExpr
          .clone ( ) ) ;
      return new Message ( newRow , pMessage.getId ( ) ) ;
    }
    else if ( pMessage.getE ( ) instanceof Row )
    {
      /*
       * If the child Expression of the Message is a Row and the Row has zero
       * children the Expression gets stuck.
       */
      Row row = ( Row ) pMessage.getE ( ) ;
      if ( row.getExpressions ( ).length == 0 )
      {
        return pMessage ;
      }
      Expression firstRowChild = row.getExpressions ( 0 ) ;
      // Attribute
      if ( firstRowChild instanceof Attribute )
      {
        /*
         * If the child Expression of the Message is a Row and the first child
         * Expression of the Row is an Attribute, we have to perform SEND-ATTR.
         * We can only substitute in Methods and CurriedMethods, not in
         * Attributes.
         */
        Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
        pContext.addProofStep ( getRuleByName ( "SEND-ATTR" ) , attribute ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          Expression child = row.getExpressions ( i + 1 ) ;
          if ( child instanceof Attribute )
          {
            newRowE [ i ] = child ;
          }
          else if ( ( child instanceof Method )
              || ( child instanceof CurriedMethod ) )
          {
            newRowE [ i ] = child.substitute ( attribute.getId ( ) , attribute
                .getE ( ) ) ;
          }
          else
          {
            /*
             * Programming error: The child of the Row is not an Attribute,
             * Method or CurriedMethod. This should not happen.
             */
            throw new IllegalStateException (
                "Inconsistent L20SmallStepProofRuleSet class." ) ; //$NON-NLS-1$
          }
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      // Method or CurriedMethod
      else if ( ( firstRowChild instanceof Method )
          || ( firstRowChild instanceof CurriedMethod ) )
      {
        String id ;
        Expression methE ;
        if ( firstRowChild instanceof Method )
        {
          Method method = ( Method ) firstRowChild ;
          id = method.getId ( ) ;
          methE = method.getE ( ) ;
        }
        else
        {
          CurriedMethod curriedMethod = ( CurriedMethod ) firstRowChild ;
          id = curriedMethod.getIdentifiers ( 0 ) ;
          methE = curriedMethod.getE ( ) ;
        }
        if ( pMessage.getId ( ).equals ( id ) )
        {
          /*
           * If the child Expression of the Message is a Row, the first child
           * Expression of the Row is a Method or CurriedMethod, the Identifier
           * of the Message is equal to the Identifier of the Method or
           * CurriedMethod and there is no Method or CurriedMethod with the same
           * Identifier in the rest of the Row, we have to perform SEND-EXEC.
           */
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
            else if ( ( tmp instanceof CurriedMethod )
                && ( ( ( CurriedMethod ) tmp ).getIdentifiers ( 0 )
                    .equals ( pMessage.getId ( ) ) ) )
            {
              definedLater = true ;
              break ;
            }
          }
          if ( ! definedLater )
          {
            pContext.addProofStep (
                getRuleByName ( "SEND-EXEC" ) , firstRowChild ) ; //$NON-NLS-1$
            return methE ;
          }
        }
        /*
         * If the child Expression of the Message is a Row, the first child
         * Expression of the Row is a Method or CurriedMethod, the Identifier of
         * the Message is not equal to the Identifier of the Method or
         * CurriedMethod or there is a Method or CurriedMethod with the same
         * Identifier in the rest of the Row, we have to perform SEND-SKIP.
         */
        pContext.addProofStep ( getRuleByName ( "SEND-SKIP" ) , firstRowChild ) ; //$NON-NLS-1$
        Expression [ ] newRowE = new Expression [ row.getExpressions ( ).length - 1 ] ;
        for ( int i = 0 ; i < newRowE.length ; i ++ )
        {
          newRowE [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
        }
        return new Message ( new Row ( newRowE ) , pMessage.getId ( ) ) ;
      }
      else
      {
        /*
         * Programming error: The child of the Row is not an Attribute, Method
         * or CurriedMethod. This should not happen.
         */
        throw new IllegalStateException (
            "Inconsistent L20SmallStepProofRuleSet class." ) ; //$NON-NLS-1$
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
    /*
     * Check if all children of the Duplication are values or not.
     */
    boolean allChildrenAreValues = true ;
    for ( Expression expr : pDuplication.getExpressions ( ) )
    {
      if ( ! expr.isValue ( ) )
      {
        allChildrenAreValues = false ;
        break ;
      }
    }
    /*
     * If not all children of the Duplication are values and the first
     * Expression of the Duplication is a value, we have to perform DUPL-EVAL.
     */
    if ( ( ! allChildrenAreValues ) && ( pDuplication.getE ( ).isValue ( ) ) )
    {
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
    /*
     * If all children of the Duplication are values, the first Expression of
     * the Duplication is an ObjectExpr and this ObjectExpr is a value, we have
     * to perform DUPL-EXEC.
     */
    if ( ( allChildrenAreValues )
        && ( pDuplication.getE ( ) instanceof ObjectExpr )
        && ( pDuplication.getE ( ).isValue ( ) ) )
    {
      ObjectExpr objectExpr = ( ObjectExpr ) pDuplication.getE ( ) ;
      Row row = objectExpr.getE ( ) ;
      Expression [ ] newRowE = row.getExpressions ( ).clone ( ) ;
      /*
       * Search all Identifiers of the Duplication in the Row of the ObjectExpr
       * and replace the Attribute, if the Identifiers are equal.
       */
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
        /*
         * If the Duplication contains an Identifier which is not found in the
         * Row, the Expression gets stuck.
         */
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
      /*
       * If the current child of the Row is an Attribute, we have to perform
       * ATTR-EVAL, ATTR-RENAME or ATTR-RIGHT.
       */
      Expression currentRowChild = pRow.getExpressions ( i ) ;
      if ( currentRowChild instanceof Attribute )
      {
        /*
         * If the Attribute is not yet a value, we have to perform ATTR-EVAL.
         */
        Attribute attribute = ( Attribute ) currentRowChild ;
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
         * Rename the current Attribute and all Identifiers, which are bound to
         * the Identifier of the Attrbute.
         */
        if ( attrRename )
        {
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
            if ( tmp [ j ] instanceof Attribute )
            {
              Attribute currentAttribute = ( Attribute ) tmp [ j ] ;
              if ( currentAttribute.getId ( ).equals ( attribute.getId ( ) ) )
              {
                break ;
              }
            }
            else if ( ( tmp [ j ] instanceof Method )
                || ( tmp [ j ] instanceof CurriedMethod ) )
            {
              tmp [ j ] = tmp [ j ].substitute ( attribute.getId ( ) ,
                  new Identifier ( newId ) , true ) ;
            }
            else
            {
              /*
               * Programming error: The child of the Row is not an Attribute,
               * Method or CurriedMethod. This should not happen.
               */
              throw new IllegalStateException (
                  "Inconsistent L20SmallStepProofRuleSet class." ) ; //$NON-NLS-1$
            }
          }
          return new Row ( tmp ) ;
        }
        /*
         * If the Attribute is a value and it has not to be renamed, we have to
         * perform ATTR-RIGHT.
         */
        pContext.addProofStep ( getRuleByName ( "ATTR-RIGHT" ) , attribute ) ; //$NON-NLS-1$
      }
      else if ( ( currentRowChild instanceof Method )
          || ( currentRowChild instanceof CurriedMethod ) )
      {
        /*
         * If the current child is a Method or CurriedMethod, we have to perform
         * METH-RIGHT.
         */
        pContext.addProofStep (
            getRuleByName ( "METH-RIGHT" ) , currentRowChild ) ; //$NON-NLS-1$
      }
      else
      {
        /*
         * Programming error: The child of the Row is not an Attribute, Method
         * or CurriedMethod. This should not happen.
         */
        throw new IllegalStateException (
            "Inconsistent L20SmallStepProofRuleSet class." ) ; //$NON-NLS-1$
      }
    }
    return pRow ;
  }
}
