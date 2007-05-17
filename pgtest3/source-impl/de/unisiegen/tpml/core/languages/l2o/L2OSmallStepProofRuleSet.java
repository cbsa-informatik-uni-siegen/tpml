package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.l2.L2SmallStepProofRuleSet ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofContext ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Small step proof rules for the <code>L2O</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev:1132 $
 * @see L2SmallStepProofRuleSet
 */
public class L2OSmallStepProofRuleSet extends L2SmallStepProofRuleSet
{
  /**
   * The string of the <code>SEND-EVAL</code> rule.
   */
  private static final String SEND_EVAL = "SEND-EVAL" ; //$NON-NLS-1$


  /**
   * The string of the <code>SEND-EXEC</code> rule.
   */
  private static final String SEND_EXEC = "SEND-EXEC" ; //$NON-NLS-1$


  /**
   * The string of the <code>SEND-SKIP</code> rule.
   */
  private static final String SEND_SKIP = "SEND-SKIP" ; //$NON-NLS-1$


  /**
   * The string of the <code>SEND-ATTR</code> rule.
   */
  private static final String SEND_ATTR = "SEND-ATTR" ; //$NON-NLS-1$


  /**
   * The string of the <code>SEND-UNFOLD</code> rule.
   */
  private static final String SEND_UNFOLD = "SEND-UNFOLD" ; //$NON-NLS-1$


  /**
   * The string of the <code>OBJECT-EVAL</code> rule.
   */
  private static final String OBJECT_EVAL = "OBJECT-EVAL" ; //$NON-NLS-1$


  /**
   * The string of the <code>METHOD-RIGHT</code> rule.
   */
  private static final String METHOD_RIGHT = "METHOD-RIGHT" ; //$NON-NLS-1$


  /**
   * The string of the <code>"ATTR-RIGHT</code> rule.
   */
  private static final String ATTR_RIGHT = "ATTR-RIGHT" ; //$NON-NLS-1$


  /**
   * The string of the <code>ATTR-EVAL</code> rule.
   */
  private static final String ATTR_EVAL = "ATTR-EVAL" ; //$NON-NLS-1$


