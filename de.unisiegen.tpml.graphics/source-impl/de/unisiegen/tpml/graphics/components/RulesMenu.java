package de.unisiegen.tpml.graphics.components;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.core.smallstep.SmallStepProofRule;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeComponent;
import de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent;


/**
 * this Class manages the Rules-Popup-Menu of the different ProofModels The user
 * gets the rules he can commit to an expression in different ways. Ihe behavior
 * is set by the int tomany and MAX. If there are more than tomany rules, the
 * rules will be grouped by the language they are coming from. To let the user
 * acces directly to the rules he used last there will be MAX rules he can click
 * directly. They will appear at the top of the menu. Till now the method is
 * only called by the Bigstepper and the Smallstepper because of the count of
 * rules.
 * 
 * @author Michael
 */
public class RulesMenu
{

  /**
   * the menu wich will be returned by the getMEnu-methode
   */
  private JPopupMenu menu = new JPopupMenu ();


  /**
   * saves the last used elements MAX elements will be saved
   */
  private ArrayList < MenuRuleItem > lastUsedElements = new ArrayList < MenuRuleItem > ();


  /**
   * saves the state of the menu in the preferences to restor at the next start
   */
  private Preferences preferences;


  /**
   * saves the state of the menu to be able to revert it. It will be used if the
   * selected rule throws an exception. By this rules will not be set at the top
   * of the menu if they are wrong
   */
  private ArrayList < MenuRuleItem > revertMenu = new ArrayList < MenuRuleItem > ();


  /**
   * look at revertMenu
   */
  private ArrayList < MenuRuleItem > revertLastUsedElements = new ArrayList < MenuRuleItem > ();


  /**
   * show the submenus
   */
  boolean submenus;


  /**
   * the constructor inizailisizes the main elements
   */
  public RulesMenu ()
  {
    this.revertLastUsedElements = new ArrayList < MenuRuleItem > ();
    this.revertMenu = new ArrayList < MenuRuleItem > ();
    this.lastUsedElements = new ArrayList < MenuRuleItem > ();
    this.menu = new JPopupMenu ();
  }


  /**
   * the default tomany value
   */
  private final static int TOMANY = 15;


  /**
   * tomany organisizes the number of elements wicht must be in teh rulesmenu to
   * let the getMenu methode divide the rules into submenus by setting the value
   * to Integer.MAXVALUE until tomany rules are in the menu, no submenus will be
   * created
   */
  private int tomany = TOMANY;


  /**
   * MAX is the count of elemnts the menu will show as the last used
   */
  private static final int MAX = 10;


