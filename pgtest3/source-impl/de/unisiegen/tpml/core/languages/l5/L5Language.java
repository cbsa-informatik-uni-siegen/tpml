package de.unisiegen.tpml.core.languages.l5 ;


import java.io.Reader ;
import java_cup.runtime.lr_parser ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.languages.LanguageScanner ;
import de.unisiegen.tpml.core.languages.LanguageTranslator ;
import de.unisiegen.tpml.core.languages.LanguageTypeParser ;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner ;
import de.unisiegen.tpml.core.languages.l4.L4Language ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel ;
import de.unisiegen.tpml.core.types.MonoType ;


public class L5Language extends L4Language
{
  public static final int L5 = L4Language.L4 + 1 ;


  public L5Language ( )
  {
    super ( ) ;
  }


  //
  // Accessors
  //
  /**
   * {@inheritDoc}
   * 
   * @see languages.Language#getDescription()
   */
  @ Override
  public String getDescription ( )
  {
    return Messages.getString ( "L5Language.0" ) ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see languages.Language#getName()
   */
  @ Override
  public String getName ( )
  {
    return "L5" ; //$NON-NLS-1$
  }


  /**
   * {@inheritDoc}
   * 
   * @see languages.Language#getTitle()
   */
  @ Override
  public String getTitle ( )
  {
    return Messages.getString ( "L5Language.1" ) ; //$NON-NLS-1$
  }


  //
  // Primitives
  //
  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @ Override
  public BigStepProofModel newBigStepProofModel ( Expression expression )
  {
    return new BigStepProofModel ( expression , new L5BigStepProofRuleSet (
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
    return new SmallStepProofModel ( expression , new L5SmallStepProofRuleSet (
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
        new L5TypeCheckerProofRuleSet ( this ) ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
  @ Override
  public LanguageParser newParser ( LanguageScanner scanner )
  {
    if ( scanner == null )
    {
      throw new NullPointerException ( "scanner is null" ) ; //$NON-NLS-1$
    }
    final lr_parser parser = new L5Parser ( scanner ) ;
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
   * @see languages.Language#newScanner(java.io.Reader)
   */
  @ Override
  public LanguageScanner newScanner ( Reader reader )
  {
    if ( reader == null )
    {
      throw new NullPointerException ( "reader is null" ) ; //$NON-NLS-1$
    }
    return new L5Scanner ( reader ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @ Override
  public LanguageTranslator newTranslator ( )
  {
    return new L5LanguageTranslator ( ) ;
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
    final lr_parser parser = new L5TypeParser ( scanner ) ;
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
    return new L5TypeScanner ( reader ) ;
  }
}
