package de.unisiegen.tpml.core.languages.l2o;


import java.text.MessageFormat;

import de.unisiegen.tpml.core.Messages;
import de.unisiegen.tpml.core.expressions.Attribute;
import de.unisiegen.tpml.core.expressions.CurriedMethod;
import de.unisiegen.tpml.core.expressions.Duplication;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.expressions.Identifier;
import de.unisiegen.tpml.core.expressions.Lambda;
import de.unisiegen.tpml.core.expressions.Method;
import de.unisiegen.tpml.core.expressions.ObjectExpr;
import de.unisiegen.tpml.core.expressions.Row;
import de.unisiegen.tpml.core.expressions.Send;
import de.unisiegen.tpml.core.languages.l2.L2TypeCheckerProofRuleSet;
import de.unisiegen.tpml.core.typechecker.TypeCheckerExpressionProofNode;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofContext;
import de.unisiegen.tpml.core.typechecker.TypeCheckerProofNode;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment;
import de.unisiegen.tpml.core.types.ArrowType;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.ObjectType;
import de.unisiegen.tpml.core.types.RecType;
import de.unisiegen.tpml.core.types.RowType;
import de.unisiegen.tpml.core.types.TypeVariable;


/**
 * The type proof rules for the <code>L2O</code> language.
 * 
 * @author Christian Fehler
 * @version $Id$
 * @see L2TypeCheckerProofRuleSet
 */
public class L2OTypeCheckerProofRuleSet extends L2TypeCheckerProofRuleSet
{

  /**
   * Allocates a new <code>L2OTypeCheckerProofRuleSet</code> for the specified
   * <code>language</code>.
   * 
   * @param pL2OLanguage The <code>L2O</code> or a derived language.
   * @throws NullPointerException If <code>language</code> is
   *           <code>null</code>.
   */
  public L2OTypeCheckerProofRuleSet ( L2OLanguage pL2OLanguage )
  {
    super ( pL2OLanguage );
    registerByMethodName ( L2OLanguage.L2O, "SEND", "applySend" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O, "OBJECT", "applyObject" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O, "DUPL", "applyDupl" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O, "EMPTY", "applyEmpty" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O, "ATTR", "applyAttr" ); //$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O, "METHOD", "applyMethod" ); //$NON-NLS-1$ //$NON-NLS-2$ 
  }