  /**
   * Allocates a new <code>L2OSmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L2O</tt> or a derived
   * language.
   * 
   * @param pL2OLanguage The {@link Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L2SmallStepProofRuleSet#L2SmallStepProofRuleSet(L2OLanguage)
   */
  public L2OSmallStepProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage ) ;
    // Attr
    register ( L2OLanguage.L2O , ATTR_EVAL , false ) ;
    register ( L2OLanguage.L2O , ATTR_RIGHT , false ) ;
    // Method
    register ( L2OLanguage.L2O , METHOD_RIGHT , false ) ;
    // Object
    register ( L2OLanguage.L2O , OBJECT_EVAL , false ) ;
    // Send
    register ( L2OLanguage.L2O , SEND_UNFOLD , true ) ;
    register ( L2OLanguage.L2O , SEND_ATTR , true ) ;
    register ( L2OLanguage.L2O , SEND_SKIP , true ) ;
    register ( L2OLanguage.L2O , SEND_EXEC , true ) ;
    register ( L2OLanguage.L2O , SEND_EVAL , false ) ;
  }


  /**
   * Evaluates the {@link ObjectExpr} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pObjectExpr The {@link ObjectExpr}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateObjectExpr ( SmallStepProofContext pContext ,
      ObjectExpr pObjectExpr )
  {
    /*
     * If the Expression is an ObjectExpr, we can only perform OBJECT-EVAL.
     */
    pContext.addProofStep ( getRuleByName ( OBJECT_EVAL ) , pObjectExpr ) ;
    Expression row = evaluate ( pContext , pObjectExpr.getE ( ) ) ;
    if ( row.isException ( ) )
    {
      return row ;
    }
    return new ObjectExpr ( pObjectExpr.getId ( ) , pObjectExpr.getTau ( ) ,
        row ) ;
  }


  /**
   * Evaluates the {@link Row} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pRow The {@link Row}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateRow ( SmallStepProofContext pContext , Row pRow )
  {
    Expression [ ] rowExpressions = pRow.getExpressions ( ) ;
    for ( int i = 0 ; i < rowExpressions.length ; i ++ )
    {
      /*
       * If the current child of the Row is an Attribute, we have to perform
       * ATTR-EVAL or ATTR-RIGHT.
       */
      Expression currentRowChild = rowExpressions [ i ] ;
      if ( currentRowChild instanceof Attribute )
      {
        /*
         * If the Attribute is not yet a value, we have to perform ATTR-EVAL.
         */
        Attribute attribute = ( Attribute ) currentRowChild ;
        if ( ! attribute.isValue ( ) )
        {
          pContext.addProofStep ( getRuleByName ( ATTR_EVAL ) , attribute ) ;
          Expression attrE = evaluate ( pContext , attribute.getE ( ) ) ;
          if ( attrE.isException ( ) )
          {
            return attrE ;
          }
          Expression [ ] newRowExpressions = new Expression [ rowExpressions.length ] ;
          for ( int j = 0 ; j < newRowExpressions.length ; j ++ )
          {
            if ( i != j )
            {
              newRowExpressions [ j ] = rowExpressions [ j ] ;
            }
          }
          newRowExpressions [ i ] = new Attribute ( attribute.getId ( ) , attrE ) ;
          return new Row ( newRowExpressions ) ;
        }
        /*
         * If the Attribute is a value, we have to perform ATTR-RIGHT.
         */
        pContext.addProofStep ( getRuleByName ( ATTR_RIGHT ) , attribute ) ;
      }
      else
      {
        /*
         * If the current child is a Method or CurriedMethod, we have to perform
         * METH-RIGHT.
         */
        pContext.addProofStep ( getRuleByName ( METHOD_RIGHT ) ,
            currentRowChild ) ;
      }
    }
    return pRow ;
  }


  /**
   * Evaluates the {@link Send} using <code>context</code>.
   * 
   * @param pContext The small step proof context.
   * @param pSend The {@link Send}.
   * @return The resulting {@link Expression}.
   */
  public Expression evaluateSend ( SmallStepProofContext pContext , Send pSend )
  {
    if ( ! pSend.getE ( ).isValue ( ) )
    {
      /*
       * If the child Expression of the Send is not yet a value, we have to
       * perform SEND-EVAL.
       */
      pContext.addProofStep ( getRuleByName ( SEND_EVAL ) , pSend ) ;
      Expression expr = evaluate ( pContext , pSend.getE ( ) ) ;
      if ( expr.isException ( ) )
      {
        return expr ;
      }
      return new Send ( expr , pSend.getId ( ) ) ;
    }
    else if ( pSend.getE ( ) instanceof ObjectExpr )
    {
      /*
       * If the child Expression of the Send is an ObjectExpr and the ObjectExpr
       * is a value, we have to perform SEND-UNFOLD.
       */
      ObjectExpr objectExpr = ( ObjectExpr ) pSend.getE ( ) ;
      Expression newRow ;
      newRow = objectExpr.getE ( ).substitute ( objectExpr.getId ( ) ,
          objectExpr ) ;
      pContext.addProofStep ( getRuleByName ( SEND_UNFOLD ) , pSend ) ;
      return new Send ( newRow , pSend.getId ( ) ) ;
    }
    else if ( pSend.getE ( ) instanceof Row )
    {
      /*
       * If the child Expression of the Send is a Row and the Row has zero
       * children the Expression gets stuck.
       */
      Row row = ( Row ) pSend.getE ( ) ;
      Expression [ ] rowExpressions = row.getExpressions ( ) ;
      if ( rowExpressions.length == 0 )
      {
        return pSend ;
      }
      Expression firstRowChild = rowExpressions [ 0 ] ;
      /*
       * Attribute
       */
      if ( firstRowChild instanceof Attribute )
      {
        /*
         * If the child Expression of the Send is a Row and the first child
         * Expression of the Row is an Attribute, we have to perform SEND-ATTR.
         */
        Attribute attribute = ( Attribute ) rowExpressions [ 0 ] ;
        Expression [ ] newRowExpressions = new Expression [ rowExpressions.length - 1 ] ;
        for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
        {
          Expression child = rowExpressions [ i + 1 ] ;
          newRowExpressions [ i ] = child.substitute ( attribute.getId ( ) ,
              attribute.getE ( ) ) ;
        }
        pContext.addProofStep ( getRuleByName ( SEND_ATTR ) , attribute ) ;
        return new Send ( new Row ( newRowExpressions ) , pSend.getId ( ) ) ;
      }
      /*
       * Method and CurriedMethod
       */
      Identifier id ;
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
        id = curriedMethod.getIdentifiers ( ) [ 0 ] ;
        methE = curriedMethod.getE ( ) ;
      }
      if ( pSend.getId ( ).equals ( id ) )
      {
        /*
         * If the child Expression of the Send is a Row, the first child
         * Expression of the Row is a Method or CurriedMethod, the Identifier of
         * the Send is equal to the Identifier of the Method or CurriedMethod
         * and there is no Method or CurriedMethod with the same Identifier in
         * the rest of the Row, we have to perform SEND-EXEC.
         */
        boolean definedLater = false ;
        for ( int i = 1 ; i < rowExpressions.length ; i ++ )
        {
          Expression rowChild = rowExpressions [ i ] ;
          if ( ( rowChild instanceof Method )
              && ( ( ( Method ) rowChild ).getId ( ).equals ( pSend.getId ( ) ) ) )
          {
            definedLater = true ;
            break ;
          }
          else if ( ( rowChild instanceof CurriedMethod )
              && ( ( ( CurriedMethod ) rowChild ).getIdentifiers ( ) [ 0 ]
                  .equals ( pSend.getId ( ) ) ) )
          {
            definedLater = true ;
            break ;
          }
        }
        if ( ! definedLater )
        {
          pContext.addProofStep ( getRuleByName ( SEND_EXEC ) , firstRowChild ) ;
          if ( firstRowChild instanceof CurriedMethod )
          {
            CurriedMethod curriedMethod = ( CurriedMethod ) firstRowChild ;
            Identifier [ ] identifier = curriedMethod.getIdentifiers ( ) ;
            MonoType [ ] types = curriedMethod.getTypes ( ) ;
            for ( int i = identifier.length - 1 ; i > 0 ; i -- )
            {
              methE = new Lambda ( identifier [ i ] , types [ i ] , methE ) ;
            }
            return methE ;
          }
          return methE ;
        }
      }
      /*
       * If the child Expression of the Send is a Row, the first child
       * Expression of the Row is a Method or CurriedMethod, the Identifier of
       * the Send is not equal to the Identifier of the Method or CurriedMethod
       * or there is a Method or CurriedMethod with the same Identifier in the
       * rest of the Row, we have to perform SEND-SKIP.
       */
      pContext.addProofStep ( getRuleByName ( SEND_SKIP ) , firstRowChild ) ;
      Expression [ ] newRowExpressions = new Expression [ rowExpressions.length - 1 ] ;
      for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = rowExpressions [ i + 1 ] ;
      }
      return new Send ( new Row ( newRowExpressions ) , pSend.getId ( ) ) ;
    }
    return pSend ;
  }
}
