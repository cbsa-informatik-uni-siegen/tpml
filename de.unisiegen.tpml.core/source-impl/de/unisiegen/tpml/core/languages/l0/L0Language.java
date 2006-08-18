package de.unisiegen.tpml.core.languages.l0;

import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.AbstractLanguage;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageTranslator;

/**
 * This class represents the language L0.
 *
 * @author Benedikt Meurer
 * @version $Id$
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 */
public final class L0Language extends AbstractLanguage {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L0Language</code> instance.
   */
  public L0Language() {
    // nothing to do here...
  }
  
  
  
  //
  // Accessors
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getDescription()
   */
  public String getDescription() {
    return "TODO";
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getName()
   */
  public String getName() {
    return "L0";
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getTitle()
   */
  public String getTitle() {
    return "Pure untyped λ calculus";
  }
  
  
  
  //
  // Parser/Scanner allocation
  //
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newParser(languages.LanguageScanner)
   */
 public LanguageParser newParser(LanguageScanner scanner) {
   if (scanner == null) {
     throw new NullPointerException("scanner is null");
   }
   final lr_parser parser = new L0Parser(scanner);
   return new LanguageParser() {
     public Expression parse() throws Exception {
       return (Expression)parser.parse().value;
     }
   };
 }

  /**
   * {@inheritDoc}
   *
   * @see languages.Language#newScanner(java.io.Reader)
   */
  public LanguageScanner newScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null");
    }
    return new L0Scanner(reader);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  public LanguageTranslator newTranslator() {
    return new LanguageTranslator() {
      public Expression translateToCoreSyntax(Expression expression) { return expression; }
      public Expression translateToCoreSyntax(Expression expression, boolean recursive) { return expression; }
    };
  }
}
