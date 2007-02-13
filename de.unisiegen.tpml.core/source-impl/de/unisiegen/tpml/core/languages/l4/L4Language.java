package de.unisiegen.tpml.core.languages.l4 ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.languages.LanguageTypeParser ;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner ;
import de.unisiegen.tpml.core.languages.l3.L3Language ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.types.MonoType ;


/**
 * This class represents the language L4, which serves as a factory class for L4
 * related functionality, and extends the L3 language.
 * 
 * @author Benedikt Meurer
 * @version $Rev: 279 $
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 * @see de.unisiegen.tpml.core.languages.l2.L2Language
 * @see de.unisiegen.tpml.core.languages.l3.L3Language
 */
public class L4Language extends L3Language
{
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L4 = L3Language.L3 + 10 ;


  /**
   * Allocates a new <code>L4Language</code> instance.
   * 
   * @see de.unisiegen.tpml.core.languages.l3.L3Language#L3Language()
   */
  public L4Language ( )
  {
    super ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getDescription()
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L4Language.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getName()
   */
  @ Override
  public String getName ( )
  {
    return "L4" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public int getId ( )
  {
    return L4Language.L4 ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#getTitle()
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L4Language.1" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L4BigStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public SmallStepProofModel newSmallStepProofModel ( Expression expression )
  {
    return new SmallStepProofModel ( expression , new L4SmallStepProofRuleSet (
        this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public TypeCheckerProofModel newTypeCheckerProofModel ( Expression expression )
  {
    return new TypeCheckerProofModel ( expression ,
        new L4TypeCheckerProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Language#newParser(LanguageScanner)
   */
  @ Override
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L4Parser ( scanner ) ;
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
  @ Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L4Scanner ( reader ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L4LanguageTranslator ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l3.L3Language#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  @ Override
  public LanguageTypeParser newTypeParser ( LanguageTypeScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L4TypeParser ( scanner ) ;
    return new LanguageTypeParser ( )
    {
      public MonoType parse ( ) throws Exception
      {
        return ( MonoType ) parser.parse ( ).value ;
      }
    } ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.l3.L3Language#newTypeScanner(java.io.Reader)
   */
  @ Override
  public LanguageTypeScanner newTypeScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L4TypeScanner ( reader ) ;
  }
}
