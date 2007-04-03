package de.unisiegen.tpml.core.languages.l0 ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.AbstractLanguage ;
import de.unisiegen.tpml.core.languages.AbstractLanguageTranslator ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;


/**
 * This class represents the language L0, which serves as a factory class for L0
 * related functionality.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1159 $
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 */
public class L0Language extends AbstractLanguage
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L0 = 0 ;


  /**
   * Allocates a new <code>L0Language</code> instance.
   */
  public L0Language ( )
  {
    // nothing to do here...
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getDescription()
   */
  public String getDescription ( )
  {
    return Messages.getString ( "L0Language.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  public String getName ( )
  {
    return "L0" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public int getId ( )
  {
    return L0Language.L0 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  public String getTitle ( )
  {
    return Messages.getString ( "L0Language.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L0BigStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression , new L0SmallStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(LanguageScanner)
   */
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L0Parser ( scanner ) ;
    return new LanguageParser ( )
    {
      public Expression parse ( ) throws Exception
      {
        return ( Expression ) parser.parse ( ).value ;
      }
    } ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newScanner(java.io.Reader)
   */
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L0Scanner ( reader ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newTranslator()
   */
  public LanguageTranslator newTranslator ( )
  {
    return new AbstractLanguageTranslator ( )
    {
      // Nothing do do
    } ;
  }
}
