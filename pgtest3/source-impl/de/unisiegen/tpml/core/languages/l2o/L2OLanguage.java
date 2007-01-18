package de.unisiegen.tpml.core.languages.l2o ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l4.L4Language ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;


public class L2OLanguage extends L2Language
{
  public static final int L2O = L4Language.L4 + 1 ;


  public L2OLanguage ( )
  {
    super ( ) ;
  }


  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L2OLanguage.0" ) ;
  }


  @ Override
  public String getName ( )
  {
    return "L2O" ;
  }


  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L2OLanguage.1" ) ;
  }


  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L2OBigStepProofRuleSet (
        this ) ) ;
  }


  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression , new L2OSmallStepProofRuleSet (
        this ) ) ;
  }


  @ Override
  public TypeCheckerProofModel newTypeCheckerProofModel ( Expression expression )
  {
    return new TypeCheckerProofModel ( expression ,
        new L2OTypeCheckerProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
  @ Override
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L2OParser ( scanner ) ;
    return new LanguageParser ( )
    {
      public Expression parse ( ) throws Exception
      {
        return ( Expression ) parser.parse ( ).value ;
      }
    } ;
  }


  @ Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L2OScanner ( reader ) ;
  }


  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L2OLanguageTranslator ( ) ;
  }
}
