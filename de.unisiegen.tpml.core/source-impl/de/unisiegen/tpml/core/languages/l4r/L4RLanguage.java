package de.unisiegen.tpml.core.languages.l4r;


import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;


/**
 * This class represents the language L4R, which serves as a factory class for L4R
 * related functionality, and extends the L4 language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 * @see de.unisiegen.tpml.core.languages.l3.L3Language
 * @see de.unisiegen.tpml.core.languages.l4.L4Language
 */
public class L4RLanguage extends L4Language
{

  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L4R = L4Language.L4 + 1;


  /**
   * Allocates a new <code>L4RLanguage</code> instance.
   * 
   * @see de.unisiegen.tpml.core.languages.l4.L4Language#L4Language()
   */
  public L4RLanguage ()
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
    return Messages.getString ( "L4RLanguage.0" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L4r"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L4RLanguage.L4R;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public String getTitle ()
  {
    return Messages.getString ( "L4RLanguage.1" ); //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l4.L4Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeCheckerProofModel newTypeCheckerProofModel ( Expression expression )
  {
    return new TypeCheckerProofModel ( expression,
        new L4RTypeCheckerProofRuleSet ( this ) );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l4.L4Language#newTypeInferenceProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeInferenceProofModel newTypeInferenceProofModel (
      Expression expression )
  {
    return new TypeInferenceProofModel ( expression,
        new L4RTypeInferenceProofRuleSet ( this ) );
  }
}
