import java.awt.Dimension ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.WindowAdapter ;
import java.awt.event.WindowEvent ;
import java.io.StringReader ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageFactory ;
import de.unisiegen.tpml.core.languages.LanguageParser ;
import de.unisiegen.tpml.core.smallstep.SmallStepProofModel ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;


@ SuppressWarnings ( "all" )
public class SmallStepTest
{
  private static String expression = "let rec f = lambda x. if x = 0 then 1 else x * f (x - 1) in f 3" ;


  // private static String expression = "let x = 1 in x";
  private static SmallStepProofModel model ;


  private static SmallStepView gui ;


  public static void main ( String [ ] args )
  {
    try
    {
      LanguageFactory lf = LanguageFactory.newInstance ( ) ;
      Language language = lf.getLanguageById ( "L2" ) ;
      LanguageParser parser = language.newParser ( new StringReader (
          SmallStepTest.expression ) ) ;
      model = language.newSmallStepProofModel ( parser.parse ( ) ) ;
      // SmallStepTest.evaluateModel(model);
      TestDialog dialog = new TestDialog ( ) ;
      // TypeCheckerComponent component = new TypeCheckerComponent (model);
      gui = new SmallStepView ( model ) ;
      dialog.setContent ( gui ) ;
      dialog.setSize ( new Dimension ( 640 , 480 ) ) ;
      dialog.setVisible ( true ) ;
      dialog.addWindowListener ( new WindowAdapter ( )
      {
        @ Override
        public void windowClosing ( WindowEvent e )
        {
          System.exit ( 0 ) ;
        }
      } ) ;
      // component.setAvailableWidth(dialog.getWidth());
      dialog.guess.addActionListener ( new ActionListener ( )
      {
        public void actionPerformed ( ActionEvent event )
        {
          try
          {
            gui.guess ( ) ;
          }
          catch ( Exception e )
          {
            e.printStackTrace ( ) ;
          }
        }
      } ) ;
      dialog.redoButton.addActionListener ( new ActionListener ( )
      {
        public void actionPerformed ( ActionEvent event )
        {
          try
          {
            model.redo ( ) ;
          }
          catch ( Exception e )
          {
            e.printStackTrace ( ) ;
          }
        }
      } ) ;
      dialog.undoButton.addActionListener ( new ActionListener ( )
      {
        public void actionPerformed ( ActionEvent event )
        {
          try
          {
            model.undo ( ) ;
          }
          catch ( Exception e )
          {
            e.printStackTrace ( ) ;
          }
        }
      } ) ;
    }
    catch ( Exception e )
    {
      e.printStackTrace ( ) ;
    }
  }
}
