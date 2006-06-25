package pgtest2;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
  private IWorkbenchAction quitAction;
  private IWorkbenchAction aboutAction;
  
  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
  }
  
  protected void makeActions(IWorkbenchWindow window) {
    this.quitAction = ActionFactory.QUIT.create(window);
    register(this.quitAction);
    
    this.aboutAction = ActionFactory.ABOUT.create(window);
    register(this.aboutAction);
  }
  
  protected void fillMenuBar(IMenuManager menuBar) {
    // File
    MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
    menuBar.add(fileMenu);
    fileMenu.add(this.quitAction);
    
    // Help
    MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
    menuBar.add(helpMenu);
    helpMenu.add(this.aboutAction);
  }
}
