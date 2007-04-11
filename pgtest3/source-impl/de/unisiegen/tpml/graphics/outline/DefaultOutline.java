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
import de.unisiegen.tpml.core.expressions.Attribute ;
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
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.RowType ;
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
@ SuppressWarnings (
{ "nls" , "nls" } )
public final class DefaultOutline implements Outline
{
  /**
   * The <code>String</code> for an array of children.
   */
  private static final String GET_E_N = "getExpressions" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for an array of children.
   */
  private static final String GET_TAU_N = "getTypes" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GET_E_X = "getE[0-9]+" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GET_TAU_X = "getTau[0-9]+" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GET_PHI_X = "getPhi[0-9]+" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GET_E = "getE" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GET_TAU = "getTau" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GET_PHI = "getPhi" ; //$NON-NLS-1$


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
   * Returns the node, which represents the given {@link CurriedLet}.
   * 
   * @param pCurriedLet The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedLet ( CurriedLet pCurriedLet ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedLet ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the first Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pCurriedLet
        .getBoundedIdentifiers ( 0 ) ) ;
    outlineNodeId = new OutlineNode ( pCurriedLet.getIdentifiers ( 0 ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pCurriedLet.getIdentifiers ( ).length ;
    int start = 0 ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineBinding = new OutlineBinding ( pCurriedLet
          .getBoundedIdentifiers ( i ) ) ;
      outlineNodeId = new OutlineNode ( pCurriedLet.getIdentifiers ( i ) , i ,
          outlineBinding ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedLet.getTypes ( i ) != null )
      {
        outlineNodeType = new OutlineNode ( pCurriedLet.getTypes ( i ) , i ,
            false ) ;
        createChildren ( pCurriedLet.getTypes ( i ) , outlineNodeType ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedLet.getTypes ( 0 ) != null )
    {
      outlineNodeType = new OutlineNode ( pCurriedLet.getTypes ( 0 ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pCurriedLet.getTypes ( 0 ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedLet , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedLetRec}.
   * 
   * @param pCurriedLetRec The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedLetRec ( CurriedLetRec pCurriedLetRec ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedLetRec ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the first Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pCurriedLetRec
        .getBoundedIdentifiers ( 0 ) ) ;
    outlineNodeId = new OutlineNode ( pCurriedLetRec.getIdentifiers ( 0 ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create all other Identifiers and Types.
     */
    final int length = pCurriedLetRec.getIdentifiers ( ).length ;
    int start = 0 ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineBinding = new OutlineBinding ( pCurriedLetRec
          .getBoundedIdentifiers ( i ) ) ;
      outlineNodeId = new OutlineNode ( pCurriedLetRec.getIdentifiers ( i ) ,
          i , outlineBinding ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedLetRec.getTypes ( i ) != null )
      {
        outlineNodeType = new OutlineNode ( pCurriedLetRec.getTypes ( i ) , i ,
            false ) ;
        createChildren ( pCurriedLetRec.getTypes ( i ) , outlineNodeType ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedLetRec.getTypes ( 0 ) != null )
    {
      outlineNodeType = new OutlineNode ( pCurriedLetRec.getTypes ( 0 ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pCurriedLetRec.getTypes ( 0 ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedLetRec , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedMethod}.
   * 
   * @param pCurriedMethod The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedMethod ( CurriedMethod pCurriedMethod ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedMethod ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the first Identifier.
     */
    outlineNodeId = new OutlineNode ( pCurriedMethod.getIdentifiers ( 0 ) ,
        OutlineNode.NO_CHILD_INDEX , null ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create all other Identifiers and Types.
     */
    OutlineBinding outlineBinding ;
    final int length = pCurriedMethod.getIdentifiers ( ).length ;
    int start = 0 ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineBinding = new OutlineBinding ( pCurriedMethod
          .getBoundedIdentifiers ( i ) ) ;
      outlineNodeId = new OutlineNode ( pCurriedMethod.getIdentifiers ( i ) ,
          i , outlineBinding ) ;
      /*
       * Create the Type of this Identifier if it is not null.
       */
      if ( pCurriedMethod.getTypes ( i ) != null )
      {
        outlineNodeType = new OutlineNode ( pCurriedMethod.getTypes ( i ) , i ,
            false ) ;
        createChildren ( pCurriedMethod.getTypes ( i ) , outlineNodeType ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pCurriedMethod.getTypes ( 0 ) != null )
    {
      outlineNodeType = new OutlineNode ( pCurriedMethod.getTypes ( 0 ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pCurriedMethod.getTypes ( 0 ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pCurriedMethod , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Duplication}.
   * 
   * @param pDuplication The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Duplication}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkDuplication ( Duplication pDuplication ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pDuplication ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeFirstE ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeE ;
    /*
     * Create the first Expression.
     */
    outlineNodeFirstE = checkExpression ( pDuplication.getE ( ) , new Integer (
        OutlineNode.NO_CHILD_INDEX ) ) ;
    outlineNode.add ( outlineNodeFirstE ) ;
    /*
     * Create all Identifiers.
     */
    for ( int i = 0 ; i < pDuplication.getIdentifiers ( ).length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineNodeId = new OutlineNode ( pDuplication.getIdentifiers ( i ) ,
          i + 1 , null ) ;
      outlineNode.add ( outlineNodeId ) ;
      /*
       * Create the current Expression.
       */
      outlineNodeE = checkExpression ( pDuplication.getExpressions ( i ) ,
          new Integer ( i + 1 ) ) ;
      outlineNode.add ( outlineNodeE ) ;
    }
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Expression}.
   */
  private final OutlineNode checkExpression ( Expression pExpression ,
      Integer pChildIndex )
  {
    for ( java.lang.reflect.Method method : this.getClass ( )
        .getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          CHECK + pExpression.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 2 ] ;
          argument [ 0 ] = pExpression ;
          argument [ 1 ] = pChildIndex ;
          return ( OutlineNode ) method.invoke ( this , argument ) ;
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
    OutlineNode node = new OutlineNode ( pExpression , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
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
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link InfixOperation}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkInfixOperation (
      InfixOperation pInfixOperation , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pInfixOperation ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    /*
     * Create the first Expression.
     */
    OutlineNode outlineNodeE1 = checkExpression ( pInfixOperation.getE1 ( ) ,
        new Integer ( 1 ) ) ;
    outlineNode.add ( outlineNodeE1 ) ;
    /*
     * Create the Infix-Operator.
     */
    OutlineNode outlineNodeOp = checkExpression ( pInfixOperation.getOp ( ) ,
        new Integer ( OutlineNode.NO_CHILD_INDEX ) ) ;
    outlineNode.add ( outlineNodeOp ) ;
    /*
     * Create the first Expression.
     */
    OutlineNode outlineNodeE2 = checkExpression ( pInfixOperation.getE2 ( ) ,
        new Integer ( 2 ) ) ;
    outlineNode.add ( outlineNodeE2 ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Lambda}.
   * 
   * @param pLambda The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLambda ( Lambda pLambda , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pLambda , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pLambda
        .getBoundedId ( ) ) ;
    outlineNodeId = new OutlineNode ( pLambda.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLambda.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pLambda.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pLambda.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLambda , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Let}.
   * 
   * @param pLet The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLet ( Let pLet , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pLet , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pLet.getBoundedId ( ) ) ;
    outlineNodeId = new OutlineNode ( pLet.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLet.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pLet.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pLet.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLet , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link LetRec}.
   * 
   * @param pLetRec The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLetRec ( LetRec pLetRec , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pLetRec , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pLetRec
        .getBoundedId ( ) ) ;
    outlineNodeId = new OutlineNode ( pLetRec.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLetRec.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pLetRec.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pLetRec.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLetRec , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Method}.
   * 
   * @param pMethod The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Method}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMethod ( Method pMethod , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pMethod , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    outlineNodeId = new OutlineNode ( pMethod.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , null ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMethod.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pMethod.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pMethod.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMethod , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLambda}.
   * 
   * @param pMultiLambda The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMultiLambda ( MultiLambda pMultiLambda ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pMultiLambda ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create all Identifiers.
     */
    OutlineBinding outlineBinding ;
    final int length = pMultiLambda.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineBinding = new OutlineBinding ( pMultiLambda
          .getBoundedIdentifiers ( i ) ) ;
      outlineNodeId = new OutlineNode ( pMultiLambda.getIdentifiers ( i ) ,
          i + 1 , outlineBinding ) ;
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLambda.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pMultiLambda.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pMultiLambda.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMultiLambda , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link MultiLet}.
   * 
   * @param pMultiLet The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMultiLet ( MultiLet pMultiLet ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pMultiLet ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create all Identifiers.
     */
    OutlineBinding outlineBinding ;
    final int length = pMultiLet.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      /*
       * Create the current Identifier.
       */
      outlineBinding = new OutlineBinding ( pMultiLet
          .getBoundedIdentifiers ( i ) ) ;
      outlineNodeId = new OutlineNode ( pMultiLet.getIdentifiers ( i ) , i + 1 ,
          outlineBinding ) ;
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLet.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pMultiLet.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pMultiLet.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pMultiLet , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link ObjectExpr}.
   * 
   * @param pObjectExpr The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkObjectExpr ( ObjectExpr pObjectExpr ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pObjectExpr ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pObjectExpr
        .getBoundedId ( ) ) ;
    outlineNodeId = new OutlineNode ( pObjectExpr.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pObjectExpr.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pObjectExpr.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pObjectExpr.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pObjectExpr , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Recursion}.
   * 
   * @param pRecursion The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkRecursion ( Recursion pRecursion ,
      Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pRecursion ,
        this.outlineUnbound , pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlineBinding outlineBinding = new OutlineBinding ( pRecursion
        .getBoundedId ( ) ) ;
    outlineNodeId = new OutlineNode ( pRecursion.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pRecursion.getTau ( ) != null )
    {
      outlineNodeType = new OutlineNode ( pRecursion.getTau ( ) ,
          OutlineNode.NO_CHILD_INDEX , false ) ;
      createChildren ( pRecursion.getTau ( ) , outlineNodeType ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pRecursion , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Row}.
   * 
   * @param pRow The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkRow ( Row pRow , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pRow , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    OutlineNode outlineNodeAttr ;
    OutlineNode outlineNodeE ;
    for ( int i = 0 ; i < pRow.getExpressions ( ).length ; i ++ )
    {
      Expression currentChild = pRow.getExpressions ( i ) ;
      if ( currentChild instanceof Attribute )
      {
        Attribute attribute = ( Attribute ) currentChild ;
        outlineNodeAttr = new OutlineNode ( attribute , this.outlineUnbound ,
            i + 1 ) ;
        OutlineBinding outlineBinding = new OutlineBinding ( pRow
            .getBoundedIdentifiers ( i ) ) ;
        outlineNodeId = new OutlineNode ( attribute.getId ( ) ,
            OutlineNode.NO_CHILD_INDEX , outlineBinding ) ;
        outlineNodeAttr.add ( outlineNodeId ) ;
        if ( attribute.getTau ( ) != null )
        {
          outlineNodeType = new OutlineNode ( attribute.getTau ( ) ,
              OutlineNode.NO_CHILD_INDEX , false ) ;
          outlineNodeAttr.add ( outlineNodeType ) ;
        }
        createChildren ( attribute , outlineNodeAttr ) ;
        outlineNode.add ( outlineNodeAttr ) ;
      }
      else if ( currentChild instanceof Method )
      {
        outlineNodeE = checkExpression ( currentChild , new Integer ( i + 1 ) ) ;
        outlineNode.add ( outlineNodeE ) ;
      }
      else if ( currentChild instanceof CurriedMethod )
      {
        outlineNodeE = checkExpression ( currentChild , new Integer ( i + 1 ) ) ;
        outlineNode.add ( outlineNodeE ) ;
      }
      else
      {
        /*
         * Programming error: The child of the Row is not an Attribute, Method
         * or CurriedMethod. This should not happen.
         */
        throw new IllegalStateException ( "Inconsistent DefaultOutline class." ) ; //$NON-NLS-1$
      }
    }
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Send}.
   * 
   * @param pSend The input {@link Expression}.
   * @param pChildIndex The child index.
   * @return The node, which represents the given {@link Send}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkSend ( Send pSend , Integer pChildIndex )
  {
    OutlineNode outlineNode = new OutlineNode ( pSend , this.outlineUnbound ,
        pChildIndex.intValue ( ) ) ;
    OutlineNode outlineNodeId ;
    /*
     * Create the children of this node.
     */
    createChildren ( pSend , outlineNode ) ;
    /*
     * Create the Identifier.
     */
    outlineNodeId = new OutlineNode ( pSend.getId ( ) ,
        OutlineNode.NO_CHILD_INDEX , null ) ;
    outlineNode.add ( outlineNodeId ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link RowType}.
   * 
   * @param pRowType The input {@link RowType}.
   * @param pChildIndex The child index.
   * @param isPhi True if the {@link Type} is a phi, false if it is a tau.
   * @return The node, which represents the given {@link RowType}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkRowType ( RowType pRowType ,
      Integer pChildIndex , Boolean isPhi )
  {
    OutlineNode outlineNode = new OutlineNode ( pRowType , pChildIndex
        .intValue ( ) , isPhi.booleanValue ( ) ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    for ( int i = 0 ; i < pRowType.getTypes ( ).length ; i ++ )
    {
      /*
       * Create the Identifier.
       */
      outlineNodeId = new OutlineNode ( pRowType.getIdentifiers ( i ) , i + 1 ,
          null ) ;
      outlineNode.add ( outlineNodeId ) ;
      /*
       * Create the Type.
       */
      outlineNodeType = checkType ( pRowType.getTypes ( i ) , new Integer (
          i + 1 ) , Boolean.FALSE ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Type}.
   * 
   * @param pType The input {@link Type}.
   * @param pChildIndex The child index.
   * @param isPhi True if the {@link Type} is a phi, false if it is a tau.
   * @return The node, which represents the given {@link Type}.
   */
  private final OutlineNode checkType ( Type pType , Integer pChildIndex ,
      Boolean isPhi )
  {
    for ( java.lang.reflect.Method method : this.getClass ( )
        .getDeclaredMethods ( ) )
    {
      if ( method.getName ( ).equals (
          CHECK + pType.getClass ( ).getSimpleName ( ) ) )
      {
        try
        {
          Object [ ] argument = new Object [ 3 ] ;
          argument [ 0 ] = pType ;
          argument [ 1 ] = pChildIndex ;
          argument [ 2 ] = isPhi ;
          return ( OutlineNode ) method.invoke ( this , argument ) ;
        }
        catch ( IllegalArgumentException e )
        {
          System.err.println ( "IllegalArgumentException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pType.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "IllegalAccessException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pType.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "InvocationTargetException: " //$NON-NLS-1$
              + this.getClass ( ).getCanonicalName ( ) + "." + CHECK //$NON-NLS-1$
              + pType.getClass ( ).getSimpleName ( ) ) ;
        }
      }
    }
    OutlineNode node = new OutlineNode ( pType , OutlineNode.NO_CHILD_INDEX ,
        isPhi.booleanValue ( ) ) ;
    /*
     * Create the children of this node.
     */
    createChildren ( pType , node ) ;
    return node ;
  }


  /**
   * Creates the children with the given {@link Expression} and adds them to the
   * given node.
   * 
   * @param pExpression The {@link Expression}, with which the children should
   *          be created.
   * @param pOutlineNode The node where the children should be added.
   */
  private final void createChildren ( Expression pExpression ,
      OutlineNode pOutlineNode )
  {
    ArrayList < Expression > children = pExpression.children ( ) ;
    int childIndex = getChildIndex ( pExpression ) ;
    OutlineNode outlineNode ;
    for ( Expression child : children )
    {
      outlineNode = checkExpression ( child , new Integer ( childIndex ) ) ;
      pOutlineNode.add ( outlineNode ) ;
      childIndex ++ ;
    }
  }


  /**
   * Creates the children with the given {@link Type} and adds them to the given
   * node.
   * 
   * @param pType The {@link Type}, with which the children should be created.
   * @param pOutlineNode The node where the children should be added.
   */
  private final void createChildren ( Type pType , OutlineNode pOutlineNode )
  {
    ArrayList < Type > children = pType.children ( ) ;
    int childIndex = getChildIndexTau ( pType ) ;
    boolean phi = false ;
    if ( childIndex == OutlineNode.NOTHING_FOUND )
    {
      childIndex = getChildIndexPhi ( pType ) ;
      phi = true ;
    }
    OutlineNode outlineNode ;
    for ( Type child : children )
    {
      if ( phi )
      {
        outlineNode = checkType ( child , new Integer ( childIndex ) ,
            Boolean.TRUE ) ;
      }
      else
      {
        outlineNode = checkType ( child , new Integer ( childIndex ) ,
            Boolean.FALSE ) ;
      }
      pOutlineNode.add ( outlineNode ) ;
      childIndex ++ ;
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
    this.rootOutlineNode = checkExpression ( this.loadedExpression ,
        new Integer ( OutlineNode.NO_CHILD_INDEX ) ) ;
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
      if ( GET_E.equals ( method.getName ( ) ) )
      {
        return OutlineNode.NO_CHILD_INDEX ;
      }
      if ( GET_E_N.equals ( method.getName ( ) ) )
      {
        return 1 ;
      }
      if ( method.getName ( ).matches ( GET_E_X ) )
      {
        result = Math.min ( result , Integer.parseInt ( method.getName ( )
            .substring ( 4 ) ) ) ;
      }
    }
    return result == Integer.MAX_VALUE ? OutlineNode.NOTHING_FOUND : result ;
  }


  /**
   * Returns the minimum child index. For example 1 if the {@link Type} is an
   * instance of {@link ArrowType}.
   * 
   * @param pType The {@link Type} to check for.
   * @return The minimum child index.
   */
  private final int getChildIndexPhi ( Type pType )
  {
    int result = Integer.MAX_VALUE ;
    for ( java.lang.reflect.Method method : pType.getClass ( ).getMethods ( ) )
    {
      if ( GET_PHI.equals ( method.getName ( ) ) )
      {
        return OutlineNode.NO_CHILD_INDEX ;
      }
      if ( method.getName ( ).matches ( GET_PHI_X ) )
      {
        result = Math.min ( result , Integer.parseInt ( method.getName ( )
            .substring ( 6 ) ) ) ;
      }
    }
    return result == Integer.MAX_VALUE ? OutlineNode.NOTHING_FOUND : result ;
  }


  /**
   * Returns the minimum child index. For example 1 if the {@link Type} is an
   * instance of {@link ArrowType}.
   * 
   * @param pType The {@link Type} to check for.
   * @return The minimum child index.
   */
  private final int getChildIndexTau ( Type pType )
  {
    int result = Integer.MAX_VALUE ;
    for ( java.lang.reflect.Method method : pType.getClass ( ).getMethods ( ) )
    {
      if ( GET_TAU.equals ( method.getName ( ) ) )
      {
        return OutlineNode.NO_CHILD_INDEX ;
      }
      if ( GET_TAU_N.equals ( method.getName ( ) ) )
      {
        return 1 ;
      }
      if ( method.getName ( ).matches ( GET_TAU_X ) )
      {
        result = Math.min ( result , Integer.parseInt ( method.getName ( )
            .substring ( 6 ) ) ) ;
      }
    }
    return result == Integer.MAX_VALUE ? OutlineNode.NOTHING_FOUND : result ;
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
