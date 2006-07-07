package bigstep;

import java.io.StringReader;

import languages.Language;
import languages.LanguageFactory;
import languages.LanguageParser;
import expressions.Expression;

/**
 * Factory class for {@link BigStepProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class BigStepProofModelFactory {
  //
  // Constructor (private)
  //
  
  /**
   * Allocates a new <code>BigStepProofModelFactory</code>.
   */
  private BigStepProofModelFactory() {
    // nothing to do here...
  }
  
  
  
  //
  // Factory methods
  //
  
  /**
   * Obtains a new instance of a <code>BigStepProofModelFactory</code>.
   * 
   * @return a new instance of a <code>BigStepProofModelFactory</code>.
   */
  public BigStepProofModelFactory newInstance() {
    return new BigStepProofModelFactory();
  }
  
  /**
   * Allocates a new {@link BigStepProofModel} to prove the
   * given <code>expression</code>.
   *  
   * @param expression the {@link Expression} to prove.
   * 
   * @return New instance of a <code>BigStepProofModel</code>.
   */
  public BigStepProofModel newProofModel(Expression expression) {
    return new BigStepProofModel(expression);
  }
  
  /**
   * Allocates a new {@link BigStepProofModel} to prove the
   * {@link Expression} that corresponds to the specified
   * <code>program</code>.
   *  
   * @param program parsable program text (currently hardcoded to
   *                the language L1).
   *  
   * @return New instance of a <code>BigStepProofModel</code>.
   * 
   * @throws Exception if an error occurred while parsing the <code>program</code>.
   * 
   */
  public BigStepProofModel newProofModel(String program) throws Exception {
    // allocate the language for "l1"
    LanguageFactory languageFactory = LanguageFactory.newInstance();
    Language language = languageFactory.getLanguageById("l1");
    
    // allocate the parser for the language
    LanguageParser parser = language.newParser(new StringReader(program));
    
    // parse the program and return a model for the expression
    return newProofModel(parser.parse());
  }
}
