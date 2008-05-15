package de.unisiegen.tpml.core.languages.l4unify;


import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.languages.LanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.l4.L4Language;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.unify.DefaultUnifyProofNode;
import de.unisiegen.tpml.core.unify.UnifyProofModel;


/**
 * This class represents the language L4Unify, which serves as a factory class
 * for L4Unify related functionality, which extends the L0 language.
 * 
 * @author Christian Fehler
 * @version $Id: L4UNIFYLanguage.java 2815 2008-04-11 13:50:41Z uhrhan $
 */
public class L4UNIFYLanguage extends AbstractLanguage
{

  /**
   * Allocates a new <code>L4Language</code> instance.
   */
  public L4UNIFYLanguage ()
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
  public static final int L4_UNIFY = L4Language.L4 + 1;


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  public String getName ()
  {
    return "L4Unify"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public int getId ()
  {
    return L4UNIFYLanguage.L4_UNIFY;
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
    final lr_parser parser = new L4UnifyParser ( scanner );
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
    return new L4UnifyScanner ( reader );
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
        new L4UnifyProofRuleSet ( this ) );
  }
}
