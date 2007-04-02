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

import de.unisiegen.tpml.core.ProofRule;
import de.unisiegen.tpml.core.languages.Language;
import de.unisiegen.tpml.graphics.bigstep.BigStepNodeComponent;
import de.unisiegen.tpml.graphics.smallstep.SmallStepNodeComponent;
import de.unisiegen.tpml.graphics.tree.TreeNodeComponent;

public class RulesMenu
{
	private JPopupMenu menu = new JPopupMenu();
	
	/**
   * saves the last used elements MAX elements will be saved
   */
  private ArrayList<MenuRuleItem> lastUsedElements = new ArrayList<MenuRuleItem>();
  
  /**
   * saves the state of the menu in the preferences to restor at the next start
   */
  private Preferences preferences;
  
  /**
   * saves the state of the menu to be able to revert it. It will be used if the selected
   * rule throws an exception. By this rules will not be set at the top of the menu if
   * they are wrong
   */
  private ArrayList <MenuRuleItem> revertMenu = new  ArrayList <MenuRuleItem> ();
  
  /**
   * look at revertMenu
   */
  private ArrayList<MenuRuleItem> revertLastUsedElements = new ArrayList <MenuRuleItem>();
  
	
	private static final int TOMANY = 15;
	private static final int MAX = 10;
	
