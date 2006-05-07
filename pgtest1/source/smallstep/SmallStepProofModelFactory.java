package smallstep;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import l1.Translator;
import l1.lexer.Lexer;
import l1.lexer.LexerException;
import l1.node.Start;
import l1.parser.Parser;
import l1.parser.ParserException;
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
   * @throws IOException if the <code>program</code> cannot be read. 
   * @throws LexerException if the <code>program</code> cannot be tokenized.
   * @throws ParserException if the <code>program</code> cannot be parsed.
   * 
   */
  public SmallStepProofModel newProofModel(String program) throws ParserException, LexerException, IOException {
    // Allocate the parser
    Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(program), 1024)));

    // Parse the input
    Start tree = parser.parse();

    // translate the AST to a small step expression
    Translator translator = new Translator();
    tree.apply(translator);

    // allocate new proof model for the parsed expression
    return newProofModel(translator.getExpression());
  }
}