  /**
   * this methode crates the popupmenus containing the rules. If there are more
   * than tomany elemnts, submenus for every language will be crated.
   * 
   * @param rules an array containing the rules
   * @param allRules an array containing all rules
   * @param lang the language: with this information, the menu crator will find
   *          out wich submenus are needed
   * @param tnc the jcomponent from wich the method is called
   * @param callBy a string "samllstep" or "bigstep"
   * @param advanced needed to decide wich rules must be included in the menu
   * @return the menu
   */
  public JPopupMenu getMenu ( ProofRule [] rules, ProofRule [] allRules,
      Language lang, final JComponent tnc, final String callBy, boolean advanced )
  {
    // first of all load the correct preferences
    if ( callBy.equalsIgnoreCase ( "bigstep" ) )
    {
      this.preferences = Preferences
          .userNodeForPackage ( BigStepNodeComponent.class );
    }
    else if ( callBy.equalsIgnoreCase ( "smallstep" ) )
    {
      this.preferences = Preferences
          .userNodeForPackage ( SmallStepNodeComponent.class );
    }

    // clear everything...
    this.revertLastUsedElements = new ArrayList < MenuRuleItem > ();
    this.revertMenu = new ArrayList < MenuRuleItem > ();
    this.lastUsedElements = new ArrayList < MenuRuleItem > ();
    this.menu = new JPopupMenu ();

    // now we want to have the ability to enable or dissable the subgrouping
    // till now it is not implemented by prefferenc
    final JRadioButtonMenuItem test = new JRadioButtonMenuItem ( "Submenus" );
    this.submenus = this.preferences.getBoolean ( "submenu", false );
    // TODO till now the submenus are always enabled
    if ( false ) // if (!submenus)
    {
      test.setSelected ( false );
      // disable the submenus by setting the tomany to Integer.MAx_VALUE
      this.tomany = Integer.MAX_VALUE;
    }
    else
    {
      test.setSelected ( true );
      this.tomany = TOMANY;
    }

    // TODO the actionlistener ist only for testing enable and disable the
    // subgrouping..
    ActionListener al1 = new ActionListener ()
    {

      public void actionPerformed ( ActionEvent e )
      {
        // if the Radiobutton is pressed, we will find out, if the Option was
        // set or not
        if ( test.isSelected () )
        {
          RulesMenu.this.submenus = true;
          setTomany ( Integer.MAX_VALUE );
          // RulesMenu.this.getPreferences().put("submenu", "true");
          RulesMenu.this.getPreferences ().putBoolean ( "submenu",
              RulesMenu.this.submenus );
        }
        else
        {
          RulesMenu.this.submenus = false;
          setTomany ( 15 );
          RulesMenu.this.getPreferences ().putBoolean ( "submenu",
              RulesMenu.this.submenus );
          // RulesMenu.this.getPreferences().put("submenu", "false");
        }
      }
    };
    test.addActionListener ( al1 );

    // TODO test enabling and disabling the submenus...
    // this.menu.add(test);

    // if to many rules we will devide in menu and submenus, otherwise there
    // will be only seperators
    // between the rules coming from the different languages
    if ( rules.length > this.tomany )
    {
      if ( rules.length > 0 )
      {

        // first get the lastUsedRules of the preferences (last state of the
        // programm)
        // get the names from the preferences, compare each with the list of all
        // usable rules, add them

        // backwards to save the ordering
        for ( int i = MAX - 1 ; i >= 0 ; i-- )
        {
          String name = this.preferences.get ( "rule" + i, "" );

          if ( name.equalsIgnoreCase ( "" ) )
          {
            // do nothing if the rule has no name, the rule dose not exist
          }
          else
          {
            for ( ProofRule a : allRules )
            {

              if ( new MenuRuleItem ( a ).getText ().equalsIgnoreCase ( name ) )
              {
                // add at the beginning of the list to save the order
                boolean isIn = isIn ( name, this.lastUsedElements );

                if ( !isIn )
                {
                  this.lastUsedElements.add ( 0, new MenuRuleItem ( a ) );

                  MenuRuleItem tmp = new MenuRuleItem ( a );
                  // the actionlistener ist needed to be able to set the
                  // position of a selected
                  // rule
                  ActionListener al = new ActionListener ()
                  {

                    public void actionPerformed ( ActionEvent e )
                    {
                      // to be able to revert the changes in the menu if the
                      // rule throws an exception
                      saveToRevert ();
                      // if the rule is selected it will be moved to the top of
                      // the menu
                      moveToTop ( ( ( MenuRuleItem ) e.getSource () )
                          .getText (), MAX );
                      // save this state of the menu to the preferences
                      save ();
                    }
                  };
                  tmp.addActionListener ( al );
                  // inset at the top of the meun (the preferences are walked
                  // throu
                  if ( ( callBy.equalsIgnoreCase ( "bigstep" ) )
                      || ( callBy.equalsIgnoreCase ( "smallstep" ) && ( ( ( SmallStepProofRule ) a )
                          .isAxiom () || !advanced ) ) )
                  {
                    this.menu.insert ( tmp, 0 );
                  }
                }
              }
            }
          }
        }
        save ();
        // saveToRevert();

        // build the submenu
        int group = rules [ 0 ].getGroup ();
        // a seperator ist needed if there are last used elements
        if ( this.lastUsedElements.size () > 0 )
        {
          this.menu.addSeparator ();
        }

        JMenu subMenu;

        // the hasmap contains the names of the languages connected to the
        // group-number
        HashMap < Number, String > names = getLanguageNames ( lang );
        subMenu = new JMenu ( names
            .get ( new Integer ( rules [ 0 ].getGroup () ) ) );

        // evry rule
        for ( final ProofRule r : rules )
        {
          if ( ( callBy.equalsIgnoreCase ( "bigstep" ) )
              || ( callBy.equalsIgnoreCase ( "smallstep" ) && ( ( ( SmallStepProofRule ) r )
                  .isAxiom () || !advanced ) ) )
          {
            if ( r.getGroup () != group )
            {
              if ( subMenu != null )
              {
                this.menu.add ( subMenu );
              }
              subMenu = new JMenu ( names.get ( new Integer ( r.getGroup () ) ) );
            }
            MenuRuleItem tmp = new MenuRuleItem ( r );
            ActionListener al = new ActionListener ()
            {

              public void actionPerformed ( ActionEvent e )
              {
                // look if the list is full
                if ( RulesMenu.this.getLastUsedElements ().size () < MAX )
                {

                  MenuRuleItem lastUsed = new MenuRuleItem ( r );
                  // check if the element is in the list
                  // boolean isIn = false;
                  // for (int i = 0; i < MAX; i++)
                  // {
                  // int schleife = Math.min(MAX, lastUsedElements.size());
                  // for (int j = 0; j < schleife; j++)
                  // {
                  // if
                  // (lastUsedElements.get(j).getText().equals(lastUsed.getText()))
                  // {
                  // isIn = true;
                  // }
                  // }
                  // }
                  boolean isIn = isIn ( lastUsed.getText (), RulesMenu.this
                      .getLastUsedElements () );

                  ActionListener al2 = new ActionListener ()
                  {

                    public void actionPerformed ( ActionEvent ae )
                    {
                      // move to to
                      moveToTop ( ( ( MenuRuleItem ) ae.getSource () )
                          .getText (), MAX );

                      // the action must be called manualy if the element is in
                      // a submenu
                      // menuItemActivated((JMenuItem) e.getSource());
                      if ( callBy.equals ( "bigstep" ) )
                      {
                        ( ( BigStepNodeComponent ) tnc )
                            .handleMenuActivated ( ( JMenuItem ) ae
                                .getSource () );
                      }
                      else if ( callBy.equals ( "smallstep" ) )
                      {
                        ( ( SmallStepNodeComponent ) tnc )
                            .handleMenuActivated ( ( JMenuItem ) ae
                                .getSource () );
                      }

                    }
                  };
                  lastUsed.addActionListener ( al2 );
                  if ( !isIn )
                  {
                    saveToRevert ();
                    RulesMenu.this.getMenu ().insert ( lastUsed, 0 );
                    RulesMenu.this.getLastUsedElements ().add ( 0, lastUsed );
                  }
                  // may be we want to move it to top
                  // else
                  // {
                  // menu.remove(lastUsed);
                  // last10Elements.remove(lastUsed);
                  // menu.insert(lastUsed,0);
                  // last10Elements.add(0, lastUsed);
                  // }

                  // save the preferences to be able to reorganize
                  save ();
                }
                // if the list is allrady full the last element must be removed
                else
                {
                  MenuRuleItem lastUsed = new MenuRuleItem ( r );
                  ActionListener alMove = new ActionListener ()
                  {

                    public void actionPerformed ( ActionEvent evt )
                    {
                      moveToTop ( ( ( MenuRuleItem ) evt.getSource () )
                          .getText (), MAX );
                      // menuItemActivated((JMenuItem) e.getSource());
                    }
                  };
                  lastUsed.addActionListener ( alMove );
                  boolean isIn = false;
                  for ( int i = 0 ; i < MAX ; i++ )
                  {
                    // check if it is already in the list
                    int schleife = Math.min ( MAX, RulesMenu.this
                        .getLastUsedElements ().size () );
                    for ( int j = 0 ; j < schleife ; j++ )
                    {
                      if ( RulesMenu.this.getLastUsedElements ().get ( j )
                          .getText ().equals ( lastUsed.getText () ) )
                      {
                        isIn = true;
                      }
                    }
                  }
                  // if it is not in the list it will be added at the top and
                  // the last element will be deleted
                  if ( !isIn )
                  {
                    saveToRevert ();
                    RulesMenu.this.getLastUsedElements ().add ( 0, lastUsed );
                    RulesMenu.this.getMenu ().insert ( lastUsed, 0 );
                    RulesMenu.this.getLastUsedElements ().remove ( MAX );
                    RulesMenu.this.getMenu ().remove ( MAX );
                  }
                  // maybe we want to set it to the top position if it is
                  // allrady in the list
                  // else
                  // {
                  // last10Elements.remove(lastUsed);
                  // menu.remove(lastUsed);
                  // last10Elements.add(0, lastUsed );
                  // menu.insert(lastUsed,0);
                  // }
                  // just save it in the preferences to be able to reorganize
                  save ();
                }

                // the action must be called manualy if the element is in a
                // submenu
                if ( callBy.equals ( "bigstep" ) )
                {
                  ( ( BigStepNodeComponent ) tnc )
                      .handleMenuActivated ( ( JMenuItem ) e.getSource () );
                }
                else if ( callBy.equals ( "smallstep" ) )
                {
                  ( ( SmallStepNodeComponent ) tnc )
                      .handleMenuActivated ( ( JMenuItem ) e.getSource () );
                }
              }
            };
            tmp.addActionListener ( al );
            subMenu.add ( tmp );
            group = r.getGroup ();
          }
          this.menu.add ( subMenu );
        }
      }
      saveToRevert ();
    }
    // if ther are less than tomany rules ther will be no submenus, only
    // seperators
    // with this variable you would also be able to disable the submenufunction
    else
    {
      if ( rules.length > 0 )
      {
        int group = rules [ 0 ].getGroup ();
        for ( ProofRule r : rules )
        {
          if ( ( callBy.equalsIgnoreCase ( "bigstep" ) )
              || ( callBy.equalsIgnoreCase ( "smallstep" ) && ( ( ( SmallStepProofRule ) r )
                  .isAxiom () || !advanced ) ) )
          {
            if ( r.getGroup () != group )
            {
              this.menu.addSeparator ();
            }
            this.menu.add ( new MenuRuleItem ( r ) );
            group = r.getGroup ();
          }
        }
      }
    }

    // JPopupMenu menu = new JPopupMenu ();

    // ProofRule[] rules = this.proofModel.getRules();
    // if (rules.length > 0) {
    // int group = rules[0].getGroup();
    // for (ProofRule r : rules) {
    // if (r.getGroup() != group) {
    // menu.addSeparator();
    // }
    // menu.add(new MenuRuleItem(r));
    // group = r.getGroup();
    // }
    // }
    this.menu.addSeparator ();
    this.menu.add ( new MenuGuessItem () );
    this.menu.add ( new MenuGuessTreeItem () );
    // menu.add (this.menuTranslateItem = new MenuTranslateItem ());

    return this.menu;
  }


