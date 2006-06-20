package smallstep;

import java.io.StringReader;

import languages.Language;
import languages.LanguageFactory;
import languages.LanguageParser;
import expressions.Expression;

/**
 * Factory class for {@link SmallStepProofModel}s.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class SmallStepProofModelFactory {
  //
  // Constructor
  //
  
  /**
   * Allocates a new <code>SmallStepProofModelFactory</code>.
   */
  private SmallStepProofModelFactory() {
    // nothing to do here...
  }
  
  
  
  //
  // Factory methods
  //
  
  /**
   * Obtain a new instance of a <code>SmallStepProofModelFactory</code>.
   * 
   * @return New instance of a <code>SmallStepProofModelFactory</code>.
   */
  public static SmallStepProofModelFactory newInstance() {
    return new SmallStepProofModelFactory();
  }
  
  /**
   * Allocates a new {@link SmallStepProofModel} to prove the
   * given <code>expression</code>.
   *  
   * @param expression the {@link Expression} to prove.
   * 
   * @return New instance of a <code>SmallStepProofModel</code>.
   */
  public SmallStepProofModel newProofModel(Expression expression) {
    return new SmallStepProofModel(expression);
  }
  
  /**
   * Allocates a new {@link SmallStepProofModel} to prove the
   * {@link Expression} that corresponds to the specified
   * <code>program</code>.
   *  
   * @param program parsable program text (currently hardcoded to
   *                the language L1).
   *  
   * @return New instance of a <code>SmallStepProofModel</code>.
   * 
   * @throws Exception if an error occurred while parsing the <code>program</code>.
   * 
   */
  public SmallStepProofModel newProofModel(String program) throws Exception {
    // allocate the language for "l1"
    LanguageFactory languageFactory = LanguageFactory.newInstance();
    Language language = languageFactory.getLanguageById("l1");
    
    // allocate the parser for the language
    LanguageParser parser = language.newParser(new StringReader(program));
    
    // parse the program and return a model for the expression
    return newProofModel(parser.parse());
  }
}
