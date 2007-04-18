package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.bigstep.BigStepProofContext ;
import de.unisiegen.tpml.core.bigstep.BigStepProofNode ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.languages.l2.L2BigStepProofRuleSet ;
import de.unisiegen.tpml.core.util.BoundRenaming ;


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
   */
  private Identifier newAttributeId = null ;


  /**
   * TODO
   * 
   * @param pL2OLanguage TODO
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
    registerByMethodName ( L2OLanguage.L2O , "RENAME" , "applyRename" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateRename" ) ; //$NON-NLS-1$ 
    registerByMethodName ( L2OLanguage.L2O , "METHOD" , "applyMethod" , //$NON-NLS-1$ //$NON-NLS-2$
        "updateMethod" ) ; //$NON-NLS-1$
    // registerByMethodName ( L2OLanguage.L2O , "DUPL" , "applyDupl" ,
    // //$NON-NLS-1$ //$NON-NLS-2$
    // "updateDupl" ) ; //$NON-NLS-1$
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applyAttr ( BigStepProofContext context , BigStepProofNode node )
  {
    Row row = ( Row ) node.getExpression ( ) ;
    Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attribute )
      {
        Attribute currentAttribute = ( Attribute ) row.getExpressions ( i ) ;
        if ( currentAttribute.getId ( ).equals ( attribute.getId ( ) ) )
        {
          throw new IllegalArgumentException ( "Can not apply ATTR" ) ; //$NON-NLS-1$
        }
      }
    }
    context.addProofNode ( node , attribute.getE ( ).clone ( ) ) ;
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
    }
    context.addProofNode ( node , new Row ( newRowExpressions ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  /*
   * public void applyDupl ( BigStepProofContext context , BigStepProofNode node ) {
   * Duplication duplication = ( Duplication ) node.getExpression ( ) ;
   * ObjectExpr objectExpr = ( ObjectExpr ) duplication.getE ( ) ; Row row = (
   * Row ) objectExpr.getE ( ) ; boolean found ; for ( int i = 0 ; i <
   * duplication.getIdentifiers ( ).length ; i ++ ) { found = false ; for ( int
   * j = 0 ; j < row.getExpressions ( ).length ; j ++ ) { if (
   * row.getExpressions ( j ) instanceof Attribute ) { Attribute attribute = (
   * Attribute ) row.getExpressions ( j ) ; if ( attribute.getId ( ).equals (
   * duplication.getIdentifiers ( i ) ) ) { found = true ; break ; } } } if ( !
   * found ) { throw new IllegalArgumentException ( "Can not apply DUPL" ) ;
   * //$NON-NLS-1$ } } if ( duplication.getExpressions ( ).length == 0 ) {
   * context.setProofNodeResult ( node , objectExpr.clone ( ) ) ; } else { for (
   * int i = 0 ; i < duplication.getExpressions ( ).length ; i ++ ) { context
   * .addProofNode ( node , duplication.getExpressions ( i ).clone ( ) ) ; } } }
   */
  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applyMethod ( BigStepProofContext context , BigStepProofNode node )
  {
    Row row = ( Row ) node.getExpression ( ) ;
    if ( ( ! ( row.getExpressions ( 0 ) instanceof Method ) )
        && ( ! ( row.getExpressions ( 0 ) instanceof CurriedMethod ) ) )
    {
      throw new IllegalArgumentException ( "Can not apply METHOD" ) ; //$NON-NLS-1$
    }
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
    }
    context.addProofNode ( node , new Row ( newRowExpressions ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applyObject ( BigStepProofContext context , BigStepProofNode node )
  {
    ObjectExpr objectExpr = ( ObjectExpr ) node.getExpression ( ) ;
    Row row = ( Row ) objectExpr.getE ( ) ;
    if ( row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply OBJECT" ) ; //$NON-NLS-1$
    }
    context.addProofNode ( node , row.clone ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applyOmega ( BigStepProofContext context , BigStepProofNode node )
  {
    Row row = ( Row ) node.getExpression ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply OMEGA" ) ; //$NON-NLS-1$
    }
    context.setProofNodeResult ( node , row ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applyRename ( BigStepProofContext context , BigStepProofNode node )
  {
    Row row = ( Row ) node.getExpression ( ) ;
    Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
    boolean found = false ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attribute )
      {
        Attribute currentAttribute = ( Attribute ) row.getExpressions ( i ) ;
        if ( currentAttribute.getId ( ).equals ( attribute.getId ( ) ) )
        {
          found = true ;
          break ;
        }
      }
    }
    if ( ! found )
    {
      throw new IllegalArgumentException ( "Can not apply RENAME" ) ; //$NON-NLS-1$
    }
    context.addProofNode ( node , attribute.getE ( ).clone ( ) ) ;
    BoundRenaming boundRenaming = new BoundRenaming ( ) ;
    boundRenaming.add ( row.free ( ) ) ;
    boundRenaming.add ( attribute.getId ( ) ) ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      if ( row.getExpressions ( i ) instanceof Attribute )
      {
        Attribute currentAttribute = ( Attribute ) row.getExpressions ( i ) ;
        boundRenaming.add ( currentAttribute.getId ( ) ) ;
      }
    }
    Identifier newId = boundRenaming.newId ( attribute.getId ( ) ) ;
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    found = false ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      if ( found )
      {
        newRowExpressions [ i ] = row.getExpressions ( i + 1 ) ;
      }
      else
      {
        // TODO Attribute rename
        newRowExpressions [ i ] = row.getExpressions ( i + 1 ).substitute (
            attribute.getId ( ) , newId ) ;
      }
      if ( row.getExpressions ( i + 1 ) instanceof Attribute )
      {
        Attribute currentAttribute = ( Attribute ) row.getExpressions ( i + 1 ) ;
        if ( attribute.getId ( ).equals ( currentAttribute.getId ( ) ) )
        {
          found = true ;
        }
      }
    }
    if ( this.newAttributeId != null )
    {
      System.err.println ( "L2O: newAttributeId was not null" ) ; //$NON-NLS-1$
    }
    this.newAttributeId = newId ;
    context.addProofNode ( node , new Row ( newRowExpressions ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applySend ( BigStepProofContext context , BigStepProofNode node )
  {
    Send send = ( Send ) node.getExpression ( ) ;
    if ( send.getE ( ) instanceof Row )
    {
      throw new IllegalArgumentException ( "Can not apply SEND" ) ; //$NON-NLS-1$
    }
    context.addProofNode ( node , send.getE ( ).clone ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applySendAttr ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    Send send = ( Send ) node.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-ATTR" ) ; //$NON-NLS-1$
    }
    Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = row.getExpressions ( i + 1 ).substitute (
          attribute.getId ( ) , attribute.getE ( ) ) ;
    }
    context.addProofNode ( node , new Send ( new Row ( newRowExpressions ) ,
        send.getId ( ).clone ( ) ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applySendExec ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    Send send = ( Send ) node.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
    Identifier methodName ;
    Expression methodExpression ;
    if ( row.getExpressions ( 0 ) instanceof Method )
    {
      Method method = ( Method ) row.getExpressions ( 0 ) ;
      methodExpression = method.getE ( ).clone ( ) ;
      methodName = method.getId ( ) ;
    }
    else if ( row.getExpressions ( 0 ) instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) row.getExpressions ( 0 ) ;
      methodExpression = curriedMethod.getE ( ).clone ( ) ;
      for ( int i = curriedMethod.getIdentifiers ( ).length - 1 ; i > 0 ; i -- )
      {
        methodExpression = new Lambda ( curriedMethod.getIdentifiers ( i )
            .clone ( ) , curriedMethod.getTypes ( i ) == null ? null
            : curriedMethod.getTypes ( i ).clone ( ) , methodExpression ) ;
      }
      methodName = curriedMethod.getIdentifiers ( 0 ) ;
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
      Expression rowChild = row.getExpressions ( i ) ;
      if ( ( rowChild instanceof Method )
          && ( ( ( Method ) rowChild ).getId ( ).equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
      else if ( ( rowChild instanceof CurriedMethod )
          && ( ( ( CurriedMethod ) rowChild ).getIdentifiers ( 0 )
              .equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
    }
    if ( ! definedLater )
    {
      context.addProofNode ( node , methodExpression ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-EXEC" ) ; //$NON-NLS-1$
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void applySendSkip ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    Send send = ( Send ) node.getExpression ( ) ;
    Row row = ( Row ) send.getE ( ) ;
    if ( ! row.isValue ( ) )
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
    Identifier methodName ;
    if ( row.getExpressions ( 0 ) instanceof Method )
    {
      Method method = ( Method ) row.getExpressions ( 0 ) ;
      methodName = method.getId ( ) ;
    }
    else if ( row.getExpressions ( 0 ) instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) row.getExpressions ( 0 ) ;
      methodName = curriedMethod.getIdentifiers ( 0 ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
    boolean definedLater = false ;
    for ( int i = 1 ; i < row.getExpressions ( ).length ; i ++ )
    {
      Expression rowChild = row.getExpressions ( i ) ;
      if ( ( rowChild instanceof Method )
          && ( ( ( Method ) rowChild ).getId ( ).equals ( send.getId ( ) ) ) )
      {
        definedLater = true ;
        break ;
      }
      else if ( ( rowChild instanceof CurriedMethod )
          && ( ( ( CurriedMethod ) rowChild ).getIdentifiers ( 0 )
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
        newRowExpressions [ i ] = row.getExpressions ( i + 1 ).clone ( ) ;
      }
      context.addProofNode ( node , new Send ( new Row ( newRowExpressions ) ,
          send.getId ( ).clone ( ) ) ) ;
    }
    else
    {
      throw new IllegalArgumentException ( "Can not apply SEND-SKIP" ) ; //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(VAL)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the big step proof context.
   * @param node the node to apply the <b>(VAL)</b> rule to.
   */
  @ Override
  public void applyValue ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( ! node.getExpression ( ).isValue ( ) )
        || ( node.getExpression ( ) instanceof Row ) )
    {
      throw new IllegalArgumentException ( "Can not apply VAL" ) ; //$NON-NLS-1$
    }
    context.setProofNodeResult ( node , node.getExpression ( ) ) ;
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateAttr ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 2 )
        && ( node.getChildAt ( 0 ).isProven ( ) )
        && ( node.getChildAt ( 1 ).isProven ( ) ) )
    {
      Row row = ( Row ) node.getExpression ( ) ;
      Expression childExpression = node.getChildAt ( 0 ).getResult ( )
          .getValue ( ) ;
      Row childRow = ( Row ) node.getChildAt ( 1 ).getResult ( ).getValue ( ) ;
      Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
      Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length ] ;
      Attribute newAttribute = new Attribute ( attribute.getId ( ) , attribute
          .getTau ( ) , childExpression ) ;
      newRowExpressions [ 0 ] = newAttribute ;
      for ( int i = 1 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = childRow.getExpressions ( i - 1 ) ;
      }
      context.setProofNodeResult ( node , new Row ( newRowExpressions ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  /*
   * public void updateDupl ( BigStepProofContext context , BigStepProofNode
   * node ) { Duplication duplication = ( Duplication ) node.getExpression ( ) ;
   * ObjectExpr objectExpr = ( ObjectExpr ) duplication.getE ( ) ; Row row = (
   * Row ) objectExpr.getE ( ) ; boolean allProven = true ; for ( int i = 0 ; i <
   * node.getChildCount ( ) ; i ++ ) { if ( ! node.getChildAt ( i ).isProven ( ) ) {
   * allProven = false ; break ; } } if ( allProven ) { Expression [ ]
   * newRowExpressions = new Expression [ row.getExpressions ( ).length ] ; for (
   * int i = 0 ; i < row.getExpressions ( ).length ; i ++ ) { newRowExpressions [
   * i ] = row.getExpressions ( i ).clone ( ) ; } for ( int i = 0 ; i <
   * duplication.getIdentifiers ( ).length ; i ++ ) { for ( int j = 0 ; j <
   * newRowExpressions.length ; j ++ ) { if ( newRowExpressions [ j ] instanceof
   * Attribute ) { Attribute attribute = ( Attribute ) newRowExpressions [ j ] ;
   * if ( attribute.getId ( ).equals ( duplication.getIdentifiers ( i ) ) ) {
   * newRowExpressions [ j ] = new Attribute ( attribute.getId ( ) ,
   * attribute.getTau ( ) , node.getChildAt ( i ).getResult ( ) .getValue ( ) ) ; } } } }
   * context.setProofNodeResult ( node , new ObjectExpr ( objectExpr.getId ( )
   * .clone ( ) , objectExpr.getTau ( ) == null ? null : objectExpr .getTau (
   * ).clone ( ) , new Row ( newRowExpressions ) ) ) ; } }
   */
  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateMethod ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 1 )
        && ( node.getChildAt ( 0 ).isProven ( ) ) )
    {
      Row row = ( Row ) node.getExpression ( ) ;
      Row childRow = ( Row ) node.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      Expression [ ] newRowExpressions = new Expression [ row.getExpressions ( ).length ] ;
      newRowExpressions [ 0 ] = row.getExpressions ( 0 ).clone ( ) ;
      for ( int i = 1 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = childRow.getExpressions ( i - 1 ) ;
      }
      context.setProofNodeResult ( node , new Row ( newRowExpressions ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateObject ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 1 )
        && ( node.getChildAt ( 0 ).isProven ( ) ) )
    {
      ObjectExpr oldObjectExpr = ( ObjectExpr ) node.getExpression ( ) ;
      Row row = ( Row ) node.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      context.setProofNodeResult ( node , new ObjectExpr ( oldObjectExpr
          .getId ( ) , oldObjectExpr.getTau ( ) , row ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateRename ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 2 )
        && ( node.getChildAt ( 0 ).isProven ( ) )
        && ( node.getChildAt ( 1 ).isProven ( ) ) )
    {
      Row row = ( Row ) node.getExpression ( ) ;
      Expression expression = node.getChildAt ( 0 ).getResult ( ).getValue ( ) ;
      Row childRow = ( Row ) node.getChildAt ( 1 ).getResult ( ).getValue ( ) ;
      Attribute attribute = ( Attribute ) row.getExpressions ( 0 ) ;
      Expression [ ] newRowExpressions = new Expression [ childRow
          .getExpressions ( ).length + 1 ] ;
      Attribute newAttribute = new Attribute ( this.newAttributeId , attribute
          .getTau ( ) , expression ) ;
      newRowExpressions [ 0 ] = newAttribute ;
      for ( int i = 1 ; i < newRowExpressions.length ; i ++ )
      {
        newRowExpressions [ i ] = childRow.getExpressions ( i - 1 ) ;
      }
      context.setProofNodeResult ( node , new Row ( newRowExpressions ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateSend ( BigStepProofContext context , BigStepProofNode node )
  {
    if ( ( node.getChildCount ( ) == 1 )
        && ( node.getChildAt ( 0 ).isProven ( ) ) )
    {
      Send send = ( Send ) node.getExpression ( ) ;
      ObjectExpr objectExpr = ( ObjectExpr ) node.getChildAt ( 0 ).getResult ( )
          .getValue ( ) ;
      Row row = ( Row ) objectExpr.getE ( ) ;
      Expression newRow = row.substitute ( objectExpr.getId ( ) , objectExpr ) ;
      context.addProofNode ( node ,
          new Send ( newRow , send.getId ( ).clone ( ) ) ) ;
    }
    else if ( ( node.getChildCount ( ) == 2 )
        && ( node.getChildAt ( 1 ).isProven ( ) ) )
    {
      context.setProofNodeResult ( node , node.getChildAt ( 1 ).getResult ( ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateSendAttr ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    if ( node.getChildAt ( 0 ).isProven ( ) )
    {
      context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateSendExec ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    if ( node.getChildAt ( 0 ).isProven ( ) )
    {
      context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }


  /**
   * TODO
   * 
   * @param context TODO
   * @param node TODO
   */
  public void updateSendSkip ( BigStepProofContext context ,
      BigStepProofNode node )
  {
    if ( node.getChildAt ( 0 ).isProven ( ) )
    {
      context.setProofNodeResult ( node , node.getChildAt ( 0 ).getResult ( ) ) ;
    }
  }
}
