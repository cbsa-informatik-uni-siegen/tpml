package de.unisiegen.tpml.core.languages;

import java.io.Reader;

import de.unisiegen.tpml.core.bigstep.BigStepProofModel;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofModel;
import de.unisiegen.tpml.core.typeinference.TypeInferenceProofModel;
import de.unisiegen.tpml.core.types.MonoType;

/**
 * Base interface for all languages, which is used to create scanners and parsers for a specific language.
 *
 * @author Benedikt Meurer
 * @version $Rev:1125 $
 */
public interface Language {
  //
  // Accessors
  //
  
  /**
   * Returns the description of this language, which should be a descriptive text explaining the features
   * and abilities of this particular language.
   * 
   * @return the description of this language.
   */
  public String getDescription();
  
  /**
   * Returns the name of the language, i.e. <tt>"L0"</tt> or <tt>"L1"</tt>.
   * 
   * @return the name of the language.
   */
  public String getName();
  
  
  public int getId () ;
  
  
  /**
   * Returns the title of the language, i.e. <tt>"Pure untyped lambda calculus"</tt>.
   * 
   * @return the title of the language.
   */
  public String getTitle();
  
  
  
  //
  // Primitives
  //
  
  /**
   * Allocates a new {@link BigStepProofModel} for the <code>expression</code> in this language, which
   * is used to prove that <code>expression</code> using the big step semantic based on the rules from
   * this language.
   * 
   * @param expression the {@link Expression} for the big step proof model.
   * 
   * @return the newly allocated big step proof model.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   * 
   * @see BigStepProofModel
   */
  public BigStepProofModel newBigStepProofModel(Expression expression);
  
  /**
   * Allocates a new {@link SmallStepProofModel} for the <code>expression</code> in this language, which
   * is used to prove that <code>expression</code> using the small step semantics based on the rules from
   * this language.
   * 
   * @param expression the {@link Expression} for the small step proof model.
   * 
   * @return the newly allocated small step proof model.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   * 
   * @see SmallStepProofModel
   */
  public SmallStepProofModel newSmallStepProofModel(Expression expression);
  
  /**
   * Allocates a new {@link TypeCheckerProofModel} for the <code>expression</code> in this language, which
   * is used to prove that <code>expression</code> is well-typed using the rules from this language.
   * 
   * @param expression the {@link Expression} for the type checker proof model.
   * 
   * @return the newly allocated type checker proof model.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   * @throws UnsupportedOperationException if the language does not include a type system.
   * 
   * @see TypeCheckerProofModel
   */
  public TypeCheckerProofModel newTypeCheckerProofModel(Expression expression);
  
  /**
   * Allocates a new {@link TypeInferenceProofModel} for the <code>expression</code> in this language, which
   * is used to prove that <code>expression</code> is well-typed using the rules from this language.
   * 
   * @param expression the {@link Expression} for the type inference proof model.
   * 
   * @return the newly allocated type inference proof model.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   * @throws UnsupportedOperationException if the language does not include a type system.
   * 
   * @see TypeInferenceProofModel
   */
  public TypeInferenceProofModel newTypeInferenceProofModel(Expression expression);
  
  /**
   * Allocates a new {@link SubTypingProofModel} for the <code>expression</code> in this language, which
   * is used to prove that <code>expression</code> is well-typed using the rules from this language.
   * 
   * @param expression the {@link Expression} for the subtyping proof model.
   * 
   * @return the newly allocated subtyping proof model.
   * 
   * @throws NullPointerException if <code>expression</code> is <code>null</code>.
   * @throws UnsupportedOperationException if the language does not include a type system.
   * 
   * @see SubTypingProofModel
   */
  public SubTypingProofModel newSubTypingProofModel(MonoType type, MonoType type2);
  
  /**
   * Allocates a new {@link LanguageParser} for this language, using the specified <code>scanner</code> as
   * token source for the newly allocated parser.
   * 
   * @param scanner the {@link LanguageScanner} to use as token source for the newly allocated parser.
   *
   * @return the newly allocated parser for this language.
   * 
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  public LanguageParser newParser(LanguageScanner scanner);
  
  /**
   * Convenience wrapper method for the {@link #newParser(LanguageScanner)} method, which automatically
   * allocates a scanner for the specified <code>reader</code> using the {@link #newScanner(Reader)} method.
   * 
   * @param reader the {@link Reader} for the source input stream.
   * 
   * @return the newly allocated parser for this language.
   * 
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public LanguageParser newParser(Reader reader);

  /**
   * Allocates a new {@link LanguageScanner}, a lexer, for this language, which parses tokens from the
   * specified <code>reader</code>.
   * 
   * @param reader the {@link Reader} for the source input stream.
   * 
   * @return a newly allocated scanner for this language.
   * 
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public LanguageScanner newScanner(Reader reader);
  
  /**
   * Allocates a new {@link LanguageTranslator} for this language, which is used to translate expressions
   * to core syntax for this language.
   * 
   * @return a newly allocated language translator.
   * 
   * @see LanguageTranslator
   */
  public LanguageTranslator newTranslator();
  
  /**
   * Allocates a new {@link LanguageTypeParser} for this language, using the specified
   * <code>scanner</code> as token source for the newly allocated parser. The parser can
   * then be used to parse the tokens returned from the <code>scanner</code> into a valid
   * {@link de.unisiegen.tpml.core.types.MonoType}.
   * 
   * @param scanner the scanner from which to read the tokens.
   * 
   * @return a newly allocated type parser for this language.
   * 
   * @throws NullPointerException if <code>scanner</code> is <code>null</code>.
   */
  public LanguageTypeParser newTypeParser(LanguageTypeScanner scanner);
  
  /**
   * Convenience wrapper for the {@link #newTypeParser(LanguageTypeScanner)} method, which automatically
   * creates a new {@link LanguageTypeScanner} for the specified <code>reader</code>.
   * 
   * @param reader the reader from which to read the source code.
   * 
   * @return the newly allocated type parser for this language.
   * 
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public LanguageTypeParser newTypeParser(Reader reader);
  
  /**
   * Allocates a new {@link LanguageTypeScanner}, a lexer, for this language, which parses tokens that
   * may appear in a {@link de.unisiegen.tpml.core.types.MonoType}s string representation from the
   * specified <code>reader</code>.
   * 
   * @param reader the {@link Reader} for the source input stream.
   * 
   * @return a newly allocated scanner for this language.
   * 
   * @throws NullPointerException if <code>reader</code> is <code>null</code>.
   */
  public LanguageTypeScanner newTypeScanner(Reader reader);
}
