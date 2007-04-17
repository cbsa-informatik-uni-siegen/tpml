package de.unisiegen.tpml.graphics.outline ;


import java.awt.Rectangle ;
import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineDisplayTree ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineTimerTask ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;


/**
 * This class is the main class of the {@link Outline}. It loads the
 * {@link OutlinePreferences}, creates the {@link OutlineUI} and loads new
 * {@link Expression}s.
 * 
 * @author Christian Fehler
 * @version $Rev: 1061 $
 */
public final class DefaultOutline implements Outline
{
  /**
   * Method name for getExpressionsIndex
   */
  private static final String GET_EXPRESSIONS_INDEX = "getExpressionsIndex" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiers
   */
  private static final String GET_IDENTIFIERS = "getIdentifiers" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersIndex
   */
  private static final String GET_IDENTIFIERS_INDEX = "getIdentifiersIndex" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersPrefix
   */
  private static final String GET_IDENTIFIERS_PREFIX = "getIdentifiersPrefix" ; //$NON-NLS-1$


  /**
   * Method name for getIdentifiersBound
   */
  private static final String GET_IDENTIFIERS_BOUND = "getIdentifiersBound" ; //$NON-NLS-1$


  /**
   * Method name for getTypes
   */
  private static final String GET_TYPES = "getTypes" ; //$NON-NLS-1$


  /**
   * Method name for getTypesIndex
   */
  private static final String GET_TYPES_INDEX = "getTypesIndex" ; //$NON-NLS-1$


  /**
   * Method name for getTypesPrefix
   */
  private static final String GET_TYPES_PREFIX = "getTypesPrefix" ; //$NON-NLS-1$


  /**
   * Method name for getSortedChildren
   */
  private static final String GET_SORTED_CHILDREN = "getSortedChildren" ; //$NON-NLS-1$


  /**
   * The {@link OutlineUI}.
   * 
   * @see #getOutlineUI()
   */
  private OutlineUI outlineUI ;


  /**
   * The {@link OutlinePreferences}.
   * 
   * @see #getOutlinePreferences()
   */
  private OutlinePreferences outlinePreferences ;


  /**
   * The loaded {@link Expression} to check if the {@link Expression} has
   * changed.
   */
  private Expression loadedExpression ;


  /**
   * The {@link OutlineUnbound}, in which the unbound {@link Identifier}s in
   * the given {@link Expression} are saved.
   */
  private OutlineUnbound outlineUnbound ;


  /**
   * The <code>Timer</code> for the executing.
   */
  private Timer outlineTimer ;


  /**
   * The root node.
   */
  private OutlineNode rootOutlineNode ;


