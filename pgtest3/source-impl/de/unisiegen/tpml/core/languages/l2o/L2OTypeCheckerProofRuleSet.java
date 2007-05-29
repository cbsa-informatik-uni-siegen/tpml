package de.unisiegen.tpml.core.languages.l2o ;


import java.text.MessageFormat ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.RowType ;


/**
 * The type proof rules for the <code>L2O</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev:1132 $
 * @see L2TypeCheckerProofRuleSet
 */
public class L2OTypeCheckerProofRuleSet extends L2TypeCheckerProofRuleSet
{
  /**
   * Allocates a new <code>L2OTypeCheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param pL2OLanguage The <code>L2O</code> or a derived language.
   * @throws NullPointerException If <code>language</code> is
   *           <code>null</code>.
   */
  public L2OTypeCheckerProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage ) ;
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "OBJECT" , "applyObject" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "DUPL" , "applyDupl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "EMPTY" , "applyEmpty" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O ,
        "METHOD" , "applyMethod" , "updateMethod" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }


  /**
   * Applies the <b>(SEND)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applySend ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    Send send = ( Send ) pNode.getExpression ( ) ;
    MonoType tauSendE = pContext.newTypeVariable ( ) ;
    MonoType tauRemainingRow = pContext.newTypeVariable ( ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    pContext.addProofNode ( pNode , environment , send.getE ( ) ,
        new ObjectType ( new RowType ( new Identifier [ ]
        { send.getId ( ) } , new MonoType [ ]
        { tauSendE } , tauRemainingRow ) ) ) ;
    pContext.addEquation ( pNode.getType ( ) , tauSendE ) ;
  }


  /**
   * Applies the <b>(OBJECT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyObject ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    ObjectExpr objectExpr = ( ObjectExpr ) pNode.getExpression ( ) ;
    MonoType tau = objectExpr.getTau ( ) ;
    if ( tau == null )
    {
      MonoType rowType = pContext.newTypeVariable ( ) ;
      ObjectType objectType = new ObjectType ( rowType ) ;
      pContext.addEquation ( pNode.getType ( ) , objectType ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      environment = environment.star ( ) ;
      environment = environment.extend ( objectExpr.getId ( ) , objectType ) ;
      pContext.addProofNode ( pNode , environment , objectExpr.getE ( ) ,
          rowType ) ;
    }
    else if ( tau instanceof ObjectType )
    {
      ObjectType objectType = ( ObjectType ) tau ;
      RowType rowType = ( RowType ) objectType.getPhi ( ) ;
      pContext.addEquation ( pNode.getType ( ) , objectType ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      environment = environment.star ( ) ;
      environment = environment.extend ( objectExpr.getId ( ) , objectType ) ;
      pContext.addProofNode ( pNode , environment , objectExpr.getE ( ) ,
          rowType ) ;
    }
    else
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.5" ) , objectExpr ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(DUPL)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyDupl ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    Duplication duplication = ( Duplication ) pNode.getExpression ( ) ;
    Identifier [ ] duplicationIdentifiers = duplication.getIdentifiers ( ) ;
    Expression [ ] duplicationExpressions = duplication.getExpressions ( ) ;
    MonoType tauSelf = pContext.newTypeVariable ( ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    pContext.addProofNode ( pNode , environment , new Identifier ( "self" ) , //$NON-NLS-1$
        tauSelf ) ;
    pContext.addEquation ( pNode.getType ( ) , tauSelf ) ;
    for ( int i = 0 ; i < duplicationIdentifiers.length ; i ++ )
    {
      MonoType tauE = pContext.newTypeVariable ( ) ;
      pContext.addProofNode ( pNode , environment ,
          duplicationIdentifiers [ i ] , tauE ) ;
      pContext.addProofNode ( pNode , environment ,
          duplicationExpressions [ i ] , tauE ) ;
    }
  }


  /**
   * Applies the <b>(EMPTY)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyEmpty ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    if ( row.getExpressions ( ).length != 0 )
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.4" ) , row ) ) ; //$NON-NLS-1$
    }
    pContext.addEquation ( pNode.getType ( ) , new RowType (
        new Identifier [ 0 ] , new MonoType [ 0 ] ) ) ;
  }


  /**
   * Applies the <b>(ATTR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyAttr ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    if ( rowExpressions.length == 0 )
    {
      throw new RuntimeException ( Messages.getString ( "ProofRuleException.3" ) ) ; //$NON-NLS-1$
    }
    if ( ! ( rowExpressions [ 0 ] instanceof Attribute ) )
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.2" ) , rowExpressions [ 0 ] ) ) ; //$NON-NLS-1$ 
    }
    Attribute attribute = ( Attribute ) row.getExpressions ( ) [ 0 ] ;
    Expression e = attribute.getE ( ) ;
    MonoType tauE = pContext.newTypeVariable ( ) ;
    MonoType tauRow = pContext.newTypeVariable ( ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    environment = environment.star ( ) ;
    pContext.addProofNode ( pNode , environment , e , tauE ) ;
    environment = environment.extend ( attribute.getId ( ) , tauE ) ;
    pContext.addProofNode ( pNode , environment , row.tailRow ( ) , tauRow ) ;
    pContext.addEquation ( pNode.getType ( ) , tauRow ) ;
  }


  /**
   * Applies the <b>(METHOD)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyMethod ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    Row row = ( Row ) pNode.getExpression ( ) ;
    Expression [ ] rowExpressions = row.getExpressions ( ) ;
    if ( rowExpressions.length == 0 )
    {
      throw new RuntimeException ( Messages.getString ( "ProofRuleException.3" ) ) ; //$NON-NLS-1$
    }
    if ( rowExpressions [ 0 ] instanceof Method )
    {
      Method method = ( Method ) rowExpressions [ 0 ] ;
      Expression e = method.getE ( ) ;
      MonoType tauE = method.getTau ( ) ;
      if ( tauE == null )
      {
        tauE = pContext.newTypeVariable ( ) ;
      }
      MonoType tauRow = pContext.newTypeVariable ( ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment , e , tauE ) ;
      pContext.addProofNode ( pNode , environment , row.tailRow ( ) , tauRow ) ;
    }
    else if ( rowExpressions [ 0 ] instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) rowExpressions [ 0 ] ;
      Expression e = curriedMethod.getE ( ) ;
      MonoType [ ] types = curriedMethod.getTypes ( ) ;
      Identifier [ ] identifiers = curriedMethod.getIdentifiers ( ) ;
      for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
      {
        e = new Lambda ( identifiers [ n ] , types [ n ] , e ) ;
      }
      MonoType tauE = types [ 0 ] ;
      if ( tauE == null )
      {
        tauE = pContext.newTypeVariable ( ) ;
      }
      for ( int n = types.length - 1 ; n > 0 ; -- n )
      {
        tauE = new ArrowType ( ( types [ n ] != null ) ? types [ n ] : pContext
            .newTypeVariable ( ) , tauE ) ;
      }
      MonoType tauRow = pContext.newTypeVariable ( ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment , e , tauE ) ;
      pContext.addProofNode ( pNode , environment , row.tailRow ( ) , tauRow ) ;
    }
    else
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.1" ) , rowExpressions [ 0 ] ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Updates the <code>node</code>, to which <b>(METHOD)</b> was applied
   * previously, using <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void updateMethod ( TypeCheckerProofContext pContext ,
      TypeCheckerProofNode pNode )
  {
    if ( ( pNode.getChildCount ( ) == 2 )
        && ( pNode.getChildAt ( 0 ).isFinished ( ) )
        && ( pNode.getChildAt ( 1 ).isFinished ( ) ) )
    {
      MonoType tauE = pNode.getChildAt ( 0 ).getType ( ) ;
      RowType tauRow = ( RowType ) pNode.getChildAt ( 1 ).getType ( ) ;
      Row row = ( Row ) pNode.getExpression ( ) ;
      Expression [ ] rowExpressions = row.getExpressions ( ) ;
      if ( rowExpressions [ 0 ] instanceof Method )
      {
        Method method = ( Method ) row.getExpressions ( ) [ 0 ] ;
        RowType tmpRowType = new RowType ( new Identifier [ ]
        { method.getId ( ) } , new MonoType [ ]
        { tauE } ) ;
        RowType union = RowType.union ( tmpRowType , tauRow ) ;
        pContext.addEquation ( pNode.getType ( ) , union ) ;
      }
      else
      {
        CurriedMethod curriedMethod = ( CurriedMethod ) row.getExpressions ( ) [ 0 ] ;
        RowType tmpRowType = new RowType ( new Identifier [ ]
        { curriedMethod.getIdentifiers ( ) [ 0 ] } , new MonoType [ ]
        { tauE } ) ;
        RowType union = RowType.union ( tmpRowType , tauRow ) ;
        pContext.addEquation ( pNode.getType ( ) , union ) ;
      }
    }
  }
}
