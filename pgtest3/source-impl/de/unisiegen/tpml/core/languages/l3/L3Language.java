package de.unisiegen.tpml.core.languages.l3 ;


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
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * This class represents the language L3, which serves as a factory class for L3
 * related functionality, and extends the L2 language.
 * 
 * @author Benedikt Meurer
 * @version $Rev:1159 $
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 */
public class L3Language extends L2Language
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L3 = L2Language.L2 + 10 ;


  /**
   * Allocates a new <code>L3Language</code> instance.
   * 
   * @see de.unisiegen.tpml.core.languages.l2.L2Language#L2Language()
   */
  public L3Language ( )
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
    return Messages.getString ( "L3Language.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @ Override
  public String getName ( )
  {
    return "L3" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public int getId ( )
  {
    return L3Language.L3 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L3Language.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L3BigStepProofRuleSet (
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
    return new SmallStepProofModel ( expression , new L3SmallStepProofRuleSet (
        this ) ) ;
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
        new L3TypeCheckerProofRuleSet ( this ) ) ;
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
        new L3TypeInferenceProofRuleSet ( this ) ) ;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public MinimalTypingProofModel newMinimalTypingProofModel ( Expression expression, boolean mode )
  {
    return new MinimalTypingProofModel ( expression , new L3MinimalTypingProofRuleSet(this, mode), mode);
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
    final lr_parser parser = new L3Parser ( scanner ) ;
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
   * @see Language#newScanner(java.io.Reader)
   */
  @ Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L3Scanner ( reader ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L3LanguageTranslator ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  @ Override
  public LanguageTypeParser newTypeParser ( LanguageTypeScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L3TypeParser ( scanner ) ;
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
    return new L3TypeScanner ( reader ) ;
  }
}
