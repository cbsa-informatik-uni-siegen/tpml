package de.unisiegen.tpml.core.languages.l4sub;


import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * This class represents the language L4Subtype, which serves as a factory class
 * for L4 subtype related functionality, which extends the L3SubType
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageTypeParser
 * @see de.unisiegen.tpml.core.languages.LanguageTypeScanner
 * @see de.unisiegen.tpml.core.languages.l4.L4Language
 */
public class L4SUBLanguage extends L4Language
{

  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L4SubType = L4Language.L4 + 1;


  /**
   * Allocates a new <code>L4SUBLanguage</code> instance.
   */
  public L4SUBLanguage ()
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
    return Messages.getString ( "L4SubTypeLanguage.0" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L4SUB"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L4SUBLanguage.L4SubType;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public String getTitle ()
  {
    return Messages.getString ( "L4SubTypeLanguage.1" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#isTypeLanguage()
   */
  @Override
  public boolean isTypeLanguage ()
  {
    return true;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public BigStepProofModel newBigStepProofModel ( @SuppressWarnings ( "unused" )
  Expression pExpression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.8" ), new Integer ( getId () ) ) ); //$NON-NLS-1$
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
        .getString ( "Exception.9" ), new Integer ( getId () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newSubTypingProofModel(MonoType, MonoType, boolean)
   */
  @Override
  public RecSubTypingProofModel newRecSubTypingProofModel (
      @SuppressWarnings ( "unused" )
      MonoType type, @SuppressWarnings ( "unused" )
      MonoType type2, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    return new RecSubTypingProofModel ( type, type2,
        new L4RecSubTypingProofRuleSet ( this, mode ), mode );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public SmallStepProofModel newSmallStepProofModel (
      @SuppressWarnings ( "unused" )
      Expression pExpression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.14" ), new Integer ( getId () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newSubTypingProofModel(MonoType, MonoType, boolean)
   */
  @Override
  public SubTypingProofModel newSubTypingProofModel (
      @SuppressWarnings ( "unused" )
      MonoType type, @SuppressWarnings ( "unused" )
      MonoType type2, @SuppressWarnings ( "unused" )
      boolean mode )
  {
    return new SubTypingProofModel ( type, type2, new L4SubTypingProofRuleSet (
        this, mode ), mode );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public TypeCheckerProofModel newTypeCheckerProofModel (
      @SuppressWarnings ( "unused" )
      Expression pExpression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.10" ), new Integer ( getId () ) ) ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      @SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "Exception.13" ), new Integer ( getId () ) ) ); //$NON-NLS-1$
  }
}
