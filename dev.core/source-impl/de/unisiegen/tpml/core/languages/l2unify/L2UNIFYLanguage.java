package de.unisiegen.tpml.core.languages.l2unify;


import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.languages.LanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.l1unify.L1UNIFYLanguage;
import de.unisiegen.tpml.core.languages.l2sub.L2SUBLanguage;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.unify.DefaultUnifyProofNode;
import de.unisiegen.tpml.core.unify.UnifyProofModel;


/**
 * This class represents the language L2Unify, which serves as a factory class for L2Unify
 * related functionality, which extends the L1Unify language.
 * 
 * @author Christian Fehler
 * @version $Id: L2UNIFYLanguage.java 2815 2008-04-11 13:50:41Z uhrhan $
 */
public class L2UNIFYLanguage extends L1UNIFYLanguage
{

  /**
   * Allocates a new <code>L2UnifyLanguage</code> instance.
   */
  public L2UNIFYLanguage ()
  {
    super ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getDescription()
   */
  public String getDescription ()
  {
    // TODO
    return "TODO"; //$NON-NLS-1$
  }


  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L2_UNIFY = L2SUBLanguage.L2SubType + 1;


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L2Unify"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L2UNIFYLanguage.L2_UNIFY;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public String getTitle ()
  {
    // TODO
    return "TODO"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see AbstractLanguage#newUnifyParser(LanguageUnifyScanner)
   */
  @Override
  public LanguageUnifyParser newUnifyParser ( LanguageUnifyScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ); //$NON-NLS-1$
    }
    final lr_parser parser = new L2UnifyParser ( scanner );
    return new LanguageUnifyParser ()
    {

      public TypeEquationList parse () throws Exception
      {
        return ( TypeEquationList ) parser.parse ().value;
      }
    };
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newScanner(java.io.Reader)
   */
  @Override
  public LanguageUnifyScanner newUnifyScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ); //$NON-NLS-1$
    }
    return new L2UnifyScanner ( reader );
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newUnifyProofModel(de.unisiegen.tpml.core.entities.TypeEquationList)
   */
  @Override
  public UnifyProofModel newUnifyProofModel ( TypeEquationList eqns )
  {
    return new UnifyProofModel ( new DefaultUnifyProofNode (
        TypeSubstitutionList.EMPTY_LIST, eqns ),
        new L2UnifyProofRuleSet ( this ) );
  }
}
