package de.unisiegen.tpml.graphics.subtyping ;


import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.KeyboardFocusManager ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.ComponentAdapter ;
import java.awt.event.ComponentEvent ;
import java.awt.event.FocusEvent ;
import java.awt.event.FocusListener ;
import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.io.StringReader ;
import java.text.MessageFormat ;
import java.util.prefs.Preferences ;
import javax.swing.JButton ;
import javax.swing.JCheckBox ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JSplitPane ;
import javax.swing.border.LineBorder ;
import javax.swing.event.DocumentEvent ;
import javax.swing.event.DocumentListener ;
import javax.swing.text.BadLocationException ;
import de.unisiegen.tpml.core.ProofGuessException ;
import de.unisiegen.tpml.core.ProofModel ;
import de.unisiegen.tpml.core.languages.Language ;
import de.unisiegen.tpml.core.languages.LanguageTypeParser ;
import de.unisiegen.tpml.core.subtyping.AbstractSubTyping ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofModel ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.graphics.AbstractProofView ;
import de.unisiegen.tpml.graphics.Messages ;
import de.unisiegen.tpml.graphics.StyledLanguageEditor ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.outline.DefaultOutline ;
import de.unisiegen.tpml.graphics.outline.Outline ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.ui.SideBar ;
import de.unisiegen.tpml.ui.SideBarListener ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;
import de.unisiegen.tpml.ui.proofview.ProofViewComponent ;


/**
 * This is the Subtyping view. This view is very different to the other views.
 * It is not connected to the source-view. So the user must select the language
 * and inter the two types he wants to check. The user can change the language
 * everytime. the scond part of the view is alike the BigStepView. ({@link BigStepView})
 * where you have the view and two outlines, one for each type.
 * 
 * @author Benjamin Mies
 * @author Feivel
 */
public class SubTypingEnterTypes extends AbstractProofView
{
  /**
   * The unique serialization identifier of this class.
   */
  private static final long serialVersionUID = 5068227950528407089L ;


  /**
   * The {@link SubTypingProofModel}.
   */
  private SubTypingProofModel model ;


  /**
   * The entered and parsed type1 ({@link MonoType})
   */
  private MonoType type1 ;


  /**
   * The entered and parsed type1 ({@link MonoType})
   */
  private MonoType type2 ;


  /**
   * The last entered types
   */
  private MonoType oldType1 ;


  /***/
  private MonoType oldType2 ;


  /**
   * The {@link SubTypingComponent} component
   */
  private SubTypingComponent component ;


  /**
   * The actual choosen language
   */
  private Language language ;


  // GUI-components
  /**
   * The panel for input
   */
  private JPanel inputFields ;


  /**
   * The panel for output
   */
  private JPanel outputField ; // replaced by the Scrollpane... we hope that


  // this funktions
  /**
   * The Scrollpane for the outputfield
   */
  private JScrollPane scrollOutput ;


  /**
   * The panel for menu
   */
  private JPanel menu ;


  /**
   * The panel for the two outlines
   */
  private JPanel outline ;


  /**
   * The Textfields where the user is able to enter the types
   */
  private SideBar sideBar ;


  /***/
  private SideBar sideBar2 ;


  /***/
  private JScrollPane scrollpane ;


  /***/
  private JScrollPane scrollpane2 ;


  /***/
  private StyledLanguageEditor editor1 ;


  /***/
  private StyledLanguageEditor editor2 ;


  /***/
  private StyledTypeEnterField document1 ;


  /***/
  private StyledTypeEnterField document2 ;


  /**
   * Labels that informs the user what to do
   */
  private JLabel label ;


  /***/
  private JLabel label2 ;


  /***/
  private JLabel lOutput ;


  /***/
  private JLabel labelLanguage ;


  /**
   * The Buttons of the menu
   */
  private JButton changeLanguage ;


  /**
   * The JCheckBox to enable or disable the Outlines
   */
  private JCheckBox setOutline ;


  /**
   * The <code>JSplitPane</code> to devide the outline and the result
   */
  private JSplitPane splitPane ;


  /**
   * Black border
   */
  private LineBorder border ;


  /**
   * Outlines for better understanding the entered type 1
   */
  private DefaultOutline outline1 ;


  /**
   * Outlines for better understanding the entered type 2
   */
  private DefaultOutline outline2 ;


  /**
   * Dialog to change language
   */
  private ChangeLanguage clLanguage ;


