package de.unisiegen.tpml.core.languages.l1cbn ;


import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;


/**
 * This class represents the language L1CBN, which has the same functions as the
 * language L1, but uses Call By Name.
 * 
 * @author Christian Fehler
 * @version $Rev$
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class L1CBNLanguage extends L1Language
{
  /**
   * Allocates a new <code>L1CBNLanguage</code> instance.
   */
  public L1CBNLanguage ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#getDescription()
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L1CBNLanguage.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#getName()
   */
  @ Override
  public String getName ( )
  {
    return "L1CBN" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#getTitle()
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L1CBNLanguage.1" ) ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L1CBNBigStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression ,
        new L1CBNSmallStepProofRuleSet ( this ) ) ;
  }
}
