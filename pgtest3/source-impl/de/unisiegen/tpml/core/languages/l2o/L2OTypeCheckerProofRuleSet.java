package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode ;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
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
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
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
    // determine the type for the identifier
    Send send = ( Send ) pNode.getExpression ( ) ;
    Identifier sendM = send.getId ( ) ;
    ObjectExpr objectExpr = ( ObjectExpr ) send.getE ( ) ;
    ObjectType objectType = ( ObjectType ) objectExpr.getTau ( ) ;
    RowType rowType = ( RowType ) objectType.getPhi ( ) ;
    MonoType tau = null ;
    Identifier [ ] rowTypeIdentifiers = rowType.getIdentifiers ( ) ;
    for ( int i = 0 ; i < rowTypeIdentifiers.length ; i ++ )
    {
      if ( rowTypeIdentifiers [ i ].equals ( sendM ) )
      {
        tau = rowType.getTypes ( ) [ i ] ;
        break ;
      }
    }
    if ( tau == null )
    {
      tau = pContext.newTypeVariable ( ) ;
    }
    pContext.addEquation ( pNode.getType ( ) , tau ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    pContext.addProofNode ( pNode , environment , send.getE ( ) , tau ) ;
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
    MonoType tau1 = objectExpr.getTau ( ) ;
    if ( tau1 == null )
    {
      MonoType rowType = pContext.newTypeVariable ( ) ;
      ObjectType objectType = new ObjectType ( rowType ) ;
      pContext.addEquation ( pNode.getType ( ) , objectType ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment.extend ( objectExpr.getId ( )
          .getName ( ) , objectType ) , objectExpr.getE ( ) , rowType ) ;
    }
    else if ( tau1 instanceof ObjectType )
    {
      ObjectType objectType = ( ObjectType ) tau1 ;
      RowType rowType = ( RowType ) objectType.getPhi ( ) ;
      pContext.addEquation ( pNode.getType ( ) , objectType ) ;
      TypeEnvironment environment = pNode.getEnvironment ( ) ;
      pContext.addProofNode ( pNode , environment.extend ( objectExpr.getId ( )
          .getName ( ) , objectType ) , objectExpr.getE ( ) , rowType ) ;
    }
    else
    {
      throw new RuntimeException ( "Can not apply OBJECT" ) ; //$NON-NLS-1$
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
    Expression [ ] oldRowExpressions = row.getExpressions ( ) ;
    Expression [ ] newRowExpressions = new Expression [ oldRowExpressions.length - 1 ] ;
    for ( int i = 0 ; i < newRowExpressions.length ; i ++ )
    {
      newRowExpressions [ i ] = oldRowExpressions [ i + 1 ] ;
    }
    Attribute attribute = ( Attribute ) oldRowExpressions [ 0 ] ;
    Expression e = attribute.getE ( ) ;
    MonoType tauE = pContext.newTypeVariable ( ) ;
    MonoType tauRow = pContext.newTypeVariable ( ) ;
    TypeEnvironment environment = pNode.getEnvironment ( ) ;
    pContext.addProofNode ( pNode , environment , e , tauE ) ;
    pContext.addProofNode ( pNode , environment.extend ( attribute.getId ( )
        .getName ( ) , tauE ) , new Row ( newRowExpressions ) , tauRow ) ;
    pContext.addEquation ( pNode.getType ( ) , tauRow ) ;
  }
}
