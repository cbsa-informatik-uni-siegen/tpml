package de.unisiegen.tpml.core.languages.l2o ;


import java.text.MessageFormat ;
import java.util.ArrayList ;
import de.unisiegen.tpml.core.Messages ;
import de.unisiegen.tpml.core.expressions.Attribute ;
import de.unisiegen.tpml.core.expressions.CurriedMethod ;
import de.unisiegen.tpml.core.expressions.Duplication ;
import de.unisiegen.tpml.core.expressions.Expression ;
import de.unisiegen.tpml.core.expressions.Identifier ;
import de.unisiegen.tpml.core.expressions.Lambda ;
import de.unisiegen.tpml.core.expressions.Method ;
import de.unisiegen.tpml.core.expressions.ObjectExpr ;
import de.unisiegen.tpml.core.expressions.Row ;
import de.unisiegen.tpml.core.expressions.Send ;
import de.unisiegen.tpml.core.languages.l1.L1Language ;
import de.unisiegen.tpml.core.languages.l2.L2MinimalTypingProofRuleSet ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingExpressionProofNode ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofContext ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingProofNode ;
import de.unisiegen.tpml.core.minimaltyping.MinimalTypingTypesProofNode ;
import de.unisiegen.tpml.core.subtyping.SubTypingProofNode ;
import de.unisiegen.tpml.core.subtypingrec.DefaultSubType;
import de.unisiegen.tpml.core.typechecker.TypeEnvironment ;
import de.unisiegen.tpml.core.types.ArrowType ;
import de.unisiegen.tpml.core.types.MonoType ;
import de.unisiegen.tpml.core.types.ObjectType ;
import de.unisiegen.tpml.core.types.PrimitiveType ;
import de.unisiegen.tpml.core.types.RecType ;
import de.unisiegen.tpml.core.types.RowType ;


/**
 * The minimal type proof rules for the <code>L1</code> language.
 * 
 * @author Benjamin Mies
 * @see de.unisiegen.tpml.core.minimaltyping.AbstractMinimalTypingProofRuleSet
 */
