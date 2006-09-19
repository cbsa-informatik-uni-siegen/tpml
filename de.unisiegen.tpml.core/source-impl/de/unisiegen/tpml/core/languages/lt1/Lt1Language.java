package de.unisiegen.tpml.core.languages.lt1;

import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;

/**
 * This class represents the language Lt1, which serves as a factory class for L1 related functionality,
 * which extends the L1 language with a type system.
 *
 * @author Benedikt Meurer
 * @version $Id$
 *
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class Lt1Language extends L1Language {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>Lt1Language</code> instance.
   */
  public Lt1Language() {
    super();
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#getDescription()
   */
  @Override
  public String getDescription() {
    return "TODO";
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#getName()
   */
  @Override
  public String getName() {
    return "Lt1";
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#getTitle()
   */
  @Override
  public String getTitle() {
    return "Applied typed λ calculus";
  }
  
  
  
  //
  // Primitives
  //
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newBigStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public BigStepProofModel newBigStepProofModel(Expression expression) {
    return new BigStepProofModel(expression, new Lt1BigStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public SmallStepProofModel newSmallStepProofModel(Expression expression) {
    return new SmallStepProofModel(expression, new Lt1SmallStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newParser(de.unisiegen.tpml.core.languages.LanguageScanner)
   */
  @Override
  public LanguageParser newParser(LanguageScanner scanner) {
    if (scanner == null) {
      throw new NullPointerException("scanner is null");
    }
    final lr_parser parser = new Lt1Parser(scanner);
    return new LanguageParser() {
      public Expression parse() throws Exception {
        return (Expression)parser.parse().value;
      }
    };
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newScanner(java.io.Reader)
   */
  @Override
  public LanguageScanner newScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null");
    }
    return new Lt1Scanner(reader);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTranslator()
   */
  @Override
  public LanguageTranslator newTranslator() {
    return new Lt1LanguageTranslator();
  }
}
