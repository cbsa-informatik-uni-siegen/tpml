/* This file was generated by SableCC (http://www.sablecc.org/). */

package l1.analysis;

import java.util.*;
import l1.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable in;
    private Hashtable out;

    public Object getIn(Node node)
    {
        if(in == null)
        {
            return null;
        }

        return in.get(node);
    }

    public void setIn(Node node, Object in)
    {
        if(this.in == null)
        {
            this.in = new Hashtable(1);
        }

        if(in != null)
        {
            this.in.put(node, in);
        }
        else
        {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node)
    {
        if(out == null)
        {
            return null;
        }

        return out.get(node);
    }

    public void setOut(Node node, Object out)
    {
        if(this.out == null)
        {
            this.out = new Hashtable(1);
        }

        if(out != null)
        {
            this.out.put(node, out);
        }
        else
        {
            this.out.remove(node);
        }
    }
    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    public void caseAApplicationExpression(AApplicationExpression node)
    {
        defaultCase(node);
    }

    public void caseAConditionExpression(AConditionExpression node)
    {
        defaultCase(node);
    }

    public void caseALetExpression(ALetExpression node)
    {
        defaultCase(node);
    }

    public void caseAAbstractionExpression(AAbstractionExpression node)
    {
        defaultCase(node);
    }

    public void caseAIdentifierExpression(AIdentifierExpression node)
    {
        defaultCase(node);
    }

    public void caseAUnitExpression(AUnitExpression node)
    {
        defaultCase(node);
    }

    public void caseATrueExpression(ATrueExpression node)
    {
        defaultCase(node);
    }

    public void caseAFalseExpression(AFalseExpression node)
    {
        defaultCase(node);
    }

    public void caseANumberExpression(ANumberExpression node)
    {
        defaultCase(node);
    }

    public void caseAPlusExpression(APlusExpression node)
    {
        defaultCase(node);
    }

    public void caseAMinusExpression(AMinusExpression node)
    {
        defaultCase(node);
    }

    public void caseAMultiplyExpression(AMultiplyExpression node)
    {
        defaultCase(node);
    }

    public void caseADivideExpression(ADivideExpression node)
    {
        defaultCase(node);
    }

    public void caseAModuloExpression(AModuloExpression node)
    {
        defaultCase(node);
    }

    public void caseALowerThanExpression(ALowerThanExpression node)
    {
        defaultCase(node);
    }

    public void caseAGreaterThanExpression(AGreaterThanExpression node)
    {
        defaultCase(node);
    }

    public void caseALowerEqualExpression(ALowerEqualExpression node)
    {
        defaultCase(node);
    }

    public void caseAGreaterEqualExpression(AGreaterEqualExpression node)
    {
        defaultCase(node);
    }

    public void caseAEqualExpression(AEqualExpression node)
    {
        defaultCase(node);
    }

    public void caseTBlank(TBlank node)
    {
        defaultCase(node);
    }

    public void caseTPlus(TPlus node)
    {
        defaultCase(node);
    }

    public void caseTMinus(TMinus node)
    {
        defaultCase(node);
    }

    public void caseTMultiply(TMultiply node)
    {
        defaultCase(node);
    }

    public void caseTDivide(TDivide node)
    {
        defaultCase(node);
    }

    public void caseTModulo(TModulo node)
    {
        defaultCase(node);
    }

    public void caseTLowerThan(TLowerThan node)
    {
        defaultCase(node);
    }

    public void caseTGreaterThan(TGreaterThan node)
    {
        defaultCase(node);
    }

    public void caseTLowerEqual(TLowerEqual node)
    {
        defaultCase(node);
    }

    public void caseTGreaterEqual(TGreaterEqual node)
    {
        defaultCase(node);
    }

    public void caseTEqual(TEqual node)
    {
        defaultCase(node);
    }

    public void caseTLeftParen(TLeftParen node)
    {
        defaultCase(node);
    }

    public void caseTRightParen(TRightParen node)
    {
        defaultCase(node);
    }

    public void caseTDot(TDot node)
    {
        defaultCase(node);
    }

    public void caseTIf(TIf node)
    {
        defaultCase(node);
    }

    public void caseTThen(TThen node)
    {
        defaultCase(node);
    }

    public void caseTElse(TElse node)
    {
        defaultCase(node);
    }

    public void caseTLambda(TLambda node)
    {
        defaultCase(node);
    }

    public void caseTLet(TLet node)
    {
        defaultCase(node);
    }

    public void caseTIn(TIn node)
    {
        defaultCase(node);
    }

    public void caseTUnit(TUnit node)
    {
        defaultCase(node);
    }

    public void caseTTrue(TTrue node)
    {
        defaultCase(node);
    }

    public void caseTFalse(TFalse node)
    {
        defaultCase(node);
    }

    public void caseTNumber(TNumber node)
    {
        defaultCase(node);
    }

    public void caseTIdentifier(TIdentifier node)
    {
        defaultCase(node);
    }

    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    public void defaultCase(Node node)
    {
    }
}
