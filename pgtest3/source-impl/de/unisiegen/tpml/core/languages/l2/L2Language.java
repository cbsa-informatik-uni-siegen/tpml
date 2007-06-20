package de.unisiegen.tpml.core.languages.l2 ;



import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;



/**
 * This class represents the language L2, which serves as a factory class for L2
 * related functionality, and extends the L1 language.
 * 
 * @author Benedikt Meurer
 * @version $Rev:1159 $
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class L2Language extends L1Language
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L2 = L1Language.L1 + 10 ;


  /**
   * Allocates a new <code>L2Language</code> instance.
   */
  public L2Language ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getDescription()
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L2Language.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public int getId ( )
  {
    return L2Language.L2 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @ Override
  public String getName ( )
  {
    return "L2" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L2Language.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L2BigStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(LanguageScanner)
   */
  @ Override
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L2Parser ( scanner ) ;
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
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSubTypingProofModel(MonoType,MonoType,boolean)
   */
  @ Override
  public RecSubTypingProofModel newRecSubTypingProofModel ( MonoType type ,
      MonoType type2 , boolean mode )
  {
    return new RecSubTypingProofModel ( type , type2 ,
        new L2RecSubTypingProofRuleSet ( this , mode ) , mode ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newScanner(java.io.Reader)
   */
  @ Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L2Scanner ( reader ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression , new L2SmallStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSubTypingProofModel(MonoType,MonoType,boolean)
   */
  @ Override
  public SubTypingProofModel newSubTypingProofModel ( MonoType type ,
      MonoType type2 , boolean mode )
  {
    return new SubTypingProofModel ( type , type2 ,
        new L2SubTypingProofRuleSet ( this , mode ) , mode ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L2LanguageTranslator ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public TypeCheckerProofModel newTypeCheckerProofModel ( Expression expression )
  {
    return new TypeCheckerProofModel ( expression ,
        new L2TypeCheckerProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeInferenceProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      Expression expression )
  {
    return new TypeInferenceProofModel ( expression ,
        new L2TypeInferenceProofRuleSet ( this ) ) ;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public MinimalTypingProofModel newMinimalTypingProofModel ( Expression expression, boolean mode )
  {
    return new MinimalTypingProofModel ( expression , new L2MinimalTypingProofRuleSet(this, mode));
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  @ Override
  public LanguageTypeParser newTypeParser ( LanguageTypeScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L2TypeParser ( scanner ) ;
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
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newTypeScanner(java.io.Reader)
   */
  @ Override
  public LanguageTypeScanner newTypeScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L2TypeScanner ( reader ) ;
  }
}
