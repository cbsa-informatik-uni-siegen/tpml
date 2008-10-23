package de.unisiegen.tpml.core.languages.l2cunify;


import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.languages.LanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.l2c.L2CLanguage;
import de.unisiegen.tpml.core.languages.l2unify.L2UNIFYLanguage;
import de.unisiegen.tpml.core.typeinference.TypeSubstitutionList;
import de.unisiegen.tpml.core.unify.DefaultUnifyProofNode;
import de.unisiegen.tpml.core.unify.UnifyProofModel;


/**
 * This class represents the language L2CUnify, which serves as a factory class
 * for L2Unify related functionality, which extends the L2Unify language.
 * 
 * @author Christian Fehler
 * @version $Id$
 */
public class L2CUNIFYLanguage extends L2UNIFYLanguage
{

  /**
   * Allocates a new <code>L2CLanguage</code> instance.
   */
  public L2CUNIFYLanguage ()
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
  public static final int L2C_UNIFY = L2CLanguage.L2C + 1;


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @Override
  public String getName ()
  {
    return "L2CUnify"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @Override
  public int getId ()
  {
    return L2CUNIFYLanguage.L2C_UNIFY;
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
    final lr_parser parser = new L2CUnifyParser ( scanner );
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
    return new L2CUnifyScanner ( reader );
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
        TypeSubstitutionList.EMPTY_LIST, eqns ), new L2CUnifyProofRuleSet (
        this ) );
  }
}
