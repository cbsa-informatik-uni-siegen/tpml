package de.unisiegen.tpml.core.bigstepclosure;

import javax.swing.tree.TreeNode;

import de.unisiegen.tpml.core.ClosureEnvironment;
import de.unisiegen.tpml.core.DefaultClosureEnvironment;
import de.unisiegen.tpml.core.ProofStep;
import de.unisiegen.tpml.core.bigstep.BigStepProofResult;
import de.unisiegen.tpml.core.expressions.Closure;
import de.unisiegen.tpml.core.expressions.Expression;
import de.unisiegen.tpml.core.interpreters.AbstractInterpreterProofNode;
import de.unisiegen.tpml.core.interpreters.DefaultStore;
import de.unisiegen.tpml.core.interpreters.Store;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;

/**
 * TODO
 *
 */
public final class DefaultBigStepClosureProofNode extends AbstractInterpreterProofNode
    implements BigStepClosureProofNode
{
  public DefaultBigStepClosureProofNode ( Expression pExpression )
  {
    this ( new Closure(pExpression, new DefaultClosureEnvironment()), new DefaultStore());
  }
  
  public DefaultBigStepClosureProofNode(Closure closure,
      Store store)
  {
    super(closure.getExpression(), store);
    this.environment = closure.getEnvironment();
  }
  
  public BigStepClosureProofResult getResult()
  {
    return this.result;
  }
  
  public BigStepClosureProofRule getRule ()
  {
    ProofStep [] steps = getSteps ();
    return steps.length == 0
        ? null
        : ( BigStepClosureProofRule ) steps [ 0 ].getRule ();
  }
  
  public DefaultBigStepClosureProofNode getParent ()
  {
    return (DefaultBigStepClosureProofNode)super.getParent ();
  }
  
  public DefaultBigStepClosureProofNode getRoot ()
  {
    return (DefaultBigStepClosureProofNode)super.getRoot();
  }
  
  public DefaultBigStepClosureProofNode getFirstChild ()
  {
    return (DefaultBigStepClosureProofNode)super.getFirstChild();
  }
  
  public DefaultBigStepClosureProofNode getLastChild ()
  {
    return (DefaultBigStepClosureProofNode)super.getLastChild ();
  }
  
  public DefaultBigStepClosureProofNode getChildAfter ( TreeNode aChild )
  {
    return (DefaultBigStepClosureProofNode)super.getChildAfter ( aChild );
  }
  
  public DefaultBigStepClosureProofNode getChildBefore ( TreeNode aChild )
  {
    return (DefaultBigStepClosureProofNode)super.getChildBefore(aChild);
  }
  
  public DefaultBigStepClosureProofNode getChildAt(int index)
  {
    return (DefaultBigStepClosureProofNode)super.getChildAt ( index );
  }
  
  public DefaultBigStepClosureProofNode getFirstLeaf ()
  {
    return (DefaultBigStepClosureProofNode)super.getFirstLeaf ();
  }
  
  public DefaultBigStepClosureProofNode getLastLeaf ()
  {
    return (DefaultBigStepClosureProofNode)super.getLastLeaf ();
  }
  
  public boolean isProven()
  {
    return this.result != null;
  }

  public PrettyString toPrettyString()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
    .toPrettyString ();
  }
  
  public PrettyStringBuilder toPrettyStringBuilder(PrettyStringBuilderFactory fac)
  {
    PrettyStringBuilder builder = fac.newBuilder (
        this, 0 );
    
    builder.addBuilder(getExpression().toPrettyStringBuilder ( fac ), 0);
    
    return builder;
  }
  
  public LatexInstructionList getLatexInstructions()
  {
    return null;
  }
  
  public LatexStringBuilder toLatexStringBuilder(LatexStringBuilderFactory fac, int i)
  {
    return null;
  }
  
  public LatexCommandList getLatexCommands()
  {
    return null;
  }
  
  public LatexString toLatexString()
  {
    return null;
  }
  
  public LatexPackageList getLatexPackages()
  {
    return null;
  }
  
  public ClosureEnvironment getEnvironment()
  {
    return this.environment;
  }
  
  public Closure getClosure()
  {
    return new Closure(getExpression(), getEnvironment());
  }
  
  public boolean isFinished()
  {
    if ( !isProven () )
      return false;
    
    for ( int n = 0 ; n < getChildCount () ; ++n )
      if ( !getChildAt ( n ).isFinished () )
        return false;
    return true;
  }
  
  public void setResult ( BigStepClosureProofResult pResult )
  {
    if ( pResult != null && !pResult.getValue ().isException ()
        && !pResult.getValue ().isValue () )
    {
      throw new IllegalArgumentException ( "result is invalid" ); //$NON-NLS-1$
    }
    this.result = pResult;
  }

  @Override
  public String toString ()
  { 
    StringBuilder builder = new StringBuilder ();
    boolean memoryEnabled = getExpression ().containsMemoryOperations ();
    if ( memoryEnabled )
    {
      builder.append ( '(' );
    }
    builder.append ( getExpression () );
    if ( memoryEnabled )
    {
      builder.append ( ", " ); //$NON-NLS-1$
      builder.append ( getStore () );
      builder.append ( ')' );
    }
    
    builder.append (' ');
    builder.append ( getClosure().getEnvironment ().toString() );
    
    builder.append ( " \u21d3 " ); //$NON-NLS-1$
    if ( this.result != null )
    {
      if ( memoryEnabled )
      {
        builder.append ( '(' );
      }
      builder.append ( this.result.getValue () );
      if ( memoryEnabled )
      {
        builder.append ( ", " ); //$NON-NLS-1$
        builder.append ( this.result.getStore () );
        builder.append ( ')' );
      }
    }
    
    return builder.toString ();
  }
  
  private BigStepClosureProofResult result;
  
  private ClosureEnvironment environment;
}