  /**
   * @return preferences
   */
  protected Preferences getPreferences ()
  {
    return this.preferences;
  }


  /**
   * gets the names of the languages connected to the group of all languages
   * including the given language and every extended one
   * 
   * @param language the language of wich the group should start
   * @return the HashMap containing the LanguageName and the group
   */
  private HashMap < Number, String > getLanguageNames ( Language pLanguage )
  {
    Language language = pLanguage;
    HashMap < Number, String > result = new HashMap < Number, String > ();
    while ( language.getId () > 0 )
    {
      // System.out.println ( language.getId ( ) + " " + language.getName ( ) )
      // ;
      result.put ( new Integer ( language.getId () ), language.getName () );
      try
      {
        language = ( Language ) language.getClass ().getSuperclass ()
            .newInstance ();
      }
      catch ( InstantiationException e )
      {
        // Do nothing
      }
      catch ( IllegalAccessException e )
      {
        // Do nothing
      }
    }
    try
    {
      language = ( Language ) language.getClass ().getSuperclass ()
          .newInstance ();
    }
    catch ( InstantiationException e )
    {
      // Do nothing
    }
    catch ( IllegalAccessException e )
    {
      // Do nothing
    }
    // System.out.println ( language.getId ( ) + " " + language.getName ( ) ) ;
    result.put ( new Integer ( language.getId () ), language.getName () );

    return result;
  }


