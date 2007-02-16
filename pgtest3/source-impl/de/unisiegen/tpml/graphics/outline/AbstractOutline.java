package de.unisiegen.tpml.graphics.outline ;


import java.lang.reflect.InvocationTargetException ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
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
import de.unisiegen.tpml.graphics.outline.listener.OutlinePropertyChangeListener ;
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
   * Initilizes the {@link OutlinePreferences} and the {@link OutlineUI}.
   */
  public AbstractOutline ( )
  {
    this.loadedExpression = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
    Theme.currentTheme ( ).addPropertyChangeListener (
        new OutlinePropertyChangeListener ( this ) ) ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLet}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedLet ( CurriedLet pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    /*
     * Create the first Identifier.
     */
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression
        .getIdentifiers ( 0 ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pExpression , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: let f x x = x + 1 in f 1 2.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pExpression.getE1 ( ) , pExpression
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pExpression
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
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
    if ( pExpression.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pExpression.getTypes ( pExpression.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pExpression.getTypes ( pExpression.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pExpression.toPrettyString ( ).getAnnotationForPrintable (
          pExpression.getE1 ( ) ).getStartOffset ( ) ;
      String tau = pExpression.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pExpression , PrettyStyle.TYPE ,
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
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLetRec}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedLetRec (
      CurriedLetRec pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the first Identifier.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    /*
     * Check, if the bindings should be searched in E1 and E2 or only in E2.
     * Example: let x x = x in x.
     */
    boolean searchInE1 = true ;
    for ( int i = 1 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      if ( pExpression.getIdentifiers ( 0 ).equals (
          pExpression.getIdentifiers ( i ) ) )
      {
        searchInE1 = false ;
        break ;
      }
    }
    if ( searchInE1 )
    {
      outlineBinding.find ( pExpression.getE1 ( ) , pExpression
          .getIdentifiers ( 0 ) ) ;
    }
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression
        .getIdentifiers ( 0 ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pExpression , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pExpression.getE1 ( ) , pExpression
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pExpression
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
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
    if ( pExpression.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pExpression.getTypes ( pExpression.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pExpression.getTypes ( pExpression.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pExpression.toPrettyString ( ).getAnnotationForPrintable (
          pExpression.getE1 ( ) ).getStartOffset ( ) ;
      String tau = pExpression.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pExpression , PrettyStyle.TYPE ,
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
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedMethod}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedMethod (
      CurriedMethod pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    /*
     * Create the first Identifier.
     */
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME , pExpression
        .getIdentifiers ( 0 ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( OutlineNode.NO_BINDING ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create all other Identifiers and Types.
     */
    OutlineBinding outlineBinding ;
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pExpression , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: object method add x x = x ; end.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pExpression.getE ( ) , pExpression
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pExpression
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i ) ;
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          outlineNode ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
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
    if ( pExpression.getTypes ( 0 ) != null )
    {
      int start = outlinePairId.getEnd ( ) ;
      if ( ( pExpression.getTypes ( pExpression.getTypes ( ).length - 1 ) != null )
          && ( outlinePairType != null ) )
      {
        start = outlinePairType.getStart ( )
            + pExpression.getTypes ( pExpression.getTypes ( ).length - 1 )
                .toPrettyString ( ).toString ( ).length ( ) ;
      }
      int end = pExpression.toPrettyString ( ).getAnnotationForPrintable (
          pExpression.getE ( ) ).getStartOffset ( ) ;
      String tau = pExpression.getTypes ( 0 ).toPrettyString ( ).toString ( ) ;
      outlinePairType = OutlineStyle.getIndex ( pExpression , PrettyStyle.TYPE ,
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
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Duplication}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Duplication}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkDuplication (
      Duplication pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode treeNode ;
    OutlineNode outlineNode ;
    treeNode = checkExpression ( pExpression.getE ( ) ) ;
    outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
    outlineNode.setChildIndexExpression ( ) ;
    node.add ( treeNode ) ;
    /*
     * Create all Identifiers.
     */
    OutlinePair outlinePairId = null ;
    int start ;
    int end ;
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      if ( i == 0 )
      {
        outlinePairId = OutlineStyle.getIndex (
            pExpression ,
            PrettyStyle.IDENTIFIER ,
            pExpression.toPrettyString ( ).getAnnotationForPrintable (
                pExpression.getE ( ) ).getEndOffset ( ) ,
            pExpression.toPrettyString ( ).getAnnotationForPrintable (
                pExpression.getExpressions ( 0 ) ).getStartOffset ( ) )
            .get ( 0 ) ;
      }
      else
      {
        start = pExpression.toPrettyString ( ).getAnnotationForPrintable (
            pExpression.getExpressions ( i - 1 ) ).getEndOffset ( ) ;
        end = pExpression.toPrettyString ( ).getAnnotationForPrintable (
            pExpression.getExpressions ( i ) ).getStartOffset ( ) ;
        outlinePairId = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.IDENTIFIER , start , end ).get ( 0 ) ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pExpression
          .getIdentifiers ( i ) , outlinePairId , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i + 1 ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
      treeNode = checkExpression ( pExpression.getExpressions ( i ) ) ;
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
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link InfixOperation}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkInfixOperation (
      InfixOperation pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode nodeE1 = checkExpression ( pExpression.getE1 ( ) ) ;
    OutlineNode outlineNodeE1 = ( OutlineNode ) nodeE1.getUserObject ( ) ;
    outlineNodeE1.setChildIndexExpression ( 1 ) ;
    node.add ( nodeE1 ) ;
    /*
     * Create the Infix-Operator.
     */
    BinaryOperator binary = pExpression.getOp ( ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) ,
        pExpression.getE1 ( ).toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    OutlineNode outlineNode = new OutlineNode ( binary , binary.toString ( ) ,
        start , end , this.outlineUnbound ) ;
    outlineNode.setChildIndexOp ( ) ;
    DefaultMutableTreeNode nodeI = new DefaultMutableTreeNode ( outlineNode ) ;
    node.add ( nodeI ) ;
    /*
     * Create the first Expression.
     */
    DefaultMutableTreeNode nodeE2 = checkExpression ( pExpression.getE2 ( ) ) ;
    OutlineNode outlineNodeE2 = ( OutlineNode ) nodeE2.getUserObject ( ) ;
    outlineNodeE2.setChildIndexExpression ( 2 ) ;
    node.add ( nodeE2 ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Lambda}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLambda ( Lambda pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pExpression.getE ( ) , pExpression.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getId ( ) , outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Let}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLet ( Let pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getId ( ) , outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link LetRec}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLetRec ( LetRec pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pExpression.getE1 ( ) , pExpression.getId ( ) ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getId ( ) , outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Message}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Message}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMessage ( Message pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    /*
     * Create the Identifier.
     */
    int start = pExpression.toPrettyString ( ).getAnnotationForPrintable (
        pExpression.getE ( ) ).getEndOffset ( ) ;
    int end = pExpression.toPrettyString ( ).toString ( ).length ( ) ;
    ArrayList < OutlinePair > outlinePairs = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER , start , end ) ;
    OutlinePair outlinePairId = outlinePairs.get ( 0 ) ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME , pExpression
        .getId ( ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Method}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Method}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMethod ( Method pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineNode outlineNode = new OutlineNode ( METHODNAME , pExpression
        .getId ( ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNode.setChildIndexMeth ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLambda}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMultiLambda (
      MultiLambda pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    OutlineNode outlineNode ;
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( pExpression , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: (λ(x, x).x) (1, 2).
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pExpression.getE ( ) , pExpression
            .getIdentifiers ( i ) ) ;
      }
      else
      {
        outlineBinding = null ;
      }
      outlineNode = new OutlineNode ( IDENTIFIER , pExpression
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexIdentifier ( i + 1 ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLet}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMultiLet ( MultiLet pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
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
      outlineBinding = new OutlineBinding ( pExpression , outlinePairId
          .getStart ( ) , outlinePairId.getEnd ( ) , i ) ;
      /*
       * An Identifier has no binding, if an Identifier after him has the same
       * name. Example: let(x, x) = (1, 2) in x.
       */
      boolean hasBinding = true ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          hasBinding = false ;
          break ;
        }
      }
      if ( hasBinding )
      {
        outlineBinding.find ( pExpression.getE2 ( ) , pExpression
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
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link ObjectExpr}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkObjectExpr ( ObjectExpr pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    Row row = pExpression.getE ( ) ;
    boolean equalIdFound = false ;
    for ( Expression expr : row.getExpressions ( ) )
    {
      if ( expr instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) expr ;
        if ( pExpression.getId ( ).equals ( attribute.getId ( ) ) )
        {
          equalIdFound = true ;
        }
      }
      else
      {
        if ( ! equalIdFound )
        {
          outlineBinding.find ( expr , pExpression.getId ( ) ) ;
        }
      }
    }
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , pExpression
        .getId ( ) , outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , pExpression.getTau ( )
          .toPrettyString ( ).toString ( ) , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 , null ,
          this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Recursion}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkRecursion ( Recursion pExpression )
  {
    String id = pExpression.getId ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pExpression ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pExpression.getE ( ) , pExpression.getId ( ) ) ;
    OutlineNode outlineNode = new OutlineNode ( IDENTIFIER , id ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNode.setChildIndexIdentifier ( ) ;
    node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNode = new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ;
      outlineNode.setChildIndexType ( ) ;
      node.add ( new DefaultMutableTreeNode ( outlineNode ) ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Row}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkRow ( Row pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    for ( int i = 0 ; i < pExpression.getExpressions ( ).length ; i ++ )
    {
      Expression currentChild = pExpression.getExpressions ( i ) ;
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
        for ( int j = i + 1 ; j < pExpression.getExpressions ( ).length ; j ++ )
        {
          Expression tmpChild = pExpression.getExpressions ( j ) ;
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
   * @param pNode The node where the children should be added.
   */
  private final void createChildren ( Expression pExpression ,
      DefaultMutableTreeNode pNode )
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
      pNode.add ( treeNode ) ;
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
    DefaultMutableTreeNode rootNode = checkExpression ( this.loadedExpression ) ;
    OutlineNode outlineNode = ( OutlineNode ) rootNode.getUserObject ( ) ;
    outlineNode.setChildIndexExpression ( ) ;
    repaint ( rootNode ) ;
    SwingUtilities.invokeLater ( new OutlineDisplayTree ( this.outlineUI ,
        rootNode ) ) ;
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
   * @param pNode The node, which should be repainted.
   */
  private final void repaint ( DefaultMutableTreeNode pNode )
  {
    this.outlineUI.getTreeModel ( ).nodeChanged ( pNode ) ;
    OutlineNode outlineNode = ( OutlineNode ) pNode.getUserObject ( ) ;
    outlineNode.resetCaption ( ) ;
    for ( int i = 0 ; i < pNode.getChildCount ( ) ; i ++ )
    {
      repaint ( ( DefaultMutableTreeNode ) pNode.getChildAt ( i ) ) ;
    }
  }
}
