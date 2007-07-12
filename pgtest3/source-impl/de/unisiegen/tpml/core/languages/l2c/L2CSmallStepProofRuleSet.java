package de.unisiegen.tpml.core.languages.l2c ;


import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;
import de.unisiegen.tpml.core.languages.l2o.L2OSmallStepProofRuleSet ;


/**
 * Small step proof rules for the <code>L2C</code> language.
 * 
 * @author Christian Fehler
 * @version $Rev:1132 $
 * @see L2OSmallStepProofRuleSet
 */
public class L2CSmallStepProofRuleSet extends L2OSmallStepProofRuleSet
{
  /**
   * Allocates a new <code>L2CSmallStepProofRuleSet</code> for the specified
   * <code>language</code>, which must be either <tt>L2C</tt> or a derived
   * language.
   * 
   * @param pL2CLanguage The {@link Language}.
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   * @see L2OSmallStepProofRuleSet#L2OSmallStepProofRuleSet(L2OLanguage)
   */
  public L2CSmallStepProofRuleSet ( L2CLanguage pL2CLanguage )
  {
    super ( pL2CLanguage ) ;
  }
}