	//public JPopupMenu getMenu (ProofRule[] rules, ProofRule[] allRules, Language lang, final TreeNodeComponent tnc, final String callBy)
	public JPopupMenu getMenu (ProofRule[] rules, ProofRule[] allRules, Language lang, final JComponent tnc, final String callBy)
	{
//	if to many rules we will devide in menu and submenus, otherwise there will be only seperators 
    //between the rules coming from the different languages
    if (rules.length > TOMANY)
    {
      if (rules.length > 0)
      {

        //first get the lastUsedRules of the preferences (last state of the programm)

        //get the names from the preferences, compare each with the list of all usable rules, add them
        preferences = Preferences.userNodeForPackage(BigStepNodeComponent.class);
        //backwards to save the ordering
        for (int i = MAX - 1; i >= 0; i--)
        {
          String name = preferences.get("rule" + i, "");
          //TODO Testausgabe
          //System.out.println("Auslesen: rule"+i+" = "+name);

          if (name.equalsIgnoreCase(""))
          {
            // do nothing if the rule has no name, the rule dose not exist
          }
          else
          {
            //ProofRule[] allRules = proofModel.getRules();
            for (ProofRule a : allRules)
            {
              //if (new MenuRuleItem(a).getText().equalsIgnoreCase(name))
            	if (new MenuRuleItem(a).getText().equalsIgnoreCase(name))
              {
                //add at the beginning of the list to save the order
            		//TODO Baustelle, Menüs gehen nicht
            		boolean isIn=isIn(name, lastUsedElements);
            		
                if(!isIn) 
                	{
		                lastUsedElements.add(0, new MenuRuleItem(a));
		                	
		                MenuRuleItem tmp = new MenuRuleItem(a);
		                //the actionlistener ist needed to be able to set the position of a selected 
		                //rule
		                ActionListener al = new ActionListener() {
		                  public void actionPerformed(ActionEvent e)
		                  {
		                    //to be able to revert the changes in the menu if the rule throws an exception
		                  	saveToRevert();
		                  	//if the rule is selected it will be moved to the top of the menu
		                  	moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
		                  	//save this state of the menu to the preferences
		                  	save();
		                  }
		                };
		                tmp.addActionListener(al);
		                //inset at the top of the meun (the preferences are walked throu 
		                menu.insert(tmp, 0);
                	}
              }
            }
          }
        }
        save();
        

        //build the submenu
        int group = rules[0].getGroup();
        //a seperator ist needed if there are last used elements
        if (lastUsedElements.size() > 0)
        {
          menu.addSeparator();
        }

        // JMenu Smenu=new JMenu(Messages.getString("Language.0")+ " "
        // +rules[0].getGroup());
        JMenu subMenu;
        //Language lang = proofModel.getLanguage();
        
        //the hasmap contains teh names of the languages connected to the group-number
        HashMap <Number,String>names = getLanguageNames(lang);
        subMenu = new JMenu(names.get(rules[0].getGroup()));
        
        // Jede Regel
        for (final ProofRule r : rules)
        {
          //if (((SmallStepProofRule) r).isAxiom() || !advanced)
          {
            if (r.getGroup() != group)
            {
              if (subMenu != null)
              {
                menu.add(subMenu);
              }              
              subMenu = new JMenu(names.get(r.getGroup()));
            }
            MenuRuleItem tmp = new MenuRuleItem(r);
            ActionListener al = new ActionListener() {
              public void actionPerformed(ActionEvent e)
              {
              	//look if the list is full
                if (lastUsedElements.size() < MAX)
                {
                	
                  MenuRuleItem lastUsed = new MenuRuleItem(r);
                  //check if the element is in the list
//                  boolean isIn = false;
//                  for (int i = 0; i < MAX; i++)
//                  {
//                    int schleife = Math.min(MAX, lastUsedElements.size());
//                    for (int j = 0; j < schleife; j++)
//                    {
//                      if (lastUsedElements.get(j).getText().equals(lastUsed.getText()))
//                      {
//                        isIn = true;
//                      }
//                    }
//                  }
                  boolean isIn = isIn (lastUsed.getText(), lastUsedElements);
                  
                  ActionListener al = new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                      //move to to
                    	moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
                 
                      //the action must be called manualy if the element is in a submenu
                      //menuItemActivated((JMenuItem) e.getSource());
                    	if (callBy.equals("bigstep"))
                    	{
                    		((BigStepNodeComponent) tnc).handleMenuActivated((JMenuItem) e.getSource());	
                    	}
                    	else if (callBy.equals("smallstep"))
                    	{
                    		((SmallStepNodeComponent) tnc).handleMenuActivated((JMenuItem) e.getSource());	
                    	}
                    	
                    }
                  };
                  lastUsed.addActionListener(al);
                  if (!isIn)
                  {
                  	//TODO Testuasgabe
                  	//System.out.println();
                    //System.out.println("Die Liste enthälten den Eintrag bisher: "+isIn);
                  	saveToRevert();
                    menu.insert(lastUsed, 0);
                    lastUsedElements.add(0, lastUsed);
                    //TODO Testausgabe
                    //System.out.println("jetzt sollte sie drin sein! "+isIn(lastUsed.getText(), lastUsedElements));
                  }
                  //may be we want to move it to top
                  // else
                  // {
                  // menu.remove(lastUsed);
                  // last10Elements.remove(lastUsed);
                  // menu.insert(lastUsed,0);
                  // last10Elements.add(0, lastUsed);
                  // }

                  //save the preferences to be able to reorganize
                  save();
                }
                //if the list is allrady full the last element must be removed
                else
                {
                  MenuRuleItem lastUsed = new MenuRuleItem(r);
                  ActionListener al = new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                      moveToTop(((MenuRuleItem) e.getSource()).getText(), MAX);
                      //menuItemActivated((JMenuItem) e.getSource());
                    }
                  };
                  lastUsed.addActionListener(al);
                  boolean isIn = false;
                  for (int i = 0; i < MAX; i++)
                  {
                    //check if it is already in the list
                    int schleife = Math.min(MAX, lastUsedElements.size());
                    for (int j = 0; j < schleife; j++)
                    {
                      if (lastUsedElements.get(j).getText().equals(lastUsed.getText()))
                      {
                        isIn = true;
                      }
                    }
                  }
                  //if it is not in the list it will be added at the top and the last element will be deleted
                  if (!isIn)
                  {
                  	saveToRevert();
                    lastUsedElements.add(0, lastUsed);
                    menu.insert(lastUsed, 0);
                    lastUsedElements.remove(MAX);
                    menu.remove(MAX);
                  }
                  //maybe we want to set it to the top position if it is allrady in the list
                  // else
                  // {
                  // last10Elements.remove(lastUsed);
                  // menu.remove(lastUsed);
                  // last10Elements.add(0, lastUsed );
                  // menu.insert(lastUsed,0);
                  // }
                  //just save it in the preferences to be able to reorganize
                 save();
                }
                
                //the action must be called manualy if the element is in a submenu
                //menuItemActivated((JMenuItem) e.getSource());
                if (callBy.equals("bigstep"))
              	{
              		((BigStepNodeComponent) tnc).handleMenuActivated((JMenuItem) e.getSource());	
              	}
              	else if (callBy.equals("smallstep"))
              	{
              		((SmallStepNodeComponent) tnc).handleMenuActivated((JMenuItem) e.getSource());	
              	}
                

              }
            };
            tmp.addActionListener(al);
            subMenu.add(tmp);
            group = r.getGroup();
          }
          menu.add(subMenu);
        }
      }
    }
    //if ther are less than TOMANY rules ther will be no submenus, only seperators
    //with this variable you would also be able to disable the submenufunction
    else
    {
      if (rules.length > 0)
      {
        int group = rules[0].getGroup();
        for (ProofRule r : rules)
        {
          //if (((SmallStepProofRule) r).isAxiom() || !advanced)
          {
            if (r.getGroup() != group)
            {
              menu.addSeparator();
            }
            menu.add(new MenuRuleItem(r));
            group = r.getGroup();
          }
        }
      }
    }

    //JPopupMenu menu = new JPopupMenu ();
    
    //ProofRule[] rules = this.proofModel.getRules();
    //if (rules.length > 0) {
    //  int group = rules[0].getGroup();
    //  for (ProofRule r : rules) {
    //    if (r.getGroup() != group) {
    //      menu.addSeparator();
    //    }
    //    menu.add(new MenuRuleItem(r));
    //    group = r.getGroup();
    //  }
    //}
    menu.addSeparator();
    menu.add (new MenuGuessItem ());
    menu.add (new MenuGuessTreeItem ());
    //menu.add (this.menuTranslateItem = new MenuTranslateItem ());

		return menu;
	}
	
	/**
   * gets the names of the languages connected to the group of all languages including the given
   * language and every extended one 
   *
   * @param language
   * 						the language of wich the group should start
   * @return		the HashMap containing the LanguageName and the group
   */
  private HashMap getLanguageNames(Language language)
	{
		HashMap <Number,String> result = new HashMap<Number,String>();
		while ( language.getId ( ) > 0 )
    {
      //System.out.println ( language.getId ( ) + " " + language.getName ( ) ) ;
      result.put(language.getId ( ), language.getName ( ));
      try
      {
        language = ( Language ) language.getClass ( ).getSuperclass ( )
            .newInstance ( ) ;
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
      language = ( Language ) language.getClass ( ).getSuperclass ( )
          .newInstance ( ) ;
    }
    catch ( InstantiationException e )
    {
      // Do nothing
    }
    catch ( IllegalAccessException e )
    {
      // Do nothing
    }
    //System.out.println ( language.getId ( ) + " " + language.getName ( ) ) ;
    result.put(language.getId ( ), language.getName ( ));
		
		return result;
	}

	/**
   * Moves the first element of the menu to top, corresponding to the label.
   * only elements from 0 to max will be recognized.
   *
   * @param label
   * 					the label of the element should be moved
   * @param max
   * 					the index of the last element should be moved.
   */
  private void moveToTop(String label, int max)
	{
		for (int i = 0; i < max; i++)
    {
      try
      {
        MenuRuleItem toCompare = (MenuRuleItem) menu.getComponent(i);
        MenuRuleItem tmp2 = lastUsedElements.get(i);
        // vergleiche die Namen, wenn sie übereinstimmen
        if (toCompare.getText().equals(label))
        {
          //System.out.println("wieder nach oben!");
          // nach oeben schieben
          menu.add(menu.getComponent(i), 0);
          // die anderen sind uninteressant, wenn wir einen
          // Treffer hatten
          //break;
        }
        
        if (tmp2.getText().equals(label))
        {
        	lastUsedElements.remove(i);
        	lastUsedElements.add(0, tmp2);        	
        } 
      }
      catch (ClassCastException ex)
      {
        // Sollte eigentlich nie ausgeführt werden...
      	//System.out.println("NEIN!");
      }
      save();
    }
	}

	/**
	 * saves the state of the menu (last 10 elements) to the windows regestry
	 *
	 */
	private void save()
	{
		for (int i = 0; i < lastUsedElements.size(); i++)
    {
      //TODO Testausgabe
			//System.out.println("rule"+i+" = "+lastUsedElements.get(i).getText());
      preferences.put("rule" + i, lastUsedElements.get(i).getText());
    }
	}
	
	/**
	 * saves the state of the menu to be able to revert changes
	 *
	 */
	private void saveToRevert()
	{
		//als erstes das Menü durchlaufen und in die Liste packen
		if (revertMenu.size()>0)
			{
				revertMenu.clear();
			}
		
		boolean isRuleItem = false;
		
		int i = 0;
		
		if (menu.getComponent(i) instanceof MenuRuleItem)
		{
			isRuleItem = true;
		}
		
		while (isRuleItem)
		{
			revertMenu.add(i, (MenuRuleItem)menu.getComponent(i));
			i++;
			if (menu.getComponent(i) instanceof MenuRuleItem)
			{
				isRuleItem = true;
			}
			else
			{
				isRuleItem = false;
			}
		}
		
		//jetzt die Last10Elements sichern
		revertLastUsedElements.clear();
		revertLastUsedElements.addAll(lastUsedElements);
	}
	
	/**
	 * reverts the changes in the menu
	 *
	 */
	private void revertMenu()
	{
		//TODO Testausgabe
		//System.out.println();
    //System.out.println();
		//System.out.println("Revert ist aufgerufen!");
		//als erstes das Menü von den Einträgen befreien
		boolean isRuleItem = false;
		
		int i = 0;
		
		if (menu.getComponent(i) instanceof MenuRuleItem)
		{
			isRuleItem = true;
		}
		
		while (isRuleItem)
		{
			menu.remove(i);
			if (menu.getComponent(i) instanceof MenuRuleItem)
			{
				isRuleItem = true;
			}
			else
			{
				isRuleItem = false;
			}
		}
		
		//Die Einträge wieder hinzufügen
		for (i=0; i<revertMenu.size(); i++)
		{
			menu.insert(revertMenu.get(i),i);
		}
		
		//last10Elements zurücksetzen
		lastUsedElements.clear();
		lastUsedElements.addAll(revertLastUsedElements);
	}
	
	private boolean isIn ( String label, ArrayList list )
	{
		boolean isIn = false;
		//TODO Testausgabe
		//System.out.println("in der Liste sind...");
		for (int l = 0; l<list.size(); l++)
		{
			//TODO Testausgabe
			//System.out.print(lastUsedElements.get(l).getText()+", ");
			if (label.equalsIgnoreCase(lastUsedElements.get(l).getText()))
			{
				isIn=true;
			}
		}
		return isIn;
	}


}