package de.unisiegen.tpml.core.languages ;


import java.io.Reader;
import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;


/**
 * Abstract base class for classes that implement the
 * {@link de.unisiegen.tpml.core.languages.Language} interface.
 * 
 * @author Benedikt Meurer
 * @version $Rev:415 $
 * @see de.unisiegen.tpml.core.languages.Language
 */
public abstract class AbstractLanguage implements Language
{
  /**
   * Allocates a new <code>AbstractLanguage</code>.
   */
  protected AbstractLanguage ( )
  {
    // nothing to do here.
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public TypeCheckerProofModel newTypeCheckerProofModel (
      @ SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newMinimalTypingProofModel(de.unisiegen.tpml.core.expressions.Expression, Boolean)
   */
  public MinimalTypingProofModel newMinimalTypingProofModel (
      @ SuppressWarnings ( "unused" )
      Expression expression, @SuppressWarnings("unused")
		boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeInferenceProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public TypeInferenceProofModel newTypeInferenceProofModel (
      @ SuppressWarnings ( "unused" )
      Expression expression )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSubTypingProofModel(MonoType, MonoType, boolean)
   */
  public SubTypingProofModel newSubTypingProofModel ( 
		  @SuppressWarnings("unused")	MonoType type, 
		  @SuppressWarnings("unused") MonoType type2, 
		  @SuppressWarnings("unused")	boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newRecSubTypingProofModel(MonoType type, MonoType type2, boolean mode)
   */
  public RecSubTypingProofModel newRecSubTypingProofModel ( 
		  @SuppressWarnings("unused")	MonoType type, 
		  @SuppressWarnings("unused")	MonoType type2, 
		  @SuppressWarnings("unused")	boolean mode )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(java.io.Reader)
   */
  public final LanguageParser newParser ( Reader reader )
  {
    return newParser ( newScanner ( reader ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  public LanguageTypeParser newTypeParser ( @ SuppressWarnings ( "unused" )
  LanguageTypeScanner scanner )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeParser(java.io.Reader)
   */
  public final LanguageTypeParser newTypeParser ( Reader reader )
  {
    return newTypeParser ( newTypeScanner ( reader ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTypeScanner(java.io.Reader)
   */
  public LanguageTypeScanner newTypeScanner ( @ SuppressWarnings ( "unused" )
  Reader reader )
  {
    throw new UnsupportedOperationException ( MessageFormat.format ( Messages
        .getString ( "AbstractLanguage.0" ) , getName ( ) ) ) ; //$NON-NLS-1$
  }
  
  /**
   * 
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#isTypeLanguage()
   */
  public boolean isTypeLanguage() {
	  return false;
  }
}