public class L2OMinimalTypingProofRuleSet extends L2MinimalTypingProofRuleSet
{
  /**
   * Allocates a new <code>L1MinimalTypingProofRuleSet</code> for the
   * specified <code>language</code>.
   * 
   * @param language the <code>L1</code> or a derived language.
   * @param mode the actual choosen mode
   * @throws NullPointerException if <code>language</code> is
   *           <code>null</code>.
   */
  public L2OMinimalTypingProofRuleSet ( L1Language language , boolean mode )
  {
    super ( language , mode ) ;
    // register the type rules
    if ( ! mode )
    { // beginner mode
      registerByMethodName ( L2OLanguage.L2O , "TRANS" , "applyTrans" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      registerByMethodName ( L2OLanguage.L2O ,
          "OBJECT-WIDTH" , "applyObjectWidth" ) ; //$NON-NLS-1$ //$NON-NLS-2$
      registerByMethodName ( L2OLanguage.L2O ,
          "OBJECT-DEPTH" , "applyObjectDepth" , "updateObjectDepth" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      registerByMethodName ( L1Language.L1 , "REFL" , "applyRefl" ) ; //$NON-NLS-1$ //$NON-NLS-2$
    }
    registerByMethodName ( L2OLanguage.L2O , "EMPTY" , "applyEmpty" ) ;//$NON-NLS-1$ //$NON-NLS-2$
    registerByMethodName ( L2OLanguage.L2O ,
        "SEND" , "applySend" , "updateSend" ) ;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L2OLanguage.L2O ,
        "OBJECT" , "applyObject" , "updateObject" ) ;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L2OLanguage.L2O ,
        "DUPL-SUBSUME" , "applyDuplSubsume" , "updateDuplSubsume" ) ;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L2OLanguage.L2O ,
        "METHOD-SUBSUME" , "applyMethodSubsume" , "updateMethodSubsume" ) ;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    registerByMethodName ( L2OLanguage.L2O ,
        "ATTR" , "applyAttr" , "updateAttr" ) ;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }


  /**
   * Applies the <b>(SEND)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applySend ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Send send = ( Send ) node.getExpression ( ) ;
    // generate new child node
    context.addProofNode ( node , node.getEnvironment ( ) , send.getE ( ) ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(SEND)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(SEND)</b>.
   */
  public void updateSend ( @ SuppressWarnings ( "unused" )
  MinimalTypingProofContext context , MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Send send = ( Send ) node.getExpression ( ) ;
    if ( node.getFirstChild ( ).isFinished ( ) )
    {
      ObjectType object = ( ObjectType ) node.getFirstChild ( ).getType ( ) ;
      RowType row = ( RowType ) object.getPhi ( ) ;
      Identifier id = send.getId ( ) ;
      int index = row.getIndexOfIdentifier ( id ) ;
      // check if identifier of send is in the row type
      if ( index < 0 )
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "MinimalTypingException.6" ) , node.getExpression ( ).toString ( ) ) ) ; //$NON-NLS-1$
      // set the type of this node
      context.setNodeType ( node , row.getTypes ( ) [ index ] ) ;
    }
  }


  /**
   * Applies the <b>(OBJECT)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyObject ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    ObjectExpr object = ( ObjectExpr ) node.getExpression ( ) ;
    MonoType tau = object.getTau ( ) ;
    // check if user entered type for self
    if ( tau == null )
    {
      throw new RuntimeException ( MessageFormat.format ( Messages
          .getString ( "MinimalTypingException.2" ) , "self" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
    }
    // if type is a rec type, unfold to get a object type
    if ( tau instanceof RecType )
    {
      RecType rec = ( RecType ) tau ;
      tau = rec.getTau ( ).substitute ( rec.getTypeName ( ) , rec ) ;
    }
    TypeEnvironment environment = node.getEnvironment ( ) ;
    environment = environment.star ( ) ;
    // generate new child node
    context.addProofNode ( node ,
        environment.extend ( object.getId ( ) , tau ) , object.getRow ( ) ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(OBJECT)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(OBJECT)</b>.
   */
  public void updateObject ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    if ( node.getFirstChild ( ).isFinished ( ) )
    {
      MinimalTypingExpressionProofNode child = ( MinimalTypingExpressionProofNode ) node
          .getFirstChild ( ) ;
      TypeEnvironment environment = child.getEnvironment ( ) ;
      Identifier self = new Identifier ( "self" , Identifier.Set.SELF ) ; //$NON-NLS-1$
      ObjectType objectType = ( ObjectType ) environment.get ( self ) ;
      RowType type = ( RowType ) objectType.getPhi ( ) ;
      RowType type2 = ( RowType ) child.getType ( ) ;
      try
      {
        // check type <: type2 and type2 <: type
        // if both true type = type2
        subtypeInternal ( type , type2 ) ;
        subtypeInternal ( type2 , type ) ;
      }
      catch ( Exception e )
      {
        throw new RuntimeException (
            MessageFormat
                .format (
                    Messages.getString ( "MinimalTypingException.7" ) , node.getExpression ( ).toString ( ) ) ) ; //$NON-NLS-1$
      }
      ObjectType object = new ObjectType ( type ) ;
      // set the type of this node
      context.setNodeType ( node , object ) ;
    }
  }


  /**
   * Applies the <b>(DUPL-SUBSUME)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyDuplSubsume ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    @ SuppressWarnings ( "unused" )
    Duplication duplication = ( Duplication ) node.getExpression ( ) ;
    Identifier self = new Identifier ( "self" , Identifier.Set.SELF ) ; //$NON-NLS-1$
    // generate new child node
    context.addProofNode ( node , node.getEnvironment ( ) , self ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(DUPL-SUBSUME)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(DUPL-SUBSUME)</b>.
   */
  public void updateDuplSubsume ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Duplication duplication = ( Duplication ) node.getExpression ( ) ;
    if ( node.getChildCount ( ) == 1 && node.getLastChild ( ).isFinished ( ) )
    {
      // generate new child node
      context.addProofNode ( node , node.getEnvironment ( ) , duplication
          .getExpressions ( ) [ 0 ] ) ;
    }
    else if ( node.getChildCount ( ) > 1 && node.getLastChild ( ).isFinished ( ) )
    {
      // check if a:tau und e:tau' tau'<: tau
      TypeEnvironment environment = node.getEnvironment ( ) ;
      Identifier id = duplication.getIdentifiers ( ) [ node.getChildCount ( ) - 2 ] ;
      MonoType tau = null ;
      try
      {
        tau = ( MonoType ) environment.get ( id ) ;
      }
      catch ( Exception e )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.9" ) , id.toString ( ) ) ) ; //$NON-NLS-1$
      }
      MonoType tau2 = node.getLastChild ( ).getType ( ) ;
      if ( tau == null || tau2 == null )
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.2" ) , "self" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
      try
      {
        subtypeInternal ( tau , tau2 ) ;
        subtypeInternal ( tau2 , tau ) ;
      }
      catch ( Exception e )
      {
        throw new RuntimeException ( MessageFormat.format ( Messages
            .getString ( "MinimalTypingException.2" ) , "self" ) ) ; //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    if ( node.getChildCount ( ) == duplication.getIdentifiers ( ).length + 1 )
    {
      // set the type of this node
      context.setNodeType ( node , node.getFirstChild ( ).getType ( ) ) ;
      // all childs added, so nothing more to do
      return ;
    }
    // generate new child node
    context.addProofNode ( node , node.getEnvironment ( ) , duplication
        .getExpressions ( ) [ node.getChildCount ( ) - 1 ] ) ;
  }


  /**
   * Applies the <b>(METHOD-SUBSUME)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyMethodSubsume ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Row row = ( Row ) node.getExpression ( ) ;
    if ( row.getExpressions ( ).length > 0
        && ( ( row.getExpressions ( ) [ 0 ] instanceof Method ) || ( row
            .getExpressions ( ) [ 0 ] instanceof CurriedMethod ) ) )
    {
      Identifier self = new Identifier ( "self" , Identifier.Set.SELF ) ; //$NON-NLS-1$
      // generate new child node
      context.addProofNode ( node , node.getEnvironment ( ) , self ) ;
      return ;
    }
    throw new ClassCastException ( "expression is not instance of Method" ) ; //$NON-NLS-1$
  }


  /**
   * Updates the <code>node</code> to which <b>(METHOD-SUBSUME)</b> was
   * applied previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(METHOD-SUBSUME)</b>.
   */
  public void updateMethodSubsume ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Row row = ( Row ) node.getExpression ( ) ;
    if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) )
    {
      if ( row.getExpressions ( ) [ 0 ] instanceof Method )
      {
        Method method = ( Method ) row.getExpressions ( ) [ 0 ] ;
        // generate new child node
        context
            .addProofNode ( node , node.getEnvironment ( ) , method.getE ( ) ) ;
      }
      else
      {
        CurriedMethod curriedMethod = ( CurriedMethod ) row.getExpressions ( ) [ 0 ] ;
        Expression curriedMethodE = curriedMethod.getE ( ) ;
        MonoType [ ] types = curriedMethod.getTypes ( ) ;
        Identifier [ ] identifiers = curriedMethod.getIdentifiers ( ) ;
        for ( int n = identifiers.length - 1 ; n > 0 ; -- n )
        {
          curriedMethodE = new Lambda ( identifiers [ n ] , types [ n ] ,
              curriedMethodE ) ;
        }
        // generate new child node
        context
            .addProofNode ( pNode , node.getEnvironment ( ) , curriedMethodE ) ;
      }
    }
    else if ( node.getChildCount ( ) == 2
        && node.getChildAt ( 1 ).isFinished ( ) )
    {
      // Create the tailRow and add it as Expression of a new Child
      Expression [ ] expressions = ( ( Row ) node.getExpression ( ) )
          .getExpressions ( ) ;
      Expression [ ] tailRow = new Expression [ expressions.length - 1 ] ;
      for ( int i = 1 ; i < expressions.length ; i ++ )
      {
        tailRow [ i - 1 ] = expressions [ i ] ;
      }
      Row newRow = new Row ( tailRow ) ;
      // generate new child node
      context.addProofNode ( node , node.getEnvironment ( ) , newRow ) ;
    }
    else if ( node.getChildCount ( ) == 3
        && node.getChildAt ( 2 ).isFinished ( ) )
    {
      Expression expression = row.getExpressions ( ) [ 0 ] ;
      MonoType type = node.getFirstChild ( ).getType ( ) ;
      // if type is a rec type, unfold to get a object type
      if ( type instanceof RecType )
      {
        RecType rec = ( RecType ) type ;
        type = rec.getTau ( ).substitute ( rec.getTypeName ( ) , rec ) ;
      }
      ObjectType object = ( ObjectType ) type ;
      RowType rowType = ( RowType ) object.getPhi ( ) ;
      Identifier [ ] identifiers = rowType.getIdentifiers ( ) ;
      MonoType [ ] types2 = rowType.getTypes ( ) ;
      if ( expression instanceof Method )
      {
        Identifier m = ( expression instanceof Method ? ( ( Method ) expression )
            .getId ( )
            : ( ( CurriedMethod ) expression ).getIdentifiers ( ) [ 0 ] ) ;
        MonoType tau = null ;
        for ( int i = 0 ; i < identifiers.length ; i ++ )
        {
          if ( m.equals ( identifiers [ i ] ) )
          {
            tau = types2 [ i ] ;
            break ;
          }
        }
        if ( tau == null )
          throw new RuntimeException ( MessageFormat.format ( Messages
              .getString ( "MinimalTypingException.2" ) , m.toString ( ) ) ) ; //$NON-NLS-1$
        // generate new child node
        context.addProofNode ( node , tau , node.getChildAt ( 1 ).getType ( ) ) ;
      }
      else
      {
        CurriedMethod method = ( CurriedMethod ) expression ;
        Identifier m = method.getIdentifiers ( ) [ 0 ] ;
        type = types2 [ rowType.getIndexOfIdentifier ( m ) ] ;
        MonoType [ ] types = method.getTypes ( ) ;
        MonoType childType = node.getChildAt ( 1 ).getType ( ) ;
        while ( childType instanceof ArrowType )
        {
          childType = ( ( ArrowType ) childType ).getTau2 ( ) ;
        }
        ArrowType arrow = new ArrowType ( types [ types.length - 1 ] ,
            childType ) ;
        for ( int i = types.length - 2 ; i > 1 ; i -- )
        {
          arrow = new ArrowType ( types [ i ] , arrow ) ;
        }
        // generate new child node
        context.addProofNode ( node , arrow , type ) ;
      }
    }
    else if ( node.getChildCount ( ) == 4
        && node.getChildAt ( 3 ).isFinished ( ) )
    {
      Expression expression = row.getExpressions ( ) [ 0 ] ;
      MonoType type = node.getChildAt ( 1 ).getType ( ) ;
      while ( type instanceof ArrowType )
      {
        type = ( ( ArrowType ) type ).getTau2 ( ) ;
      }
      MonoType type2 = ( expression instanceof Method ? ( ( Method ) expression )
          .getTau ( )
          : ( ( CurriedMethod ) expression ).getTypes ( ) [ 0 ] ) ;
      if ( type2 != null )
        // generate new child node
        context.addProofNode ( node , type , type2 ) ;
      else
      {
        Identifier [ ] ids = new Identifier [ 1 ] ;
        ids [ 0 ] = ( expression instanceof Method ? ( ( Method ) expression )
            .getIdentifiers ( ) [ 0 ] : ( ( CurriedMethod ) expression )
            .getIdentifiers ( ) [ 0 ] ) ;
        MonoType [ ] types =
        { node.getChildAt ( 1 ).getType ( ) } ;
        RowType rowType = new RowType ( ids , types ) ;
        RowType phi = ( RowType ) node.getChildAt ( 2 ).getType ( ) ;
        rowType = RowType.union ( rowType , phi ) ;
        // set the type of this node
        context.setNodeType ( node , rowType ) ;
      }
    }
    else if ( node.getChildCount ( ) == 5
        && node.getChildAt ( 4 ).isFinished ( ) )
    {
      Expression expression = row.getExpressions ( ) [ 0 ] ;
      Identifier [ ] ids = new Identifier [ 1 ] ;
      ids [ 0 ] = ( expression instanceof Method ? ( ( Method ) expression )
          .getIdentifiers ( ) [ 0 ] : ( ( CurriedMethod ) expression )
          .getIdentifiers ( ) [ 0 ] ) ;
      MonoType [ ] types =
      { node.getChildAt ( 1 ).getType ( ) } ;
      RowType rowType = new RowType ( ids , types ) ;
      RowType phi = ( RowType ) node.getChildAt ( 2 ).getType ( ) ;
      rowType = RowType.union ( rowType , phi ) ;
      // set the type of this node
      context.setNodeType ( node , rowType ) ;
    }
  }


  /**
   * Applies the <b>(ATTR)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyAttr ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Row row = ( Row ) node.getExpression ( ) ;
    if ( row.getExpressions ( ).length > 0
        && row.getExpressions ( ) [ 0 ] instanceof Attribute )
    {
      Attribute attr = ( Attribute ) row.getExpressions ( ) [ 0 ] ;
      TypeEnvironment environment = node.getEnvironment ( ) ;
      // generate new child node
      context.addProofNode ( node , environment , attr.getE ( ) ) ;
      return ;
    }
    throw new ClassCastException ( ) ;
  }


  /**
   * Updates the <code>node</code> to which <b>(ATTR)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(ATTR)</b>.
   */
  public void updateAttr ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    if ( node.getChildCount ( ) == 1 && node.getFirstChild ( ).isFinished ( ) )
    {
      Row rowExpression = ( Row ) node.getExpression ( ) ;
      Attribute attr = ( Attribute ) rowExpression.getExpressions ( ) [ 0 ] ;
      TypeEnvironment environment = node.getEnvironment ( ) ;
      // Create the tailRow and add it as Expression of a new Child
      Expression [ ] expressions = ( ( Row ) node.getExpression ( ) )
          .getExpressions ( ) ;
      Expression [ ] tailRow = new Expression [ expressions.length - 1 ] ;
      for ( int i = 1 ; i < expressions.length ; i ++ )
      {
        tailRow [ i - 1 ] = expressions [ i ] ;
      }
      Row row = new Row ( tailRow ) ;
      // generate new child node
      context.addProofNode ( node , environment.extend ( attr.getId ( ) , node
          .getFirstChild ( ).getType ( ) ) , row ) ;
    }
    if ( node.getChildCount ( ) == 2 && node.getChildAt ( 1 ).isFinished ( ) )
    {
      // set the type of this node
      context.setNodeType ( node , node.getChildAt ( 1 ).getType ( ) ) ;
    }
  }


  /**
   * Applies the <b>(EMPTY)</b> rule to the
   * <code>node</node> using the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyEmpty ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingExpressionProofNode node = ( MinimalTypingExpressionProofNode ) pNode ;
    Row row = ( Row ) node.getExpression ( ) ;
    Expression [ ] expressions = row.getExpressions ( ) ;
    if ( expressions.length == 0 )
    {
      // set the type of this node
      context.setNodeType ( node , new RowType ( new Identifier [ 0 ] ,
          new MonoType [ 0 ] ) ) ;
      return ;
    }
    throw new IllegalArgumentException ( Messages
        .getString ( "MinimalTypingException.8" ) ) ; //$NON-NLS-1$
  }


  /**
   * Applies the <b>(TRANS)</b> rule to the <code>node</code> using the
   * <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyTrans ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode ;
    try
    {
      ObjectType type = ( ObjectType ) node.getType2 ( ) ;
      ObjectType type2 = ( ObjectType ) node.getType ( ) ;
      ArrayList < Identifier > newIds = new ArrayList < Identifier > ( ) ;
      ArrayList < MonoType > newTypes = new ArrayList < MonoType > ( ) ;
      RowType r1 = ( RowType ) ( type ).getPhi ( ) ;
      RowType r2 = ( RowType ) ( type2 ).getPhi ( ) ;
      Identifier [ ] ids1 = r1.getIdentifiers ( ) ;
      Identifier [ ] ids2 = r2.getIdentifiers ( ) ;
      MonoType [ ] types2 = r2.getTypes ( ) ;
      boolean goOn ;
      for ( int i = 0 ; i < ids1.length ; i ++ )
      {
        goOn = false ;
        for ( int j = 0 ; j < ids2.length ; j ++ )
        {
          if ( ids1 [ i ].equals ( ids2 [ j ] ) )
          {
            newIds.add ( ids2 [ j ] ) ;
            newTypes.add ( types2 [ j ] ) ;
            goOn = true ;
            break ;
          }
        }
        if ( goOn ) continue ;
        throw new RuntimeException ( Messages
            .getString ( "SubTypingException.5" ) ) ; //$NON-NLS-1$
      }
      Identifier [ ] tmpIds = new Identifier [ newIds.size ( ) ] ;
      for ( int i = 0 ; i < newIds.size ( ) ; i ++ )
      {
        tmpIds [ i ] = newIds.get ( i ) ;
      }
      MonoType [ ] tmpTypes = new MonoType [ newTypes.size ( ) ] ;
      for ( int i = 0 ; i < newTypes.size ( ) ; i ++ )
      {
        tmpTypes [ i ] = newTypes.get ( i ) ;
      }
      // ObjectType newType = new ObjectType ( new RowType ( (Identifier[])
      // newIds.toArray ( ),(MonoType[]) newTypes.toArray ( ) ) );
      ObjectType newType = new ObjectType ( new RowType ( tmpIds , tmpTypes ) ) ;
      // generate new child node
      context.addProofNode ( node , type2 , newType ) ;
      // generate new child node
      context.addProofNode ( node , newType , type ) ;
    }
    catch ( ClassCastException e )
    {
      MonoType type = node.getType ( ) ;
      MonoType type2 = node.getType2 ( ) ;
      // if both types instance of Primitive Type throw Exception
      if ( type instanceof PrimitiveType && type2 instanceof PrimitiveType )
      {
        throw new IllegalArgumentException ( Messages
            .getString ( "SubTypingException.1" ) ) ; //$NON-NLS-1$
      }
      // generate new child node
      context.addProofNode ( node , type , type ) ;
      // generate new child node
      context.addProofNode ( node , type , type2 ) ;
      SubTypingProofNode parent = ( SubTypingProofNode ) node.getParent ( ) ;
      int count = 0 ;
      // check how often the trans rule was applied
      while ( parent != null )
      {
        if ( parent.getRule ( ).toString ( ).equals ( "TRANS" ) ) { //$NON-NLS-1$
          count ++ ;
        }
        else break ;
        parent = ( SubTypingProofNode ) parent.getParent ( ) ;
      }
      // if applied 15 times the trans rule throw Exception
      if ( count >= 15 )
        throw new RuntimeException ( Messages
            .getString ( "SubTypingException.2" ) ) ; //$NON-NLS-1$
    }
  }


  /**
   * Applies the <b>(OBJECT-WIDTH)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyObjectWidth ( @SuppressWarnings("unused")
MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode ;
    boolean goOn ;
    ObjectType type = ( ObjectType ) node.getType ( ) ;
    ObjectType type2 = ( ObjectType ) node.getType2 ( ) ;
    RowType r1 = ( RowType ) ( type ).getPhi ( ) ;
    RowType r2 = ( RowType ) ( type2 ).getPhi ( ) ;
    Identifier [ ] ids1 = r1.getIdentifiers ( ) ;
    Identifier [ ] ids2 = r2.getIdentifiers ( ) ;
    MonoType [ ] types = r1.getTypes ( ) ;
    MonoType [ ] types2 = r2.getTypes ( ) ;
    for ( int i = 0 ; i < ids2.length ; i ++ )
    {
      goOn = false ;
      for ( int j = 0 ; j < ids1.length ; j ++ )
      {
        if ( ids2 [ i ].equals ( ids1 [ j ] ) )
        {
          if ( ! ( types2 [ i ].equals ( types [ j ] ) ) )
          {
            throw new RuntimeException ( Messages
                .getString ( "SubTypingException.5" ) ) ; //$NON-NLS-1$
          }
          goOn = true ;
          break ;
        }
      }
      if ( ! goOn )
      {
        throw new RuntimeException ( Messages
            .getString ( "SubTypingException.5" ) ) ; //$NON-NLS-1$
      }
      node.getSeenTypes ( ).add ( new DefaultSubType( node.getType ( ), node.getType2 ( )) );
    }
  }


  /**
   * Applies the <b>(OBJECT-DEPTH)</b> rule to the <code>node</code> using
   * the <code>context</code>.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the minimal typing proof node.
   */
  public void applyObjectDepth ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode ;
    ObjectType type = ( ObjectType ) node.getType ( ) ;
    ObjectType type2 = ( ObjectType ) node.getType2 ( ) ;
    RowType r1 = ( RowType ) ( type ).getPhi ( ) ;
    RowType r2 = ( RowType ) ( type2 ).getPhi ( ) ;
    // boolean goOn;
    Identifier [ ] ids1 = r1.getIdentifiers ( ) ;
    Identifier [ ] ids2 = r2.getIdentifiers ( ) ;
    MonoType [ ] types = r1.getTypes ( ) ;
    MonoType [ ] types2 = r2.getTypes ( ) ;
    int index = r2.getIndexOfIdentifier ( ids1 [ 0 ] ) ;
    if ( ids1.length == ids2.length && index > - 1 )
    {
      /*
       * for ( int i = 0; i < ids1.length; i++ ) { goOn = false; for ( int j =
       * 0; j < ids2.length; j++ ) { if ( ids1[i].equals ( ids2[j] ) ) { //
       * generate new child node context.addProofNode ( node, types[i],
       * types2[j] ); goOn = true; } } if ( goOn ) continue; break; }
       */
      context.addProofNode ( node , types [ 0 ] , types2 [ index ] ) ;
    }
    else throw new RuntimeException ( Messages
        .getString ( "SubTypingException.5" ) ) ; //$NON-NLS-1$
    node.getSeenTypes ( ).add ( new DefaultSubType( node.getType ( ), node.getType2 ( )) );
  }


  /**
   * Updates the <code>node</code> to which <b>(OBJECT-DEPTH)</b> was applied
   * previously.
   * 
   * @param context the minimal typing proof context.
   * @param pNode the node to update according to <b>(OBJECT-DEPTH)</b>.
   */
  public void updateObjectDepth ( MinimalTypingProofContext context ,
      MinimalTypingProofNode pNode )
  {
    MinimalTypingTypesProofNode node = ( MinimalTypingTypesProofNode ) pNode ;
    ObjectType type = ( ObjectType ) node.getType ( ) ;
    ObjectType type2 = ( ObjectType ) node.getType2 ( ) ;
    RowType r1 = ( RowType ) ( type ).getPhi ( ) ;
    RowType r2 = ( RowType ) ( type2 ).getPhi ( ) ;
    Identifier [ ] ids1 = r1.getIdentifiers ( ) ;
    MonoType [ ] types = r1.getTypes ( ) ;
    MonoType [ ] types2 = r2.getTypes ( ) ;
    if ( node.isFinished ( ) && node.getChildCount ( ) < ids1.length )
    {
      int index = r2.getIndexOfIdentifier ( ids1 [ node.getChildCount ( ) ] ) ;
      if ( index < 0 )
        throw new RuntimeException ( Messages
            .getString ( "SubTypingException.5" ) ) ; //$NON-NLS-1$
      context.addProofNode ( node , types [ node.getChildCount ( ) ] ,
          types2 [ index ] ) ;
    }
  }
}