  /**
   * Applies the <b>(SEND)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applySend ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Send send = ( Send ) pNode.getExpression ();
    MonoType tauSendE = node.getType ();
    MonoType tauRemainingRow = pContext.newTypeVariable ();
    TypeEnvironment environment = node.getEnvironment ();
    pContext.addProofNode ( node, environment, send.getE (), new ObjectType (
        new RowType ( new Identifier []
        { send.getId () }, new MonoType []
        { tauSendE }, tauRemainingRow ) ) );
  }


  /**
   * Applies the <b>(OBJECT)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyObject ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    ObjectExpr objectExpr = ( ObjectExpr ) node.getExpression ();
    MonoType tau = objectExpr.getTau ();
    if ( tau == null )
    {
      MonoType rowType = pContext.newTypeVariable ();
      ObjectType objectType = new ObjectType ( rowType );
      pContext.addEquation ( node.getType (), objectType );
      TypeEnvironment environment = node.getEnvironment ();
      environment = environment.star ();
      environment = environment.extend ( objectExpr.getId (), objectType );
      pContext.addProofNode ( node, environment, objectExpr.getRow (), rowType );
    }
    else if ( tau instanceof ObjectType )
    {
      ObjectType objectType = ( ObjectType ) tau;
      RowType rowType = ( RowType ) objectType.getPhi ();
      Row row = objectExpr.getRow ();
      if ( row.getNumberOfDifferentMethods () != rowType.getTypes ().length )
      {
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "UnificationException.2" ), node.getType (), objectType ) ); //$NON-NLS-1$
      }
      pContext.addEquation ( node.getType (), objectType );
      TypeEnvironment environment = node.getEnvironment ();
      environment = environment.star ();
      environment = environment.extend ( objectExpr.getId (), objectType );
      pContext.addProofNode ( node, environment, objectExpr.getRow (), rowType );
    }
    else if ( ( tau instanceof RecType )
        && ( ( ( RecType ) tau ).getTau () instanceof ObjectType ) )
    {
      RecType recType = ( RecType ) tau;
      ObjectType objectType = ( ObjectType ) recType.getTau ().substitute (
          recType.getTypeName (), recType );
      RowType rowType = ( RowType ) objectType.getPhi ();
      Row row = objectExpr.getRow ();
      if ( row.getNumberOfDifferentMethods () != rowType.getTypes ().length )
      {
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "UnificationException.2" ), node.getType (), objectType ) ); //$NON-NLS-1$
      }
      pContext.addEquation ( node.getType (), objectType );
      TypeEnvironment environment = node.getEnvironment ();
      environment = environment.star ();
      environment = environment.extend ( objectExpr.getId (), objectType );
      pContext.addProofNode ( node, environment, objectExpr.getRow (), rowType );
    }
    else
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.5" ), objectExpr ) ); //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(DUPL)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyDupl ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Duplication duplication = ( Duplication ) node.getExpression ();
    Identifier [] duplicationIdentifiers = duplication.getIdentifiers ();
    Expression [] duplicationExpressions = duplication.getExpressions ();
    MonoType tauSelf = pContext.newTypeVariable ();
    TypeEnvironment environment = node.getEnvironment ();
    pContext.addProofNode ( node, environment, new Identifier (
        "self", Identifier.Set.SELF ), //$NON-NLS-1$
        tauSelf );
    pContext.addEquation ( node.getType (), tauSelf );
    for ( int i = 0 ; i < duplicationIdentifiers.length ; i++ )
    {
      MonoType tauE = pContext.newTypeVariable ();
      pContext.addProofNode ( node, environment, duplicationIdentifiers [ i ],
          tauE );
      pContext.addProofNode ( node, environment, duplicationExpressions [ i ],
          tauE );
    }
  }


  /**
   * Applies the <b>(EMPTY)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param node The type checker proof node.
   */
  public void applyEmpty ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode node )
  {
    Row row = ( Row ) node.getExpression ();
    if ( row.getExpressions ().length != 0 )
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.4" ), row ) ); //$NON-NLS-1$
    }
    pContext.addEquation ( node.getType (), new RowType ( new Identifier [ 0 ],
        new MonoType [ 0 ] ) );
  }


  /**
   * Applies the <b>(ATTR)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyAttr ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Row row = ( Row ) node.getExpression ();
    Expression [] rowExpressions = row.getExpressions ();
    if ( rowExpressions.length == 0 )
    {
      throw new RuntimeException ( Messages.getString ( "ProofRuleException.3" ) ); //$NON-NLS-1$
    }
    if ( ! ( rowExpressions [ 0 ] instanceof Attribute ) )
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.2" ), rowExpressions [ 0 ] ) ); //$NON-NLS-1$ 
    }
    Attribute attribute = ( Attribute ) row.getExpressions () [ 0 ];
    Expression e = attribute.getE ();
    MonoType tauE = pContext.newTypeVariable ();
    MonoType tauRow = pContext.newTypeVariable ();
    TypeEnvironment environmentE = node.getEnvironment ();
    environmentE = environmentE.star ();
    pContext.addProofNode ( node, environmentE, e, tauE );
    TypeEnvironment environmentTailRow = node.getEnvironment ();
    environmentTailRow = environmentTailRow.extend ( attribute.getId (), tauE );
    pContext.addProofNode ( node, environmentTailRow, row.tailRow (), tauRow );
    pContext.addEquation ( node.getType (), tauRow );
  }


  /**
   * Applies the <b>(METHOD)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param pContext The type checker proof context.
   * @param pNode The type checker proof node.
   */
  public void applyMethod ( TypeCheckerProofContext pContext,
      TypeCheckerProofNode pNode )
  {
    TypeCheckerExpressionProofNode node = ( TypeCheckerExpressionProofNode ) pNode;
    Row row = ( Row ) node.getExpression ();
    Expression [] rowExpressions = row.getExpressions ();
    if ( rowExpressions.length == 0 )
    {
      throw new RuntimeException ( Messages.getString ( "ProofRuleException.3" ) ); //$NON-NLS-1$
    }
    if ( rowExpressions [ 0 ] instanceof Method )
    {
      Method method = ( Method ) rowExpressions [ 0 ];
      Expression methodE = method.getE ();
      MonoType methodTau = method.getTau ();
      if ( methodTau == null )
      {
        methodTau = pContext.newTypeVariable ();
      }
      MonoType tauRow = pContext.newTypeVariable ();
      MonoType nodeType = node.getType ();
      TypeEnvironment environment = node.getEnvironment ();
      pContext.addProofNode ( node, environment, methodE, methodTau );
      boolean foundInTailRow = false;
      for ( int i = 1 ; i < row.getExpressions ().length ; i++ )
      {
        if ( row.getExpressions () [ i ] instanceof Method )
        {
          Identifier id = ( ( Method ) row.getExpressions () [ i ] ).getId ();
          if ( method.getId ().equals ( id ) )
          {
            foundInTailRow = true;
            break;
          }
        }
        else if ( row.getExpressions () [ i ] instanceof CurriedMethod )
        {
          Identifier id = ( ( CurriedMethod ) row.getExpressions () [ i ] )
              .getIdentifiers () [ 0 ];
          if ( method.getId ().equals ( id ) )
          {
            foundInTailRow = true;
            break;
          }
        }
      }
      /*
       * The Type of the node is a TypeVariable
       */
      if ( nodeType instanceof TypeVariable )
      {
        /*
         * In the rest of the Row exists a Method with the same Identifier as
         * the Identifier of the Method.
         */
        if ( foundInTailRow )
        {
          RowType newRowType = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext
              .addProofNode ( node, environment, row.tailRow (), newRowType );
          RowType union = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
        else
        {
          pContext.addProofNode ( node, environment, row.tailRow (), tauRow );
          RowType union = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
      }
      /*
       * The Type of the node is a RowType and the first Identifier of the
       * RowType is equals to the Identifier of the method.
       */
      else if ( ( nodeType instanceof RowType )
          && ( method.getId ().equals ( ( ( RowType ) nodeType )
              .getIdentifiers () [ 0 ] ) ) )
      {
        /*
         * In the rest of the Row exists a Method with the same Identifier as
         * the Identifier of the Method.
         */
        if ( foundInTailRow )
        {
          RowType newRowType = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext
              .addProofNode ( node, environment, row.tailRow (), newRowType );
          RowType union = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
        else
        {
          pContext.addProofNode ( node, environment, row.tailRow (), tauRow );
          RowType union = new RowType ( new Identifier []
          { method.getId () }, new MonoType []
          { methodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
      }
      /*
       * The Type of the node is a RowType and the first Identifier of the
       * RowType is not equals to the Identifier of the method.
       */
      else
      {
        RowType rowType = ( RowType ) nodeType;
        RowType unionRow = new RowType ( new Identifier []
        { rowType.getIdentifiers () [ 0 ] }, new MonoType []
        { rowType.getTypes () [ 0 ] }, tauRow );
        pContext.addProofNode ( node, environment, row.tailRow (), unionRow );
        RowType unionEquation = new RowType ( new Identifier []
        { method.getId () }, new MonoType []
        { methodTau }, tauRow );
        MonoType remainingRowType = rowType.getRemainingRowType ();
        if ( remainingRowType != null )
        {
          pContext.addEquation ( remainingRowType, unionEquation );
        }
      }
    }
    else if ( rowExpressions [ 0 ] instanceof CurriedMethod )
    {
      CurriedMethod curriedMethod = ( CurriedMethod ) rowExpressions [ 0 ];
      Expression curriedMethodE = curriedMethod.getE ();
      MonoType [] types = curriedMethod.getTypes ();
      Identifier [] identifiers = curriedMethod.getIdentifiers ();
      for ( int n = identifiers.length - 1 ; n > 0 ; --n )
      {
        curriedMethodE = new Lambda ( identifiers [ n ], types [ n ],
            curriedMethodE );
      }
      MonoType curriedMethodTau = types [ 0 ];
      if ( curriedMethodTau == null )
      {
        curriedMethodTau = pContext.newTypeVariable ();
      }
      for ( int n = types.length - 1 ; n > 0 ; --n )
      {
        curriedMethodTau = new ArrowType ( ( types [ n ] != null ) ? types [ n ]
            : pContext.newTypeVariable (), curriedMethodTau );
      }
      MonoType tauRow = pContext.newTypeVariable ();
      MonoType nodeType = node.getType ();
      TypeEnvironment environment = node.getEnvironment ();
      pContext.addProofNode ( node, environment, curriedMethodE,
          curriedMethodTau );
      boolean foundInTailRow = false;
      for ( int i = 1 ; i < row.getExpressions ().length ; i++ )
      {
        if ( row.getExpressions () [ i ] instanceof Method )
        {
          Identifier id = ( ( Method ) row.getExpressions () [ i ] ).getId ();
          if ( identifiers [ 0 ].equals ( id ) )
          {
            foundInTailRow = true;
            break;
          }
        }
        else if ( row.getExpressions () [ i ] instanceof CurriedMethod )
        {
          Identifier id = ( ( CurriedMethod ) row.getExpressions () [ i ] )
              .getIdentifiers () [ 0 ];
          if ( identifiers [ 0 ].equals ( id ) )
          {
            foundInTailRow = true;
            break;
          }
        }
      }
      /*
       * The Type of the node is a TypeVariable
       */
      if ( nodeType instanceof TypeVariable )
      {
        /*
         * In the rest of the Row exists a Method with the same Identifier as
         * the Identifier of the Method.
         */
        if ( foundInTailRow )
        {
          RowType newRowType = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext
              .addProofNode ( node, environment, row.tailRow (), newRowType );
          RowType union = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
        else
        {
          pContext.addProofNode ( node, environment, row.tailRow (), tauRow );
          RowType union = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
      }
      /*
       * The Type of the node is a RowType and the first Identifier of the
       * RowType is equals to the Identifier of the method.
       */
      else if ( ( nodeType instanceof RowType )
          && ( identifiers [ 0 ].equals ( ( ( RowType ) nodeType )
              .getIdentifiers () [ 0 ] ) ) )
      {
        /*
         * In the rest of the Row exists a Method with the same Identifier as
         * the Identifier of the Method.
         */
        if ( foundInTailRow )
        {
          RowType newRowType = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext
              .addProofNode ( node, environment, row.tailRow (), newRowType );
          RowType union = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
        else
        {
          pContext.addProofNode ( node, environment, row.tailRow (), tauRow );
          RowType union = new RowType ( new Identifier []
          { identifiers [ 0 ] }, new MonoType []
          { curriedMethodTau }, tauRow );
          pContext.addEquation ( node.getType (), union );
        }
      }
      /*
       * The Type of the node is a RowType and the first Identifier of the
       * RowType is not equals to the Identifier of the method.
       */
      else
      {
        RowType rowType = ( RowType ) nodeType;
        RowType unionRow = new RowType ( new Identifier []
        { rowType.getIdentifiers () [ 0 ] }, new MonoType []
        { rowType.getTypes () [ 0 ] }, tauRow );
        pContext.addProofNode ( node, environment, row.tailRow (), unionRow );
        RowType unionEquation = new RowType ( new Identifier []
        { identifiers [ 0 ] }, new MonoType []
        { curriedMethodTau }, tauRow );
        MonoType remainingRowType = rowType.getRemainingRowType ();
        if ( remainingRowType != null )
        {
          pContext.addEquation ( remainingRowType, unionEquation );
        }
      }
    }
    else
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "ProofRuleException.1" ), rowExpressions [ 0 ] ) ); //$NON-NLS-1$
    }
  }
}
