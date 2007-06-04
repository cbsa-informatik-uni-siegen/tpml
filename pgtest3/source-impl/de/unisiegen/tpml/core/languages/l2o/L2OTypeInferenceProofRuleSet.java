package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;


/**
 * TODO
 * 
 * @author Benjamin Mies
 */
public class L2OTypeInferenceProofRuleSet extends L2OTypeCheckerProofRuleSet
{
  /**
   * TODO
   * 
   * @param language
   */
  public L2OTypeInferenceProofRuleSet ( L2OLanguage language )
  {
    super ( language ) ;
    unregister ( "ABSTR" ) ; //$NON-NLS-1$ 
    unregister ( "AND" ) ;//$NON-NLS-1$ 
    unregister ( "APP" ) ;//$NON-NLS-1$ 
    unregister ( "COND" ) ;//$NON-NLS-1$ 
    unregister ( "CONST" ) ;//$NON-NLS-1$ 
    unregister ( "ID" ) ;//$NON-NLS-1$ 
    unregister ( "LET" ) ;//$NON-NLS-1$ 
    unregister ( "OR" ) ;//$NON-NLS-1$ 
    unregister ( "REC" ) ; //$NON-NLS-1$ 
    unregister ( "SEND" ) ; //$NON-NLS-1$ 
    unregister ( "OBJECT" ) ; //$NON-NLS-1$ 
    unregister ( "DUPL" ) ; //$NON-NLS-1$
    unregister ( "EMPTY" ) ; //$NON-NLS-1$ 
    unregister ( "ATTR" ) ; //$NON-NLS-1$ 
    unregister ( "METHOD" ) ; //$NON-NLS-1$ 
    // register the type rules
    registerByMethodName ( L1Language.L1 , "UNIFY" , "applyUnify" ) ; //$NON-NLS-1$//$NON-NLS-2$
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
}
