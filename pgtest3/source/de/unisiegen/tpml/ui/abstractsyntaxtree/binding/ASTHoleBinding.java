package de.unisiegen.tpml.ui.abstractsyntaxtree.binding ;


import java.util.Enumeration ;
import java.util.LinkedList ;
import de.unisiegen.tpml.core.expressions.CurriedLet ;
import de.unisiegen.tpml.core.expressions.CurriedLetRec ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Let ;
import de.unisiegen.tpml.core.expressions.LetRec ;
import de.unisiegen.tpml.core.expressions.MultiLambda ;
import de.unisiegen.tpml.core.expressions.MultiLet ;
import de.unisiegen.tpml.core.expressions.Recursion ;
import de.unisiegen.tpml.core.prettyprinter.PrettyAnnotation ;


public class ASTHoleBinding
{
  private Expression expression ;


  private LinkedList < ASTBinding > list ;


  private LinkedList < ASTPair > listASTPair ;


  public ASTHoleBinding ( Expression pExpression )
  {
    this.expression = pExpression ;
    this.listASTPair = new LinkedList < ASTPair > ( ) ;
    calculate ( ) ;
  }


  private void calculateList ( LinkedList < ASTBinding > pList ,
      Expression pExpression )
  {
    if ( pExpression instanceof CurriedLetRec )
    {
      CurriedLetRec tmp = ( CurriedLetRec ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression ) ;
      ast.add ( tmp , tmp.getIdentifiers ( 0 ) ) ;
      for ( int i = 1 ; i < tmp.getIdentifiers ( ).length ; i ++ )
      {
        ast.add ( tmp.getE1 ( ) , tmp.getIdentifiers ( i ) ) ;
      }
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof CurriedLet )
    {
      CurriedLet tmp = ( CurriedLet ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression ) ;
      ast.add ( tmp.getE2 ( ) , tmp.getIdentifiers ( 0 ) ) ;
      for ( int i = 1 ; i < tmp.getIdentifiers ( ).length ; i ++ )
      {
        ast.add ( tmp.getE1 ( ) , tmp.getIdentifiers ( i ) ) ;
      }
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof MultiLambda )
    {
      MultiLambda tmp = ( MultiLambda ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp , tmp
          .getIdentifiers ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof MultiLet )
    {
      MultiLet tmp = ( MultiLet ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp.getE2 ( ) , tmp
          .getIdentifiers ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      notFree.add ( tmp.getE1 ( ) ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof LetRec )
    {
      LetRec tmp = ( LetRec ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp , tmp.getId ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof Let )
    {
      Let tmp = ( Let ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp.getE2 ( ) , tmp
          .getId ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      notFree.add ( tmp.getE1 ( ) ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof Lambda )
    {
      Lambda tmp = ( Lambda ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp , tmp.getId ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else if ( pExpression instanceof Recursion )
    {
      Recursion tmp = ( Recursion ) pExpression ;
      ASTBinding ast = new ASTBinding ( pExpression , tmp , tmp.getId ( ) ) ;
      pList.add ( ast ) ;
      LinkedList < Expression > notFree = ast.getNotFree ( ) ;
      for ( Expression e : notFree )
      {
        calculateList ( pList , e ) ;
      }
    }
    else
    {
      Enumeration < Expression > children = pExpression.children ( ) ;
      while ( children.hasMoreElements ( ) )
      {
        Expression child = children.nextElement ( ) ;
        calculateList ( pList , child ) ;
      }
    }
  }


  private void calculate ( )
  {
    this.list = new LinkedList < ASTBinding > ( ) ;
    calculateList ( this.list , this.expression ) ;
    for ( int i = 0 ; i < this.list.size ( ) ; i ++ )
    {
      for ( int j = 0 ; j < this.list.get ( i ).size ( ) ; j ++ )
      {
        for ( int k = 0 ; k < this.list.get ( i ).size ( j ) ; k ++ )
        {
          Expression e1 = this.list.get ( i ).getHoleExpression ( ) ;
          PrettyAnnotation pa1 = this.expression.toPrettyString ( )
              .getAnnotationForPrintable ( e1 ) ;
          int startIndex = pa1.getStartOffset ( ) ;
          int endIndex = startIndex ;
          // CurriedLetRec
          if ( e1 instanceof CurriedLetRec )
          {
            String [ ] idList = ( ( CurriedLetRec ) e1 ).getIdentifiers ( ) ;
            startIndex = pa1.getStartOffset ( ) ;
            endIndex = startIndex ;
            int lengthIdentifier = 0 ;
            for ( int n = 0 ; n < j ; n ++ )
            {
              lengthIdentifier += idList [ n ].length ( ) ;
            }
            startIndex += lengthIdentifier + 8 + j ;
            endIndex += lengthIdentifier + 7 + idList [ j ].length ( ) + j ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          // CurriedLet
          else if ( e1 instanceof CurriedLet )
          {
            String [ ] idList = ( ( CurriedLet ) e1 ).getIdentifiers ( ) ;
            startIndex = pa1.getStartOffset ( ) ;
            endIndex = startIndex ;
            int lengthIdentifier = 0 ;
            for ( int n = 0 ; n < j ; n ++ )
            {
              lengthIdentifier += idList [ n ].length ( ) ;
            }
            startIndex += lengthIdentifier + 4 + j ;
            endIndex += lengthIdentifier + 3 + idList [ j ].length ( ) + j ;
            lengthIdentifier += idList [ j ].length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          // MultiLambda
          else if ( e1 instanceof MultiLambda )
          {
            String [ ] idList = ( ( MultiLambda ) e1 ).getIdentifiers ( ) ;
            startIndex = pa1.getStartOffset ( ) ;
            endIndex = startIndex ;
            int lengthIdentifier = 0 ;
            for ( int n = 0 ; n < j ; n ++ )
            {
              lengthIdentifier += idList [ n ].length ( ) ;
            }
            startIndex += lengthIdentifier + 2 + ( j * 2 ) ;
            endIndex += lengthIdentifier + 1 + idList [ j ].length ( )
                + ( j * 2 ) ;
            lengthIdentifier += idList [ j ].length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          // MultiLet
          else if ( e1 instanceof MultiLet )
          {
            String [ ] idList = ( ( MultiLet ) e1 ).getIdentifiers ( ) ;
            startIndex = pa1.getStartOffset ( ) ;
            endIndex = startIndex ;
            int lengthIdentifier = 0 ;
            for ( int n = 0 ; n < j ; n ++ )
            {
              lengthIdentifier += idList [ n ].length ( ) ;
            }
            startIndex += lengthIdentifier + 5 + ( j * 2 ) ;
            endIndex += lengthIdentifier + 4 + idList [ j ].length ( )
                + ( j * 2 ) ;
            lengthIdentifier += idList [ j ].length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          else if ( e1 instanceof LetRec )
          {
            startIndex += 8 ;
            endIndex += 7 + ( ( LetRec ) e1 ).getId ( ).length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          else if ( e1 instanceof Let )
          {
            startIndex += 4 ;
            endIndex += 3 + ( ( Let ) e1 ).getId ( ).length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          else if ( e1 instanceof Lambda )
          {
            startIndex += 1 ;
            endIndex += ( ( Lambda ) e1 ).getId ( ).length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
          else if ( e1 instanceof Recursion )
          {
            startIndex += 4 ;
            endIndex += 3 + ( ( Recursion ) e1 ).getId ( ).length ( ) ;
            Expression e2 = this.list.get ( i ).get ( j , k ) ;
            PrettyAnnotation pa2 = this.expression.toPrettyString ( )
                .getAnnotationForPrintable ( e2 ) ;
            ASTPair ap = new ASTPair ( startIndex , endIndex ) ;
            if ( isNewIdentifier ( ap ) )
            {
              this.listASTPair.add ( ap ) ;
            }
            addNewBinding ( ap , new ASTPair ( pa2.getStartOffset ( ) , pa2
                .getEndOffset ( ) ) ) ;
          }
        }
      }
    }
  }


  private boolean isNewIdentifier ( ASTPair pASTPair )
  {
    for ( int i = 0 ; i < this.listASTPair.size ( ) ; i ++ )
    {
      if ( this.listASTPair.get ( i ).equals ( pASTPair ) )
      {
        return false ;
      }
    }
    return true ;
  }


  private void addNewBinding ( ASTPair pIdentifier , ASTPair pBinding )
  {
    for ( int i = 0 ; i < this.listASTPair.size ( ) ; i ++ )
    {
      if ( this.listASTPair.get ( i ).equals ( pIdentifier ) )
      {
        this.listASTPair.get ( i ).add ( pBinding ) ;
        return ;
      }
    }
  }


  public LinkedList < ASTPair > getASTPairs ( )
  {
    return this.listASTPair ;
  }
}
