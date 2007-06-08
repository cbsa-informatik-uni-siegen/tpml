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
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RowType ;
import de.unisiegen.tpml.core.types.TypeVariable ;


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
    registerByMethodName ( L2OLanguage.L2O , "METHOD" , "applyMethod" ) ; //$NON-NLS-1$ //$NON-NLS-2$ 
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
    MonoType tauSendE = pNode.getType ( ) ;
    MonoType tauRemainingRow = pContext.newTypeVariable ( ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    pContext.addProofNode ( pNode , environment , send.getE ( ) ,
        new ObjectType ( new RowType ( new Identifier [ ]
        { send.getId ( ) } , new MonoType [ ]
        { tauSendE } , tauRemainingRow ) ) ) ;
    // pContext.addEquation ( pNode.getType ( ) , tauSendE ) ;
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
      if ( ( objectExpr.getE ( ) instanceof Row )
          && ( ( ( Row ) objectExpr.getE ( ) ).getExpressions ( ).length != rowType
              .getTypes ( ).length ) )
      {
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "UnificationException.2" ) , pNode.getType ( ) , objectType ) ) ; //$NON-NLS-1$
      }
      pContext.addEquation ( pNode.getType ( ) , objectType ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      environment = environment.star ( ) ;
      environment = environment.extend ( objectExpr.getId ( ) , objectType ) ;
      pContext.addProofNode ( pNode , environment , objectExpr.getE ( ) ,
          rowType ) ;
    }
    else if ( ( tau instanceof RecType )
        && ( ( ( RecType ) tau ).getTau ( ) instanceof ObjectType ) )
    {
      // TODO
      RecType recType = ( RecType ) tau ;
      ObjectType objectType = ( ObjectType ) recType.getTau ( ) ;
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
    TypeEnvironment environmentE = pNode.getEnvironment ( ) ;
    environmentE = environmentE.star ( ) ;
    pContext.addProofNode ( pNode , environmentE , e , tauE ) ;
    TypeEnvironment environmentTailRow = pNode.getEnvironment ( ) ;
    environmentTailRow = environmentTailRow
        .extend ( attribute.getId ( ) , tauE ) ;
    pContext.addProofNode ( pNode , environmentTailRow , row.tailRow ( ) ,
        tauRow ) ;
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
      Expression methodE = method.getE ( ) ;
      MonoType methodTau = method.getTau ( ) ;
      if ( methodTau == null )
      {
        methodTau = pContext.newTypeVariable ( ) ;
      }
      MonoType tauRow = pContext.newTypeVariable ( ) ;
      MonoType nodeType = pNode.getType ( ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment , methodE , methodTau ) ;
      if ( ( nodeType instanceof TypeVariable )
          || ( ( nodeType instanceof RowType ) && ( method.getId ( )
              .equals ( ( ( RowType ) nodeType ).getIdentifiers ( ) [ 0 ] ) ) ) )
      {
        pContext.addProofNode ( pNode , environment , row.tailRow ( ) , tauRow ) ;
        RowType union = new RowType ( new Identifier [ ]
        { method.getId ( ) } , new MonoType [ ]
        { methodTau } , tauRow ) ;
        pContext.addEquation ( pNode.getType ( ) , union ) ;
      }
      else
      {
        RowType rowType = ( RowType ) nodeType ;
        RowType unionRow = new RowType ( new Identifier [ ]
        { rowType.getIdentifiers ( ) [ 0 ] } , new MonoType [ ]
        { rowType.getTypes ( ) [ 0 ] } , tauRow ) ;
        pContext.addProofNode ( pNode , environment , row.tailRow ( ) ,
            unionRow ) ;
        RowType unionEquation = new RowType ( new Identifier [ ]
        { method.getId ( ) } , new MonoType [ ]
        { methodTau } , tauRow ) ;
        MonoType remainingRowType = rowType.getRemainingRowType ( ) ;
        if ( remainingRowType != null )
        {
          pContext.addEquation ( remainingRowType , unionEquation ) ;
        }
      }
    }
    else if ( rowExpressions [ 0 ] instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) rowExpressions [ 0 ] ;
      Expression curriedMethodE = curriedMethod.getE ( ) ;
      MonoType [ ] types = curriedMethod.getTypes ( ) ;
      Identifier [ ] identifiers = curriedMethod.getIdentifiers ( ) ;
      for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
      {
        curriedMethodE = new Lambda ( identifiers [ n ] , types [ n ] ,
            curriedMethodE ) ;
      }
      MonoType curriedMethodTau = types [ 0 ] ;
      if ( curriedMethodTau == null )
      {
        curriedMethodTau = pContext.newTypeVariable ( ) ;
      }
      for ( int n = types.length - 1 ; n > 0 ; -- n )
      {
        curriedMethodTau = new ArrowType ( ( types [ n ] != null ) ? types [ n ]
            : pContext.newTypeVariable ( ) , curriedMethodTau ) ;
      }
      MonoType tauRow = pContext.newTypeVariable ( ) ;
      MonoType nodeType = pNode.getType ( ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment , curriedMethodE ,
          curriedMethodTau ) ;
      if ( ( nodeType instanceof TypeVariable )
          || ( ( nodeType instanceof RowType ) && ( identifiers [ 0 ]
              .equals ( ( ( RowType ) nodeType ).getIdentifiers ( ) [ 0 ] ) ) ) )
      {
        pContext.addProofNode ( pNode , environment , row.tailRow ( ) , tauRow ) ;
        RowType union = new RowType ( new Identifier [ ]
        { identifiers [ 0 ] } , new MonoType [ ]
        { curriedMethodTau } , tauRow ) ;
        pContext.addEquation ( pNode.getType ( ) , union ) ;
      }
      else
      {
        RowType rowType = ( RowType ) nodeType ;
        RowType unionRow = new RowType ( new Identifier [ ]
        { rowType.getIdentifiers ( ) [ 0 ] } , new MonoType [ ]
        { rowType.getTypes ( ) [ 0 ] } , tauRow ) ;
        pContext.addProofNode ( pNode , environment , row.tailRow ( ) ,
            unionRow ) ;
        RowType unionEquation = new RowType ( new Identifier [ ]
        { identifiers [ 0 ] } , new MonoType [ ]
        { curriedMethodTau } , tauRow ) ;
        MonoType remainingRowType = rowType.getRemainingRowType ( ) ;
        if ( remainingRowType != null )
        {
          pContext.addEquation ( remainingRowType , unionEquation ) ;
        }
      }
    }
    else
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.1" ) , rowExpressions [ 0 ] ) ) ; //$NON-NLS-1$
    }
  }
}
