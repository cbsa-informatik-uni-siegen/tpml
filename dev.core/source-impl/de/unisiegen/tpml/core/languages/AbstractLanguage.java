package de.unisiegen.tpml.core.languages;


import java.io.Reader;
import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.bigstepclosure.BigStepClosureProofModel;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.unify.UnifyProofModel;


/**
 * Abstract base class for classes that implement the
 * {@link de.unisiegen.tpml.core.languages.Language} interface.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.Language
 */
public abstract class AbstractLanguage implements Language
{

  /**
   * Allocates a new <code>AbstractLanguage</code>.
   */
  protected AbstractLanguage ()
  {
    // nothing to do here.
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#isTypeLanguage()
   */
  public boolean isTypeLanguage ()
  {
    return false;
  }
  
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#isUnifyLanguage()
   */
  public boolean isUnifyLanguage()
  {
    return false;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public BigStepProofModel newBigStepProofModel ( @SuppressWarnings ( "unused" )
  Expression expression )
  {
    // TODO i18n
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepClosureProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public BigStepClosureProofModel newBigStepClosureProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression,
   *      boolean)
   */
  public MinimalTypingProofModel newMinimalTypingProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newParser(de.unisiegen.tpml.core.languages.LanguageScanner)
   */
  public LanguageParser newParser ( @SuppressWarnings ( "unused" )
  LanguageScanner scanner )
  {
    // TODO
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(java.io.Reader)
   */
  public final LanguageParser newParser ( Reader reader )
  {
    return newParser ( newScanner ( reader ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newRecSubTypingProofModel(MonoType
   *      type, MonoType type2, boolean mode)
   */
  public RecSubTypingProofModel newRecSubTypingProofModel (
      @SuppressWarnings ( "unused" )
      MonoType type, @SuppressWarnings ( "unused" )
      MonoType type2, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newScanner(java.io.Reader)
   */
  public LanguageScanner newScanner ( @SuppressWarnings ( "unused" )
  Reader reader )
  {
    // TODO
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public SmallStepProofModel newSmallStepProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    // TODO
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSubTypingProofModel(MonoType,
   *      MonoType, boolean)
   */
  public SubTypingProofModel newSubTypingProofModel (
      @SuppressWarnings ( "unused" )
      MonoType type, @SuppressWarnings ( "unused" )
      MonoType type2, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  public LanguageTranslator newTranslator ()
  {
    // TODO
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public TypeCheckerProofModel newTypeCheckerProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeInferenceProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public TypeInferenceProofModel newTypeInferenceProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  public LanguageTypeParser newTypeParser ( @SuppressWarnings ( "unused" )
  LanguageTypeScanner scanner )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(java.io.Reader)
   */
  public final LanguageTypeParser newTypeParser ( Reader reader )
  {
    return newTypeParser ( newTypeScanner ( reader ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeScanner(java.io.Reader)
   */
  public LanguageTypeScanner newTypeScanner ( @SuppressWarnings ( "unused" )
  Reader reader )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newUnifyParser(de.unisiegen.tpml.core.languages.LanguageUnifyScanner)
   */
  public LanguageUnifyParser newUnifyParser ( @SuppressWarnings ( "unused" )
  LanguageUnifyScanner scanner )
  {
    // TODO i18n
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newUnifyParser(java.io.Reader)
   */
  public LanguageUnifyParser newUnifyParser ( Reader reader )
  {
    return newUnifyParser ( newUnifyScanner ( reader ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newUnifyScanner(java.io.Reader)
   */
  public LanguageUnifyScanner newUnifyScanner ( @SuppressWarnings ( "unused" )
  Reader reader )
  {
    // TODO i18n
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newUnifyProofModel(de.unisiegen.tpml.core.entities.TypeEquationList)
   */
  public UnifyProofModel newUnifyProofModel ( @SuppressWarnings("unused")
  TypeEquationList eqns )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ), getName () ) ); //$NON-NLS-1$
  }
}
