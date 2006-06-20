package languages;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.StringReader;

import smallstep.SmallStepProofModel;
import smallstep.SmallStepProofModelFactory;
import smallstep.test.SmallStepTreeView;
import expressions.Expression;

/**
 * TODO Add documentation here.
 *
 * @author Benedikt Meurer
 * @version $Id$
 */
public final class Test1 {
  //private static final String SIMPLE = "let x = 1 in rec y.y,2,x+1";
  //private static final String SIMPLE = "let rec f = lambda op.lambda x.lambda y.op x y + 1 in f (+) 6 7";
  //private static final String SIMPLE = "let x = 1;let y = 1 in y in x;x + 1; x x";
  //private static final String SIMPLE = "hd (cons (2, (1 :: 1 :: [8])))";
  //private static final String SIMPLE = "let x = ref 1 in (x := !x + 1; x := false; !x)";
  //private static final String SIMPLE = "let n = ref 2 in let x = ref 1 in (while !n > 0 do (x := !n * !x; n := !n - 1)); !x";
  private static final String SIMPLE = "fst (snd (1, 2), 4)";
    
  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      // parse the simple expression
      LanguageFactory languageFactory = LanguageFactory.newInstance();
      Language language = languageFactory.getLanguageById("l1");
      LanguageParser parser = language.newParser(new StringReader(SIMPLE));
      Expression e = parser.parse();
      
      // parse the program
      SmallStepProofModelFactory factory = SmallStepProofModelFactory.newInstance();
      SmallStepProofModel model = factory.newProofModel(e);
      
      // evaluate the resulting small step expression
      SmallStepTreeView tv = new SmallStepTreeView(model);
      tv.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      tv.setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
