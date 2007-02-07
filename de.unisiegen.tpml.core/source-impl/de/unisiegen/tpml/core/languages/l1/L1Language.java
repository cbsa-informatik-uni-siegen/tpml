package de.unisiegen.tpml.core.languages.l1;

import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.languages.LanguageTypeParser;
import de.unisiegen.tpml.core.languages.LanguageTypeScanner;
import de.unisiegen.tpml.core.languages.l0.L0Language;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * This class represents the language L1, which serves as a factory class for L1 related functionality,
 * which extends the L0 language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 */
public class L1Language extends L0Language {
  //
  // Constants
  //
  
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L1 = L0Language.L0 + 1;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L1Language</code> instance.
   */
  public L1Language() {
    super();
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getDescription()
   */
  @Override
  public String getDescription() {
    return Messages.getString("L1Language.0"); //$NON-NLS-1$
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getName()
   */
  @Override
  public String getName() {
    return "L1"; //$NON-NLS-1$
  }
  
  
  public int getId ()
  {
    return L1Language.L1;
  }
  
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getTitle()
   */
  @Override
  public String getTitle() {
    return Messages.getString("L1Language.1"); //$NON-NLS-1$
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public BigStepProofModel newBigStepProofModel(Expression expression) {
    return new BigStepProofModel(expression, new L1BigStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public SmallStepProofModel newSmallStepProofModel(Expression expression) {
    return new SmallStepProofModel(expression, new L1SmallStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeCheckerProofModel newTypeCheckerProofModel(Expression expression) {
    return new TypeCheckerProofModel(expression, new L1TypeCheckerProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
  @Override
 public LanguageParser newParser(LanguageScanner scanner) {
   if (scanner == null) {
     throw new NullPointerException("scanner is null"); //$NON-NLS-1$
   }
   final lr_parser parser = new L1Parser(scanner);
   return new LanguageParser() {
     public Expression parse() throws Exception {
       return (Expression)parser.parse().value;
     }
   };
 }

  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newScanner(java.io.Reader)
   */
  @Override
  public LanguageScanner newScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null"); //$NON-NLS-1$
    }
    return new L1Scanner(reader);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @Override
  public LanguageTranslator newTranslator() {
    return new L1LanguageTranslator();
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newTypeParser(de.unisiegen.tpml.core.languages.LanguageTypeScanner)
   */
  @Override
  public LanguageTypeParser newTypeParser(LanguageTypeScanner scanner) {
    if (scanner == null) {
      throw new NullPointerException("scanner is null"); //$NON-NLS-1$
    }
    final lr_parser parser = new L1TypeParser(scanner);
    return new LanguageTypeParser() {
      public MonoType parse() throws Exception {
        return (MonoType)parser.parse().value;
      }
    };
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.AbstractLanguage#newTypeScanner(java.io.Reader)
   */
  @Override
  public LanguageTypeScanner newTypeScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null"); //$NON-NLS-1$
    }
    return new L1TypeScanner(reader);
  }
}