  /**
   * The {@link Outline.Start}.
   */
  private Outline.Start outlineStart ;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pOutlineStart The {@link Outline.Start}.
   */
  public DefaultOutline ( Outline.Start pOutlineStart )
  {
    this.outlineStart = pOutlineStart ;
    this.loadedExpression = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    if ( ( this.outlineStart.equals ( Outline.Start.BIGSTEP ) )
        || ( this.outlineStart.equals ( Outline.Start.TYPECHECKER ) ) )
    {
      disableAutoUpdate ( ) ;
    }
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given node.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pOutlineNode The node where the children should be added.
   */
  @ SuppressWarnings ( "unchecked" )
  private final void createExpression ( Expression pExpression ,
      OutlineNode pOutlineNode )
  {
    // Child Expression
    int [ ] expressionsIndex = null ;
    // Identifier
    Identifier [ ] identifiers = null ;
    int [ ] identifiersIndex = null ;
    String [ ] identifiersPrefix = null ;
    // Bounded Identifier
    ArrayList < ArrayList < Identifier >> boundedIdentifiers = null ;
    // Type
    MonoType [ ] types = null ;
    int [ ] typesIndex = null ;
    String [ ] typesPrefix = null ;
    // Sorted Children
    PrettyPrintable [ ] sortedChildren = null ;
    for ( Class < Object > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.ChildrenExpressions.class ) )
      {
        try
        {
          expressionsIndex = ( int [ ] ) pExpression.getClass ( ).getMethod (
              GET_EXPRESSIONS_INDEX , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        try
        {
          identifiers = ( Identifier [ ] ) pExpression.getClass ( ).getMethod (
              GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          identifiersIndex = ( int [ ] ) pExpression.getClass ( ).getMethod (
              GET_IDENTIFIERS_INDEX , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          identifiersPrefix = ( String [ ] ) pExpression.getClass ( )
              .getMethod ( GET_IDENTIFIERS_PREFIX , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) )
      {
        try
        {
          identifiers = ( Identifier [ ] ) pExpression.getClass ( ).getMethod (
              GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          identifiersIndex = ( int [ ] ) pExpression.getClass ( ).getMethod (
              GET_IDENTIFIERS_INDEX , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          identifiersPrefix = ( String [ ] ) pExpression.getClass ( )
              .getMethod ( GET_IDENTIFIERS_PREFIX , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
          boundedIdentifiers = ( ArrayList < ArrayList < Identifier >> ) pExpression
              .getClass ( )
              .getMethod ( GET_IDENTIFIERS_BOUND , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        try
        {
          types = ( MonoType [ ] ) pExpression.getClass ( ).getMethod (
              GET_TYPES , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          typesIndex = ( int [ ] ) pExpression.getClass ( ).getMethod (
              GET_TYPES_INDEX , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
          typesPrefix = ( String [ ] ) pExpression.getClass ( ).getMethod (
              GET_TYPES_PREFIX , new Class [ 0 ] ).invoke ( pExpression ,
              new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        try
        {
          sortedChildren = ( PrettyPrintable [ ] ) pExpression.getClass ( )
              .getMethod ( GET_SORTED_CHILDREN , new Class [ 0 ] ).invoke (
                  pExpression , new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
    }
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    OutlineNode outlineNodeE ;
    OutlineBinding outlineBinding ;
    if ( sortedChildren == null )
    {
      if ( ( identifiers != null ) && ( identifiersIndex != null )
          && ( identifiersPrefix != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i ++ )
        {
          if ( boundedIdentifiers == null )
          {
            outlineBinding = null ;
          }
          else
          {
            outlineBinding = new OutlineBinding ( boundedIdentifiers.get ( i ) ) ;
          }
          outlineNodeId = new OutlineNode ( identifiers [ i ] ,
              identifiersPrefix [ i ] , identifiersIndex [ i ] , outlineBinding ) ;
          // Identifier - Type
          if ( ( types != null ) && ( typesIndex != null )
              && ( typesPrefix != null ) )
          {
            for ( int j = 0 ; j < types.length ; j ++ )
            {
              if ( ( types [ j ] != null ) && ( typesIndex [ j ] != - 1 )
                  && ( typesIndex [ j ] == identifiersIndex [ i ] ) )
              {
                outlineNodeType = new OutlineNode ( types [ j ] ,
                    typesPrefix [ j ] , typesIndex [ j ] ) ;
                createType ( types [ j ] , outlineNodeType ) ;
                outlineNodeId.add ( outlineNodeType ) ;
              }
            }
          }
          pOutlineNode.add ( outlineNodeId ) ;
        }
      }
      if ( ( types != null ) && ( typesIndex != null )
          && ( typesPrefix != null ) )
      {
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          if ( ( types [ i ] != null ) && ( typesIndex [ i ] == - 1 ) )
          {
            outlineNodeType = new OutlineNode ( types [ i ] ,
                typesPrefix [ i ] , typesIndex [ i ] ) ;
            createType ( types [ i ] , outlineNodeType ) ;
            pOutlineNode.add ( outlineNodeType ) ;
          }
        }
      }
      if ( expressionsIndex != null )
      {
        ArrayList < Expression > children = pExpression.children ( ) ;
        for ( int i = 0 ; i < children.size ( ) ; i ++ )
        {
          Expression child = children.get ( i ) ;
          outlineNodeE = new OutlineNode ( child , this.outlineUnbound , child
              .getPrefix ( ) , expressionsIndex [ i ] ) ;
          createExpression ( child , outlineNodeE ) ;
          pOutlineNode.add ( outlineNodeE ) ;
        }
      }
    }
    // Sorted Children
    else
    {
      for ( int i = 0 ; i < sortedChildren.length ; i ++ )
      {
        PrettyPrintable current = sortedChildren [ i ] ;
        boolean found = false ;
        if ( ( identifiers != null ) && ( identifiersIndex != null )
            && ( identifiersPrefix != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j ++ )
          {
            if ( current == identifiers [ j ] )
            {
              if ( boundedIdentifiers == null )
              {
                outlineBinding = null ;
              }
              else
              {
                outlineBinding = new OutlineBinding ( boundedIdentifiers
                    .get ( i ) ) ;
              }
              outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                  identifiersPrefix [ j ] , identifiersIndex [ j ] ,
                  outlineBinding ) ;
              pOutlineNode.add ( outlineNodeId ) ;
              found = true ;
              break ;
            }
          }
        }
        if ( ( ! found ) && ( types != null ) && ( typesIndex != null )
            && ( typesPrefix != null ) )
        {
          for ( int j = 0 ; j < types.length ; j ++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ] ,
                  typesPrefix [ j ] , typesIndex [ j ] ) ;
              createType ( types [ j ] , outlineNodeType ) ;
              pOutlineNode.add ( outlineNodeType ) ;
              found = true ;
              break ;
            }
          }
        }
        if ( ( ! found ) && ( expressionsIndex != null ) )
        {
          ArrayList < Expression > children = pExpression.children ( ) ;
          for ( int j = 0 ; j < children.size ( ) ; j ++ )
          {
            if ( current == children.get ( j ) )
            {
              Expression child = children.get ( j ) ;
              outlineNodeE = new OutlineNode ( child , this.outlineUnbound ,
                  child.getPrefix ( ) , expressionsIndex [ j ] ) ;
              createExpression ( child , outlineNodeE ) ;
              pOutlineNode.add ( outlineNodeE ) ;
              found = true ;
              break ;
            }
          }
        }
      }
    }
  }


  /**
   * Creates the children with the given {@link Type} and adds them to the given
   * node.
   * 
   * @param pType The {@link Type}, with which the children should be created.
   * @param pOutlineNode The node where the children should be added.
   */
  private final void createType ( Type pType , OutlineNode pOutlineNode )
  {
    // Type
    MonoType [ ] types = null ;
    int [ ] typesIndex = null ;
    String [ ] typesPrefix = null ;
    // Identifier
    Identifier [ ] identifiers = null ;
    int [ ] identifiersIndex = null ;
    String [ ] identifiersPrefix = null ;
    // Sorted Children
    PrettyPrintable [ ] sortedChildren = null ;
    for ( Class < Object > currentInterface : pType.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        try
        {
          types = ( MonoType [ ] ) pType.getClass ( ).getMethod ( GET_TYPES ,
              new Class [ 0 ] ).invoke ( pType , new Object [ 0 ] ) ;
          typesIndex = ( int [ ] ) pType.getClass ( ).getMethod (
              GET_TYPES_INDEX , new Class [ 0 ] ).invoke ( pType ,
              new Object [ 0 ] ) ;
          typesPrefix = ( String [ ] ) pType.getClass ( ).getMethod (
              GET_TYPES_PREFIX , new Class [ 0 ] ).invoke ( pType ,
              new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        try
        {
          identifiers = ( Identifier [ ] ) pType.getClass ( ).getMethod (
              GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pType ,
              new Object [ 0 ] ) ;
          identifiersIndex = ( int [ ] ) pType.getClass ( ).getMethod (
              GET_IDENTIFIERS_INDEX , new Class [ 0 ] ).invoke ( pType ,
              new Object [ 0 ] ) ;
          identifiersPrefix = ( String [ ] ) pType.getClass ( ).getMethod (
              GET_IDENTIFIERS_PREFIX , new Class [ 0 ] ).invoke ( pType ,
              new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        try
        {
          sortedChildren = ( PrettyPrintable [ ] ) pType.getClass ( )
              .getMethod ( GET_SORTED_CHILDREN , new Class [ 0 ] ).invoke (
                  pType , new Object [ 0 ] ) ;
        }
        catch ( IllegalArgumentException e )
        {
          // Do nothing
        }
        catch ( SecurityException e )
        {
          // Do nothing
        }
        catch ( IllegalAccessException e )
        {
          // Do nothing
        }
        catch ( InvocationTargetException e )
        {
          // Do nothing
        }
        catch ( NoSuchMethodException e )
        {
          // Do nothing
        }
      }
    }
    if ( ( types != null ) && ( typesIndex != null ) && ( typesPrefix != null ) )
    {
      OutlineNode outlineNodeId ;
      OutlineNode outlineNodeType ;
      if ( sortedChildren == null )
      {
        if ( ( identifiers != null ) && ( identifiersIndex != null )
            && ( identifiersPrefix != null ) )
        {
          for ( int i = 0 ; i < identifiers.length ; i ++ )
          {
            outlineNodeId = new OutlineNode ( identifiers [ i ] ,
                identifiersPrefix [ i ] , identifiersIndex [ i ] , null ) ;
            pOutlineNode.add ( outlineNodeId ) ;
          }
        }
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          outlineNodeType = new OutlineNode ( types [ i ] , typesPrefix [ i ] ,
              typesIndex [ i ] ) ;
          createType ( types [ i ] , outlineNodeType ) ;
          pOutlineNode.add ( outlineNodeType ) ;
        }
      }
      // Sorted Children
      else
      {
        for ( int i = 0 ; i < sortedChildren.length ; i ++ )
        {
          PrettyPrintable current = sortedChildren [ i ] ;
          boolean found = false ;
          if ( ( identifiers != null ) && ( identifiersIndex != null )
              && ( identifiersPrefix != null ) )
          {
            for ( int j = 0 ; j < identifiers.length ; j ++ )
            {
              if ( current == identifiers [ j ] )
              {
                outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                    identifiersPrefix [ j ] , identifiersIndex [ j ] , null ) ;
                pOutlineNode.add ( outlineNodeId ) ;
                found = true ;
                break ;
              }
            }
          }
          if ( ! found )
          {
            for ( int j = 0 ; j < types.length ; j ++ )
            {
              if ( current == types [ j ] )
              {
                outlineNodeType = new OutlineNode ( types [ j ] ,
                    typesPrefix [ j ] , typesIndex [ j ] ) ;
                createType ( types [ j ] , outlineNodeType ) ;
                pOutlineNode.add ( outlineNodeType ) ;
                found = true ;
                break ;
              }
            }
          }
        }
      }
    }
  }


  /**
   * Disables the auto update <code>JCheckBox</code> and the
   * <code>JMenuItem</code>. Removes the <code>ItemListener</code> and the
   * <code>ActionListener</code>.
   */
  private final void disableAutoUpdate ( )
  {
    // Disable AutoUpdate, remove Listener and deselect
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).removeItemListener (
        this.outlineUI.getOutlineItemListener ( ) ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).removeActionListener (
        this.outlineUI.getOutlineActionListener ( ) ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final void execute ( )
  {
    if ( this.loadedExpression == null )
    {
      return ;
    }
    this.outlineUnbound = new OutlineUnbound ( this.loadedExpression ) ;
    this.rootOutlineNode = new OutlineNode ( this.loadedExpression ,
        this.outlineUnbound , this.loadedExpression.getPrefix ( ) ,
        OutlineNode.NO_CHILD_INDEX ) ;
    createExpression ( this.loadedExpression , this.rootOutlineNode ) ;
    repaint ( this.rootOutlineNode ) ;
    this.outlineUI.setError ( false ) ;
    SwingUtilities.invokeLater ( new OutlineDisplayTree ( this ) ) ;
  }


  /**
   * Cancels the execute <code>Timer</code>.
   */
  private final void executeTimerCancel ( )
  {
    if ( this.outlineTimer != null )
    {
      this.outlineTimer.cancel ( ) ;
      this.outlineTimer = null ;
    }
  }


  /**
   * Starts the execute <code>Timer</code>, which will execute the rebuild of
   * a new tree in the {@link Outline} after the given delay, if it is not
   * canceled during this time.
   * 
   * @param pDelay Delay in milliseconds before task is to be executed.
   */
  private final void executeTimerStart ( int pDelay )
  {
    if ( pDelay < 0 )
    {
      throw new RuntimeException ( "Delay is smaller than 0" ) ; //$NON-NLS-1$
    }
    this.outlineTimer = new Timer ( ) ;
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ) , pDelay ) ;
  }


  /**
   * Returns the <code>JPanel</code> of the {@link OutlineUI}.
   * 
   * @return The <code>JPanel</code> of the {@link OutlineUI}.
   * @see de.unisiegen.tpml.graphics.outline.Outline#getJPanelOutline()
   */
  public final JPanel getJPanelOutline ( )
  {
    return this.outlineUI.getJPanelMain ( ) ;
  }


  /**
   * Returns the {@link OutlinePreferences}.
   * 
   * @return The {@link OutlinePreferences}.
   * @see #outlinePreferences
   */
  public final OutlinePreferences getOutlinePreferences ( )
  {
    return this.outlinePreferences ;
  }


  /**
   * Returns the {@link OutlineUI}.
   * 
   * @return The {@link OutlineUI}.
   * @see #outlineUI
   */
  public final OutlineUI getOutlineUI ( )
  {
    return this.outlineUI ;
  }


  /**
   * This method loads a new {@link Expression} into the {@link Outline}. It
   * checks if the new {@link Expression} is different to the current loaded
   * {@link Expression}, if not it does nothing and returns. It does also
   * nothing if the auto update is disabled and the change does not come from a
   * <code>MouseEvent</code>. In the <code>BigStep</code> and the
   * <code>TypeChecker</code> view it does also nothing if the change does not
   * come from a <code>MouseEvent</code>.
   * 
   * @param pExpression The new {@link Expression}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public final void loadExpression ( Expression pExpression ,
      Outline.Execute pExecute )
  {
    if ( pExpression == null )
    {
      executeTimerCancel ( ) ;
      if ( ( this.outlinePreferences.isAutoUpdate ( ) )
          || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_EDITOR ) ) )
      {
        this.outlineUI.setError ( true ) ;
      }
      return ;
    }
    /*
     * Do not update, if the the auto change comes from the Editor and the auto
     * update is disabled.
     */
    if ( ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_EDITOR ) )
        && ( ! this.outlinePreferences.isAutoUpdate ( ) ) )
    {
      return ;
    }
    /*
     * Do not update, if the the auto change comes from the SmallStepper and the
     * auto update is disabled.
     */
    if ( ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_SMALLSTEP ) )
        && ( ! this.outlinePreferences.isAutoUpdate ( ) ) )
    {
      return ;
    }
    /*
     * Do not update, if the the auto change comes from the BigStepper.
     */
    if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_BIGSTEP ) )
    {
      return ;
    }
    /*
     * Do not update, if the the auto change comes from the TypeChecker.
     */
    if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_TYPECHECKER ) )
    {
      return ;
    }
    this.outlineUI.setError ( false ) ;
    /*
     * Do not update, if the the loaded Expression is equal to the new
     * Expression.
     */
    if ( ( this.loadedExpression != null )
        && ( pExpression.equals ( this.loadedExpression ) ) )
    {
      return ;
    }
    this.loadedExpression = pExpression ;
    executeTimerCancel ( ) ;
    /*
     * Execute the new Expression immediately, if the change is an init change
     * or a change because of a mouse click.
     */
    if ( ( pExecute.equals ( Outline.Execute.INIT_EDITOR ) )
        || ( pExecute.equals ( Outline.Execute.INIT_SMALLSTEP ) )
        || ( pExecute.equals ( Outline.Execute.INIT_BIGSTEP ) )
        || ( pExecute.equals ( Outline.Execute.INIT_TYPECHECKER ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_EDITOR ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_SMALLSTEP ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_BIGSTEP ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_TYPECHECKER ) ) )
    {
      execute ( ) ;
    }
    else if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_EDITOR ) )
    {
      executeTimerStart ( 250 ) ;
    }
    else if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_SMALLSTEP ) )
    {
      executeTimerStart ( 250 ) ;
    }
  }


  /**
   * Repaints the root node and all of its children with the new color settings.
   */
  public final void propertyChanged ( )
  {
    propertyChanged ( ( OutlineNode ) this.outlineUI.getTreeModel ( )
        .getRoot ( ) ) ;
  }


  /**
   * Repaints the root node and all of its children with the new color settings
   * and resets the caption.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void propertyChanged ( OutlineNode pOutlineNode )
  {
    pOutlineNode.propertyChanged ( ) ;
    pOutlineNode.updateCaption ( ) ;
    this.outlineUI.getTreeModel ( ).nodeChanged ( pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      propertyChanged ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Repaints the given node and all of its children and resets the caption.
   * 
   * @param pOutlineNode The node, which should be repainted.
   */
  private final void repaint ( OutlineNode pOutlineNode )
  {
    pOutlineNode.updateCaption ( ) ;
    this.outlineUI.getTreeModel ( ).nodeChanged ( pOutlineNode ) ;
    for ( int i = 0 ; i < pOutlineNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( OutlineNode ) pOutlineNode.getChildAt ( i ) ) ;
    }
  }


  /**
   * Sets the root node in the {@link OutlineUI}.
   */
  public final void setRootNode ( )
  {
    this.outlineUI.setRootNode ( this.rootOutlineNode ) ;
    updateBreaks ( ) ;
  }


  /**
   * Updates the breaks in the {@link OutlineNode}.
   */
  public final void updateBreaks ( )
  {
    if ( this.rootOutlineNode == null )
    {
      return ;
    }
    final int distance = 20 ;
    JScrollPane jScrollPaneOutline = this.outlineUI.getJScrollPaneOutline ( ) ;
    OutlineNode currentNode ;
    TreePath currentTreePath ;
    Rectangle rectangle ;
    Enumeration < ? > enumeration = this.rootOutlineNode
        .breadthFirstEnumeration ( ) ;
    while ( enumeration.hasMoreElements ( ) )
    {
      currentNode = ( OutlineNode ) enumeration.nextElement ( ) ;
      currentTreePath = new TreePath ( currentNode.getPath ( ) ) ;
      rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
          currentTreePath ) ;
      if ( rectangle != null )
      {
        /*
         * Remove a break from the node, if it is to small and a break can be
         * removed. If the node is after the remove to big, a break is added.
         */
        boolean removed = false ;
        while ( ( currentNode.breaksCanRemove ( ) )
            && ( ( rectangle.x + rectangle.width ) < ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNode.breakCountRemove ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
          /*
           * If the node is after the remove to big, a break is added.
           */
          if ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
              .getSize ( ).width - distance ) )
          {
            currentNode.breakCountAdd ( ) ;
            this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
            rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
                currentTreePath ) ;
            break ;
          }
          removed = true ;
        }
        /*
         * Add a break to the node, if it is to big and more breaks can be
         * added.
         */
        while ( ( ! removed )
            && ( currentNode.breaksCanAdd ( ) )
            && ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNode.breakCountAdd ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
        }
      }
    }
  }
}
