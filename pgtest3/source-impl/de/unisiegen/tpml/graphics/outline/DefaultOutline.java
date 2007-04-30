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
import de.unisiegen.tpml.core.subtyping.SubTypingEnterTypes ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.bigstep.BigStepView ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineActionListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineItemListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineMouseListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeModelListener ;
import de.unisiegen.tpml.graphics.outline.node.OutlineNode ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineDisplayTree ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineTimerTask ;
import de.unisiegen.tpml.graphics.outline.ui.OutlineUI ;
import de.unisiegen.tpml.graphics.outline.util.OutlinePreferences ;
import de.unisiegen.tpml.graphics.smallstep.SmallStepView ;
import de.unisiegen.tpml.graphics.typechecker.TypeCheckerView ;
import de.unisiegen.tpml.ui.editor.TextEditorPanel ;


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
  private PrettyPrintable loadedPrettyPrintable ;


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
   * TODO
   */
  private TextEditorPanel textEditorPanel ;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   */
  private DefaultOutline ( )
  {
    this.loadedPrettyPrintable = null ;
    this.rootOutlineNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pBigStepView TODO
   */
  public DefaultOutline ( BigStepView pBigStepView )
  {
    this ( ) ;
    disableAutoUpdate ( ) ;
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pBigStepView.getJSplitPane ( ) , this ) ) ;
    pBigStepView.addPropertyChangeListener ( new OutlinePropertyChangeListener (
        pBigStepView.getJSplitPane ( ) , this ) ) ;
    pBigStepView.getBigStepProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pBigStepView
            .getBigStepProofModel ( ) ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSmallStepView TODO
   */
  public DefaultOutline ( SmallStepView pSmallStepView )
  {
    this ( ) ;
    this.outlineUI.getJPanelMain ( )
        .addComponentListener (
            new OutlineComponentListener ( pSmallStepView.getJSplitPane ( ) ,
                this ) ) ;
    pSmallStepView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pSmallStepView.getJSplitPane ( ) , this ) ) ;
    pSmallStepView.getSmallStepProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pSmallStepView
            .getSmallStepProofModel ( ) ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTextEditorPanel TODO
   */
  public DefaultOutline ( TextEditorPanel pTextEditorPanel )
  {
    this ( ) ;
    this.textEditorPanel = pTextEditorPanel ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pTextEditorPanel.getJSplitPane ( ) ,
            this ) ) ;
    // MouseListener
    this.textEditorPanel.getEditor ( ).addMouseListener (
        new OutlineMouseListener ( pTextEditorPanel ) ) ;
    // PropertyChangeListener
    this.textEditorPanel
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            this.textEditorPanel.getJSplitPane ( ) , this ) ) ;
    // ActionListener
    OutlineActionListener outlineActionListener = new OutlineActionListener (
        this.outlineUI ) ;
    this.outlineUI.getJMenuItemExpand ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemExpandAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapse ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCollapseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemClose ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCloseAll ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemCopy ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuPreferences ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemSelection ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemBinding ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemUnbound ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemReplace ( ).addActionListener (
        outlineActionListener ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).addActionListener (
        outlineActionListener ) ;
    // ComponentListener
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    // OutlineItemListener
    OutlineItemListener outlineItemListener = new OutlineItemListener (
        this.outlineUI ) ;
    this.outlineUI.getJCheckBoxSelection ( ).addItemListener (
        outlineItemListener ) ;
    
    this.outlineUI.getJCheckBoxBinding().addItemListener ( outlineItemListener ) ;
    
    this.outlineUI.getJCheckBoxFree().addItemListener ( outlineItemListener ) ;
    this.outlineUI.getJCheckBoxReplace().addItemListener ( outlineItemListener ) ;
    this.outlineUI.getJCheckBoxAutoUpdate().addItemListener (outlineItemListener ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pTypeCheckerView TODO
   */
  public DefaultOutline ( TypeCheckerView pTypeCheckerView )
  {
    this ( ) ;
    disableAutoUpdate ( ) ;
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( pTypeCheckerView.getJSplitPane ( ) ,
            this ) ) ;
    pTypeCheckerView
        .addPropertyChangeListener ( new OutlinePropertyChangeListener (
            pTypeCheckerView.getJSplitPane ( ) , this ) ) ;
    pTypeCheckerView.getTypeCheckerProofModel ( ).addTreeModelListener (
        new OutlineTreeModelListener ( this , pTypeCheckerView
            .getTypeCheckerProofModel ( ) ) ) ;
  }


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   * 
   * @param pSubTypingEnterTypes TODO
   */
  public DefaultOutline ( SubTypingEnterTypes pSubTypingEnterTypes )
  {
    this ( ) ;
    // TODO Some listeners
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
    // Bound Identifier
    ArrayList < ArrayList < Identifier >> identifiersBound = null ;
    // Type
    MonoType [ ] types = null ;
    int [ ] typesIndex = null ;
    // Sorted Children
    PrettyPrintable [ ] sortedChildren = null ;
    // Invoke methods
    for ( Class < Object > currentInterface : pExpression.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.ChildrenExpressions.class ) )
      {
        expressionsIndex = getIndex ( pExpression , GET_EXPRESSIONS_INDEX ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pExpression , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.BoundIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pExpression , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pExpression ) ;
        identifiersBound = getIdentifiersBound ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        typesIndex = getIndex ( pExpression , GET_TYPES_INDEX ) ;
        types = getTypes ( pExpression ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        sortedChildren = getSortedChildren ( pExpression ) ;
      }
    }
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    OutlineNode outlineNodeE ;
    OutlineBinding outlineBinding ;
    // No sorted children
    if ( sortedChildren == null )
    {
      if ( ( identifiers != null ) && ( identifiersIndex != null ) )
      {
        for ( int i = 0 ; i < identifiers.length ; i ++ )
        {
          if ( identifiersBound == null )
          {
            outlineBinding = null ;
          }
          else
          {
            outlineBinding = new OutlineBinding ( identifiersBound.get ( i ) ) ;
          }
          outlineNodeId = new OutlineNode ( identifiers [ i ] ,
              identifiersIndex [ i ] , outlineBinding ) ;
          // Identifier - Type
          if ( ( types != null ) && ( typesIndex != null ) )
          {
            for ( int j = 0 ; j < types.length ; j ++ )
            {
              if ( ( types [ j ] != null ) && ( typesIndex [ j ] != - 1 )
                  && ( typesIndex [ j ] == identifiersIndex [ i ] ) )
              {
                outlineNodeType = new OutlineNode ( types [ j ] ,
                    typesIndex [ j ] ) ;
                createType ( types [ j ] , outlineNodeType ) ;
                outlineNodeId.add ( outlineNodeType ) ;
              }
            }
          }
          pOutlineNode.add ( outlineNodeId ) ;
        }
      }
      if ( ( types != null ) && ( typesIndex != null ) )
      {
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          if ( ( types [ i ] != null ) && ( typesIndex [ i ] == - 1 ) )
          {
            outlineNodeType = new OutlineNode ( types [ i ] , typesIndex [ i ] ) ;
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
          outlineNodeE = new OutlineNode ( child , this.outlineUnbound ,
              expressionsIndex [ i ] ) ;
          createExpression ( child , outlineNodeE ) ;
          pOutlineNode.add ( outlineNodeE ) ;
        }
      }
    }
    // Sorted children
    else
    {
      for ( int i = 0 ; i < sortedChildren.length ; i ++ )
      {
        PrettyPrintable current = sortedChildren [ i ] ;
        boolean found = false ;
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int j = 0 ; j < identifiers.length ; j ++ )
          {
            if ( current == identifiers [ j ] )
            {
              if ( identifiersBound == null )
              {
                outlineBinding = null ;
              }
              else
              {
                outlineBinding = new OutlineBinding ( identifiersBound.get ( i ) ) ;
              }
              outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                  identifiersIndex [ j ] , outlineBinding ) ;
              pOutlineNode.add ( outlineNodeId ) ;
              found = true ;
              break ;
            }
          }
        }
        if ( ( ! found ) && ( types != null ) && ( typesIndex != null ) )
        {
          for ( int j = 0 ; j < types.length ; j ++ )
          {
            if ( current == types [ j ] )
            {
              outlineNodeType = new OutlineNode ( types [ j ] , typesIndex [ j ] ) ;
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
                  expressionsIndex [ j ] ) ;
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
    // Identifier
    Identifier [ ] identifiers = null ;
    int [ ] identifiersIndex = null ;
    // Sorted children
    PrettyPrintable [ ] sortedChildren = null ;
    for ( Class < Object > currentInterface : pType.getClass ( )
        .getInterfaces ( ) )
    {
      if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultTypes.class ) )
      {
        typesIndex = getIndex ( pType , GET_TYPES_INDEX ) ;
        types = getTypes ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.DefaultIdentifiers.class ) )
      {
        identifiersIndex = getIndex ( pType , GET_IDENTIFIERS_INDEX ) ;
        identifiers = getIdentifiers ( pType ) ;
      }
      else if ( currentInterface
          .equals ( de.unisiegen.tpml.core.interfaces.SortedChildren.class ) )
      {
        sortedChildren = getSortedChildren ( pType ) ;
      }
    }
    if ( ( types != null ) && ( typesIndex != null ) )
    {
      OutlineNode outlineNodeId ;
      OutlineNode outlineNodeType ;
      // No sorted children
      if ( sortedChildren == null )
      {
        if ( ( identifiers != null ) && ( identifiersIndex != null ) )
        {
          for ( int i = 0 ; i < identifiers.length ; i ++ )
          {
            outlineNodeId = new OutlineNode ( identifiers [ i ] ,
                identifiersIndex [ i ] , null ) ;
            pOutlineNode.add ( outlineNodeId ) ;
          }
        }
        for ( int i = 0 ; i < types.length ; i ++ )
        {
          outlineNodeType = new OutlineNode ( types [ i ] , typesIndex [ i ] ) ;
          createType ( types [ i ] , outlineNodeType ) ;
          pOutlineNode.add ( outlineNodeType ) ;
        }
      }
      // Sorted children
      else
      {
        for ( int i = 0 ; i < sortedChildren.length ; i ++ )
        {
          PrettyPrintable current = sortedChildren [ i ] ;
          boolean found = false ;
          if ( ( identifiers != null ) && ( identifiersIndex != null ) )
          {
            for ( int j = 0 ; j < identifiers.length ; j ++ )
            {
              if ( current == identifiers [ j ] )
              {
                outlineNodeId = new OutlineNode ( identifiers [ j ] ,
                    identifiersIndex [ j ] , null ) ;
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
                    typesIndex [ j ] ) ;
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
    //this.outlineUI.getJCheckBoxAutoUpdate ( ).removeItemListener (
      //  this.outlineUI.getOutlineItemListener ( ) ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    // TODO
    // this.outlineUI.getJMenuItemAutoUpdate ( ).removeActionListener (
    // this.outlineUI.getOutlineActionListener ( ) ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final void execute ( )
  {
    if ( this.loadedPrettyPrintable == null )
    {
      return ;
    }
    if ( this.loadedPrettyPrintable instanceof Expression )
    {
      Expression expression = ( Expression ) this.loadedPrettyPrintable ;
      this.outlineUnbound = new OutlineUnbound ( expression ) ;
      this.rootOutlineNode = new OutlineNode ( expression ,
          this.outlineUnbound , OutlineNode.NO_CHILD_INDEX ) ;
      createExpression ( expression , this.rootOutlineNode ) ;
    }
    else if ( this.loadedPrettyPrintable instanceof Type )
    {
      Type type = ( Type ) this.loadedPrettyPrintable ;
      this.rootOutlineNode = new OutlineNode ( type ,
          OutlineNode.NO_CHILD_INDEX ) ;
      createType ( type , this.rootOutlineNode ) ;
    }
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
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @return TODO
   */
  private final Identifier [ ] getIdentifiers ( Object pInvokedFrom )
  {
    try
    {
      return ( Identifier [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_IDENTIFIERS , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @return TODO
   */
  @ SuppressWarnings ( "unchecked" )
  private final ArrayList < ArrayList < Identifier >> getIdentifiersBound (
      Object pInvokedFrom )
  {
    try
    {
      return ( ArrayList < ArrayList < Identifier >> ) pInvokedFrom.getClass ( )
          .getMethod ( GET_IDENTIFIERS_BOUND , new Class [ 0 ] ).invoke (
              pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @param pMethod TODO
   * @return TODO
   */
  private final int [ ] getIndex ( Object pInvokedFrom , String pMethod )
  {
    try
    {
      return ( int [ ] ) pInvokedFrom.getClass ( ).getMethod ( pMethod ,
          new Class [ 0 ] ).invoke ( pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
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
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @return TODO
   */
  private final PrettyPrintable [ ] getSortedChildren ( Object pInvokedFrom )
  {
    try
    {
      return ( PrettyPrintable [ ] ) pInvokedFrom.getClass ( ).getMethod (
          GET_SORTED_CHILDREN , new Class [ 0 ] ).invoke ( pInvokedFrom ,
          new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
  }


  /**
   * Returns the textEditorPanel.
   * 
   * @return The textEditorPanel.
   * @see #textEditorPanel
   */
  public TextEditorPanel getTextEditorPanel ( )
  {
    return this.textEditorPanel ;
  }


  /**
   * TODO
   * 
   * @param pInvokedFrom TODO
   * @return TODO
   */
  private final MonoType [ ] getTypes ( Object pInvokedFrom )
  {
    try
    {
      return ( MonoType [ ] ) pInvokedFrom.getClass ( ).getMethod ( GET_TYPES ,
          new Class [ 0 ] ).invoke ( pInvokedFrom , new Object [ 0 ] ) ;
    }
    catch ( IllegalArgumentException e )
    {
      System.err.println ( "DefaultOutline: IllegalArgumentException" ) ; //$NON-NLS-1$
    }
    catch ( SecurityException e )
    {
      System.err.println ( "DefaultOutline: SecurityException" ) ; //$NON-NLS-1$
    }
    catch ( IllegalAccessException e )
    {
      System.err.println ( "DefaultOutline: IllegalAccessException" ) ; //$NON-NLS-1$
    }
    catch ( InvocationTargetException e )
    {
      System.err.println ( "DefaultOutline: InvocationTargetException" ) ; //$NON-NLS-1$
    }
    catch ( NoSuchMethodException e )
    {
      System.err.println ( "DefaultOutline: NoSuchMethodException" ) ; //$NON-NLS-1$
    }
    return null ;
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
   * @param pPrettyPrintable The new {@link PrettyPrintable}.
   * @param pExecute The {@link Outline.Execute}.
   */
  public final void loadPrettyPrintable ( PrettyPrintable pPrettyPrintable ,
      Outline.Execute pExecute )
  {
    if ( pPrettyPrintable == null )
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
    if ( ( this.loadedPrettyPrintable != null )
        && ( pPrettyPrintable.equals ( this.loadedPrettyPrintable ) ) )
    {
      return ;
    }
    this.loadedPrettyPrintable = pPrettyPrintable ;
    executeTimerCancel ( ) ;
    /*
     * Execute the new Expression immediately, if the change is an init change
     * or a change because of a mouse click.
     */
    if ( ( pExecute.equals ( Outline.Execute.INIT_EDITOR ) )
        || ( pExecute.equals ( Outline.Execute.INIT_SMALLSTEP ) )
        || ( pExecute.equals ( Outline.Execute.INIT_BIGSTEP ) )
        || ( pExecute.equals ( Outline.Execute.INIT_TYPECHECKER ) )
        || ( pExecute.equals ( Outline.Execute.INIT_TYPEINFERENCE ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_EDITOR ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_SMALLSTEP ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_BIGSTEP ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_TYPECHECKER ) )
        || ( pExecute.equals ( Outline.Execute.MOUSE_CLICK_TYPEINFERENCE ) ) )
    {
      execute ( ) ;
    }
    else if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_EDITOR ) )
    {
      executeTimerStart ( 500 ) ;
    }
    else if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_SMALLSTEP ) )
    {
      executeTimerStart ( 250 ) ;
    }
    else if ( pExecute.equals ( Outline.Execute.AUTO_CHANGE_TYPEINFERENCE ) )
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
