package de.unisiegen.tpml.core.languages.l2o ;


import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet ;


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
  }
}