  /**
   * flag which shows if elements already initialized
   */
  private boolean initialized = false ;


  /**
   * hte division of the JSplitPane
   */
  private double dividerLocation = 0.6 ;


  private Preferences preferences ;


  //
  // Constructor
  //
  /**
   * Allocates a new <code>SubTypingEnterTypes</code>
   * 
   * @param modelP The model
   */
  public SubTypingEnterTypes ( SubTypingProofModel modelP )
  {
    super ( ) ;
    model = modelP ;
    selectNewLanguage ( ) ;
  }


  private void selectNewLanguage ( )
  {
    // open a dialog to choose language
    clLanguage = new ChangeLanguage ( null , this ) ;
    // remove the languages without Typs
    clLanguage.removeLanguage ( 0 ) ;
    clLanguage.removeLanguage ( 0 ) ;
    clLanguage.setLocationRelativeTo ( this ) ;
    clLanguage.setVisible ( true ) ;
  }


  /**
   * inizialisize the gui
   */
  private void initComponents ( )
  {
    type1 = null ;
    type2 = null ;
    // this.setSize ( 800, 600 );
    splitPane = new JSplitPane ( JSplitPane.VERTICAL_SPLIT ) ;
    splitPane.setOneTouchExpandable ( true ) ;
    splitPane.setDividerLocation ( dividerLocation ) ;
    setLayout ( new GridBagLayout ( ) ) ;
    GridBagConstraints constraints = new GridBagConstraints ( ) ;
    constraints.fill = GridBagConstraints.BOTH ;
    border = new LineBorder ( Color.GRAY , 1 ) ;
    menu = new JPanel ( ) ;
    menu.setLayout ( new GridBagLayout ( ) ) ;
    menu.setBorder ( border ) ;
    changeLanguage = new JButton ( Messages.getString ( "changeLanguage" ) ) ; //$NON-NLS-1$
    changeLanguage.addActionListener ( new ActionListener ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void actionPerformed ( ActionEvent event )
      {
        SubTypingEnterTypes.this.setEnabled ( false ) ;
        selectNewLanguage ( ) ;
        //				
        // clLanguage = new ChangeLanguage ( null, SubTypingEnterTypes.this );
        // clLanguage.setLocationRelativeTo ( SubTypingEnterTypes.this );
        // clLanguage.setVisible ( true );
      }
    } ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 0 ;
    constraints.weightx = 1 ;
    constraints.weighty = 0 ;
    constraints.insets = new Insets ( 5 , 10 , 5 , 10 ) ;
    this.menu.add ( changeLanguage , constraints ) ;
    labelLanguage = new JLabel ( MessageFormat.format ( Messages
        .getString ( "actualLanguage" ) , //$NON-NLS-1$
        language.getName ( ) ) ) ;
    constraints.gridx = 1 ;
    constraints.gridy = 0 ;
    constraints.weightx = 3 ;
    constraints.weighty = 0 ;
    this.menu.add ( labelLanguage , constraints ) ;
    setOutline = new JCheckBox ( Messages.getString ( "showOutline" ) ) ; //$NON-NLS-1$
    preferences = Preferences.userNodeForPackage ( Outline.class ) ;
    boolean select = preferences.getBoolean ( "setOutline" , true ) ;
    setOutline.setSelected ( select ) ;
    setOutline.setToolTipText ( Messages.getString ( "tooltipOutline" ) ) ; //$NON-NLS-1$
    setOutline.addItemListener ( new ItemListener ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void itemStateChanged ( ItemEvent e )
      {
        if ( e.getStateChange ( ) == ItemEvent.SELECTED )
        {
          SubTypingEnterTypes.this.preferences
              .putBoolean ( "setOutline" , true ) ;
          SubTypingEnterTypes.this.outline.setVisible ( true ) ;
          splitPane.setOneTouchExpandable ( true ) ;
          splitPane
              .setDividerLocation ( SubTypingEnterTypes.this.dividerLocation ) ;
        }
        if ( e.getStateChange ( ) == ItemEvent.DESELECTED )
        {
          SubTypingEnterTypes.this.preferences.putBoolean ( "setOutline" ,
              false ) ;
          SubTypingEnterTypes.this.outline.setVisible ( false ) ;
          splitPane.setOneTouchExpandable ( false ) ;
        }
      }
    } ) ;
    constraints.gridx = 2 ;
    constraints.gridy = 0 ;
    constraints.weightx = 2 ;
    constraints.weighty = 0 ;
    this.menu.add ( setOutline , constraints ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 0 ;
    constraints.weighty = 0 ;
    constraints.insets = new Insets ( 15 , 15 , 15 , 15 ) ;
    this.add ( menu , constraints ) ;
    inputFields = new JPanel ( ) ;
    inputFields.setLayout ( new GridBagLayout ( ) ) ;
    inputFields.setBorder ( border ) ;
    this.label = new JLabel ( Messages.getString ( "firstType" ) ) ; //$NON-NLS-1$
    constraints.gridx = 0 ;
    constraints.gridy = 0 ;
    constraints.weightx = 1 ;
    constraints.weighty = 0 ;
    constraints.gridwidth = 2 ;
    constraints.insets = new Insets ( 5 , 10 , 5 , 10 ) ;
    this.inputFields.add ( this.label , constraints ) ;
    this.label2 = new JLabel ( Messages.getString ( "secondType" ) ) ; //$NON-NLS-1$
    constraints.gridx = 0 ;
    constraints.gridy = 2 ;
    constraints.weightx = 1 ;
    constraints.weighty = 0 ;
    constraints.gridwidth = 2 ;
    this.inputFields.add ( label2 , constraints ) ;
    this.editor1 = new StyledLanguageEditor ( ) ;
    this.document1 = new StyledTypeEnterField ( language ) ;
    // this.editor1.getInputMap ( ).put(KeyStroke.getKeyStroke("F2"),
    // "nextComponent");
    // this.editor1.getActionMap().put("nextComponent",nextComponent());
    // this.editor1.addKeyListener(new KeyAdapter(){
    // public void keyPressed(KeyEvent arg0) {
    // if (arg0.getKeyCode() == KeyEvent.VK_TAB){
    // //remove tabs and so on
    // //this is now done by the Fucuslistener
    // if (arg0.getModifiers ( ) == KeyEvent.SHIFT_MASK)
    // {
    // lastEditor ( );
    // }
    // else {
    // nextEditor ( );
    // }
    //           
    // }
    // }
    // });
    this.editor1.setFocusTraversalKeys (
        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS , null ) ;
    this.editor1.setFocusTraversalKeys (
        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS , null ) ;
    this.editor1.addFocusListener ( new FocusListener ( )
    {
      public void focusGained ( FocusEvent e )
      {
        // editor1.setText ( editor1.getText ( ).trim ( ));
        // editor2.setText ( editor2.getText ( ).trim ( ));
        editor1.setSelectionStart ( 0 ) ;
        editor1.setSelectionEnd ( editor1.getText ( ).length ( ) ) ;
      }


      public void focusLost ( FocusEvent e )
      {
        // editor1.setText ( editor1.getText ( ).trim ( ));
        // editor2.setText ( editor2.getText ( ).trim ( ));
        // tell the ProofviewComponent the new model to provide the
        // redo/undo/pong
        ( ( ProofViewComponent ) getParent ( ) )
            .setModel ( ( ProofModel ) model ) ;
        ( ( ProofViewComponent ) getParent ( ) )
            .setPongStatus ( ! SubTypingEnterTypes.this.model.isCheating ( )
                && SubTypingEnterTypes.this.model.isFinished ( ) ) ;
      }
    } ) ;
    // inputFields.addKeyListener ( new KeyListener() )
    this.scrollpane = new JScrollPane ( ) ;
    this.sideBar = new SideBar ( this.scrollpane , this.document1 ,
        this.editor1 ) ;
    this.sideBar.addSideBarListener ( new SideBarListener ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void markText ( int left , int right )
      {
        SubTypingEnterTypes.this.selectErrorText ( left , right ) ;
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void insertText ( int pIndex , String pText )
      {
        int countSpaces = 0 ;
        try
        {
          while ( SubTypingEnterTypes.this.document1.getText ( pIndex + countSpaces ,
              1 ).equals ( " " ) ) //$NON-NLS-1$
          {
            countSpaces ++ ;
          }
        }
        catch ( BadLocationException e )
        {
          // Do nothing
        }
        try
        {
          int offset = 0 ;
          String text = pText ;
          if ( ( countSpaces >= 1 )
              && ( text.substring ( 0 , 1 ).equals ( " " ) ) ) //$NON-NLS-1$
          {
            text = text.substring ( 1 ) ;
            offset ++ ;
            countSpaces -- ;
          }
          if ( ( countSpaces >= 1 )
              && ( text.substring ( text.length ( ) - 1 ).equals ( " " ) ) ) //$NON-NLS-1$
          {
            text = text.substring ( 0 , text.length ( ) - 1 ) ;
          }
          SubTypingEnterTypes.this.document1.insertString ( pIndex + offset , text ,
              null ) ;
        }
        catch ( BadLocationException e )
        {
          // Do nothing
        }
      }
    } ) ;
    this.editor1.setDocument ( this.document1 ) ;
    this.outline1 = new DefaultOutline ( this , editor1 ) ;
    this.document1.addDocumentListener ( new DocumentListener ( )
    {
      public void changedUpdate ( DocumentEvent e )
      {
        // Nothing to do so far
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void insertUpdate ( DocumentEvent e )
      {
        type1 = eventHandling ( editor1 , type1 , oldType1 , outline1 ) ;
        if ( type1 != oldType1 )
        {
          if ( type2 != null )
          {
            // TODO mal probieren ob ein neues Model nicht geiler ist...
            // model.setRoot(type1, type2);
            model = language.newSubTypingProofModel ( type1 , type2 ,
                isAdvanced ( ) ) ;
            // tell the ProofviewComponent the new model to provide the
            // redo/undo/pong
            // this ist now done bye the Docuslisteners
            // ((ProofViewComponent)getParent()).setModel((ProofModel)model);
            component = new SubTypingComponent ( model , isAdvanced ( ) ) ;
            scrollOutput.setViewportView ( component ) ;
            validate ( ) ;
            // model.is
            // check ( );
          }
        }
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void removeUpdate ( DocumentEvent e )
      {
        type1 = eventHandling ( editor1 , type1 , oldType1 , outline1 ) ;
        // if (type1 != oldType1)
        // check ( );
      }
    } ) ;
    this.scrollpane.setViewportView ( this.editor1 ) ;
    this.scrollpane
        .setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_NEVER ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 1 ;
    constraints.weightx = 0 ;
    constraints.gridwidth = 1 ;
    constraints.insets = new Insets ( 5 , 0 , 5 , 0 ) ;
    this.inputFields.add ( this.sideBar , constraints ) ;
    constraints.gridx = 1 ;
    constraints.gridy = 1 ;
    constraints.weightx = 10 ;
    this.inputFields.add ( this.scrollpane , constraints ) ;
    this.editor2 = new StyledLanguageEditor ( ) ;
    // the editors need an KeyListener to provide the next and last component
    // change
    // this.editor2.addKeyListener(new KeyAdapter(){
    // public void keyPressed(KeyEvent arg0) {
    // if (arg0.getKeyCode() == KeyEvent.VK_TAB)
    // {
    // //remove tabs and so on
    // //this is now done by the FocusListenrs
    // if (arg0.getModifiers ( ) == KeyEvent.SHIFT_MASK)
    // {
    // lastEditor ( );
    // }
    // else
    // {
    // nextEditor ( );
    // }
    // }
    // }
    // });
    this.editor2.setFocusTraversalKeys (
        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS , null ) ;
    this.editor2.setFocusTraversalKeys (
        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS , null ) ;
    this.editor2.addFocusListener ( new FocusListener ( )
    {
      public void focusGained ( FocusEvent e )
      {
        // editor1.setText ( editor1.getText ( ).trim ( ));
        // editor2.setText ( editor2.getText ( ).trim ( ));
        editor2.setSelectionStart ( 0 ) ;
        editor2.setSelectionEnd ( editor1.getText ( ).length ( ) ) ;
      }


      public void focusLost ( FocusEvent e )
      {
        // editor1.setText ( editor1.getText ( ).trim ( ));
        // editor2.setText ( editor2.getText ( ).trim ( ));
        // tell the ProofviewComponent the new model to provide the
        // redo/undo/pong
        ( ( ProofViewComponent ) getParent ( ) )
            .setModel ( ( ProofModel ) model ) ;
        ( ( ProofViewComponent ) getParent ( ) )
            .setPongStatus ( ! SubTypingEnterTypes.this.model.isCheating ( )
                && SubTypingEnterTypes.this.model.isFinished ( ) ) ;
      }
    } ) ;
    this.document2 = new StyledTypeEnterField ( language ) ;
    this.scrollpane2 = new JScrollPane ( ) ;
    this.sideBar2 = new SideBar ( this.scrollpane2 , this.document2 ,
        this.editor2 ) ;
    this.sideBar2.addSideBarListener ( new SideBarListener ( )
    {
      @ SuppressWarnings ( "synthetic-access" )
      public void markText ( int left , int right )
      {
        SubTypingEnterTypes.this.selectErrorText ( left , right ) ;
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void insertText ( int pIndex , String pText )
      {
        int countSpaces = 0 ;
        try
        {
          while ( SubTypingEnterTypes.this.document2.getText ( pIndex + countSpaces ,
              1 ).equals ( " " ) ) //$NON-NLS-1$
          {
            countSpaces ++ ;
          }
        }
        catch ( BadLocationException e )
        {
          // Do nothing
        }
        try
        {
          int offset = 0 ;
          String text = pText ;
          if ( ( countSpaces >= 1 )
              && ( text.substring ( 0 , 1 ).equals ( " " ) ) ) //$NON-NLS-1$
          {
            text = text.substring ( 1 ) ;
            offset ++ ;
            countSpaces -- ;
          }
          if ( ( countSpaces >= 1 )
              && ( text.substring ( text.length ( ) - 1 ).equals ( " " ) ) ) //$NON-NLS-1$
          {
            text = text.substring ( 0 , text.length ( ) - 1 ) ;
          }
          SubTypingEnterTypes.this.document2.insertString ( pIndex + offset , text ,
              null ) ;
        }
        catch ( BadLocationException e )
        {
          // Do nothing
        }
      }
    } ) ;
    this.outline2 = new DefaultOutline ( this , editor2 ) ;
    this.document2.addDocumentListener ( new DocumentListener ( )
    {
      public void changedUpdate ( DocumentEvent e )
      {
        // Nothing to do so far
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void insertUpdate ( DocumentEvent e )
      {
        type2 = eventHandling ( editor2 , type2 , oldType2 , outline2 ) ;
        if ( type2 != oldType2 )
        {
          // TODO mal probieren ob ein neues Model nicht geiler ist...
          // model.setRoot(type1, type2);
          model = language.newSubTypingProofModel ( type1 , type2 ,
              isAdvanced ( ) ) ;
          // tell the ProofviewComponent the new model to provide the
          // redo/undo/pong
          // ((ProofViewComponent)getParent()).setModel((ProofModel)model);
          component = new SubTypingComponent ( model , isAdvanced ( ) ) ;
          scrollOutput.setViewportView ( component ) ;
          validate ( ) ;
          // model.is
          // check ( );
        }
      }


      @ SuppressWarnings ( "synthetic-access" )
      public void removeUpdate ( DocumentEvent e )
      {
        type2 = eventHandling ( editor2 , type2 , oldType2 , outline2 ) ;
        if ( type2 != oldType2 )
        {
          // model.setRoot(type1, type2);
          model = language.newSubTypingProofModel ( type1 , type2 ,
              isAdvanced ( ) ) ;
          // tell the ProofviewComponent the new model to provide the redo
          component = new SubTypingComponent ( model , isAdvanced ( ) ) ;
          scrollOutput.setViewportView ( component ) ;
          validate ( ) ;
          // check ( );
        }
      }
    } ) ;
    this.editor2.setDocument ( this.document2 ) ;
    this.scrollpane2.setViewportView ( this.editor2 ) ;
    this.scrollpane2
        .setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_NEVER ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 3 ;
    constraints.weightx = 0 ;
    this.inputFields.add ( this.sideBar2 , constraints ) ;
    constraints.gridx = 1 ;
    constraints.gridy = 3 ;
    constraints.weightx = 10 ;
    this.inputFields.add ( this.scrollpane2 , constraints ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 1 ;
    constraints.weighty = 0 ;
    constraints.insets = new Insets ( 15 , 15 , 15 , 15 ) ;
    this.add ( inputFields , constraints ) ;
    scrollOutput = new JScrollPane ( ) ;
    scrollOutput.setBackground ( Color.WHITE ) ;
    outputField = new JPanel ( ) ;
    outputField.setBackground ( Color.WHITE ) ;
    outputField.setLayout ( new GridBagLayout ( ) ) ;
    // outputField.setLayout( null);
    outputField.setBorder ( border ) ;
    // labelOutput = new JLabel ( Messages.getString ( "result" ) );
    // //$NON-NLS-1$
    constraints.gridx = 0 ;
    constraints.gridy = 0 ;
    constraints.gridwidth = 1 ;
    constraints.weighty = 1 ;
    constraints.weightx = 1 ;
    constraints.insets = new Insets ( 15 , 15 , 15 , 15 ) ;
    component = new SubTypingComponent ( model , isAdvanced ( ) ) ;
    this.outputField.add ( this.scrollOutput , constraints ) ;
    this.scrollOutput.setViewportView ( this.component ) ;
    this.scrollOutput.getViewport ( ).setBackground ( Color.WHITE ) ;
    this.scrollOutput.addComponentListener ( new ComponentAdapter ( )
    {
      @ Override
      public void componentResized ( ComponentEvent event )
      {
        SubTypingEnterTypes.this.component
            .setAvailableWidth ( SubTypingEnterTypes.this.scrollOutput
                .getViewport ( ).getWidth ( ) ) ;
      }
    } ) ;
    // this.outputField.add(component, constraints);
    lOutput = new JLabel ( " " ) ; //$NON-NLS-1$
    constraints.gridx = 0 ;
    constraints.gridy = 1 ;
    constraints.gridwidth = 1 ;
    constraints.weighty = 0 ;
    constraints.weightx = 0 ;
    // this.outputField.add ( lOutput, constraints );
    constraints.gridx = 0 ;
    constraints.gridy = 2 ;
    constraints.weighty = 5 ;
    constraints.weightx = 0 ;
    constraints.insets = new Insets ( 15 , 15 , 15 , 15 ) ;
    // this.add ( outputField, constraints );
    splitPane.setLeftComponent ( scrollOutput ) ;
    splitPane.setDividerLocation ( dividerLocation ) ;
    outline = new JPanel ( ) ;
    outline.setLayout ( new GridBagLayout ( ) ) ;
    outline.setBorder ( border ) ;
    JPanel jPanelOutline1 = this.outline1.getPanel ( ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 0 ;
    constraints.weightx = 1 ;
    constraints.weighty = 5 ;
    constraints.gridwidth = 1 ;
    constraints.insets = new Insets ( 5 , 5 , 5 , 5 ) ;
    this.outline.add ( jPanelOutline1 , constraints ) ;
    JPanel jPanelOutline2 = this.outline2.getPanel ( ) ;
    constraints.gridx = 1 ;
    constraints.gridy = 0 ;
    constraints.weighty = 5 ;
    this.outline.add ( jPanelOutline2 , constraints ) ;
    constraints.gridx = 0 ;
    constraints.gridy = 3 ;
    constraints.weighty = 5 ;
    constraints.insets = new Insets ( 15 , 15 , 15 , 15 ) ;
    // this.add ( outline, constraints );
    splitPane.setRightComponent ( outline ) ;
    this.add ( splitPane , constraints ) ;
    this.setVisible ( true ) ;
    this.validate ( ) ;
    if ( ! select )
    {
      SubTypingEnterTypes.this.outline.setVisible ( false ) ;
      splitPane.setOneTouchExpandable ( false ) ;
    }
    splitPane.setDividerLocation ( dividerLocation ) ;
  }


  /**
   * privides the TAB-Key Funktion in the Textfields if the user is in editor1
   * he comes to editor2 and inversly
   */
  private void nextEditor ( )
  {
    // normal variant: The focus switches from editor1 to editor2 and fro,
    // editor2 to the next component that will be the buttonmenu at the top
    if ( editor1.hasFocus ( ) )
    {
      editor1.transferFocus ( ) ;
    }
    if ( editor2.hasFocus ( ) )
    {
      editor2.transferFocus ( ) ;
    }
  }


  /**
   * privides the TAB-Key Funktion in the Textfields if the user is in editor2
   * he comes to editor1 and inversly
   */
  private void lastEditor ( )
  {
    // normal variant: The focus switches from editor1 to editor2 and fro,
    // editor2 to the next component that will be the buttonmenu at the top
    if ( editor1.hasFocus ( ) )
    {
      editor1.transferFocusBackward ( ) ;
    }
    if ( editor2.hasFocus ( ) )
    {
      editor2.transferFocusBackward ( ) ;
    }
    // other variant: The focus only switchs between the 2 editors
    // if (editor1.hasFocus ( ))
    // {
    // editor2.requestFocus ( );
    // }
    // else
    // {
    // editor1.requestFocus ( );
    // }
  }


  /**
   * sets the errortext of the languagestylededitor
   * 
   * @param left
   * @param right
   */
  private void selectErrorText ( int left , int right )
  {
    this.editor1.select ( left , right ) ;
  }


  /**
   * mal gucken, ob wir die überhautp noch brauchen... TODO Dokumentation
   */
  // void check() {
  // if (type1 != null && type2 != null) {
  // if (AbstractSubTyping.check ( type1, type2 )) {
  // lOutput.setText ( Messages.getString ( "subtypeFound" ) ) ; //$NON-NLS-1$
  // lOutput.setForeground(Color.green);
  // }
  //
  // else {
  // lOutput.setText ( Messages.getString ( "noSubtype" ) ); //$NON-NLS-1$
  // lOutput.setForeground(Color.red);
  // }
  // }
  // }
  /**
   * @param editor The Editor to read the Type
   * @param pType The type
   * @param oldType The last type
   * @param outline The outline
   * @return The Monotype
   */
  MonoType eventHandling ( StyledLanguageEditor editor , MonoType pType ,
      MonoType oldType , DefaultOutline outline )
  {
    MonoType type ;
    lOutput.setText ( " " ) ; //$NON-NLS-1$
    try
    {
      LanguageTypeParser parser = this.language
          .newTypeParser ( new StringReader ( editor.getText ( ) ) ) ;
      type = parser.parse ( ) ;
      outline.load ( type , Outline.ExecuteAutoChange.SUBTYPING ) ;
      return type ;
    }
    catch ( Exception e )
    {
      outline.load ( null , Outline.ExecuteAutoChange.SUBTYPING ) ;
      if ( editor.getText ( ).length ( ) == 0 ) outline.setError ( false ) ;
      return null ;
    }
  }


  /**
   * set the actual language, and update the language where needed
   * 
   * @param pLanguage
   */
  public void setLanguage ( Language pLanguage )
  {
    if ( pLanguage != null )
    {
      this.language = pLanguage ;
      // ther ist no chance to actualisize the language of a model so wee need a
      // new one
      model = language.newSubTypingProofModel ( type1 , type2 , isAdvanced ( ) ) ;
      // tell the ProofviewComponent the new model to provide the redo/undo/pong
      ( ( ProofViewComponent ) getParent ( ) ).setModel ( ( ProofModel ) model ) ;
      // model.setRoot(type1, type2);
      // model.setMode(isAdvanced());
      if ( ! initialized )
      {
        initComponents ( ) ;
        initialized = true ;
      }
      else
      {
        this.document1.setLanguage ( language ) ;
        type1 = eventHandling ( editor1 , type1 , oldType1 , outline1 ) ;
        this.document2.setLanguage ( language ) ;
        type2 = eventHandling ( editor2 , type2 , oldType2 , outline2 ) ;
        // check ( );
        try
        {
          this.document1.processChanged ( ) ;
        }
        catch ( BadLocationException e )
        {
          // Nothing to do
        }
        this.labelLanguage.setText ( MessageFormat.format ( Messages
            .getString ( "actualLanguage" ) , language.getName ( ) ) ) ; //$NON-NLS-1$
      }
      this.setEnabled ( true ) ;
    }
    else if ( this.language == null )
    {
      this.setEnabled ( false ) ;
    }
  }


  /**
   * provides a Guess
   * 
   * @throws IllegalStateException
   * @throws ProofGuessException
   */
  public void guess ( ) throws IllegalStateException , ProofGuessException
  {
    this.component.guess ( ) ;
  }


  /**
   * {@inheritDoc}
   * 
   * @see de.unisiegen.tpml.graphics.AbstractProofView#setAdvanced(boolean)
   */
  @ Override
  public void setAdvanced ( boolean advanced )
  {
    super.setAdvanced ( advanced ) ;
    model.setMode ( advanced ) ;
    if ( this.component != null )
    {
      this.component.setAdvanced ( isAdvanced ( ) ) ;
    }
  }


  /**
   * Returns the outline1.
   * 
   * @return The outline1.
   * @see #outline1
   */
  public DefaultOutline getOutline1 ( )
  {
    return this.outline1 ;
  }


  /**
   * Returns the outline2.
   * 
   * @return The outline2.
   * @see #outline2
   */
  public DefaultOutline getOutline2 ( )
  {
    return this.outline2 ;
  }
}
