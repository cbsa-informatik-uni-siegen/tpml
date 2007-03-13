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
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlinePair ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineStyle ;
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
   * Caption of the {@link Identifier}s.
   */
  private static final String IDENTIFIER = "Identifier" ; //$NON-NLS-1$


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
    // setEnabledUI ( false ) ;
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
   * @return The node, which represents the given {@link CurriedLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedLet ( CurriedLet pCurriedLet )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedLet ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
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
    outlineNodeId = new OutlineNode ( IDENTIFIER , pCurriedLet
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pCurriedLet
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i ) ;
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
        outlineNodeType = new OutlineNode ( pCurriedLet.getTypes ( i )
            .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
            outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            this.outlineUnbound ) ;
        outlineNodeType.setChildIndexType ( i ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
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
      outlineNodeType = new OutlineNode ( pCurriedLet.getTypes ( 0 )
          .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
          this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link CurriedLetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedLetRec ( CurriedLetRec pCurriedLetRec )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedLetRec ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
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
    outlineNodeId = new OutlineNode ( IDENTIFIER , pCurriedLetRec
        .getIdentifiers ( 0 ) , outlinePairId , outlineBinding ,
        this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pCurriedLetRec
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i ) ;
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
        outlineNodeType = new OutlineNode ( pCurriedLetRec.getTypes ( i )
            .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
            outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            this.outlineUnbound ) ;
        outlineNodeType.setChildIndexType ( i ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
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
      outlineNodeType = new OutlineNode ( pCurriedLetRec.getTypes ( 0 )
          .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
          this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link CurriedMethod}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkCurriedMethod ( CurriedMethod pCurriedMethod )
  {
    OutlineNode outlineNode = new OutlineNode ( pCurriedMethod ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeM ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pCurriedMethod , PrettyStyle.IDENTIFIER ) ;
    /*
     * Create the first Identifier.
     */
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    outlineNodeM = new OutlineNode ( METHODNAME , pCurriedMethod
        .getIdentifiers ( 0 ) , outlinePairId , null , this.outlineUnbound ) ;
    outlineNodeM.setChildIndexMeth ( OutlineNode.NO_BINDING ) ;
    outlineNode.add ( outlineNodeM ) ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pCurriedMethod
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i ) ;
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
        outlineNodeType = new OutlineNode ( pCurriedMethod.getTypes ( i )
            .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
            outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
            this.outlineUnbound ) ;
        outlineNodeType.setChildIndexType ( i ) ;
        outlineNodeId.add ( outlineNodeType ) ;
      }
      outlineNode.add ( outlineNodeId ) ;
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
      outlineNodeType = new OutlineNode ( pCurriedMethod.getTypes ( 0 )
          .getCaption ( ) , tau , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
          this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link Duplication}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkDuplication ( Duplication pDuplication )
  {
    OutlineNode outlineNode = new OutlineNode ( pDuplication ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeFirstE ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeE ;
    /*
     * Create the first Expression.
     */
    outlineNodeFirstE = checkExpression ( pDuplication.getE ( ) ) ;
    outlineNodeFirstE.setChildIndexExpression ( ) ;
    outlineNode.add ( outlineNodeFirstE ) ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pDuplication
          .getIdentifiers ( i ) , outlinePairId , null , this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i + 1 ) ;
      outlineNode.add ( outlineNodeId ) ;
      outlineNodeE = checkExpression ( pDuplication.getExpressions ( i ) ) ;
      outlineNodeE.setChildIndexExpression ( i + 1 ) ;
      outlineNode.add ( outlineNodeE ) ;
    }
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Expression}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Expression}.
   */
  private final OutlineNode checkExpression ( Expression pExpression )
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
    OutlineNode node = new OutlineNode ( pExpression , this.outlineUnbound ) ;
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
  private final OutlineNode checkInfixOperation ( InfixOperation pInfixOperation )
  {
    OutlineNode outlineNode = new OutlineNode ( pInfixOperation ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeE1 ;
    OutlineNode outlineNodeE2 ;
    OutlineNode outlineNodeOp ;
    /*
     * Create the first Expression.
     */
    outlineNodeE1 = checkExpression ( pInfixOperation.getE1 ( ) ) ;
    outlineNodeE1.setChildIndexExpression ( 1 ) ;
    outlineNode.add ( outlineNodeE1 ) ;
    /*
     * Create the Infix-Operator.
     */
    BinaryOperator binary = pInfixOperation.getOp ( ) ;
    int start = pInfixOperation.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) ,
        pInfixOperation.getE1 ( ).toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    outlineNodeOp = new OutlineNode ( binary , binary.toString ( ) , start ,
        end , this.outlineUnbound ) ;
    outlineNodeOp.setChildIndexOp ( ) ;
    outlineNode.add ( outlineNodeOp ) ;
    /*
     * Create the first Expression.
     */
    outlineNodeE2 = checkExpression ( pInfixOperation.getE2 ( ) ) ;
    outlineNodeE2.setChildIndexExpression ( 2 ) ;
    outlineNode.add ( outlineNodeE2 ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Lambda}.
   * 
   * @param pLambda The input {@link Expression}.
   * @return The node, which represents the given {@link Lambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLambda ( Lambda pLambda )
  {
    OutlineNode outlineNode = new OutlineNode ( pLambda , this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLambda ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLambda ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLambda.getE ( ) , pLambda.getId ( ) ) ;
    outlineNodeId = new OutlineNode ( IDENTIFIER , pLambda.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLambda.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLambda ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLambda.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pLambda.getTau ( ).getCaption ( ) ,
          tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link Let}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLet ( Let pLet )
  {
    OutlineNode outlineNode = new OutlineNode ( pLet , this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLet ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLet , outlinePairId
        .getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLet.getE2 ( ) , pLet.getId ( ) ) ;
    outlineNodeId = new OutlineNode ( IDENTIFIER , pLet.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLet.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLet ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLet.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pLet.getTau ( ).getCaption ( ) , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link LetRec}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkLetRec ( LetRec pLetRec )
  {
    OutlineNode outlineNode = new OutlineNode ( pLetRec , this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pLetRec ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pLetRec ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pLetRec.getE1 ( ) , pLetRec.getId ( ) ) ;
    outlineBinding.find ( pLetRec.getE2 ( ) , pLetRec.getId ( ) ) ;
    outlineNodeId = new OutlineNode ( IDENTIFIER , pLetRec.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pLetRec.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pLetRec ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pLetRec.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pLetRec.getTau ( ).getCaption ( ) ,
          tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
      outlineNode.add ( outlineNodeType ) ;
    }
    /*
     * Create the children of this node.
     */
    createChildren ( pLetRec , outlineNode ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Message}.
   * 
   * @param pMessage The input {@link Expression}.
   * @return The node, which represents the given {@link Message}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMessage ( Message pMessage )
  {
    OutlineNode outlineNode = new OutlineNode ( pMessage , this.outlineUnbound ) ;
    OutlineNode outlineNodeM ;
    /*
     * Create the children of this node.
     */
    createChildren ( pMessage , outlineNode ) ;
    /*
     * Create the Identifier.
     */
    int start = pMessage.toPrettyString ( ).getAnnotationForPrintable (
        pMessage.getE ( ) ).getEndOffset ( ) + 1 ;
    int end = pMessage.toPrettyString ( ).toString ( ).length ( ) ;
    ArrayList < OutlinePair > outlinePairs = OutlineStyle.getIndex ( pMessage ,
        PrettyStyle.IDENTIFIER , start , end ) ;
    OutlinePair outlinePairId = outlinePairs.get ( 0 ) ;
    outlineNodeM = new OutlineNode ( METHODNAME , pMessage.getId ( ) ,
        outlinePairId , null , this.outlineUnbound ) ;
    outlineNodeM.setChildIndexMeth ( ) ;
    outlineNode.add ( outlineNodeM ) ;
    return outlineNode ;
  }


  /**
   * Returns the node, which represents the given {@link Method}.
   * 
   * @param pMethod The input {@link Expression}.
   * @return The node, which represents the given {@link Method}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMethod ( Method pMethod )
  {
    OutlineNode outlineNode = new OutlineNode ( pMethod , this.outlineUnbound ) ;
    OutlineNode outlineNodeM ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pMethod ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    outlineNodeM = new OutlineNode ( METHODNAME , pMethod.getId ( ) ,
        outlinePairId , null , this.outlineUnbound ) ;
    outlineNodeM.setChildIndexMeth ( ) ;
    outlineNode.add ( outlineNodeM ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMethod.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMethod ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMethod.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pMethod.getTau ( ).getCaption ( ) ,
          tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link MultiLambda}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMultiLambda ( MultiLambda pMultiLambda )
  {
    OutlineNode outlineNode = new OutlineNode ( pMultiLambda ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pMultiLambda , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pMultiLambda
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i + 1 ) ;
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLambda.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMultiLambda ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMultiLambda.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode (
          pMultiLambda.getTau ( ).getCaption ( ) , tau , outlinePairType
              .getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
          this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link MultiLet}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkMultiLet ( MultiLet pMultiLet )
  {
    OutlineNode outlineNode = new OutlineNode ( pMultiLet , this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create all Identifiers.
     */
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pMultiLet , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    final int length = pMultiLet.getIdentifiers ( ).length ;
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
      outlineNodeId = new OutlineNode ( IDENTIFIER , pMultiLet
          .getIdentifiers ( i ) , outlinePairId , outlineBinding ,
          this.outlineUnbound ) ;
      outlineNodeId.setChildIndexIdentifier ( i + 1 ) ;
      outlineNode.add ( outlineNodeId ) ;
    }
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pMultiLet.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pMultiLet ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pMultiLet.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pMultiLet.getTau ( ).getCaption ( ) ,
          tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link ObjectExpr}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkObjectExpr ( ObjectExpr pObjectExpr )
  {
    OutlineNode outlineNode = new OutlineNode ( pObjectExpr ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pObjectExpr ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pObjectExpr ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pObjectExpr.getE ( ) , pObjectExpr.getId ( ) ) ;
    outlineNodeId = new OutlineNode ( IDENTIFIER , pObjectExpr.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pObjectExpr.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pObjectExpr ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pObjectExpr.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode (
          pObjectExpr.getTau ( ).getCaption ( ) , pObjectExpr.getTau ( )
              .toPrettyString ( ).toString ( ) , outlinePairType.getStart ( ) ,
          outlinePairType.getStart ( ) + tau.length ( ) - 1 ,
          this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link Recursion}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkRecursion ( Recursion pRecursion )
  {
    OutlineNode outlineNode = new OutlineNode ( pRecursion ,
        this.outlineUnbound ) ;
    OutlineNode outlineNodeId ;
    OutlineNode outlineNodeType ;
    /*
     * Create the Identifier.
     */
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pRecursion ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( pRecursion ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
    outlineBinding.find ( pRecursion.getE ( ) , pRecursion.getId ( ) ) ;
    outlineNodeId = new OutlineNode ( IDENTIFIER , pRecursion.getId ( ) ,
        outlinePairId , outlineBinding , this.outlineUnbound ) ;
    outlineNodeId.setChildIndexIdentifier ( ) ;
    outlineNode.add ( outlineNodeId ) ;
    /*
     * Create the Type of this Expression if it is not null.
     */
    if ( pRecursion.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pRecursion ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pRecursion.getTau ( ).toPrettyString ( ).toString ( ) ;
      outlineNodeType = new OutlineNode ( pRecursion.getTau ( ).getCaption ( ) ,
          tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
      outlineNodeType.setChildIndexType ( ) ;
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
   * @return The node, which represents the given {@link Row}.
   */
  @ SuppressWarnings ( "unused" )
  private final OutlineNode checkRow ( Row pRow )
  {
    OutlineNode outlineNode = new OutlineNode ( pRow , this.outlineUnbound ) ;
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
        outlineNodeAttr = new OutlineNode ( attribute , this.outlineUnbound ) ;
        outlineNodeAttr.setChildIndexExpression ( i + 1 ) ;
        OutlinePair outlinePairId = OutlineStyle.getIndex ( attribute ,
            PrettyStyle.IDENTIFIER ).get ( 0 ) ;
        OutlineBinding outlineBinding = new OutlineBinding ( attribute ,
            outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , 0 ) ;
        for ( int j = i + 1 ; j < pRow.getExpressions ( ).length ; j ++ )
        {
          Expression tmpChild = pRow.getExpressions ( j ) ;
          outlineBinding.find ( tmpChild , attribute.getId ( ) ) ;
          if ( ( tmpChild instanceof Attribute )
              && ( ( ( Attribute ) tmpChild ).getId ( ).equals ( attribute
                  .getId ( ) ) ) )
          {
            break ;
          }
        }
        outlineNodeId = new OutlineNode ( IDENTIFIER , attribute.getId ( ) ,
            outlinePairId , outlineBinding , this.outlineUnbound ) ;
        outlineNodeId.setChildIndexIdentifier ( ) ;
        outlineNodeAttr.add ( outlineNodeId ) ;
        if ( attribute.getTau ( ) != null )
        {
          OutlinePair outlinePairType = OutlineStyle.getIndex ( attribute ,
              PrettyStyle.TYPE ).get ( 0 ) ;
          String tau = attribute.getTau ( ).toPrettyString ( ).toString ( ) ;
          outlineNodeType = new OutlineNode ( attribute.getTau ( )
              .getCaption ( ) , attribute.getTau ( ).toPrettyString ( )
              .toString ( ) , outlinePairType.getStart ( ) , outlinePairType
              .getStart ( )
              + tau.length ( ) - 1 , this.outlineUnbound ) ;
          outlineNodeType.setChildIndexType ( ) ;
          outlineNodeAttr.add ( outlineNodeType ) ;
        }
        createChildren ( attribute , outlineNodeAttr ) ;
        outlineNode.add ( outlineNodeAttr ) ;
      }
      else if ( currentChild instanceof Method )
      {
        outlineNodeE = checkExpression ( currentChild ) ;
        outlineNodeE.setChildIndexExpression ( i + 1 ) ;
        outlineNode.add ( outlineNodeE ) ;
      }
      else if ( currentChild instanceof CurriedMethod )
      {
        outlineNodeE = checkExpression ( currentChild ) ;
        outlineNodeE.setChildIndexExpression ( i + 1 ) ;
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
    Enumeration < Expression > children = pExpression.children ( ) ;
    int childIndex = getChildIndex ( pExpression ) ;
    Expression child ;
    OutlineNode outlineNode ;
    while ( children.hasMoreElements ( ) )
    {
      child = children.nextElement ( ) ;
      outlineNode = checkExpression ( child ) ;
      if ( childIndex == OutlineNode.NO_CHILD_INDEX )
      {
        outlineNode.setChildIndexExpression ( OutlineNode.NO_CHILD_INDEX ) ;
      }
      else
      {
        outlineNode.setChildIndexExpression ( childIndex ) ;
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
    this.rootOutlineNode = checkExpression ( this.loadedExpression ) ;
    this.rootOutlineNode.setChildIndexExpression ( ) ;
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
        // setEnabledUI ( false ) ;
        this.outlineUI.setError ( true ) ;
        // this.loadedExpression = null ;
        // this.outlineUI.setRootNode ( null ) ;
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
    pOutlineNode.resetCaption ( ) ;
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
    pOutlineNode.resetCaption ( ) ;
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
