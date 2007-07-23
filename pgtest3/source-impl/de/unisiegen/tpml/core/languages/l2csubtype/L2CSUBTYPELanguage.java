package de.unisiegen.tpml.core.languages.l2csubtype;

import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l2c.L2CLanguage;
import de.unisiegen.tpml.core.languages.l2o.L2OLanguage;
import de.unisiegen.tpml.core.languages.l2osubtype.L2ORecSubTypingProofRuleSet;
import de.unisiegen.tpml.core.languages.l2osubtype.L2OSubTypingProofRuleSet;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofModel;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.subtypingrec.RecSubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;

public class L2CSUBTYPELanguage extends L2OLanguage {

	/**
	   * The group id for proof rules of this language.
	   * 
	   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
	   */
	  public static final int L2CSubType = L2CLanguage.L2C + 1 ;
	  
	public L2CSUBTYPELanguage ( ) {
	super();
	}



	
	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#getDescription()
	   */
	  public String getDescription ( )
	  {
	    return Messages.getString ( "L2CSubTypeLanguage.0" ) ; //$NON-NLS-1$
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#getName()
	   */
	  public String getName ( )
	  {
	    return "L2CSubType" ; //$NON-NLS-1$
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#getTitle()
	   */
	  public int getId ( )
	  {
	    return L2CSUBTYPELanguage.L2CSubType ;
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#getTitle()
	   */
	  public String getTitle ( )
	  {
	    return Messages.getString ( "L2CSubTypeLanguage.1" ) ; //$NON-NLS-1$
	  }
	
	@Override
	public boolean isTypeLanguage() {
		  return true;
	  }
	
	 /**
	   * {@inheritDoc}
	   */
	  @ Override
	  public BigStepProofModel newBigStepProofModel (
	      @ SuppressWarnings ( "unused" )
	      Expression pExpression )
	  {
		  throw new UnsupportedOperationException ( MessageFormat.format ( Messages
		        .getString ( "Exception.8" ), new Integer(getId() ) ) ) ; //$NON-NLS-1$
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
		        .getString ( "Exception.9" ), new Integer(getId() ) ) ) ; //$NON-NLS-1
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#newSubTypingProofModel(MonoType, MonoType, boolean)
	   */
	  @ Override
	  public RecSubTypingProofModel newRecSubTypingProofModel (
	      @ SuppressWarnings ( "unused" )
	      MonoType type , @ SuppressWarnings ( "unused" )
	      MonoType type2 , @ SuppressWarnings ( "unused" )
	      boolean mode )
	  {
		  return new RecSubTypingProofModel(type, type2, new L2ORecSubTypingProofRuleSet(this, mode), mode);
	  }


	  /**
	   * {@inheritDoc}
	   */
	  @ Override
	  public SmallStepProofModel newSmallStepProofModel ( Expression pExpression )
	  {
		  throw new UnsupportedOperationException ( MessageFormat.format ( Messages
		        .getString ( "Exception.14" ), new Integer(getId() ) ) ) ; //$NON-NLS-1
	  }


	  /**
	   * {@inheritDoc}
	   * 
	   * @see Language#newSubTypingProofModel(MonoType, MonoType, boolean)
	   */
	  @ Override
	  public SubTypingProofModel newSubTypingProofModel (
	      @ SuppressWarnings ( "unused" )
	      MonoType type , @ SuppressWarnings ( "unused" )
	      MonoType type2 , @ SuppressWarnings ( "unused" )
	      boolean mode )
	  {
		  return new SubTypingProofModel(type, type2, new L2OSubTypingProofRuleSet(this, mode), mode);
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
		        .getString ( "Exception.10" ), new Integer(getId() ) ) ) ; //$NON-NLS-1
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
		        .getString ( "Exception.13" ), new Integer(getId() ) ) ) ; //$NON-NLS-1
	  }

}
