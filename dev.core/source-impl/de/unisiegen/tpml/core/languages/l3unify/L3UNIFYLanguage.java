package de.unisiegen.tpml.core.languages.l3unify;


import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.languages.LanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.l3.L3Language;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.unify.DefaultUnifyProofNode;
import de.unisiegen.tpml.core.unify.UnifyProofModel;


/**
 * This class represents the language L3, which serves as a factory class for L3
 * related functionality, which extends the L2 language.
 * 
 * @author Christian Fehler
 * @version $Id: L3UNIFYLanguage.java 2815 2008-04-11 13:50:41Z uhrhan $
 */
public class L3UNIFYLanguage extends AbstractLanguage
{

  /**
   * Allocates a new <code>L3Language</code> instance.
   */
  public L3UNIFYLanguage ()
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
  public static final int L3_UNIFY = L3Language.L3 + 1;


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  public String getName ()
  {
    return "L3Unify"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public int getId ()
  {
    return L3UNIFYLanguage.L3_UNIFY;
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
    final lr_parser parser = new L3UnifyParser ( scanner );
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
    return new L3UnifyScanner ( reader );
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
        new L3UnifyProofRuleSet ( this ) );
  }
}
