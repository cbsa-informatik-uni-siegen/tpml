package de.unisiegen.tpml.core.languages.l2o ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.AbstractProofRule ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.AbstractLanguage ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.languages.LanguageTypeParser ;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner ;
import de.unisiegen.tpml.core.languages.l0.L0Language ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2.L2Language ;
import de.unisiegen.tpml.core.languages.l2cbn.L2CBNLanguage ;
import de.unisiegen.tpml.core.languages.l2osubtype.L2ORecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.languages.l2osubtype.L2OSubTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * This class represents the language L2O, which serves as a factory class for
 * L2O related functionality, and extends the L2 language.
 * 
 * @author Christian Fehler
 * @version $Rev:1159 $
 * @see Language
 * @see LanguageParser
 * @see LanguageScanner
 * @see LanguageTranslator
 * @see L0Language
 * @see L1Language
 * @see L2Language
 */
public class L2OLanguage extends L2Language
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see AbstractProofRule#getGroup()
   */
  public static final int L2O = L2CBNLanguage.L2CBN + 1 ;


  /**
   * Allocates a new <code>L2OLanguage</code> instance.
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
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression,
   *      boolean)
   */
  @ Override
  public MinimalTypingProofModel newMinimalTypingProofModel (
      Expression expression , boolean mode )
  {
    return new MinimalTypingProofModel ( expression ,
        new L2OMinimalTypingProofRuleSet ( this , mode ) , mode ) ;
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


  /**
   * {@inheritDoc}
   */
  @ Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      Expression expression )
  {
    return new TypeInferenceProofModel ( expression ,
        new L2OTypeInferenceProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLanguage#newTypeParser(LanguageTypeScanner)
   */
  @ Override
  public LanguageTypeParser newTypeParser ( LanguageTypeScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L2OTypeParser ( scanner ) ;
    return new LanguageTypeParser ( )
    {
      public MonoType parse ( ) throws Exception
      {
        return ( MonoType ) parser.parse ( ).value ;
      }
    } ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeScanner(java.io.Reader)
   */
  @ Override
  public LanguageTypeScanner newTypeScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L2OTypeScanner ( reader ) ;
  }
}
