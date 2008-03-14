package de.unisiegen.tpml.core.languages.l1unify;


import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.entities.TypeEquationList;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.languages.LanguageUnifyParser;
import de.unisiegen.tpml.core.languages.LanguageUnifyScanner;
import de.unisiegen.tpml.core.languages.l1sub.L1SUBLanguage;


/**
 * This class represents the language L1, which serves as a factory class for L1
 * related functionality, which extends the L0 language.
 * 
 * @author Christian Fehler
 * @version $Rev:1159 $
 */
public class L1UNIFYLanguage extends AbstractLanguage
{

  /**
   * Allocates a new <code>L1Language</code> instance.
   */
  public L1UNIFYLanguage ()
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
  public static final int L1_UNIFY = L1SUBLanguage.L1SubType + 1;


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  public String getName ()
  {
    return "L1Unify"; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public int getId ()
  {
    return L1UNIFYLanguage.L1_UNIFY;
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
    final lr_parser parser = new L1UnifyParser ( scanner );
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
    return new L1UnifyScanner ( reader );
  }
}
