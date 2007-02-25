package de.unisiegen.tpml.graphics.outline ;


import java.awt.Rectangle ;
import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreePath ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.Theme ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlinePair ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineStyle ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineComponentListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
import de.unisiegen.tpml.graphics.outline.listener.OutlineTreeExpansionListener ;
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
public final class AbstractOutline implements Outline
{
  /**
   * Caption of the {@link Identifier}s.
   */
  private static final String IDENTIFIER = "Identifier" ; //$NON-NLS-1$


  /**
   * Caption of the {@link Type}s.
   */
  private static final String TYPE = "Type" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the name of a {@link Method}.
   */
  private static final String METHODNAME = "Method-Name" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GETEX = "getE[0-9]+" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GETE = "getE" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the beginning of the check methods.
   */
  private static final String CHECK = "check" ; //$NON-NLS-1$


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
  private DefaultMutableTreeNode rootNode ;


  /**
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   */
  public AbstractOutline ( )
  {
    this.loadedExpression = null ;
    this.rootNode = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
    this.outlineUI.getJPanelMain ( ).addComponentListener (
        new OutlineComponentListener ( this ) ) ;
    this.outlineUI.getJTreeOutline ( ).addTreeExpansionListener (
        new OutlineTreeExpansionListener ( this ) ) ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLet}.
   * 
   * @param pCurriedLet The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedLet ( CurriedLet pCurriedLet )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pCurriedLet , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pCurriedLet , PrettyStyle.IDENTIFIER ) ;
    /*
     * Create the first Identifier.
     */
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( pCurriedLet ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pCurriedLet.getE2 ( ) , pCurriedLet
        .getIdentifiers ( 0 ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pCurriedLet
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pCurriedLet.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pCurriedLet , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: let f x x = x + 1 in f 1 2.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pCurriedLet.getIdentifiers ( i ).equals (
            pCurriedLet.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pCurriedLet.getE1 ( ) , pCurriedLet
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pCurriedLet
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedLet.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pCurriedLet.toPrettyString ( )
            .getAnnotationForPrintable ( pCurriedLet.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pCurriedLet ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pCurriedLet.getTypes ( i ).toPrettyString ( ).toString ( ) ;
        outlineNode = new OutlineNode ( TYPE , tau , outlinePairType
            .getStart ( ) , outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            null , this.outlineUnbound ) ;
        outlineNode.setChildIndexType ( i ) ;
        identifier.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
      }
      node.add ( identifier ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedLet.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pCurriedLet.getTypes ( pCurriedLet.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pCurriedLet.getTypes ( pCurriedLet.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pCurriedLet.toPrettyString ( ).getAnnotationForPrintable (
          pCurriedLet.getE1 ( ) ).getStartOffset ( ) ;
      String tau = pCurriedLet.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pCurriedLet , PrettyStyle.TYPE ,
          start , end ).get ( 0 ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLetRec}.
   * 
   * @param pCurriedLetRec The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedLetRec (
      CurriedLetRec pCurriedLetRec )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pCurriedLetRec , this.outlineUnbound ) ) ;
    /*
     * Create the first Identifier.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pCurriedLetRec , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( pCurriedLetRec ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    /*
     * Check, if the bindings should be searched in E1 and E2 or only in E2.
     * Example: let x x = x in x.
     */
    boolean searchInE1 = true ;
    for ( int i = 1 ; i < pCurriedLetRec.getIdentifiers ( ).length ; i ++ )
    {
      if ( pCurriedLetRec.getIdentifiers ( 0 ).equals (
          pCurriedLetRec.getIdentifiers ( i ) ) )
      {
        searchInE1 = false ;
        break ;
      }
    }
    if ( searchInE1 )
    {
      outlineBinding.find ( pCurriedLetRec.getE1 ( ) , pCurriedLetRec
          .getIdentifiers ( 0 ) ) ;
    }
    outlineBinding.find ( pCurriedLetRec.getE2 ( ) , pCurriedLetRec
        .getIdentifiers ( 0 ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pCurriedLetRec
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pCurriedLetRec.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pCurriedLetRec , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pCurriedLetRec.getIdentifiers ( i ).equals (
            pCurriedLetRec.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pCurriedLetRec.getE1 ( ) , pCurriedLetRec
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pCurriedLetRec
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedLetRec.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pCurriedLetRec.toPrettyString ( )
            .getAnnotationForPrintable ( pCurriedLetRec.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pCurriedLetRec ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pCurriedLetRec.getTypes ( i ).toPrettyString ( )
            .toString ( ) ;
        outlineNode = new OutlineNode ( TYPE , tau , outlinePairType
            .getStart ( ) , outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            null , this.outlineUnbound ) ;
        outlineNode.setChildIndexType ( i ) ;
        identifier.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
      }
      node.add ( identifier ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedLetRec.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pCurriedLetRec.getTypes ( pCurriedLetRec.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pCurriedLetRec.getTypes ( pCurriedLetRec.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pCurriedLetRec.toPrettyString ( ).getAnnotationForPrintable (
          pCurriedLetRec.getE1 ( ) ).getStartOffset ( ) ;
      String tau = pCurriedLetRec.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pCurriedLetRec ,
          PrettyStyle.TYPE , start , end ).get ( 0 ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedLetRec , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedMethod}.
   * 
   * @param pCurriedMethod The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedMethod (
      CurriedMethod pCurriedMethod )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pCurriedMethod , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pCurriedMethod , PrettyStyle.IDENTIFIER ) ;
    /*
     * Create the first Identifier.
     */
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME , pCurriedMethod
        .getIdentifiers ( 0 ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( OutlineNode.NO_BINDING ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    OutlineBinding outlineBinding ;
    final int length = pCurriedMethod.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pCurriedMethod , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: object method add x x = x ; end.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pCurriedMethod.getIdentifiers ( i ).equals (
            pCurriedMethod.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pCurriedMethod.getE ( ) , pCurriedMethod
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pCurriedMethod
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedMethod.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pCurriedMethod.toPrettyString ( )
            .getAnnotationForPrintable ( pCurriedMethod.getE ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pCurriedMethod ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pCurriedMethod.getTypes ( i ).toPrettyString ( )
            .toString ( ) ;
        outlineNode = new OutlineNode ( TYPE , tau , outlinePairType
            .getStart ( ) , outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            null , this.outlineUnbound ) ;
        outlineNode.setChildIndexType ( i ) ;
        identifier.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
      }
      node.add ( identifier ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedMethod.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pCurriedMethod.getTypes ( pCurriedMethod.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pCurriedMethod.getTypes ( pCurriedMethod.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pCurriedMethod.toPrettyString ( ).getAnnotationForPrintable (
          pCurriedMethod.getE ( ) ).getStartOffset ( ) ;
      String tau = pCurriedMethod.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pCurriedMethod ,
          PrettyStyle.TYPE , start , end ).get ( 0 ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedMethod , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Duplication}.
   * 
   * @param pDuplication The input {@link Expression}.
   * @return The node, which represents the given {@link Duplication}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkDuplication (
      Duplication pDuplication )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pDuplication , this.outlineUnbound ) ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode treeNode ;
    OutlineNode outlineNode ;
    treeNode = checkExpression ( pDuplication.getE ( ) ) ;
    outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
    outlineNode.setChildIndexExpression ( ) ;
    node.add ( treeNode ) ;
    /*
     * Create all Identifiers.
     */
    OutlinePair outlinePairId = null ;
    int start ;
    int end ;
    for ( int i = 0 ; i < pDuplication.getIdentifiers ( ).length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      if ( i == 0 )
      {
        outlinePairId = OutlineStyle.getIndex (
            pDuplication ,
            PrettyStyle.IDENTIFIER ,
            pDuplication.toPrettyString ( ).getAnnotationForPrintable (
                pDuplication.getE ( ) ).getEndOffset ( ) ,
            pDuplication.toPrettyString ( ).getAnnotationForPrintable (
                pDuplication.getExpressions ( 0 ) ).getStartOffset ( ) ).get (
            0 ) ;
      }
      else
      {
        start = pDuplication.toPrettyString ( ).getAnnotationForPrintable (
            pDuplication.getExpressions ( i - 1 ) ).getEndOffset ( ) ;
        end = pDuplication.toPrettyString ( ).getAnnotationForPrintable (
            pDuplication.getExpressions ( i ) ).getStartOffset ( ) ;
        outlinePairId = OutlineStyle.getIndex ( pDuplication ,
            PrettyStyle.IDENTIFIER , start , end ).get ( 0 ) ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pDuplication
          .getIdentifiers ( i ) , outlinePairId , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i + 1 ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
      treeNode = checkExpression ( pDuplication.getExpressions ( i ) ) ;
      outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
      outlineNode.setChildIndexExpression ( i + 1 ) ;
      node.add ( treeNode ) ;
    }
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Expression}.
   */
  private final DefaultMutableTreeNode checkExpression ( Expression pExpression )
  {
    for ( java.lang.reflect.Method method : this.getClass ( )
        .getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          CHECK + pExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 1 ] ;
          argument [ 0 ] = pExpression ;
          return ( DefaultMutableTreeNode ) method.invoke ( this , argument ) ;
        }
        catch ( IllegalArgumentException e )
        {
          System.err.println ( "IllegalArgumentException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "IllegalAccessException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "InvocationTargetException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
      }
    }
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link InfixOperation}.
   * 
   * @param pInfixOperation The input {@link Expression}.
   * @return The node, which represents the given {@link InfixOperation}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkInfixOperation (
      InfixOperation pInfixOperation )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pInfixOperation , this.outlineUnbound ) ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode nodeE1 = checkExpression ( pInfixOperation.getE1 ( ) ) ;
    OutlineNode outlineNodeE1 = ( OutlineNode ) nodeE1.getUserObject ( ) ;
    outlineNodeE1.setChildIndexExpression ( 1 ) ;
    node.add ( nodeE1 ) ;
    /*
     * Create the Infix-Operator.
     */
    BinaryOperator binary = pInfixOperation.getOp ( ) ;
    int start = pInfixOperation.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) ,
        pInfixOperation.getE1 ( ).toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    OutlineNode outlineNode = new OutlineNode ( binary , binary.toString ( ) ,
        start , end , this.outlineUnbound ) ;
    outlineNode.setChildIndexOp ( ) ;
    DefaultMutableTreeNode nodeI = new DefaultMutableTreeNode ( outlineNode ) ;
    node.add ( nodeI ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode nodeE2 = checkExpression ( pInfixOperation.getE2 ( ) ) ;
    OutlineNode outlineNodeE2 = ( OutlineNode ) nodeE2.getUserObject ( ) ;
    outlineNodeE2.setChildIndexExpression ( 2 ) ;
    node.add ( nodeE2 ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Lambda}.
   * 
   * @param pLambda The input {@link Expression}.
   * @return The node, which represents the given {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLambda ( Lambda pLambda )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLambda , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLambda ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLambda ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLambda.getE ( ) , pLambda.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pLambda.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLambda.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLambda ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLambda.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Let}.
   * 
   * @param pLet The input {@link Expression}.
   * @return The node, which represents the given {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLet ( Let pLet )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLet , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLet ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLet , outlinePairId
        .getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLet.getE2 ( ) , pLet.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pLet.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLet.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLet ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLet.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link LetRec}.
   * 
   * @param pLetRec The input {@link Expression}.
   * @return The node, which represents the given {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLetRec ( LetRec pLetRec )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pLetRec , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLetRec ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLetRec ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLetRec.getE1 ( ) , pLetRec.getId ( ) ) ;
    outlineBinding.find ( pLetRec.getE2 ( ) , pLetRec.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pLetRec.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLetRec.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLetRec ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLetRec.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLetRec , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Message}.
   * 
   * @param pMessage The input {@link Expression}.
   * @return The node, which represents the given {@link Message}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMessage ( Message pMessage )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMessage , this.outlineUnbound ) ) ;
    /*
     * Create the children of this node.
     */
    createChildren ( pMessage , node ) ;
    /*
     * Create the Identifier.
     */
    int start = pMessage.toPrettyString ( ).getAnnotationForPrintable (
        pMessage.getE ( ) ).getEndOffset ( ) ;
    int end = pMessage.toPrettyString ( ).toString ( ).length ( ) ;
    ArrayList < OutlinePair > outlinePairs = OutlineStyle.getIndex ( pMessage ,
        PrettyStyle.IDENTIFIER , start , end ) ;
    OutlinePair outlinePairId = outlinePairs.get ( 0 ) ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME ,
        pMessage.getId ( ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Method}.
   * 
   * @param pMethod The input {@link Expression}.
   * @return The node, which represents the given {@link Method}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMethod ( Method pMethod )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMethod , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pMethod ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME , pMethod.getId ( ) ,
        outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMethod.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMethod ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMethod.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMethod , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLambda}.
   * 
   * @param pMultiLambda The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMultiLambda (
      MultiLambda pMultiLambda )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMultiLambda , this.outlineUnbound ) ) ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pMultiLambda , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    OutlineNode outlineNode ;
    final int length = pMultiLambda.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pMultiLambda , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: (λ(x, x).x) (1, 2).
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pMultiLambda.getIdentifiers ( i ).equals (
            pMultiLambda.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pMultiLambda.getE ( ) , pMultiLambda
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pMultiLambda
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i + 1 ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLambda.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMultiLambda ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMultiLambda.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMultiLambda , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLet}.
   * 
   * @param pMultiLet The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMultiLet ( MultiLet pMultiLet )
  {
    String [ ] idList = pMultiLet.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pMultiLet , this.outlineUnbound ) ) ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pMultiLet , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    OutlineNode outlineNode ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pMultiLet , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: let(x, x) = (1, 2) in x.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pMultiLet.getIdentifiers ( i ).equals (
            pMultiLet.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pMultiLet.getE2 ( ) , pMultiLet
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , idList [ i ] ,
          outlinePairId , outlineBinding , this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i + 1 ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLet.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMultiLet ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMultiLet.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMultiLet , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link ObjectExpr}.
   * 
   * @param pObjectExpr The input {@link Expression}.
   * @return The node, which represents the given {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkObjectExpr ( ObjectExpr pObjectExpr )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pObjectExpr , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pObjectExpr ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pObjectExpr ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    Row row = pObjectExpr.getE ( ) ;
    boolean equalIdFound = false ;
    for ( Expression expr : row.getExpressions ( ) )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        if ( pObjectExpr.getId ( ).equals ( attribute.getId ( ) ) )
        {
          equalIdFound = true ;
        }
      }
      else
      {
        if ( ! equalIdFound )
        {
          outlineBinding.find ( expr , pObjectExpr.getId ( ) ) ;
        }
      }
    }
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pObjectExpr
        .getId ( ) , outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pObjectExpr.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pObjectExpr ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pObjectExpr.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , pObjectExpr.getTau ( )
          .toPrettyString ( ).toString ( ) , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 , null ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pObjectExpr , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Recursion}.
   * 
   * @param pRecursion The input {@link Expression}.
   * @return The node, which represents the given {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkRecursion ( Recursion pRecursion )
  {
    String id = pRecursion.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pRecursion , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pRecursion ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pRecursion ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pRecursion.getE ( ) , pRecursion.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , id ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pRecursion.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pRecursion ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pRecursion.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pRecursion , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Row}.
   * 
   * @param pRow The input {@link Expression}.
   * @return The node, which represents the given {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkRow ( Row pRow )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pRow , this.outlineUnbound ) ) ;
    for ( int i = 0 ; i < pRow.getExpressions ( ).length ; i ++ )
    {
      Expression currentChild = pRow.getExpressions ( i ) ;
      if ( currentChild instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) currentChild ;
        OutlineNode outlineNode = new OutlineNode ( attribute ,
            this.outlineUnbound ) ;
        outlineNode.setChildIndexExpression ( i + 1 ) ;
        DefaultMutableTreeNode nodeAttr = new DefaultMutableTreeNode (
            outlineNode ) ;
        OutlinePair outlinePairId = OutlineStyle.getIndex ( attribute ,
            PrettyStyle.IDENTIFIER ).get ( 0 ) ;
        OutlineBinding outlineBinding = new OutlineBinding ( attribute ,
            outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
        for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
        {
          Expression tmpChild = pRow.getExpressions ( j ) ;
          if ( ! ( tmpChild instanceof Attribute ) )
          {
            outlineBinding.find ( tmpChild , attribute.getId ( ) ) ;
          }
          if ( ( tmpChild instanceof Attribute )
              && ( ( ( Attribute ) tmpChild ).getId ( ).equals ( attribute
                  .getId ( ) ) ) )
          {
            break ;
          }
        }
        outlineNode = new OutlineNode ( IDENTIFIER , attribute.getId ( ) ,
            outlinePairId , outlineBinding , this.outlineUnbound ) ;
        outlineNode.setChildIndexIdentifier ( ) ;
        nodeAttr.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
        if ( attribute.getTau ( ) != null )
        {
          OutlinePair outlinePairType = OutlineStyle.getIndex ( attribute ,
              PrettyStyle.TYPE ).get ( 0 ) ;
          String tau = attribute.getTau ( ).toPrettyString ( ).toString ( ) ;
          outlineNode = new OutlineNode ( TYPE , attribute.getTau ( )
              .toPrettyString ( ).toString ( ) , outlinePairType.getStart ( ) ,
              outlinePairType.getStart ( ) + tau.length ( ) - 1 , null ,
              this.outlineUnbound ) ;
          outlineNode.setChildIndexType ( ) ;
          nodeAttr.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
        }
        createChildren ( attribute , nodeAttr ) ;
        node.add ( nodeAttr ) ;
      }
      else if ( currentChild instanceof Method )
      {
        DefaultMutableTreeNode treeNode = checkExpression ( currentChild ) ;
        OutlineNode outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
        outlineNode.setChildIndexExpression ( i + 1 ) ;
        node.add ( treeNode ) ;
      }
      else if ( currentChild instanceof CurriedMethod )
      {
        DefaultMutableTreeNode treeNode = checkExpression ( currentChild ) ;
        OutlineNode outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
        outlineNode.setChildIndexExpression ( i + 1 ) ;
        node.add ( treeNode ) ;
      }
    }
    return node ;
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given node.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pDefaultMutableTreeNode The node where the children should be added.
   */
  private final void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pDefaultMutableTreeNode )
  {
    Enumeration < Expression > children = pExpression.children ( ) ;
    int childIndex = getChildIndex ( pExpression ) ;
    Expression child ;
    DefaultMutableTreeNode treeNode ;
    OutlineNode outlineNode ;
    while ( children.hasMoreElements ( ) )
    {
      child = children.nextElement ( ) ;
      treeNode = checkExpression ( child ) ;
      outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
      if ( childIndex == OutlineNode.NO_CHILD_INDEX )
      {
        outlineNode.setChildIndexExpression ( OutlineNode.NO_CHILD_INDEX ) ;
      }
      else
      {
        outlineNode.setChildIndexExpression ( childIndex ) ;
      }
      pDefaultMutableTreeNode.add ( treeNode ) ;
      childIndex ++ ;
    }
  }


  /**
   * Disables the auto update <code>JCheckBox</code> and the
   * <code>JMenuItem</code>. Removes the <code>ItemListener</code> and the
   * <code>ActionListener</code>.
   */
  public final void disableAutoUpdate ( )
  {
    // Disable AutoUpdate, remove Listener and deselect
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).removeItemListener (
        this.outlineUI.getJCheckBoxAutoUpdate ( ).getItemListeners ( ) [ 0 ] ) ;
    this.outlineUI.getJCheckBoxAutoUpdate ( ).setSelected ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setEnabled ( false ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).removeActionListener (
        this.outlineUI.getJMenuItemAutoUpdate ( ).getActionListeners ( ) [ 0 ] ) ;
    this.outlineUI.getJMenuItemAutoUpdate ( ).setSelected ( false ) ;
  }


  /**
   * Execute the rebuild of a new tree in the {@link Outline}.
   */
  public final void execute ( )
  {
    this.outlineUnbound = new OutlineUnbound ( this.loadedExpression ) ;
    this.rootNode = checkExpression ( this.loadedExpression ) ;
    OutlineNode outlineNode = ( OutlineNode ) this.rootNode.getUserObject ( ) ;
    outlineNode.setChildIndexExpression ( ) ;
    repaint ( this.rootNode ) ;
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
   * a new tree in the {@link Outline} after 250ms, if it is not canceled during
   * this time.
   */
  private final void executeTimerStart ( )
  {
    this.outlineTimer = new Timer ( ) ;
    this.outlineTimer.schedule ( new OutlineTimerTask ( this ) , 250 ) ;
  }


  /**
   * Returns the minimum child index. For example 0 if the {@link Expression} is
   * an instance of {@link Condition}, or 1 if the {@link Expression} is an
   * instance of {@link Let}.
   * 
   * @param pExpression The {@link Expression} to check for.
   * @return The minimum child index.
   */
  private final int getChildIndex ( Expression pExpression )
  {
    int result = Integer.MAX_VALUE ;
    for ( java.lang.reflect.Method method : pExpression.getClass ( )
        .getMethods ( ) )
    {
      if ( GETE.equals ( method.getName ( ) ) )
      {
        return OutlineNode.NO_CHILD_INDEX ;
      }
      if ( method.getName ( ).matches ( GETEX ) )
      {
        result = Math.min ( result , Integer.parseInt ( method.getName ( )
            .substring ( 4 ) ) ) ;
      }
    }
    return result == Integer.MAX_VALUE ? 1 : result ;
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
   * @param pModus The modus who is calling this method.
   */
  public final void loadExpression ( Expression pExpression , int pModus )
  {
    if ( ( this.loadedExpression != null )
        && ( pExpression.equals ( this.loadedExpression ) ) )
    {
      return ;
    }
    if ( ( pModus == Outline.CHANGE_SMALLSTEP )
        && ( ! this.outlinePreferences.isAutoUpdate ( ) ) )
    {
      return ;
    }
    if ( pModus == Outline.CHANGE_BIGSTEP )
    {
      return ;
    }
    if ( pModus == Outline.CHANGE_TYPECHECKER )
    {
      return ;
    }
    this.loadedExpression = pExpression ;
    executeTimerCancel ( ) ;
    if ( ( pModus == Outline.INIT ) || ( pModus == Outline.MOUSE_CLICK ) )
    {
      execute ( ) ;
    }
    else
    {
      executeTimerStart ( ) ;
    }
  }


  /**
   * Repaints the root node and all of its children.
   */
  public final void repaint ( )
  {
    repaint ( ( DefaultMutableTreeNode ) AbstractOutline.this.outlineUI
        .getTreeModel ( ).getRoot ( ) ) ;
  }


  /**
   * Repaints the given node and all of its children and resets the caption.
   * 
   * @param pDefaultMutableTreeNode The node, which should be repainted.
   */
  private final void repaint ( DefaultMutableTreeNode pDefaultMutableTreeNode )
  {
    OutlineNode outlineNode = ( OutlineNode ) pDefaultMutableTreeNode
        .getUserObject ( ) ;
    outlineNode.resetCaption ( ) ;
    this.outlineUI.getTreeModel ( ).nodeChanged ( pDefaultMutableTreeNode ) ;
    for ( int i = 0 ; i < pDefaultMutableTreeNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pDefaultMutableTreeNode
          .getChildAt ( i ) ) ;
    }
  }


  /**
   * Sets the root node in the {@link OutlineUI}.
   */
  public final void setRootNode ( )
  {
    this.outlineUI.setRootNode ( this.rootNode ) ;
  }


  /**
   * Updates the breaks in the {@link OutlineNode}.
   */
  public final void updateBreaks ( )
  {
    final int distance = 10 ;
    JScrollPane jScrollPaneOutline = this.outlineUI.getJScrollPaneOutline ( ) ;
    DefaultMutableTreeNode currentNode ;
    OutlineNode currentNodeOutline ;
    TreePath currentTreePath ;
    Rectangle rectangle ;
    Enumeration < ? > enumeration = this.rootNode.breadthFirstEnumeration ( ) ;
    while ( enumeration.hasMoreElements ( ) )
    {
      currentNode = ( DefaultMutableTreeNode ) enumeration.nextElement ( ) ;
      currentNodeOutline = ( OutlineNode ) currentNode.getUserObject ( ) ;
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
        while ( ( currentNodeOutline.breaksCanRemove ( ) )
            && ( ( rectangle.x + rectangle.width ) < ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNodeOutline.breakCountRemove ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
          /*
           * If the node is after the remove to big, a break is added.
           */
          if ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
              .getSize ( ).width - distance ) )
          {
            currentNodeOutline.breakCountAdd ( ) ;
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
            && ( currentNodeOutline.breaksCanAdd ( ) )
            && ( ( rectangle.x + rectangle.width ) > ( jScrollPaneOutline
                .getSize ( ).width - distance ) ) )
        {
          currentNodeOutline.breakCountAdd ( ) ;
          this.outlineUI.getTreeModel ( ).nodeChanged ( currentNode ) ;
          rectangle = this.outlineUI.getJTreeOutline ( ).getPathBounds (
              currentTreePath ) ;
        }
      }
    }
  }
}
