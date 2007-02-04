package de.unisiegen.tpml.graphics.outline ;


import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.Timer ;
import javax.swing.JPanel ;
import javax.swing.SwingUtilities ;
import javax.swing.tree.DefaultMutableTreeNode ;
import de.unisiegen.tpml.core.expressions.Attr ;
import de.unisiegen.tpml.core.expressions.BinaryOperator ;
import de.unisiegen.tpml.core.expressions.Condition ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.CurriedMeth ;
import de.unisiegen.tpml.core.expressions.DuplicatedRow ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.InfixOperation ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.Location ;
import de.unisiegen.tpml.core.expressions.Message ;
import de.unisiegen.tpml.core.expressions.Meth ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.prettyprinter.PrettyStyle ;
import de.unisiegen.tpml.core.types.Type ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineBinding ;
import de.unisiegen.tpml.graphics.outline.binding.OutlinePair ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineStyle ;
import de.unisiegen.tpml.graphics.outline.binding.OutlineUnbound ;
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
   * The <code>String</code> for the name of a {@link Location}.
   */
  private static final String NAME = "Name" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the name of a {@link Meth}.
   */
  private static final String METHODNAME = "Method-Name" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for more than one child.
   */
  private static final String GETEX = "getE[0-9]{1}" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for only one child.
   */
  private static final String GETE = "getE" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the beginning of the check methods.
   */
  private static final String CHECK = "check" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the integer one.
   */
  private static final String ONE = "1" ; //$NON-NLS-1$


  /**
   * The <code>String</code> for the integer two.
   */
  private static final String TWO = "2" ; //$NON-NLS-1$


  /**
   * String, between the description of the parent node, like e1, and the
   * description of the current node, like Identifier.
   */
  private static final String BETWEEN = "  -  " ; //$NON-NLS-1$


  /**
   * Indicates, that an {@link Expression} has only one child.
   */
  private static final int ONLY_ONE_CHILD = - 1 ;


  /**
   * Caption of the {@link Expression}.
   */
  private static final String EXPRESSION = "e" ; //$NON-NLS-1$


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
   * The old {@link Expression} to check if the {@link Expression} has changed.
   */
  private Expression oldExpression ;


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
    this.oldExpression = null ;
    this.outlinePreferences = new OutlinePreferences ( ) ;
    this.outlineUI = new OutlineUI ( this ) ;
  }


  /**
   * Returns the node, which represents the given {@link Attr}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Attr}.
   */
  /*
   * @ SuppressWarnings ( "unused" ) private final DefaultMutableTreeNode
   * checkAttr ( Attr pExpression ) { DefaultMutableTreeNode node = new
   * DefaultMutableTreeNode ( new OutlineNode ( pExpression ,
   * this.outlineUnbound ) ) ; OutlinePair outlinePair = OutlineStyle.getIndex (
   * pExpression , PrettyStyle.IDENTIFIER ).get ( 0 ) ; node.add ( new
   * DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
   * pExpression.getIdentifier ( ) , outlinePair.getStart ( ) , outlinePair
   * .getEnd ( ) , null , this.outlineUnbound ) ) ) ; if ( pExpression.getTau ( ) !=
   * null ) { OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
   * PrettyStyle.TYPE ).get ( 0 ) ; String tau = pExpression.getTau (
   * ).toPrettyString ( ).toString ( ) ; node.add ( new DefaultMutableTreeNode (
   * new OutlineNode ( TYPE , pExpression.getTau ( ).toPrettyString ( ).toString ( ) ,
   * outlinePairType.getStart ( ) , outlinePairType.getStart ( ) + tau.length ( ) -
   * 1 , null , this.outlineUnbound ) ) ) ; } createChildren ( pExpression ,
   * node ) ; return node ; }
   */
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
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression
        .getIdentifiers ( 0 ) ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pExpression.getIdentifiers ( 0 ) , outlinePairId.getStart ( ) ,
        outlinePairId.getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE1 ( ) , pExpression
          .getIdentifiers ( i ) ) ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          outlineBinding = null ;
        }
      }
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          new OutlineNode ( IDENTIFIER , pExpression.getIdentifiers ( i ) ,
              outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) ,
              outlineBinding , this.outlineUnbound ) ) ;
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
        identifier.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE ,
            tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
                + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
      }
      node.add ( identifier ) ;
    }
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
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
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
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pExpression.getIdentifiers ( 0 ) , outlinePairId.getStart ( ) ,
        outlinePairId.getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE1 ( ) , pExpression
          .getIdentifiers ( i ) ) ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          outlineBinding = null ;
        }
      }
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          new OutlineNode ( IDENTIFIER , pExpression.getIdentifiers ( i ) ,
              outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) ,
              outlineBinding , this.outlineUnbound ) ) ;
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE1 ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
        identifier.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE ,
            tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
                + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
      }
      node.add ( identifier ) ;
    }
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
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link CurriedMeth}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link CurriedMeth}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkCurriedMeth (
      CurriedMeth pExpression )
  {
    String [ ] idList = pExpression.getIdentifiers ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId = outlinePairIdList.get ( 0 ) ;
    OutlinePair outlinePairType = null ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        idList [ 0 ] , outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) ,
        null , this.outlineUnbound ) ) ) ;
    OutlineBinding outlineBinding ;
    final int length = idList.length ;
    for ( int i = 1 ; i < length ; i ++ )
    {
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE ( ) , pExpression
          .getIdentifiers ( i ) ) ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          outlineBinding = null ;
        }
      }
      DefaultMutableTreeNode identifier = new DefaultMutableTreeNode (
          new OutlineNode ( IDENTIFIER , idList [ i ] , outlinePairId
              .getStart ( ) , outlinePairId.getEnd ( ) , outlineBinding ,
              this.outlineUnbound ) ) ;
      if ( pExpression.getTypes ( i ) != null )
      {
        int start = outlinePairId.getEnd ( ) ;
        int end = i == length - 1 ? pExpression.toPrettyString ( )
            .getAnnotationForPrintable ( pExpression.getE ( ) )
            .getStartOffset ( ) : outlinePairIdList.get ( i + 1 ).getStart ( ) ;
        outlinePairType = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.TYPE , start , end ).get ( 0 ) ;
        String tau = pExpression.getTypes ( i ).toPrettyString ( ).toString ( ) ;
        identifier.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE ,
            tau , outlinePairType.getStart ( ) , outlinePairType.getStart ( )
                + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
      }
      node.add ( identifier ) ;
    }
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
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link DuplicatedRow}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link DuplicatedRow}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkDuplicatedRow (
      DuplicatedRow pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = null ;
    OutlineNode outlineNode ;
    int start ;
    int end ;
    for ( int i = 0 ; i < pExpression.getIdentifiers ( ).length ; i ++ )
    {
      if ( i == 0 )
      {
        outlinePair = OutlineStyle.getIndex (
            pExpression ,
            PrettyStyle.IDENTIFIER ,
            0 ,
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
        outlinePair = OutlineStyle.getIndex ( pExpression ,
            PrettyStyle.IDENTIFIER , start , end ).get ( 0 ) ;
      }
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          pExpression.getIdentifiers ( i ) , outlinePair.getStart ( ) ,
          outlinePair.getEnd ( ) , null , this.outlineUnbound ) ) ) ;
      DefaultMutableTreeNode treeNode ;
      treeNode = checkExpression ( pExpression.getExpressions ( i ) ) ;
      outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
      outlineNode.appendDescription ( EXPRESSION + ( i + 1 ) + BETWEEN ) ;
      outlineNode.resetCaption ( ) ;
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
    for ( Method method : this.getClass ( ).getDeclaredMethods ( ) )
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
          System.err.println ( "IllegalArgumentException: " + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( IllegalAccessException e )
        {
          System.err.println ( "IllegalAccessException: " + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
        catch ( InvocationTargetException e )
        {
          System.err.println ( "InvocationTargetException: " + CHECK //$NON-NLS-1$
              + pExpression.getClass ( ).getSimpleName ( ) ) ;
        }
      }
    }
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
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
    Expression e1 = pExpression.getE1 ( ) ;
    Expression e2 = pExpression.getE2 ( ) ;
    BinaryOperator binary = pExpression.getOp ( ) ;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    DefaultMutableTreeNode node0 = checkExpression ( e1 ) ;
    OutlineNode astNode0 = ( OutlineNode ) node0.getUserObject ( ) ;
    astNode0.appendDescription ( EXPRESSION + ONE + BETWEEN ) ;
    astNode0.resetCaption ( ) ;
    node.add ( node0 ) ;
    int start = pExpression.toPrettyString ( ).toString ( ).indexOf (
        binary.toString ( ) , e1.toPrettyString ( ).toString ( ).length ( ) ) ;
    int end = start + binary.toString ( ).length ( ) - 1 ;
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode (
        new OutlineNode ( binary , binary.toString ( ) , start , end , null ,
            this.outlineUnbound ) ) ;
    node.add ( node1 ) ;
    DefaultMutableTreeNode ex2 = checkExpression ( e2 ) ;
    OutlineNode node2 = ( OutlineNode ) ex2.getUserObject ( ) ;
    node2.appendDescription ( EXPRESSION + TWO + BETWEEN ) ;
    node2.resetCaption ( ) ;
    node.add ( ex2 ) ;
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
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
    outlineBinding.find ( pExpression.getE ( ) , pExpression.getId ( ) ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pExpression.getId ( ) , outlinePairId.getStart ( ) , outlinePairId
            .getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression.getId ( ) ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pExpression.getId ( ) , outlinePairId.getStart ( ) , outlinePairId
            .getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
    outlineBinding.find ( pExpression.getE1 ( ) , pExpression.getId ( ) ) ;
    outlineBinding.find ( pExpression.getE2 ( ) , pExpression.getId ( ) ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
        pExpression.getId ( ) , outlinePairId.getStart ( ) , outlinePairId
            .getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
    createChildren ( pExpression , node ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Location}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Location}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkLocation ( Location pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    int start = 0 ;
    int end = start - 1 + pExpression.getName ( ).length ( ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( NAME ,
        pExpression.getName ( ) , start , end , null , this.outlineUnbound ) ) ) ;
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
    int start = pExpression.toPrettyString ( ).getAnnotationForPrintable (
        pExpression.getE ( ) ).getEndOffset ( ) ;
    int end = pExpression.toPrettyString ( ).toString ( ).length ( ) ;
    ArrayList < OutlinePair > outlinePairs = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER , start , end ) ;
    OutlinePair outlinePair = outlinePairs.get ( 0 ) ;
    createChildren ( pExpression , node ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( METHODNAME ,
        pExpression.getId ( ) , outlinePair.getStart ( ) , outlinePair
            .getEnd ( ) , null , this.outlineUnbound ) ) ) ;
    return node ;
  }


  /**
   * Returns the node, which represents the given {@link Meth}.
   * 
   * @param pExpression The input {@link Expression}.
   * @return The node, which represents the given {@link Meth}.
   */
  @ SuppressWarnings ( "unused" )
  private final DefaultMutableTreeNode checkMeth ( Meth pExpression )
  {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode ( new OutlineNode (
        pExpression , this.outlineUnbound ) ) ;
    OutlinePair outlinePair = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( METHODNAME ,
        pExpression.getId ( ) , outlinePair.getStart ( ) , outlinePair
            .getEnd ( ) , null , this.outlineUnbound ) ) ) ;
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    final int length = pExpression.getIdentifiers ( ).length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE ( ) , pExpression
          .getIdentifiers ( i ) ) ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          outlineBinding = null ;
        }
      }
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          pExpression.getIdentifiers ( i ) , outlinePairId.getStart ( ) ,
          outlinePairId.getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    }
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    ArrayList < OutlinePair > outlinePairIdList = OutlineStyle.getIndex (
        pExpression , PrettyStyle.IDENTIFIER ) ;
    OutlinePair outlinePairId ;
    OutlineBinding outlineBinding ;
    final int length = idList.length ;
    for ( int i = 0 ; i < length ; i ++ )
    {
      outlinePairId = outlinePairIdList.get ( i ) ;
      outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE2 ( ) , pExpression
          .getIdentifiers ( i ) ) ;
      for ( int j = i + 1 ; j < length ; j ++ )
      {
        if ( pExpression.getIdentifiers ( i ).equals (
            pExpression.getIdentifiers ( j ) ) )
        {
          outlineBinding = null ;
        }
      }
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          idList [ i ] , outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) ,
          outlineBinding , this.outlineUnbound ) ) ) ;
    }
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    ArrayList < OutlinePair > list = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ) ;
    if ( ( ! pExpression.getId ( ).equals ( "self" ) ) && ( list.size ( ) > 0 ) ) //$NON-NLS-1$
    {
      OutlinePair outlinePair = list.get ( 0 ) ;
      OutlineBinding outlineBinding = new OutlineBinding ( ) ;
      outlineBinding.find ( pExpression.getE ( ) , pExpression.getId ( ) ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER ,
          pExpression.getId ( ) , outlinePair.getStart ( ) , outlinePair
              .getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
    }
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE ,
          pExpression.getTau ( ).toPrettyString ( ).toString ( ) ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
    OutlinePair outlinePairId = OutlineStyle.getIndex ( pExpression ,
        PrettyStyle.IDENTIFIER ).get ( 0 ) ;
    OutlineBinding outlineBinding = new OutlineBinding ( ) ;
    outlineBinding.find ( pExpression.getE ( ) , pExpression.getId ( ) ) ;
    node.add ( new DefaultMutableTreeNode ( new OutlineNode ( IDENTIFIER , id ,
        outlinePairId.getStart ( ) , outlinePairId.getEnd ( ) , outlineBinding ,
        this.outlineUnbound ) ) ) ;
    if ( pExpression.getTau ( ) != null )
    {
      OutlinePair outlinePairType = OutlineStyle.getIndex ( pExpression ,
          PrettyStyle.TYPE ).get ( 0 ) ;
      String tau = pExpression.getTau ( ).toPrettyString ( ).toString ( ) ;
      node.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE , tau ,
          outlinePairType.getStart ( ) , outlinePairType.getStart ( )
              + tau.length ( ) - 1 , null , this.outlineUnbound ) ) ) ;
    }
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
      if ( pExpression.getExpressions ( i ) instanceof Attr )
      {
        Attr attr = ( Attr ) pExpression.getExpressions ( i ) ;
        OutlineNode outlineNode = new OutlineNode ( attr , this.outlineUnbound ) ;
        outlineNode.appendDescription ( EXPRESSION + ( i + 1 ) + BETWEEN ) ;
        outlineNode.resetCaption ( ) ;
        DefaultMutableTreeNode nodeAttr = new DefaultMutableTreeNode (
            outlineNode ) ;
        OutlineBinding outlineBinding = new OutlineBinding ( ) ;
        for ( int j = i + 1 ; j < pExpression.getExpressions ( ).length ; j ++ )
        {
          Expression child = pExpression.getExpressions ( j ) ;
          outlineBinding.find ( child , attr.getId ( ) ) ;
          if ( ( child instanceof Attr )
              && ( ( ( Attr ) child ).getId ( ).equals ( attr.getId ( ) ) ) )
          {
            break ;
          }
        }
        OutlinePair outlinePair = OutlineStyle.getIndex ( attr ,
            PrettyStyle.IDENTIFIER ).get ( 0 ) ;
        nodeAttr.add ( new DefaultMutableTreeNode ( new OutlineNode (
            IDENTIFIER , attr.getId ( ) , outlinePair.getStart ( ) ,
            outlinePair.getEnd ( ) , outlineBinding , this.outlineUnbound ) ) ) ;
        if ( attr.getTau ( ) != null )
        {
          OutlinePair outlinePairType = OutlineStyle.getIndex ( attr ,
              PrettyStyle.TYPE ).get ( 0 ) ;
          String tau = attr.getTau ( ).toPrettyString ( ).toString ( ) ;
          nodeAttr.add ( new DefaultMutableTreeNode ( new OutlineNode ( TYPE ,
              attr.getTau ( ).toPrettyString ( ).toString ( ) , outlinePairType
                  .getStart ( ) , outlinePairType.getStart ( ) + tau.length ( )
                  - 1 , null , this.outlineUnbound ) ) ) ;
        }
        createChildren ( attr , nodeAttr ) ;
        node.add ( nodeAttr ) ;
      }
      else if ( pExpression.getExpressions ( i ) instanceof Meth )
      {
        DefaultMutableTreeNode treeNode = checkExpression ( pExpression
            .getExpressions ( i ) ) ;
        OutlineNode outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
        outlineNode.appendDescription ( EXPRESSION + ( i + 1 ) + BETWEEN ) ;
        outlineNode.resetCaption ( ) ;
        node.add ( treeNode ) ;
      }
      else if ( pExpression.getExpressions ( i ) instanceof CurriedMeth )
      {
        DefaultMutableTreeNode treeNode = checkExpression ( pExpression
            .getExpressions ( i ) ) ;
        OutlineNode outlineNode = ( OutlineNode ) treeNode.getUserObject ( ) ;
        outlineNode.appendDescription ( EXPRESSION + ( i + 1 ) + BETWEEN ) ;
        outlineNode.resetCaption ( ) ;
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
      if ( childIndex == ONLY_ONE_CHILD )
      {
        outlineNode.appendDescription ( EXPRESSION + BETWEEN ) ;
      }
      else
      {
        outlineNode.appendDescription ( EXPRESSION + childIndex + BETWEEN ) ;
      }
      outlineNode.resetCaption ( ) ;
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
    this.outlineUnbound = new OutlineUnbound ( this.oldExpression ) ;
    DefaultMutableTreeNode root = checkExpression ( this.oldExpression ) ;
    SwingUtilities
        .invokeLater ( new OutlineDisplayTree ( this.outlineUI , root ) ) ;
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
    int result = 10 ;
    for ( Method method : pExpression.getClass ( ).getMethods ( ) )
    {
      if ( GETE.equals ( method.getName ( ) ) )
      {
        return ONLY_ONE_CHILD ;
      }
      if ( method.getName ( ).matches ( GETEX ) )
      {
        result = Math.min ( result , Integer.parseInt ( String.valueOf ( method
            .getName ( ).charAt ( 4 ) ) ) ) ;
      }
    }
    return result == 10 ? 1 : result ;
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
    if ( ( this.oldExpression != null )
        && ( pExpression.equals ( this.oldExpression ) ) )
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
    this.oldExpression = pExpression ;
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
}