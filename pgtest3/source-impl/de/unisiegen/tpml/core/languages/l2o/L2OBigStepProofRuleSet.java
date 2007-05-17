package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * Big step proof rules for the <b>L2O</b> and derived languages.
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 * @see L2BigStepProofRuleSet
 */
public class L2OBigStepProofRuleSet extends L2BigStepProofRuleSet
{
  /**
   * Allocates a new <code>L2OBigStepProofRuleSet</code> with the specified
   * <code>language</code>, which is the <b>L2O</b> or a derived language.
   * 
   * @param pL2OLanguage The language for the proof rule set.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L2BigStepProofRuleSet#L2BigStepProofRuleSet(L2Language)
   */
  public L2OBigStepProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage ) ;
    registerByMethodName ( L2OLanguage.L2O , "OBJECT" , "applyObject" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateObject" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateSend" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "SEND-ATTR" , "applySendAttr" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateSendAttr" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "SEND-SKIP" , "applySendSkip" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateSendSkip" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "SEND-EXEC" , "applySendExec" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateSendExec" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "OMEGA" , "applyOmega" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateAttr" ) ; //$NON-NLS-1$
    registerByMethodName ( L2OLanguage.L2O , "METHOD" , "applyMethod" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateMethod" ) ; //$NON-NLS-1$
  }


  /**
   * Applies the <b>(ATTR)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(ATTR)</b> rule to.
   */
  public void applyAttr ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    Attribute attribute = ( Attribute ) rowExpressions [ 0 ] ;
    pContext.addProofNode ( pNode , attribute.getE ( ) ) ;
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = rowExpressions [ i + 1 ] ;
    }
    pContext.addProofNode ( pNode , new Row ( newRowExpressions ) ) ;
  }


  /**
   * Applies the <b>(METHOD)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(METHOD)</b> rule to.
   */
  public void applyMethod ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    if ( ( ! ( rowExpressions [ 0 ] instanceof Method ) )
        && ( ! ( rowExpressions [ 0 ] instanceof CurriedMethod ) ) )
    {
      throw new IllegalArgumentException ( "Can not apply METHOD" ) ; //$NON-NLS-1$
    }
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = rowExpressions [ i + 1 ] ;
    }
    pContext.addProofNode ( pNode , new Row ( newRowExpressions ) ) ;
  }


  /**
   * Applies the <b>(OBJECT)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(OBJECT)</b> rule to.
   */
  public void applyObject ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    ObjectExpr objectExpr = ( ObjectExpr ) pNode.getExpression ( ) ;
    Row row = ( Row ) objectExpr.getE ( ) ;
    if ( row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply OBJECT" ) ; //$NON-NLS-1$
    }
    pContext.addProofNode ( pNode , row ) ;
  }


  /**
   * Applies the <b>(OMEGA)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(OMEGA)</b> rule to.
   */
  public void applyOmega ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply OMEGA" ) ; //$NON-NLS-1$
    }
    pContext.setProofNodeResult ( pNode , row ) ;
  }


  /**
   * Applies the <b>(SEND)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(SEND)</b> rule to.
   */
  public void applySend ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    Send send = ( Send ) pNode.getExpression ( ) ;
    if ( send.getE ( ) instanceof Row )
    {
      throw new IllegalArgumentException ( "Can not apply SEND" ) ; //$NON-NLS-1$
    }
    pContext.addProofNode ( pNode , send.getE ( ) ) ;
  }


  /**
   * Applies the <b>(SEND-ATTR)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(SEND-ATTR)</b> rule to.
   */
  public void applySendAttr ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    Send send = ( Send ) pNode.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-ATTR" ) ; //$NON-NLS-1$
    }
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    Attribute attribute = ( Attribute ) rowExpressions [ 0 ] ;
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = rowExpressions [ i + 1 ].substitute ( attribute
          .getId ( ) , attribute.getE ( ) ) ;
    }
    pContext.addProofNode ( pNode , new Send ( new Row ( newRowExpressions ) ,
        send.getId ( ) ) ) ;
  }


  /**
   * Applies the <b>(SEND-EXEC)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(SEND-EXEC)</b> rule to.
   */
  public void applySendExec ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    Send send = ( Send ) pNode.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    Identifier methodName ;
    Expression methodExpression ;
    if ( rowExpressions [ 0 ] instanceof Method )
    {
      Method method = ( Method ) rowExpressions [ 0 ] ;
      methodExpression = method.getE ( ) ;
      methodName = method.getId ( ) ;
    }
    else if ( rowExpressions [ 0 ] instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) rowExpressions [ 0 ] ;
      methodExpression = curriedMethod.getE ( ) ;
      Identifier [ ] identifiers = curriedMethod.getIdentifiers ( ) ;
      MonoType [ ] types = curriedMethod.getTypes ( ) ;
      for ( int i = identifiers.length - 1 ; i > 0 ; i -- )
      {
        methodExpression = new Lambda ( identifiers [ i ] , types [ i ] ,
            methodExpression ) ;
      }
      methodName = identifiers [ 0 ] ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
    if ( ! ( send.getId ( ).equals ( methodName ) ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
    boolean definedLater = false ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      Expression rowChild = rowExpressions [ i ] ;
      if ( ( rowChild instanceof Method )
          && ( ( ( Method ) rowChild ).getId ( ).equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
      else if ( ( rowChild instanceof CurriedMethod )
          && ( ( ( CurriedMethod ) rowChild ).getIdentifiers ( ) [ 0 ]
              .equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
    }
    if ( ! definedLater )
    {
      pContext.addProofNode ( pNode , methodExpression ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(SEND-SKIP)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(SEND-SKIP)</b> rule to.
   */
  public void applySendSkip ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    Send send = ( Send ) pNode.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    Identifier methodName ;
    if ( rowExpressions [ 0 ] instanceof Method )
    {
      Method method = ( Method ) rowExpressions [ 0 ] ;
      methodName = method.getId ( ) ;
    }
    else if ( rowExpressions [ 0 ] instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) rowExpressions [ 0 ] ;
      methodName = curriedMethod.getIdentifiers ( ) [ 0 ] ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
    boolean definedLater = false ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      Expression rowChild = rowExpressions [ i ] ;
      if ( ( rowChild instanceof Method )
          && ( ( ( Method ) rowChild ).getId ( ).equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
      else if ( ( rowChild instanceof CurriedMethod )
          && ( ( ( CurriedMethod ) rowChild ).getIdentifiers ( ) [ 0 ]
              .equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
    }
    if ( ( definedLater ) || ( ! ( send.getId ( ).equals ( methodName ) ) ) )
    {
      Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
      for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = rowExpressions [ i + 1 ] ;
      }
      pContext.addProofNode ( pNode , new Send ( new Row ( newRowExpressions ) ,
          send.getId ( ) ) ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(VAL)</b> rule to the <code>pNode</code> using the
   * <code>pContext</code>.
   * 
   * @param pContext The big step proof pContext.
   * @param pNode The node to apply the <b>(VAL)</b> rule to.
   */
  @ Override
  public void applyValue ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    if ( ( ! pNode.getExpression ( ).isValue ( ) )
        || ( pNode.getExpression ( ) instanceof Row ) )
    {
      throw new IllegalArgumentException ( "Can not apply VAL" ) ; //$NON-NLS-1$
    }
    pContext.setProofNodeResult ( pNode , pNode.getExpression ( ) ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(ATTR)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(ATTR)</b>.
   */
  public void updateAttr ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 2 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) )
        && ( pNode.getChildAt ( 1 ).isProven ( ) ) )
    {
      Row row = ( Row ) pNode.getExpression ( ) ;
      Expression childExpression = pNode.getChildAt ( 0 ).getResult ( )
          .getValue ( ) ;
      Row childRow = ( Row ) pNode.getChildAt ( 1 ).getResult ( ).getValue ( ) ;
      Attribute attribute = ( Attribute ) row.getExpressions ( ) [ 0 ] ;
      Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length ] ;
      Attribute newAttribute = new Attribute ( attribute.getId ( ) ,
          childExpression ) ;
      newRowExpressions [ 0 ] = newAttribute ;
      for ( int i = 1 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = childRow.getExpressions ( ) [ i - 1 ] ;
      }
      pContext.setProofNodeResult ( pNode , new Row ( newRowExpressions ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(METHOD)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(METHOD)</b>.
   */
  public void updateMethod ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      Row row = ( Row ) pNode.getExpression ( ) ;
      Row childRow = ( Row ) pNode.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length ] ;
      newRowExpressions [ 0 ] = row.getExpressions ( ) [ 0 ] ;
      for ( int i = 1 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = childRow.getExpressions ( ) [ i - 1 ] ;
      }
      pContext.setProofNodeResult ( pNode , new Row ( newRowExpressions ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(OBJECT)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(OBJECT)</b>.
   */
  public void updateObject ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      ObjectExpr oldObjectExpr = ( ObjectExpr ) pNode.getExpression ( ) ;
      Row row = ( Row ) pNode.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      pContext.setProofNodeResult ( pNode , new ObjectExpr ( oldObjectExpr
          .getId ( ) , oldObjectExpr.getTau ( ) , row ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(SEND)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(SEND)</b>.
   */
  public void updateSend ( BigStepProofContext pContext , BigStepProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 1 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) ) )
    {
      Send send = ( Send ) pNode.getExpression ( ) ;
      ObjectExpr objectExpr = ( ObjectExpr ) pNode.getChildAt ( 0 )
          .getResult ( ).getValue ( ) ;
      Row row = ( Row ) objectExpr.getE ( ) ;
      Expression newRow ;
      newRow = row.substitute ( objectExpr.getId ( ) , objectExpr ) ;
      pContext.addProofNode ( pNode , new Send ( newRow , send.getId ( ) ) ) ;
    }
    else if ( ( pNode.getChildCount ( ) == 2 )
        && ( pNode.getChildAt ( 0 ).isProven ( ) )
        && ( pNode.getChildAt ( 1 ).isProven ( ) ) )
    {
      pContext
          .setProofNodeResult ( pNode , pNode.getChildAt ( 1 ).getResult ( ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(SEND-ATTR)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(SEND-ATTR)</b>.
   */
  public void updateSendAttr ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( pNode.getChildAt ( 0 ).isProven ( ) )
    {
      pContext
          .setProofNodeResult ( pNode , pNode.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(SEND-EXEC)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(SEND-EXEC)</b>.
   */
  public void updateSendExec ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( pNode.getChildAt ( 0 ).isProven ( ) )
    {
      pContext
          .setProofNodeResult ( pNode , pNode.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }


  /**
   * Updates the <code>node</code> to which <b>(SEND-SKIP)</b> was applied
   * previously.
   * 
   * @param pContext The big step proof context.
   * @param pNode The node to update according to <b>(SEND-SKIP)</b>.
   */
  public void updateSendSkip ( BigStepProofContext pContext ,
      BigStepProofNode pNode )
  {
    if ( pNode.getChildAt ( 0 ).isProven ( ) )
    {
      pContext
          .setProofNodeResult ( pNode , pNode.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }
}
