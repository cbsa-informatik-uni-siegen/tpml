package de.unisiegen.tpml.core.languages.l2o;

import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.RowType;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class L2OTypeInferenceProofRuleSet extends L2OTypeCheckerProofRuleSet {

	/**
	 * TODO
	 *
	 * @param language
	 */
	public L2OTypeInferenceProofRuleSet(L2OLanguage language) {
		super(language);
		
		unregister ( "ABSTR"  ) ; //$NON-NLS-1$ 
		unregister (  "AND" ) ;//$NON-NLS-1$ 
		unregister (  "APP"  ) ;//$NON-NLS-1$ 
		unregister (  "COND"  ) ;//$NON-NLS-1$ 
		unregister ( "CONST"  ) ;//$NON-NLS-1$ 
		unregister (  "ID"  ) ;//$NON-NLS-1$ 
		unregister (  "LET"  ) ;//$NON-NLS-1$ 
		unregister (  "OR"  ) ;//$NON-NLS-1$ 
		unregister (  "REC" ) ; //$NON-NLS-1$ 
		unregister (  "SEND"  ) ; //$NON-NLS-1$ 
		unregister (  "OBJECT"  ) ; //$NON-NLS-1$ 
		unregister (  "DUPL"  ) ; //$NON-NLS-1$
    unregister (  "EMPTY"  ) ; //$NON-NLS-1$ 
    unregister (  "ATTR"  ) ; //$NON-NLS-1$ 
    unregister (  "METHOD"  ) ; //$NON-NLS-1$ 
		
//	 register the type rules
    registerByMethodName (L1Language.L1, "UNIFY", "applyUnify");  //$NON-NLS-1$//$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "ABSTR" , "applyAbstr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "AND" , "applyAnd" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "APP" , "applyApp" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "COND" , "applyCond" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "CONST" , "applyConst" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "ID" , "applyId" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L1Language.L1 , "LET" , "applyLet" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "OR" , "applyOr" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2Language.L2 , "REC" , "applyRec" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "SEND" , "applySend" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "OBJECT" , "applyObject" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "DUPL" , "applyDupl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "EMPTY" , "applyEmpty" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "ATTR" , "applyAttr" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O , "METHOD" , "applyMethod" ) ; //$NON-NLS-1$ //$NON-NLS-2$ 
    
		
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
      pContext.addProofNode ( pNode , pNode.getEnvironment ( ) , e , tauE ) ;
      pContext.addProofNode ( pNode , pNode.getEnvironment ( ) ,
          row.tailRow ( ) , tauRow ) ;
      
      
      Identifier [] identifiers = new Identifier[1];
      identifiers[0]= pNode.getExpression ( ).getIdentifiersAll ( ).get ( 0 );
      MonoType [ ] types = new MonoType[1];
      types[0]= tauE;
      
      RowType union = new RowType(identifiers, types, tauRow);
      pContext.addEquation ( pNode.getType ( ) , union ) ;
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
      pContext.addProofNode ( pNode , pNode.getEnvironment ( ) , e , tauE ) ;
      pContext.addProofNode ( pNode , pNode.getEnvironment ( ) ,
          row.tailRow ( ) , tauRow ) ;
    }
    else
    {
      throw new RuntimeException ( "The first child of the row is not a method or a curried method." ) ; //$NON-NLS-1$
    }
  }

}
