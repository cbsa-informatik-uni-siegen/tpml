package de.unisiegen.tpml.core.languages.l2;

import java.io.Reader;

import java_cup.runtime.lr_parser;
import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.languages.LanguageParser;
import de.unisiegen.tpml.core.languages.LanguageScanner;
import de.unisiegen.tpml.core.languages.LanguageTranslator;
import de.unisiegen.tpml.core.languages.l0.L0Language;
import de.unisiegen.tpml.core.languages.l1.L1Language;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;

/**
 * This class represents the language L2, which serves as a factory class for L2 related functionality,
 * and extends the L1 language.
 *
 * @author Benedikt Meurer
 * @version $Rev$
 * 
 * @see de.unisiegen.tpml.core.languages.Language
 * @see de.unisiegen.tpml.core.languages.LanguageParser
 * @see de.unisiegen.tpml.core.languages.LanguageScanner
 * @see de.unisiegen.tpml.core.languages.LanguageTranslator
 * @see de.unisiegen.tpml.core.languages.l0.L0Language
 * @see de.unisiegen.tpml.core.languages.l1.L1Language
 */
public class L2Language extends L1Language {
  //
  // Constants
  //
  
  /**
   * The group id for proof rules of this language.
   * 
   * @see de.unisiegen.tpml.core.AbstractProofRule#getGroup()
   */
  public static final int L2 = L1Language.L1 + 1;
  
  
  
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>L2Language</code> instance.
   */
  public L2Language() {
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
    return Messages.getString("L2Language.0"); //$NON-NLS-1$
  }
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getName()
   */
  @Override
  public String getName() {
    return "L2"; //$NON-NLS-1$
  }
  
  
  public int getId ()
  {
    return L2Language.L2;
  }
  
  
  /**
   * {@inheritDoc}
   *
   * @see languages.Language#getTitle()
   */
  @Override
  public String getTitle() {
    return Messages.getString("L2Language.1"); //$NON-NLS-1$
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
    return new BigStepProofModel(expression, new L2BigStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newSmallStepProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public SmallStepProofModel newSmallStepProofModel(Expression expression) {
    return new SmallStepProofModel(expression, new L2SmallStepProofRuleSet(this));
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.l1.L1Language#newTypeCheckerProofModel(de.unisiegen.tpml.core.expressions.Expression)
   */
  @Override
  public TypeCheckerProofModel newTypeCheckerProofModel(Expression expression) {
    return new TypeCheckerProofModel(expression, new L2TypeCheckerProofRuleSet(this));
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
   final lr_parser parser = new L2Parser(scanner);
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
  @Override
  public LanguageScanner newScanner(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("reader is null"); //$NON-NLS-1$
    }
    return new L2Scanner(reader);
  }
  
  /**
   * {@inheritDoc}
   *
   * @see de.unisiegen.tpml.core.languages.Language#newTranslator()
   */
  @Override
  public LanguageTranslator newTranslator() {
    return new L2LanguageTranslator();
  }
}
