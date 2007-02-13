package de.unisiegen.tpml.core.languages.l2o ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l2cbn.L2CBNLanguage ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;


/**
 * TODO
 * 
 * @author Christian Fehler
 * @version $Rev: 1066 $
 */
public class L2OLanguage extends L2Language
{
  /**
   * TODO
   */
  public static final int L2O = L2CBNLanguage.L2CBN + 1 ;


  /**
   * TODO
   */
  public L2OLanguage ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L2OLanguage.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public int getId ( )
  {
    return L2OLanguage.L2O ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getName ( )
  {
    return "L2O" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L2OLanguage.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression pExpression )
  {
    return new BigStepProofModel ( pExpression , new L2OBigStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public LanguageParser newParser ( LanguageScanner pLanguageScanner )
  {
    if ( pLanguageScanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L2OParser ( pLanguageScanner ) ;
    return new LanguageParser ( )
    {
      public Expression parse ( ) throws Exception
      {
        return ( Expression ) parser.parse ( ).value ;
      }
    } ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public LanguageScanner newScanner ( Reader pReader )
  {
    if ( pReader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L2OScanner ( pReader ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression pExpression )
  {
    return new SmallStepProofModel ( pExpression ,
        new L2OSmallStepProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L2OLanguageTranslator ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public TypeCheckerProofModel newTypeCheckerProofModel ( Expression pExpression )
  {
    return new TypeCheckerProofModel ( pExpression ,
        new L2OTypeCheckerProofRuleSet ( this ) ) ;
  }
}