  /**
   * Moves the first element of the menu to top, corresponding to the label.
   * only elements from 0 to max will be recognized.
   * 
   * @param label the label of the element should be moved
   * @param max the index of the last element should be moved.
   */
  void moveToTop ( String label, int max )
  {
    for ( int i = 0 ; i < max ; i++ )
    {
      try
      {
        MenuRuleItem toCompare = ( MenuRuleItem ) this.menu.getComponent ( i );
        MenuRuleItem tmp2 = this.lastUsedElements.get ( i );
        // compair their names
        if ( toCompare.getText ().equals ( label ) )
        {
          // insert at the top
          this.menu.add ( this.menu.getComponent ( i ), 0 );
        }

        if ( tmp2.getText ().equals ( label ) )
        {
          this.lastUsedElements.remove ( i );
          this.lastUsedElements.add ( 0, tmp2 );
        }
      }
      catch ( ClassCastException ex )
      {
        // should never trough
      }
      save ();
    }
  }


  /**
   * saves the state of the menu (last 10 elements) to the windows regestry
   */
  public void save ()
  {
    for ( int i = 0 ; i < this.lastUsedElements.size () ; i++ )
    {
      this.preferences.put ( "rule" + i, this.lastUsedElements.get ( i )
          .getText () );
    }
  }


