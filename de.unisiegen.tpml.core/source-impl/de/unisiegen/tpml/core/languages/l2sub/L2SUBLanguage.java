package de.unisiegen.tpml.core.languages.l2sub;


import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l2.L2Language;
import de.unisiegen.tpml.core.languages.l2c.L2CLanguage;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * This class represents the language L2Subtype, which serves as a factory class
 * for L2 subtype related functionality, which extends the L1SubType
 * 
 * @author Benjamin Mies
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageTypeParser
 * @see de.unisiegen.tpml.core.languages.LanguageTypeScanner
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 */
public class L2SUBLanguage extends L2Language
{

  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L2SubType = L2CLanguage.L2C + 1;


  /**
   * Allocates a new <code>L2SUBLanguage</code> instance.
   */
  public L2SUBLanguage ()
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
    return Messages.getString ( "L2SubTypeLanguage.0" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L2SUB"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L2SUBLanguage.L2SubType;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public String getTitle ()
  {
    return Messages.getString ( "L2SubTypeLanguage.1" ); //$NON-NLS-1$
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
        new L2RecSubTypingProofRuleSet ( this, mode ), mode );
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
    return new SubTypingProofModel ( type, type2, new L2SubTypingProofRuleSet (
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
