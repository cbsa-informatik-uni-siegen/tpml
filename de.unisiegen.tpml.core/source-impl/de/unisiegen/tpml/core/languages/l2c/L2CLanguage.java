package de.unisiegen.tpml.core.languages.l2c ;


import java.io.Reader ;
import java.text.MessageFormat ;
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
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * This class represents the language L2C, which serves as a factory class for
 * L2C related functionality, and extends the L2O language.
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
 * @see L2OLanguage
 */
public class L2CLanguage extends L2OLanguage
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see AbstractProofRule#getGroup()
   */
  public static final int L2C = L2OLanguage.L2O + 1 ;


  /**
   * Allocates a new <code>L2CLanguage</code> instance.
   */
  public L2CLanguage ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L2CLanguage.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public int getId ( )
  {
    return L2CLanguage.L2C ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getName ( )
  {
    return "L2C" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L2CLanguage.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression pExpression )
  {
    return new BigStepProofModel ( pExpression , new L2CBigStepProofRuleSet (
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
      @ SuppressWarnings ( "unused" )
      Expression expression , @ SuppressWarnings ( "unused" )
      boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.9" ) , getName ( ) ) ) ; //$NON-NLS-1$
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
    final lr_parser parser = new L2CParser ( pLanguageScanner ) ;
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
    return new L2CScanner ( pReader ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression pExpression )
  {
    return new SmallStepProofModel ( pExpression ,
        new L2CSmallStepProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L2CLanguageTranslator ( ) ;
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public TypeCheckerProofModel newTypeCheckerProofModel (
      @ SuppressWarnings ( "unused" )
      Expression pExpression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.12" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @ Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      @ SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.13" ) , getName ( ) ) ) ; //$NON-NLS-1$
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
    final lr_parser parser = new L2CTypeParser ( scanner ) ;
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
    return new L2CTypeScanner ( reader ) ;
  }
}