  /**
   * saves the state of the menu to be able to revert changes
   */
  void saveToRevert ()
  {
    // get the menu into a list

    // clear revertMenu
    if ( this.revertMenu.size () > 0 )
    {
      this.revertMenu.clear ();
    }

    boolean isRuleItem = false;

    int i = 0;

    if ( this.menu.getComponent ( i ) instanceof MenuRuleItem )
    {
      isRuleItem = true;
    }

    while ( isRuleItem )
    {
      this.revertMenu.add ( i, ( MenuRuleItem ) this.menu.getComponent ( i ) );
      i++ ;
      if ( this.menu.getComponent ( i ) instanceof MenuRuleItem )
      {
        isRuleItem = true;
      }
      else
      {
        isRuleItem = false;
      }
    }

    // save the lastUsedElements
    this.revertLastUsedElements.clear ();
    this.revertLastUsedElements.addAll ( this.lastUsedElements );
  }


  /**
   * reverts the changes in the menu
   */
  public void revertMenu ()
  {
    // firt remove all entries from menu
    boolean isRuleItem = false;

    int i = 0;

    if ( this.menu.getComponent ( i ) instanceof MenuRuleItem )
    {
      isRuleItem = true;
    }

    while ( isRuleItem )
    {
      this.menu.remove ( i );
      if ( this.menu.getComponent ( i ) instanceof MenuRuleItem )
      {
        isRuleItem = true;
      }
      else
      {
        isRuleItem = false;
      }
    }

    // add the entries
    for ( i = 0 ; i < this.revertMenu.size () ; i++ )
    {
      this.menu.insert ( this.revertMenu.get ( i ), i );
    }

    // reset the lastUsedElements
    this.lastUsedElements.clear ();
    this.lastUsedElements.addAll ( this.revertLastUsedElements );
  }


  /**
   * checks if the elment given by its name is in the given arraylist the
   * elements will be compared by its names
   * 
   * @param label the name of the item
   * @param list the list contaning the item or not
   * @return a boolean
   */
  boolean isIn ( String label, ArrayList list )
  {
    boolean isIn = false;

    for ( int l = 0 ; l < list.size () ; l++ )
    {
      if ( label.equalsIgnoreCase ( this.lastUsedElements.get ( l ).getText () ) )
      {
        isIn = true;
      }
    }
    return isIn;
  }


  /**
   * @return the tomany
   */
  public int getTomany ()
  {
    return this.tomany;
  }


  /**
   * @param pTomany the tomany to set
   */
  public void setTomany ( int pTomany )
  {
    this.tomany = pTomany;
  }


  /**
   * @return the lastUsedElements
   */
  public ArrayList < MenuRuleItem > getLastUsedElements ()
  {
    return this.lastUsedElements;
  }


  /**
   * @return the menu
   */
  public JPopupMenu getMenu ()
  {
    return this.menu;
  }
}
