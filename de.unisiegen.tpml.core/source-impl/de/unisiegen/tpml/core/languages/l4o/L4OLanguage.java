package de.unisiegen.tpml.core.languages.l4o;


import java.io.Reader;
import java.text.MessageFormat;

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
import de.unisiegen.tpml.core.languages.l4r.L4RLanguage;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * This class represents the language L4O, which serves as a factory class for
 * L4O related functionality, and extends the L4R language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 * @see de.unisiegen.tpml.core.languages.l3.L3Language
 */
public class L4OLanguage extends L4RLanguage
{

  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L4O = L4RLanguage.L4R + 1;


  /**
   * Allocates a new <code>L4OLanguage</code> instance.
   * 
   * @see de.unisiegen.tpml.core.languages.l4r.L4RLanguage#L4RLanguage()
   */
  public L4OLanguage ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getDescription()
   */
  @Override
  public String getDescription ()
  {
    return Messages.getString ( "L4OLanguage.0" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L4OLanguage.L4O;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L4o"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public String getTitle ()
  {
    return Messages.getString ( "L4OLanguage.1" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public BigStepProofModel newBigStepProofModel ( @SuppressWarnings ( "unused" )
  Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.8" ), new Integer ( getName () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression,
   *      boolean)
   */
  @Override
  public MinimalTypingProofModel newMinimalTypingProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.9" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(LanguageScanner)
   */
  @Override
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ); //$NON-NLS-1$
    }
    final lr_parser parser = new L4OParser ( scanner );
    return new LanguageParser ()
    {

      public Expression parse () throws Exception
      {
        return ( Expression ) parser.parse ().value;
      }
    };
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newScanner(java.io.Reader)
   */
  @Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ); //$NON-NLS-1$
    }
    return new L4OScanner ( reader );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression, new L4OSmallStepProofRuleSet (
        this ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @Override
  public LanguageTranslator newTranslator ()
  {
    return new L4OLanguageTranslator ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeCheckerProofModel newTypeCheckerProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.10" ), new Integer ( getName () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeInferenceProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.13" ), new Integer ( getName () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l4.L4Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  @Override
  public LanguageTypeParser newTypeParser ( LanguageTypeScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ); //$NON-NLS-1$
    }
    final lr_parser parser = new L4OTypeParser ( scanner );
    return new LanguageTypeParser ()
    {

      public MonoType parse () throws Exception
      {
        return ( MonoType ) parser.parse ().value;
      }
    };
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l4.L4Language#newTypeScanner(java.io.Reader)
   */
  @Override
  public LanguageTypeScanner newTypeScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ); //$NON-NLS-1$
    }
    return new L4OTypeScanner ( reader );
  }
}
